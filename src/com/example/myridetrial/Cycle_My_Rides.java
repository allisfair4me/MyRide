package com.example.myridetrial;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle.Delegate;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Cycle_My_Rides extends ActionBarActivity {

	ArrayList<HashMap<String, String>> myrides = Get_Bike.myRideList;
	
	ListView list;
	
	private String TAG_ID = "id";
	private String TAG_Start_time = "start_time";
	private String TAG_Source = "source";
	private String TAG_End_time = "end_time";
	private String TAG_Destination = "destination";
	private String TAG_Cycle_id = "cycle_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cycle_my_rides);
		
		list = (ListView) findViewById(R.id.listview);
		showRidesList();
		
	}
	
	void showRidesList(){
		ListAdapter adapter = new SimpleAdapter(Cycle_My_Rides.this, myrides, R.layout.ride_list, new String[]{TAG_Start_time,TAG_Source,TAG_End_time,TAG_Destination,TAG_Cycle_id}, new int[]{R.id.startTime,R.id.source,R.id.endTime,R.id.destination,R.id.cycle_id});
		list.setAdapter(adapter);
	}
}
