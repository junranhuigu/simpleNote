package com.junranhuigu.simpleNote;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil {

	/**
	 * 翻转图片
	 * @param imagePath 图像文件路径
	 * @param outPath 保存图像文件路径 如果为空则处理后图像会覆盖原文件
	 * */
	public static void flip(String imagePath, String outPath) {
    	String inPath = imagePath;
    	File inFile = new File(inPath);
    	String inFileFormat = inPath.substring(inPath.lastIndexOf(".") + 1);
    	if(StringUtil.isEmpty(outPath)){
    		outPath = inFile.getAbsolutePath();
    	}
    	File outFile = new File(outPath);
    	
    	try {
    		BufferedImage bufferedimage = ImageIO.read(inFile);
    		int w = bufferedimage.getWidth();
    		int h = bufferedimage.getHeight();
    		BufferedImage img = new BufferedImage(w, h, bufferedimage
    				.getColorModel().getTransparency());
    		Graphics2D graphics2d = img.createGraphics();
    		graphics2d.drawImage(bufferedimage, 0, 0, w, h, w, 0, 0, h, null);
    		graphics2d.dispose();
    		ImageIO.write(img, inFileFormat, outFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 缩放图片
	 * @param imagePath 图像文件路径
	 * @param outPath 保存图像文件路径 如果为空则处理后图像会覆盖原文件
	 * @param scalePercent 缩放百分比 最小为1
	 * */
	public static void scale(String imagePath, String outPath, int scalePercent) { 
		String inPath = imagePath;
    	File inFile = new File(inPath);
    	String inFileFormat = inPath.substring(inPath.lastIndexOf(".") + 1);
    	if(StringUtil.isEmpty(outPath)){
    		outPath = inFile.getAbsolutePath();
    	}
    	File outFile = new File(outPath);
    	
	    try { 
	    	BufferedImage bufferedImage = ImageIO.read(inFile); 
	    	int width = bufferedImage.getWidth() * scalePercent / 100; 
	    	int height = bufferedImage.getHeight() * scalePercent / 100; 
	  
	    	BufferedImage img = new BufferedImage(width, height, bufferedImage.getColorModel().getTransparency());
	    	Graphics graphics = img.createGraphics();
    		graphics.drawImage(bufferedImage, 0, 0, width, height, null);
	    	graphics.dispose(); 
	  
	    	ImageIO.write(img, inFileFormat, outFile); 
	    } catch (Exception e) { 
	    	e.printStackTrace(); 
	    } 
    } 

	/**
	 * 生成一个指定大小的正方形图片，按比例保留原图片，剩余部分进行颜色填充
	 * @param imagePath 图像文件路径
	 * @param outPath 保存图像文件路径 如果为空则处理后图像会覆盖原文件
	 * @param scalePercent 缩放百分比 最小为1
	 * */
	public static void preview(String imagePath, String outPath, int len, Color color){
		String inPath = imagePath;
    	File inFile = new File(inPath);
    	String inFileFormat = inPath.substring(inPath.lastIndexOf(".") + 1);
    	if(StringUtil.isEmpty(outPath)){
    		outPath = inFile.getAbsolutePath();
    	}
    	File outFile = new File(outPath);
    	
    	try {
	    	BufferedImage bufferedImage = ImageIO.read(inFile);
	    	int scalePercent = len * 100 / Math.max(bufferedImage.getWidth(), bufferedImage.getHeight());
			int width = bufferedImage.getWidth() * scalePercent / 100;
	    	int height = bufferedImage.getHeight() * scalePercent / 100;
	  
	    	BufferedImage img = new BufferedImage(len, len, bufferedImage.getColorModel().getTransparency());
	    	Graphics2D graphics = img.createGraphics();
	    	graphics.setBackground(color);
	    	graphics.clearRect(0, 0, len, len);
    		graphics.drawImage(bufferedImage, (len - width) / 2, (len - height) / 2, width, height, null);
	    	graphics.dispose();
	  
	    	ImageIO.write(img, inFileFormat, outFile); 
	    } catch (Exception e) { 
	    	e.printStackTrace(); 
	    } 
	}
}
