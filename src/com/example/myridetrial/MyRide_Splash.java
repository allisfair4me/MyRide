package com.example.myridetrial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MyRide_Splash extends Activity {

	

	@Override
	protected void onCreate(Bundle myRideStartState) {
		// TODO Auto-generated method stub
		super.onCreate(myRideStartState);
		setContentView(R.layout.myride_splash);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(5000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally {
					Intent openMainActivity = new Intent("com.example.myridetrial.MAINACTIVITY");
					startActivity(openMainActivity);
				}
			}
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
