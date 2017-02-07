package com.alipay.util;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImgUtil {
	/**
	 * @param srcImgPath 待切割图片路径
	 * @param destImgPath 切割后图片路径
	 * @param destImgW 所需宽度
	 * @param destImgH 所需高度
	 */
	public static void createThumb (String srcImgPath , String destImgPath, int destImgW , int destImgH){
	    //原图片等比例缩小或放大之后的图片
	    int narrowImgW;
	    int narrowImgH;
	    //原图片大小
	    int srcImgW;
	    int srcImgH;
	    try {
	        BufferedImage bi = ImageIO. read(new File(srcImgPath));
	        srcImgW = bi.getWidth();
	        srcImgH = bi.getHeight();
	        // 转换图片尺寸与目标尺寸比较 ， 如果转换图片较小，说明转换图片相对于目标图片来说高较小，需要以高为基准进行缩放。
	        if((float )srcImgW /srcImgH > (float)destImgW / destImgH){
	            narrowImgW = ( int)(((float )destImgH / (float)srcImgH)*srcImgW);
	            narrowImgH = destImgH;
	            //按照原图以高为基准等比例缩放、或放大。这一步高为所需图片的高度，宽度肯定会比目标宽度宽。
	            int cutNarrowImgSize = (narrowImgW - destImgW)/2;
	            BufferedImage narrowImg = new BufferedImage(narrowImgW, narrowImgH,BufferedImage.TYPE_INT_RGB);
	            narrowImg.getGraphics().drawImage(bi.getScaledInstance(narrowImgW, narrowImgH, Image.SCALE_SMOOTH ), 0, 0, null);
	            //等比例缩放完成后宽度与目标尺寸宽度相比较 ， 将多余宽的部分分为两份 ，左边删除一部分
	            Image image = narrowImg.getScaledInstance(narrowImgW, narrowImgH, Image.SCALE_DEFAULT );
	            CropImageFilter cropFilter = new CropImageFilter(cutNarrowImgSize, 0, narrowImgW-cutNarrowImgSize, narrowImgH);
	            Image img = Toolkit.getDefaultToolkit().createImage( new FilteredImageSource(image.getSource(), cropFilter));
	            BufferedImage cutLiftNarrowImg = new BufferedImage( narrowImgW-cutNarrowImgSize, narrowImgH,BufferedImage.TYPE_INT_RGB );
	            cutLiftNarrowImg.getGraphics().drawImage(img, 0, 0, null);
	            //右边删除一部分
	            image = cutLiftNarrowImg.getScaledInstance(narrowImgW-cutNarrowImgSize, narrowImgH, Image.SCALE_DEFAULT );
	            cropFilter = new CropImageFilter(0, 0, narrowImgW-cutNarrowImgSize*2, narrowImgH);
	            img = Toolkit.getDefaultToolkit().createImage( new FilteredImageSource(image.getSource(), cropFilter));
	            BufferedImage cutRightNarrowImg = new BufferedImage( narrowImgW-cutNarrowImgSize*2, narrowImgH,BufferedImage.TYPE_INT_RGB );
	            Graphics g = cutRightNarrowImg.getGraphics();
	            g.drawImage(img, 0, 0, null); // 绘制截取后的图
	            g.dispose();
	            //输出为文件 最终为所需要的格式
	            ImageIO. write(cutRightNarrowImg, "JPEG", new File(destImgPath));
	        }
	        else{ //以宽度为基准
	            narrowImgW = destImgW;
	            narrowImgH = ( int) (((float )destImgW / (float)srcImgW)*srcImgH);
	            int cutNarrowImgSize = (narrowImgH - destImgH)/2;

	            BufferedImage narrowImg = new BufferedImage(narrowImgW, narrowImgH,BufferedImage.TYPE_INT_RGB);
	            narrowImg.getGraphics().drawImage(bi.getScaledInstance(narrowImgW, narrowImgH, Image.SCALE_SMOOTH ), 0, 0, null);

	            Image image = narrowImg.getScaledInstance(narrowImgW, narrowImgH, Image.SCALE_DEFAULT );
	            CropImageFilter cropFilter = new CropImageFilter(0, cutNarrowImgSize, narrowImgW, narrowImgH-cutNarrowImgSize);
	            Image img = Toolkit.getDefaultToolkit().createImage( new FilteredImageSource(image.getSource(), cropFilter));
	            BufferedImage cutTopNarrowImg = new BufferedImage( narrowImgW, narrowImgH-cutNarrowImgSize,BufferedImage. TYPE_INT_RGB);
	            cutTopNarrowImg.getGraphics().drawImage(img, 0, 0, null);

	            image = cutTopNarrowImg.getScaledInstance(narrowImgW, narrowImgH-cutNarrowImgSize, Image. SCALE_DEFAULT);
	            cropFilter = new CropImageFilter(0, 0, narrowImgW, narrowImgH-cutNarrowImgSize*2);
	            img = Toolkit.getDefaultToolkit().createImage( new FilteredImageSource(image.getSource(), cropFilter));
	            BufferedImage cutBottomNarrowImg = new BufferedImage( narrowImgW, narrowImgH-cutNarrowImgSize*2,BufferedImage. TYPE_INT_RGB);
	            Graphics g = cutBottomNarrowImg.getGraphics();
	            g.drawImage(img, 0, 0, null);
	            g.dispose();
	            ImageIO. write(cutBottomNarrowImg, "JPEG", new File(destImgPath));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public static void main(String []args){
	    createThumb("G://3.png", "G://223.jpg", 320, 212);
	}


	}



