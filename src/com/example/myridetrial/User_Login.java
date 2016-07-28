package com.example.myridetrial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class User_Login extends Activity implements View.OnClickListener {

	private boolean haveConnectedWifi = false;
	private boolean haveConnectedMobile = false;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void haveNetworkConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button logout = (Button) findViewById(R.id.logout);
		Button contactAdm = (Button) findViewById(R.id.contactAdm);
		Button myRides = (Button) findViewById(R.id.myRides);
		myRides.setOnClickListener(this);
		Button getBike = (Button) findViewById(R.id.getBike);
//		final boolean checkNet = haveNetworkConnection();

		getBike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				haveNetworkConnection();
				if (haveConnectedWifi == true || haveConnectedMobile == true) {
					Intent openMap = new Intent("com.example.myridetrial.GET_BIKE");
					startActivity(openMap);
				} else {
					Toast.makeText(getApplicationContext(), "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
				}
			}
		});

		contactAdm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openContactAdmin = new Intent("com.example.myridetrial.CONTACT_ADMIN");
				startActivity(openContactAdmin);
			}
		});

		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Logged out Successfully!", Toast.LENGTH_SHORT).show();
				Intent returnLogin = new Intent("com.example.myridetrial.MAINACTIVITY");
				startActivity(returnLogin);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button b = (Button) v;
		switch (b.getId()) {
		case R.id.myRides:
			Intent openMyRides = new Intent("com.example.myridetrial.CYCLE_MY_RIDES");
			startActivity(openMyRides);
			break;

		default:
			break;
		}
	}
}
