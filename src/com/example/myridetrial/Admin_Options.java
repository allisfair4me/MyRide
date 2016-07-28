package com.example.myridetrial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_Options extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_options);
		
		Button trackRides = (Button) findViewById(R.id.allRides);
		Button trackOneRide = (Button) findViewById(R.id.trackBike);
		Button checkDock = (Button) findViewById(R.id.checkDock);
		Button findUser = (Button) findViewById(R.id.checkUser);
		Button adminEdit = (Button) findViewById(R.id.admEdit);
		Button adminLogout = (Button) findViewById(R.id.admlogout);
		
		trackRides.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openCheckRides = new Intent("com.example.myridetrial.CHECK_RIDES");
				startActivity(openCheckRides);
				finish();
			}
		});
		
		adminLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openMain = new Intent("com.example.myridetrial.MAINACTIVITY");
				startActivity(openMain);
				finish();
			}
		});
	}
}
