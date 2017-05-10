package net.pers.yothin.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;

/*
 * 图片工具类
 */
public class ImageUtil {

	/*
	 * 缩放图片，按宽或高，比例缩放
	 * oldFile:源图片路径
	 * width:缩放的宽
	 * height:缩放的高
	 * quality:生成的图片的质量
	 * newFile:生成图片的路径
	 */
	public static void compressImage(String oldFile, int width, int height, double quality, String newFile)
			throws IOException {

		Thumbnails.of(oldFile).size(width, height).outputQuality(quality).toFile(newFile);
	}

	/*
	 * 缩放图片，按比例
	 * scale:缩放比例
	 */
	public static void scaleImage(String oldFile, double scale, double quality, String newFile) throws IOException {

		Thumbnails.of(oldFile).scale(scale).outputQuality(quality).toFile(newFile);
	}

	/*
	 * 加图片水印
	 * position:位置点(如 Positions.CENTER)
	 * markFile:水印图片
	 * alpha:透明度(0-1)
	 * 
	 */
	public static void watermarkImage(String oldFile, Positions position, String markFile, float alpha, double quality, String newFile) throws IOException{
		
		Image src = ImageIO.read(new File(oldFile));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		Thumbnails.of(oldFile).size(width, height)
			.watermark(position,ImageIO.read(new File(markFile)),alpha)
			.outputQuality(quality)
			.toFile(newFile);
	}
	
	/*
	 * 旋转图片
	 * rotate:旋转的角度(顺时针)
	 */
	public static void rotateImage(String oldFile, double rotate, double quality, String newFile) throws IOException{
		
		Image src = ImageIO.read(new File(oldFile));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		Thumbnails.of(oldFile).size(width, height).rotate(90).outputQuality(quality).toFile(newFile);
	}
	
	/*
	 * 裁剪图片
	 * width:裁剪的宽度
	 * height:裁剪的高度
	 */
	public static void cutImage(String oldFile, Positions position, int width, int height, double quality, String newFile) throws IOException{
		
		Thumbnails.of(oldFile).sourceRegion(position, width, height).size(width, height).outputQuality(quality).toFile(newFile);
	}
	
	/*
	 * 加文字水印
	 * text:水印文本
	 * fontName:字体类型(如Font.SANS_SERIF)
	 * fontStyle:字体样式(如Font.BOLD)
	 * color:文本颜色(如Color.RED)
	 * fontSize:字体大小
	 * x:
	 * y:偏移量
	 * 
	 */
	public static void watermarkText(String oldFile, String text, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha, String newFile) throws Exception {
		File oldfile = null;
		Image src = null;
		FileOutputStream out = null;
		try {
			oldfile = new File(oldFile);
			src = ImageIO.read(oldfile);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);

			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			int textWidth = fontSize * getLength(text);
			int textHeight = fontSize;
			int widthDiff = width - textWidth;
			int heightDiff = height - textHeight;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff - fontSize / 2;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff - fontSize / 2;
			}

			g.drawString(text, x, y + textHeight);
			g.dispose();
			out = new FileOutputStream(new File(newFile));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
		} catch (Exception e) {
			throw new Exception("图片添加文字水印异常:" + e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/*
	 * 压缩图片
	 */
	public static void zipImage(String oldFile, String newFile) throws Exception {
		File oldfile = null;
		Image src = null;
		FileOutputStream out = null;
		try {
			oldfile = new File(oldFile);
			src = ImageIO.read(oldfile);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, null);
			g.dispose();
			out = new FileOutputStream(new File(newFile));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/*
	 * 获取文本长度
	 * utf-8下，一个中文汉字符号占3个字节
	 * gbk下，一个中文汉字符号占2个字节
	 * 英文字母符号都是占1个字节
	 * Windows一般为gbk编码
	 */
	public static int getLength(String text) {
		int textLength = text.length();
		int length = textLength;
		for (int i = 0; i < textLength; i++) {
			if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
				length++;
			}
		}
		return (length % 2 == 0) ? length / 2 : length / 2 + 1;
	}
	
	public static void main(String[] args) throws Exception {
		ImageUtil.watermarkText("f:/1.jpg", "你好", Font.SANS_SERIF, Font.BOLD, Color.gray, 120, -1, 0, 0.7f, "f:/11.jpg");
	}
}
