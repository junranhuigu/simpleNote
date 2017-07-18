package com.junranhuigu.simpleNote.vo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.junranhuigu.simpleNote.util.ChannelUtil;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	App app = new App();
       System.out.println(app.sayHello());
//       String path = "C:\\Users\\jiawei\\Desktop\\IMG_20170625_150057_HDR.jpg";
       List<String> paths = new ArrayList<>();
       paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170423_175914.jpg");
//       paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170205_110835.jpg");
//       paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170625_150057_HDR.jpg");
//       paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170625_145122_HDR.jpg");
//       paths.add("C:\\Users\\jiawei\\Desktop\\问题导向的知识管理.png");
//       paths.add("C:\\Users\\jiawei\\Desktop\\微信图片_20170705112327.jpg");
//       for(String path : paths){
//    	   app.readPhotoInfo(new File(path));
//       }
       
//      ImageUtil.flip("C:\\Users\\jiawei\\Desktop\\IMG_20170423_175915.jpg", "");
//      ImageUtil.scale("C:\\Users\\jiawei\\Desktop\\IMG_20170423_175915.jpg", "C:\\Users\\jiawei\\Desktop\\IMG_20170423_175916.jpg", 20);
//      ImageUtil.preview("C:\\Users\\jiawei\\Desktop\\微信图片_20170705112327.jpg", "C:\\Users\\jiawei\\Desktop\\IMG_20170423_175916.jpg", 100, Color.WHITE);
      
      String path = "https://api.douban.com/v2/book/search";
      Map<String, String> params = new HashMap<>();
      params.put("q", "我的奋斗+罗永浩");
      params.put("count", "5");
      String resp = ChannelUtil.httpsGet(path + "?" + ChannelUtil.getSortQueryString(params), false);
      System.out.println(resp);
      
      path = "http://api.douban.com/v2/movie/search";
      params.clear();
      params.put("q", "魔戒");
      params.put("count", "5");
      params.put("scope", "movie_basic_r");
      resp = ChannelUtil.httpGet(path + "?" + ChannelUtil.getSortQueryString(params), "UTF-8");
      System.out.println(resp);
//      System.out.println(structure);
      
    }
    
    public String sayHello(){
    	return "hello";
    }
    
    public String sayFuck(){
    	return "fuck";
    }
    
    public void readPhotoInfo(File photo) throws Exception{
    	Metadata data = ImageMetadataReader.readMetadata(photo);
    	
		//图片信息
    	for(Directory directory : data.getDirectories()){
    		for(Tag tag : directory.getTags()){
    			System.out.println(tag);
    		}
    	}
    }
    
    
}
