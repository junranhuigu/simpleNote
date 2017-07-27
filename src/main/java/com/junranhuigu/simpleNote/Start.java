package com.junranhuigu.simpleNote;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.junranhuigu.simpleJson.JsonUtil;
import com.junranhuigu.simpleNote.structure.Separator;
import com.junranhuigu.simpleNote.util.FileUtil;
import com.junranhuigu.simpleNote.vo.PhotoAnalysor;
import com.junranhuigu.simpleNote.vo.PhotoInfo;
import com.junranhuigu.simpleNote.vo.SimpleNote;
import com.junranhuigu.simpleNote.vo.TimeLine;

public class Start {
	public static void main(String[] args) {
		String packPath = "C:\\Users\\jiawei\\Desktop\\img";
		File pack = new File(packPath);
		LoggerFactory.getLogger(Start.class).info("开始抓取图片");
		Set<String> imgPaths = new HashSet<>();
		findAllFiles(pack, imgPaths);//
		imgPaths.removeAll(readAnalysisedPhotoData(pack, "root.path"));
		List<String> imgs = new ArrayList<>(); 
		for(String path : imgPaths){
			File file = new File(path);
			try(	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
				if(FileTypeDetector.detectFileType(bis) != FileType.Unknown){
					imgs.add(path);
				}
			} catch (Exception e) {}
		}
		LoggerFactory.getLogger(Start.class).info("抓取图片共计：" + imgs.size());
		if(imgs.size() > 0){
			LoggerFactory.getLogger(Start.class).info("开始分析图片数据");
			try {
				List<PhotoInfo> infos = PhotoAnalysor.analysis(imgs);
				LoggerFactory.getLogger(Start.class).info("图片数据分析完毕，开始保存数据");
				List<String> contents = new ArrayList<>();
				for(PhotoInfo info : infos){
					contents.add(JSON.toJSONString(info) + "\n");
				}
				FileUtil.save(contents, photoInfoPath(pack), Charset.forName(TeamProperty.getInstance().get("defaultFileCharSet")));
				LoggerFactory.getLogger(Start.class).info("本次新增图片数据数量：" + contents.size());
			} catch (Exception e) {
				LoggerFactory.getLogger(Start.class).error("分析图片数据出错", e);
			}
		}
		LoggerFactory.getLogger(Start.class).info("开始读取图片信息");
		List<PhotoInfo> infos = readAnalysisedPhotoInfo(pack);
		//生成地图信息
		Map<String, String> mapNotes = new HashMap<>();
		for(PhotoInfo info : infos){
			if(!mapNotes.containsKey(info.getAddress().getMapLocations())){
				try {
					mapNotes.put(info.getAddress().getMapLocations(), SimpleNote.mapNote(info));
				} catch (Exception e) {
					LoggerFactory.getLogger(Start.class).error("读取图片" + info.getPath() + "数据出错", e);
				}
			}
		}
		StringBuilder mapParams = new StringBuilder("var params = [");
		for(String note : mapNotes.values()){
			mapParams.append(note).append(",");
		}
		mapParams.append("];");
		System.out.println(mapParams.toString());
		//生成时间线信息
		Separator<String, PhotoInfo> linePhotoSeparator = new Separator<String, PhotoInfo>() {
			@Override
			public String separate(PhotoInfo v) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				return sdf.format(v.getTime());
			}
		};
		linePhotoSeparator.addAll(infos);
		Iterator<Entry<String, List<PhotoInfo>>> ite = linePhotoSeparator.getResult().entrySet().iterator();
		Separator<String, TimeLine> lineSeparator = new Separator<String, TimeLine>() {
			@Override
			public String separate(TimeLine v) {
				return v.getYear();
			}
		};
		while(ite.hasNext()){
			Entry<String, List<PhotoInfo>> entry = ite.next();
			lineSeparator.add(new TimeLine(entry.getValue()));
		}
		HashMap<String, List<TimeLine>> lineParams = lineSeparator.getResult();
		Comparator<TimeLine> lineComparator = new Comparator<TimeLine>() {
			@Override
			public int compare(TimeLine o1, TimeLine o2) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
					long d1 = sdf.parse(o1.getYear() + o1.getDate()).getTime();
					long d2 = sdf.parse(o2.getYear() + o2.getDate()).getTime();
					if(d1 > d2){
						return -1;
					} else if(d1 < d2){
						return 1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					LoggerFactory.getLogger(Start.class).error("图片时间线信息出错", e);
					return 0;
				}
			}
		};
		for(List<TimeLine> linelist : lineParams.values()){
			Collections.sort(linelist, lineComparator);
		}
		System.out.println(JSON.toJSONString(lineParams));
	}
	
	
	private static void findAllFiles(File pack, Set<String> imgPaths){
		if(pack.isFile()){
			imgPaths.add(pack.getAbsolutePath());
		} else {
			for(File file : pack.listFiles()){
				findAllFiles(file, imgPaths);
			}
		}
	}
	
	private static Set<String> readAnalysisedPhotoData(File pack, String executingLanguage){
		Set<String> paths = new HashSet<>();
		//读取照片信息路径
		String infoPackPath = photoInfoPath(pack);
		File infoPack = new File(infoPackPath);
		try {
			if(!infoPack.getParentFile().exists()){
				infoPack.getParentFile().mkdirs();
			}
			if(!infoPack.exists()){
				infoPack.createNewFile();
			}
			try(	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infoPack), TeamProperty.getInstance().get("defaultFileCharSet")))){
				while(reader.ready()){
					String json = reader.readLine();
					List<String> imgPath = JsonUtil.select(executingLanguage, json, String.class);
					if(!imgPath.isEmpty()){
						paths.addAll(imgPath);
					}
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("", e);
		}
		return paths;
	}
	
	private static List<PhotoInfo> readAnalysisedPhotoInfo(File pack){
		List<PhotoInfo> infos = new ArrayList<>();
		//读取照片信息路径
		String infoPackPath = photoInfoPath(pack);
		File infoPack = new File(infoPackPath);
		try {
			if(!infoPack.getParentFile().exists()){
				infoPack.getParentFile().mkdirs();
			}
			if(!infoPack.exists()){
				infoPack.createNewFile();
			}
			try(	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infoPack), TeamProperty.getInstance().get("defaultFileCharSet")))){
				while(reader.ready()){
					String json = reader.readLine();
					infos.add(JSON.parseObject(json, PhotoInfo.class));
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("", e);
		}
		return infos;
	}
	
	private static String photoInfoPath(File pack){
		return pack.getAbsolutePath() + File.separator + "info" + File.separator + "photoInfo.out";
	}
}
