package net.pers.yothin.util;

import java.io.IOException;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片处理工具类
 * @author yothin
 *
 */
public class ImageUtil {
	
	/*
	 * 压缩图片
	 * oldFile:旧图片源路径
	 * 图片按比例缩小，横为width或高为height，
	 * quality:质量
	 * newFile:新图片源路径
	 */
	public static void compressImage(String oldFile, int width, int height,
			float quality, String newFile) throws IOException{
		
		Thumbnails.of(oldFile)
			.size(width, height)
			.outputQuality(quality)
			.toFile(newFile);
	}
	
	/*
	 * 
	 */
	public static void watermarkString(){
		
	}
	
	
	public static void main(String[] args) {
		try {
			ImageUtil.compressImage("f:/1.jpg", 500, 400, 1f, "f:/2.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
