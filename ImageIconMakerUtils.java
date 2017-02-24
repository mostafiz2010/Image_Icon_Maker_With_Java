package de.ianus.access.web.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageIconMakerUtils {

	private final File ICON_FOlDER_PATH;
	private final File IMAGE_FOLDER_PATH;
	private static final String ICON_FOlDER_NAME = File.separator + "test2";
	private static final int IMG_WIDTH = 150;
	private static final int IMG_HEIGHT = 150;
	
	
	public ImageIconMakerUtils(String dataFolderPath) {
		IMAGE_FOLDER_PATH = new File(dataFolderPath);
		ICON_FOlDER_PATH = new File(newIconFolderPath(IMAGE_FOLDER_PATH));
	}

	private String newIconFolderPath(File imageFolder) {
		return imageFolder.getAbsolutePath() + ICON_FOlDER_NAME;
	}
	
	private void createIconFromImage() {
		if(!ICON_FOlDER_PATH.exists()){
			ICON_FOlDER_PATH.mkdirs();
		}
		for(File imageFile : IMAGE_FOLDER_PATH.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File f, String s) {	
				return s.toLowerCase().endsWith(".jpg") || s.toLowerCase().endsWith(".png");
			}
		})){	
		//imageToIconMakerEngine(imageFile, new File(createIconName(imageFile)), IMG_HEIGHT, IMG_WIDTH);
			if(!imageToIconMakerEngine(imageFile, new File(createIconName(imageFile)), IMG_HEIGHT, IMG_WIDTH))
			break;	
		}
		System.out.println("Icon Creation DONE");
	}

	private String createIconName(File image) {
		return ICON_FOlDER_PATH.getAbsolutePath()
				+ File.separator
				+ image.getName()
				;
	}

	private boolean imageToIconMakerEngine(File imageFile, File imageIconAddress, int imgHeight, int imgWidth) {
		
		boolean result = false;
		final BufferedImage iconSize = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2d = iconSize.createGraphics();
		graphics2d.setComposite(AlphaComposite.Src);
		
		//below three lines are for RenderingHints for better image quality at cost of higher processing time
//		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
//		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		BufferedImage originalImage;
		try {
			originalImage = ImageIO.read(imageFile);
			if(originalImage != null){
				graphics2d.drawImage(
						originalImage
						, 0, 0, iconSize.getWidth()-1, iconSize.getHeight()-1
						, 0, 0, originalImage.getWidth()-1, originalImage.getHeight()-1
						, null
					);
				graphics2d.dispose();
			result = ImageIO.write(iconSize, "PNG", imageIconAddress);
			System.out.println(imageIconAddress);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		try{
			ImageIconMakerUtils factory = new ImageIconMakerUtils("/Users/mostafizur/Desktop/test/pic");
			factory.createIconFromImage();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
