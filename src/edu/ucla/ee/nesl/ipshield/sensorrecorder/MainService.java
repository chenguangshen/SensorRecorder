package edu.ucla.ee.nesl.ipshield.sensorrecorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service implements SensorEventListener {
	public static final String TAG = "MainService";
	private final IBinder mBinder = new LocalBinder();
	private SensorManager mSensorManager;
	private Sensor mAcc;
	private ArrayList<SensorEvent> sensorBuffer;
	
	public class LocalBinder extends Binder {
		MainService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MainService.this;
        }
    }
	
	public void init() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAcc = (Sensor) mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorBuffer = new ArrayList<SensorEvent>();
	}

	public void startRecording() {
		mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void stopRecording() {
		mSensorManager.unregisterListener(this);
		File file = null;
		File dir = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/sensor_recording");
		dir.mkdirs();
		file = new File(dir, "SensorRecording_" + new Date().toString() + ".txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (SensorEvent event:sensorBuffer) {
				bw.append(event.values[0] + ","+ event.values[1] + "," + event.values[2] + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TAG, "data size = " + sensorBuffer.size());
		sensorBuffer.clear();
		
		Toast.makeText(this, "data saved in /sdcard/sensor_recording/" + file.getName(), Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		sensorBuffer.add(event);
	}
	

}
