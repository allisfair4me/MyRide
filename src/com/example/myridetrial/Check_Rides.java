package com.example.myridetrial;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Check_Rides extends FragmentActivity implements LocationListener {

	double ride_location[][] = { { 13.962390, 75.509088 }, { 13.962764, 75.511144 } };

	private GoogleMap myMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_rides);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		myMap = mapFragment.getMap();
		
		LatLng first_loc = new LatLng(ride_location[0][0],ride_location[0][1]);
		
		Button back = (Button) findViewById(R.id.backToAdmin);
		back.setOnClickListener(onClickListener);
		myMap.addMarker(new MarkerOptions().position(new LatLng(ride_location[0][0],ride_location[0][1])).title("C01"));
		myMap.addMarker(new MarkerOptions().position(new LatLng(ride_location[1][0],ride_location[1][1])).title("C02"));
		
		myMap.moveCamera(CameraUpdateFactory.newLatLng(first_loc));
		myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.backToAdmin:
				Intent openAdminOptions = new Intent("com.example.myridetrial.ADMIN_OPTIONS");
				startActivity(openAdminOptions);
				finish();
			}
		}

	};

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
