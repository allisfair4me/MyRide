package com.example.myridetrial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Admin_Use extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_use);
		
		Button get_bike = (Button) findViewById(R.id.getBike);
		Button myRides = (Button) findViewById(R.id.myRides);
		Button adminWork = (Button) findViewById(R.id.admStuff);
		Button editPtofile = (Button) findViewById(R.id.edit);
		Button logout = (Button) findViewById(R.id.logout);
		
		myRides.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openMyRides = new Intent("com.example.myridetrial.CYCLE_MY_RIDES");
				startActivity(openMyRides);	
			}
		});
		
		adminWork.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openAdminOptions = new Intent("com.example.myridetrial.ADMIN_OPTIONS");
				startActivity(openAdminOptions);
			}
		});
		
		get_bike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openMap = new Intent("com.example.myridetrial.GET_BIKE");
				startActivity(openMap);
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Logged out Successfully!", Toast.LENGTH_SHORT).show();
				Intent returnLogin = new Intent("com.example.myridetrial.MAINACTIVITY");
				finish();
				startActivity(returnLogin);
			}
		});
	}

	
}
