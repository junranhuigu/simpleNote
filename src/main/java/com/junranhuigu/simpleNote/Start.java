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
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.junranhuigu.simpleJson.JsonUtil;
import com.junranhuigu.simpleNote.structure.Separator;
import com.junranhuigu.simpleNote.util.FileUtil;
import com.junranhuigu.simpleNote.vo.PhotoAnalysor;
import com.junranhuigu.simpleNote.vo.PhotoInfo;
import com.junranhuigu.simpleNote.vo.SimpleNote;
import com.junranhuigu.simpleNote.vo.TimeLine;

public class Start {
	public static String packPath = "C:\\Users\\jiawei\\Desktop\\img";
	
	public static void main(String[] args) {
		List<String> notImgPath = new ArrayList<>();
		notImgPath.add(packPath + File.separator + "info");
		notImgPath.add(packPath + File.separator + "web");
		
		File pack = new File(packPath);
		LoggerFactory.getLogger(Start.class).info("开始抓取图片");
		Set<String> imgPaths = new HashSet<>();
		findAllFiles(pack, imgPaths);//
		imgPaths.removeAll(readAnalysisedPhotoData(pack, "root.path"));
		List<String> imgs = new ArrayList<>();
		pathFor : for(String path : imgPaths){
			File file = new File(path);
			for(String notImg : notImgPath){
				if(file.getAbsolutePath().contains(notImg)){
					continue pathFor;
				}
			}
			try(	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
				if(FileTypeDetector.detectFileType(bis) != FileType.Unknown){
					Metadata data = ImageMetadataReader.readMetadata(file);
					GpsDirectory d2 = data.getFirstDirectoryOfType(GpsDirectory.class);
					if(d2 != null && d2.getDescription(GpsDirectory.TAG_LONGITUDE) != null){//只记录有GPS信息的图片
						imgs.add(path);
					}
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
			LoggerFactory.getLogger(Start.class).info("图片数据分析完毕");
		}
		LoggerFactory.getLogger(Start.class).info("开始读取图片信息");
		List<PhotoInfo> infos = readAnalysisedPhotoInfo(pack);
		LoggerFactory.getLogger(Start.class).info("图片信息读取完毕，共计：" + infos.size());
		LoggerFactory.getLogger(Start.class).info("检测预览网页文件");
		File webPackage = new File(System.getProperty("user.dir") + File.separator + "web");
		//生成地图信息
		LoggerFactory.getLogger(Start.class).info("生成地图信息");
		Map<String, String> mapNotes = new HashMap<>();
		for(PhotoInfo info : infos){
			if(info.getAddress() != null && !mapNotes.containsKey(info.getAddress().getMapLocations())){
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
		List<String> records = new ArrayList<>();
		records.add(mapParams.toString());
		try {
			File mapParamFile = new File(webPackage.getAbsolutePath() + File.separator + "map" + File.separator + "param.js");
			FileUtil.cover(records, mapParamFile.getAbsolutePath(), Charset.forName("UTF-8"));
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("写入地图数据出错", e);
		}
		//生成时间线信息
		LoggerFactory.getLogger(Start.class).info("生成时间线信息");
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
		try {
			File lineParamFile = new File(webPackage.getAbsolutePath() + File.separator + "time" + File.separator + "params.js");
			records.clear();
			records.add("var params = " + JSON.toJSONString(lineParams) + ";");
			FileUtil.cover(records, lineParamFile.getAbsolutePath(), Charset.forName("UTF-8"));
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("写入时间线数据出错", e);
		}
		//生成详细照片信息
		LoggerFactory.getLogger(Start.class).info("开始生成预览图片信息");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder photoParams = new StringBuilder();
		photoParams.append("var params = [");
		for(PhotoInfo info : infos){
			try {
				Map<String, String> img = new HashMap<>();
				img.put("path", info.showPath());
				img.put("note", sdf.format(info.getTime()) + SimpleNote.LINE_CHAR + info.getAddress().simpleDetail() + SimpleNote.LINE_CHAR + new File(info.getPath()).getName());
				photoParams.append(JSON.toJSONString(img)).append(",");
			} catch (Exception e) {
				LoggerFactory.getLogger(Start.class).error("读取图片" + info.getPath() + "数据出错", e);
			}
		}
		photoParams.delete(photoParams.length() - 1, photoParams.length());
		photoParams.append("];");
		try {
			File photoParamFile = new File(webPackage.getAbsolutePath() + File.separator + "imgDetail" + File.separator + "js" + File.separator + "params.js");
			records.clear();
			records.add(photoParams.toString());
			FileUtil.cover(records, photoParamFile.getAbsolutePath(), Charset.forName("UTF-8"));
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("写入照片预览数据出错", e);
		}
		//转移生成的文件
		LoggerFactory.getLogger(Start.class).info("开始转移网页展示文件");
		try {
			File aim = new File(packPath + File.separator + "web");
			FileUtil.copy(aim, webPackage, true);
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("转移文件失败", e);
		}
		LoggerFactory.getLogger(Start.class).info("转移网页展示文件完毕");
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
