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

import java.util.List;

import com.ds.gyro.Preferences;
import com.ds.gyro.PrefActivity;
import com.ds.gyro.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends Activity implements SensorEventListener {

	/**
	 * 
	 */
	private SensorManager sm;
	/**
	 * 
	 */
	private InstrumentView view;

	/**
	 * 
	 */
	private AlertDialog alertDialog;
	
	/**
	 * 
	 */
	private Preferences mPreferences;
	
	/**
	 * 
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(0 == msg.what) {
    			SensorEvent event;
    			event = (SensorEvent)msg.obj;
    			switch (event.sensor.getType()) {
    				case Sensor.TYPE_ROTATION_VECTOR:
    					float q[] = new float[4];
    					SensorManager.getQuaternionFromVector(q, event.values);
    					view.setQuarterion(q, mPreferences.getOrder());
    					break;
    			}
			}
			else {
			    view.reset();
			}
		}
	};

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> typedSensors = sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
        if ((typedSensors == null) || (typedSensors.size() <= 0)) {
        }
        else {
        	sm.registerListener(this, typedSensors.get(0),
        			SensorManager.SENSOR_DELAY_FASTEST);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = new InstrumentView(this);
        view.setBackgroundColor(Color.BLACK);
        LinearLayout layout = new LinearLayout(this);
        layout.addView(view);
        setContentView(layout);
        mPreferences = new Preferences(this);

        /*
         * Keep screen on
         */
        if(mPreferences.shouldScreenStayOn()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);            
        }

		alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.Warning));
        alertDialog.setMessage(getString(R.string.Warn));
        alertDialog.show();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
		super.onPause();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
		super.onDestroy();
		sm.unregisterListener(this);
		if(alertDialog != null) {
			alertDialog.dismiss();
		}
    }

    /* (non-Javadoc)
     * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
    /* (non-Javadoc)
     * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
     */
    public void onSensorChanged(SensorEvent event) {
    	Message msg = handler.obtainMessage();
    	msg.what = 0;
    	msg.obj = event;
    	handler.sendMessage(msg);
	}
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    		case R.id.about:
                String url = getString(R.string.help);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
    	       
    		case R.id.settings:
                startActivity(new Intent(this, PrefActivity.class));
                break;
                
    		case R.id.reset:
    	        Message msg = handler.obtainMessage();
    	        msg.what = 1;
    	        handler.sendMessage(msg);
    	        
    	        Toast.makeText(this, getString(R.string.ResetHelp), Toast.LENGTH_LONG).show();
                break;
        }
    	return true;
    }
}
