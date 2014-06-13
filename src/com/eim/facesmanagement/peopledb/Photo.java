package com.eim.facesmanagement.peopledb;

import android.graphics.Bitmap;

public class Photo {
	String url;
	Bitmap bitmap;
	
	public Photo(String url, Bitmap bitmap) {
		if (url == null && bitmap == null)
			throw new IllegalArgumentException("url and bitmap cannot be both null");

		this.url = url;
		this.bitmap = bitmap;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url == null && bitmap == null)
			throw new IllegalArgumentException("url and bitmap cannot be both null");
		
		this.url = url;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		if (url == null && bitmap == null)
			throw new IllegalArgumentException("url and bitmap cannot be both null");
		
		this.bitmap = bitmap;
	}

}