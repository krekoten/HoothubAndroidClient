/**
 * 
 */
package com.hoothub.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class URLFetcher {
	
	WeakHashMap<String, Bitmap> bitmapCache = new WeakHashMap<String, Bitmap>(30);
	
	public URLFetcher() {
		
	}
	
	public Bitmap fetchImage(String imageUrl) {
		Bitmap bitmap = bitmapCache.get(imageUrl);
		if (bitmap == null) {
			URL url;
			try {
				url = new URL(imageUrl);
			} catch (MalformedURLException exception1) {
				// TODO Auto-generated catch block
				exception1.printStackTrace();
				return null;
			}
			URLConnection connection;
			try {
				connection = url.openConnection();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
				return null;
			}
			connection.setUseCaches(true);
			Object response;
			try {
				response = connection.getContent();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
				return null;
			}
			bitmap = BitmapFactory.decodeStream((InputStream) response);
			bitmapCache.put(imageUrl, bitmap);
		}
		
		return bitmap;
	}
}
