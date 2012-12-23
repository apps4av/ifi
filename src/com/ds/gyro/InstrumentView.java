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

import com.ds.gyro.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class InstrumentView extends View implements OnTouchListener {

	/**
	 * 
	 */
	private float dimxAiin;
	/**
	 * 
	 */
	private float dimyAiin;
	/**
	 * 
	 */
	private float dimxAiout;
	/**
	 * 
	 */
	private float dimyAiout;
	/**
	 * 
	 */
	private float dimxAipitch;
	/**
	 * 
	 */
	private float dimyAipitch;
	/**
	 * 
	 */
	private float dimxMin;
	/**
	 * 
	 */
	private float dimyMin;
	/**
	 * 
	 */
	private float dimxHi;
	/**
	 * 
	 */
	private float dimyHi;
	/**
	 * 
	 */
	private float dimxHiplane;
	/**
	 * 
	 */
	private float dimyHiplane;
	/**
	 * 
	 */
	private float width;
	/**
	 * 
	 */
	private float height;
	/**
	 * 
	 */
	private float scale;
	/**
	 * 
	 */
	private Paint paint;

    /**
     * 
     */
    private BitmapHolder aiinBitmap;
	/**
	 * 
	 */
	private BitmapHolder aioutBitmap;
	/**
	 * 
	 */
	private BitmapHolder aipitchBitmap;
	/**
	 * 
	 */
	private BitmapHolder hiBitmap;
	/**
	 * 
	 */
	private BitmapHolder aiinbBitmap;
	/**
	 * 
	 */
	private BitmapHolder hiplaneBitmap;
	/**
	 * 
	 */
	private BitmapHolder minBitmap;
    /**
     * 
     */
    private Matrix transform;
    /**
     * 
     */
    private double coordPitch = 0;
    /**
     * 
     */
    private double coordRoll = 0;
    /**
     * 
     */
    private double coordPitchLast = 0;
    /**
     * 
     */
    private double coordRollLast = 0;
    /**
     * 
     */
    private double currentYaw = 0;
    /**
     * 
     */
    private double CurrentPitch = 0;
    /**
     * 
     */
    private double CurrentRoll = 0;
    /**
     * 
     */
    private double coordYaw = 0;
    /**
     * 
     */
    private double coordYawLast = 0;
    
    /**
     * 
     */
    private GestureDetector mGestureDetector;
    
    /**
     * 
     */
    private double mPitchAdjust = 0;
    
    /**
     * 
     */
    private double mRollAdjust = 0;

    /**
     * 
     */
    private double mYawAdjust = 0;
    
    /**
     * 
     */
    private double mXLast = 0;
    
    /**
     * private
     */
    
	/**
	 * @param context
	 */
	public InstrumentView(Context context) {
		super(context);
	    paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Align.CENTER);
	    aiinBitmap = new BitmapHolder(context, R.drawable.ai_in);
	    aioutBitmap = new BitmapHolder(context, R.drawable.ai_out);
	    aiinbBitmap = new BitmapHolder(context, R.drawable.ai_inb);
	    aipitchBitmap = new BitmapHolder(context, R.drawable.ai_in);
	    hiBitmap = new BitmapHolder(context, R.drawable.hi);
	    hiplaneBitmap = new BitmapHolder(context, R.drawable.hi_plane);
	    minBitmap = new BitmapHolder(context, R.drawable.ai_marking);
		dimxAiin = aiinBitmap.getWidth();
		dimyAiin = aiinBitmap.getHeight();
		dimxAiout = aioutBitmap.getWidth();
		dimyAiout = aioutBitmap.getHeight();
		dimxAipitch = aipitchBitmap.getWidth();
		dimyAipitch = aipitchBitmap.getHeight();
		dimxMin = minBitmap.getWidth();
		dimyMin = minBitmap.getHeight();
		dimxHi = hiBitmap.getWidth();
		dimyHi = hiBitmap.getHeight();
		dimxHiplane = hiplaneBitmap.getWidth();
		dimyHiplane = hiplaneBitmap.getHeight();
		transform = new Matrix();
		
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context, new GestureListener());
	}

    /**
     * @param canvas
     */
    private void drawInstruments(Canvas canvas) {
		width = getWidth();
		height = getHeight();
		scale = height / (dimyAiout + dimyHi);

    	transform.setRotate(0, dimxAiin / 2.f, dimyAiin / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxAiin / 2.f * scale, 0.f);
        canvas.drawBitmap(aiinBitmap.getBitmap(), transform, paint);

    	transform.setRotate((float)coordRoll, dimxAipitch / 2.f, dimyAipitch / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxAipitch / 2.f * scale, (float)coordPitch * scale);
        canvas.drawBitmap(aipitchBitmap.getBitmap(), transform, paint);

    	transform.setRotate(0, dimxAiin / 2.f, dimyAiin / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxAiin / 2.f * scale, 0.f);
        canvas.drawBitmap(aiinbBitmap.getBitmap(), transform, paint);
        
    	transform.setRotate((float)coordRoll, dimxAiout / 2.f, dimyAiout / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxAiout / 2.f * scale, 0);
        canvas.drawBitmap(aioutBitmap.getBitmap(), transform, paint);

    	transform.setRotate(0, dimxMin / 2.f, dimyMin / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxMin / 2.f * scale, 0);
        canvas.drawBitmap(minBitmap.getBitmap(), transform, paint);

    	transform.setRotate((float)coordYaw, dimxHi / 2.f, dimyHi / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxHi / 2.f * scale, dimyAiout * scale);
        canvas.drawBitmap(hiBitmap.getBitmap(), transform, paint);
  
    	transform.setRotate(0, dimxHiplane / 2.f, dimyHiplane / 2.f);
        transform.postScale(scale, scale);
        transform.postTranslate(width / 2.f - dimxHiplane / 2.f * scale, dimyAiout * scale);
        canvas.drawBitmap(hiplaneBitmap.getBitmap(), transform, paint);
    }

	/**
	 * @param yaw
	 * @param pitch
	 * @param roll
	 */
	private void updateCoordinates(float roll, float pitch, float yaw) {
    	boolean inv = false;
    	CurrentPitch = pitch;
        CurrentRoll = -roll;
        currentYaw = yaw;
    	
        /*
         * Limit amount of invalidates and adjust on user input
         */
    	coordYaw = (currentYaw - mYawAdjust) % 360;
    	coordPitch = (CurrentPitch - mPitchAdjust) % 360;
    	coordRoll = (CurrentRoll - mRollAdjust) % 360;
    	
    	if(Math.abs(coordPitchLast - coordPitch) >= 1) {
    		coordPitchLast = coordPitch;
    		inv = true;
    	}
    	
    	if(Math.abs(coordRollLast - coordRoll) >= 1) {
    		coordRollLast = coordRoll;
    		inv = true;
    	}

    	if(Math.abs(coordYawLast - coordYaw) >= 1) {
    		coordYawLast = coordYaw;
    		inv = true;
    	}
    	
    	if(inv) {
    		invalidate();
    	}
	}

    /* (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
        drawInstruments(canvas);
    }

	/**
	 * @param rpy
	 * @param q
	 * http://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
	 */
	private void getRollPitchYaw(float rpy[], float q[]) {
		rpy[0] = (float)Math.atan2(2 * (q[0] * q[1] + q[2] * q[3]), 1 - 2 * (q[1] * q[1] + q[2] * q[2])) * 57.3f;
		rpy[1] = (float)Math.asin(2 * (q[0] * q[2] - q[3] * q[1])) * 57.3f;
		rpy[2] = (float)Math.atan2(2 * (q[0] * q[3] + q[1] * q[2]), 1 - 2 * (q[2] * q[2] + q[3] * q[3])) * 57.3f;
	}
	
	/**
	 * @param q
	 */
	void setQuarterion(float q[], int order) {
	    float[] angles = new float[3];
		getRollPitchYaw(angles, q);
		
		switch(order) {
			case 0:
				updateCoordinates(angles[1], angles[0], angles[2]);
				break;
			case 1:
				updateCoordinates(angles[1], angles[2], angles[0]);
				break;
			case 2:
				updateCoordinates(angles[0], angles[1], angles[2]);
				break;
			case 3:
				updateCoordinates(angles[0], angles[2], angles[1]);
				break;
			case 4:
				updateCoordinates(angles[2], angles[1], angles[0]);
				break;
			case 5:
				updateCoordinates(angles[2], angles[0], angles[1]);
				break;
		}
	}

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        double x = e.getX();
        double y = e.getY();
        if(y > height / 2.f) {
            /*
             * This is HI area
             */
            
            if(y < height * 3 / 4) {
                if(mXLast < x) {
                    mYawAdjust -= 2;
                }
                else if (mXLast > x) {
                    mYawAdjust += 2; 
                }
            }
            else {
                if(mXLast > x) {
                    mYawAdjust -= 2;
                }
                else if (mXLast < x) {
                    mYawAdjust += 2; 
                }                
            }
            
            mXLast = e.getX();
        }
        return true;
    }
    
    /**
     * Reset all adjustments
     */
    public void reset() {
        mYawAdjust = 0;
        mPitchAdjust = 0;
        mRollAdjust = 0;
    }
    
    /**
     * @author zkhan
     *
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /* (non-Javadoc)
         * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTap(android.view.MotionEvent)
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        /* (non-Javadoc)
         * @see android.view.GestureDetector.SimpleOnGestureListener#onLongPress(android.view.MotionEvent)
         */
        @Override
        public void onLongPress(MotionEvent e) {
            /*
             * Long press on AI area will reset it
             */            
            if(e.getY() < height / 2.f) {
                mPitchAdjust = CurrentPitch;
                mRollAdjust = CurrentRoll;
            }
        }        
    }
}
