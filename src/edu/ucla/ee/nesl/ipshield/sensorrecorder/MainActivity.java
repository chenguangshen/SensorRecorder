package edu.ucla.ee.nesl.ipshield.sensorrecorder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.ucla.ee.nesl.ipshield.sensorrecorder.MainService.LocalBinder;
import edu.ucla.nesl.mca.R;

public class MainActivity extends Activity {	
	private MainService mService;
    private boolean mBound = false;
    
    @Override
	protected void onStart() {
    	super.onStart();
		Intent intent = new Intent(this, MainService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_main);
        final Context context = this;
        
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mBound) {
					mService.startRecording();
					Toast.makeText(context, "start recording data", Toast.LENGTH_SHORT).show();
				}
			}        	
        });
        
        
        Button button2 = (Button)findViewById(R.id.ButtonStop);
        button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBound) {
					mService.stopRecording();
					//Toast.makeText(context, "stop service", Toast.LENGTH_SHORT).show();
				}
			}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
	protected void onResume() { 
    	super.onResume(); 
    }
    
    @Override
    protected void onPause() {
    	super.onResume(); 
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.init();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
