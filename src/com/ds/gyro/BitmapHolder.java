/*
Copyright (c) 2012, Zubair Khan (governer@gmail.com) 
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    *
    *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.ds.gyro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * @author zkhan
 *
 */
public class BitmapHolder {
	
	/**
	 * 
	 */
	private Bitmap mBitmap = null;
	/**
	 * 
	 */
	private Canvas mCanvas = null;
	/**
	 * 
	 */
	private int mWidth = 0;
	/**
	 * 
	 */
	private int mHeight = 0;
	/**
	 * 
	 */
	private String mName = null;

	/**
	 * @param context
	 * @param id
	 */
	public BitmapHolder(Context context, int id) {
		try {
			mBitmap = BitmapFactory.decodeResource(context.getResources(), id);
		}
   		catch(OutOfMemoryError e){
  		  System.gc();
 		}
		if(null != mBitmap) {
			mWidth = mBitmap.getWidth();
			mHeight = mBitmap.getHeight();
		}
	}

	/**
	 * @param width
	 * @param height
	 */
	public BitmapHolder(int width, int height) {
		try {
			mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		}
   		catch(OutOfMemoryError e){
    		  System.gc();
   		}
		if(null != mBitmap) {
			mWidth = mBitmap.getWidth();
			mHeight = mBitmap.getHeight();
			mCanvas = new Canvas(mBitmap);
		}
	}

	/**
	 * 
	 */
	public void recycle() {
		if(null != mBitmap) {
			mBitmap.recycle();
		}
		mBitmap = null;
		mName = null;
		mWidth = 0;
		mHeight = 0;
		mCanvas = null;
	}
	
	/**
	 * @return
	 */
	public int getWidth() {
		return mWidth;
	}
	
	/**
	 * @return
	 */
	public int getHeight() {
		return mHeight;
	}

	/**
	 * @return
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}
	
	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public void drawBitmap(Bitmap bitmap, int x, int y) {
		if((null == bitmap) || (null == mCanvas)) {
			return;
		}
		mCanvas.drawBitmap(bitmap, x, y, null);
	}

	/**
	 * @param bitmap
	 * @param transform
	 */
	public void drawBitmap(Bitmap bitmap, Matrix transform) {
		if((null == bitmap) || (null == mCanvas)) {
			return;
		}
		
		mCanvas.drawBitmap(bitmap, transform, null);
	}

}
