package com.junranhuigu.simpleNote;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.junranhuigu.simpleJson.JsonUtil;
import com.junranhuigu.simpleNote.util.FileUtil;
import com.junranhuigu.simpleNote.vo.PhotoAnalysor;
import com.junranhuigu.simpleNote.vo.PhotoInfo;

public class Start {
	public static void main(String[] args) {
		String packPath = "C:\\Users\\jiawei\\Desktop\\img";
		File pack = new File(packPath);
		LoggerFactory.getLogger(Start.class).info("开始抓取图片");
		Set<String> imgPaths = new HashSet<>();
		findAllFiles(pack, imgPaths);//
		imgPaths.removeAll(readAnalysisedPhotoPaths(pack));
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
		if(imgs.size() <= 0){
			return;
		}
		LoggerFactory.getLogger(Start.class).info("开始分析图片数据");
		try {
			List<PhotoInfo> infos = PhotoAnalysor.analysis(imgs);
			LoggerFactory.getLogger(Start.class).info("图片数据分析完毕，开始保存数据");
			List<String> contents = new ArrayList<>();
			for(PhotoInfo info : infos){
				contents.add(JSON.toJSONString(info) + "\n");
			}
			FileUtil.save(contents, photoInfoPath(pack), Charset.forName("UTF-8"));
			LoggerFactory.getLogger(Start.class).info("本次新增图片数据数量：" + contents.size());
		} catch (Exception e) {
			LoggerFactory.getLogger(Start.class).error("分析图片数据出错", e);
		}
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
	
	private static Set<String> readAnalysisedPhotoPaths(File pack){
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
			try(	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infoPack), "UTF-8"))){
				while(reader.ready()){
					String json = reader.readLine();
					List<String> imgPath = JsonUtil.select("root.path", json, String.class);
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
	
	private static String photoInfoPath(File pack){
		return pack.getAbsolutePath() + File.separator + "info" + File.separator + "photoInfo.out";
	}
}
