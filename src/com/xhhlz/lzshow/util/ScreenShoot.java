package com.xhhlz.lzshow.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;
import android.view.Window;

public class ScreenShoot {
	private int screenWidth;
	private int screenHeight;
	private Window window;
	private String fold;
	private String savePath;
	private String saveName;
	
	/**
	 * 
	 * @param width 
	 * @param height 
	 * @param mywindow   the window to get
	 * @param path   path for saving, e.g. "/LZShow/practice"
	 */
	
	public ScreenShoot(int width, int height, Window mywindow, String path)
	{
		this.screenHeight = height;
		this.screenWidth = width;
		this.window = mywindow;
		this.fold = path;	
		
		// get the system time, modify the format to be used to name the file
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
		String temp = dateFormat.format(now);
		saveName = temp+".jpg";
	}
	
	/**
	 * get the screen picture
	 * @return 0 capture the screen normally 1 failed to capture the screen 2 SD card not existing
	 */
	public int getCurrentImage()
	{
		// 1.create Bitmap
		int w = screenWidth;
		int h = screenHeight;

		Bitmap myBmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.get the screen
		View decorview = window.getDecorView();
		decorview.setDrawingCacheEnabled(true);
		myBmp = decorview.getDrawingCache();
		
		// get the height of the title bar
		Rect frame = new Rect();  
        window.getDecorView().getWindowVisibleDisplayFrame(frame);  
        int statusBarHeight = frame.top;  
  
        // delete the title bar //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);  
        Bitmap Bmp = Bitmap.createBitmap(myBmp, 0, statusBarHeight, w, h 
                - statusBarHeight); 
		
		savePath = getSDCardPath() + fold;
		if(getSDCardPath() == null)
			return 2;

		// 3.save as Bitmap
		try {
			File path = new File(savePath);
			if (!path.exists()) {
				path.mkdirs();
			}
		
			File file = new File(path, saveName);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	/**
	 * get the path of the SD card
	 * 
	 * @return if SD card existing, the path; if not, null
	 */
	private String getSDCardPath() {
		File sdcardDir = null;
		// to tell if SD card existing
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
			return sdcardDir.toString();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * get the path of the screen shot
	 * @return path of the screen shot
	 */
	public String getFilePath()
	{
		String path = savePath + '/' + saveName;
		return path;
	}
}
