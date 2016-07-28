package com.example.myridetrial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.myridetrial.MainActivity;;

public class Get_Bike extends FragmentActivity implements LocationListener {

	// Get User Aadhaar
	private String user_aadhaar = new MainActivity().user_aadhaar;

	// authenticate ride flag
	private int authRide = 0;

	// Map Attributes
	private GoogleMap myMap;
	LocationManager lm;
	LatLng myLocation;
	String locTowers;
	double myLat;
	double myLong;
	// private double latitude;
	// private double longitude;

	// private String get_area_name;

	// JSon Attributes
	String myJSON;

	private static final String TAG_RESULTS = "result";
	private static final String TAG_ID = "dock_id";
	private static final String TAG_LATITUDE = "dock_latitude";
	private static final String TAG_LONGITUDE = "dock_longitude";
	private static final String TAG_AREA_NAME = "area_name";

	private String[][] locationList = new String[20][4];

	JSONArray locations = null;

	// random cycle number
	private String[] cycle_id_array = { "C01", "C02", "C03" };
	Random randomcycle = new Random();
	int select = randomcycle.nextInt(cycle_id_array.length);
	private String cycle_id = cycle_id_array[select];

	// get area name
	private EditText areaNameEdit;

	// Dock stations
	private String pointA = new String("PESITM");
	private double pointA_latitude = 13.963439;
	private double pointA_longitude = 75.509812;

	private String pointB = new String("PESITM SBM");
	private double pointB_latitude = 13.964286;
	private double pointB_longitude = 75.508421;

	private String pointC = new String("Government School");
	private double pointC_latitude = 13.959991;
	private double pointC_longitude = 75.515399;

	// Start-End Ride Flag
	private int rideFlag = 0;

	// ListAdapter Requirements
	public HashMap<String, String> rideList = new HashMap<String, String>();
	public String area = null;
	public String ride_cycle_id = null;
	public String ride_source = null;
	public String ride_destination = null;
	static public int ride_id = 0;
	public String start_time = null;
	public String end_time = null;
	static ArrayList<HashMap<String, String>> myRideList = new ArrayList<HashMap<String, String>>();

	public ListView myRides;

	private void authenticate(final String user_aadhaar) {
		class AuthAsync extends AsyncTask<String, Void, String> {

			private Dialog loadingDialog;
			String result = null;

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String u_aadhaar = params[0];

				InputStream is = null;
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_aadhaar", u_aadhaar));

				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://myridepes.esy.es/ride-authenticate.php"); // give
																										// PHP
																										// url
																										// here.

					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
					StringBuilder sb = new StringBuilder();

					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					result = sb.toString();

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return result;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				loadingDialog = ProgressDialog.show(Get_Bike.this, "Please Wait", "Loading...");
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				// System.out.println(result);
				String s = result.trim();
				loadingDialog.dismiss();
				if (s.equalsIgnoreCase("successrfid")) {
					authRide = 1;
				} else if (s.equalsIgnoreCase("failrfid")) {
					Toast.makeText(getApplicationContext(), "Please Swipe Again..."+s, Toast.LENGTH_SHORT).show();
				} else if (s.equalsIgnoreCase("fail")) {
					Toast.makeText(getApplicationContext(), "Please Swipe Again..."+s, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Please swipe your card..."+s, Toast.LENGTH_SHORT).show();
				}
			}
		}

		AuthAsync la = new AuthAsync();
		la.execute(user_aadhaar);

	}

	// ArrayList<HashMap<String, String>> locationList;

	/*
	 * //creates array of locations protected void createLocationList() { try {
	 * JSONObject jsonObj = new JSONObject(myJSON); locations =
	 * jsonObj.getJSONArray(TAG_RESULTS);
	 * 
	 * for (int i = 0; i < locations.length(); i++) { JSONObject c =
	 * locations.getJSONObject(i); String dock_id = c.getString(TAG_ID); String
	 * dock_latitude = c.getString(TAG_LATITUDE); String dock_longitude =
	 * c.getString(TAG_LONGITUDE); String dock_area_name =
	 * c.getString(TAG_AREA_NAME);
	 * 
	 * locationList[i][0] = dock_id; locationList[i][1] = dock_latitude;
	 * locationList[i][2] = dock_longitude; locationList[i][3] = dock_area_name;
	 * } } catch (JSONException e) { e.printStackTrace(); } }
	 */
	// get json from php at server side
	/*
	 * public void getData() { class GetDataJSON extends AsyncTask<String, Void,
	 * String> {
	 * 
	 * @Override protected String doInBackground(String... params) { // TODO
	 * Auto-generated method stub DefaultHttpClient httpclient = new
	 * DefaultHttpClient(new BasicHttpParams()); HttpPost httppost = new
	 * HttpPost("http://myridepes.esy.es/get_docking_station.php");
	 * 
	 * httppost.setHeader("Content-type", "application/json");
	 * 
	 * InputStream inputS = null; String result = null; try { HttpResponse
	 * response = httpclient.execute(httppost); HttpEntity entity =
	 * response.getEntity();
	 * 
	 * inputS = entity.getContent();
	 * 
	 * BufferedReader reader = new BufferedReader(new InputStreamReader(inputS,
	 * "UTF-8"), 8); StringBuilder sb = new StringBuilder();
	 * 
	 * String line = null; while ((line = reader.readLine()) != null) {
	 * 
	 * sb.append(line + "\n");
	 * 
	 * } result = sb.toString(); } catch (Exception e) {
	 * Toast.makeText(getApplicationContext(), "Oops!",
	 * Toast.LENGTH_SHORT).show(); } return result; }
	 * 
	 * @Override protected void onPostExecute(String result) { // TODO
	 * Auto-generated method stub myJSON = result; //createLocationList(); }
	 * 
	 * GetDataJSON g = new GetDataJSON(); g.execute(); } }
	 */
	/*
	 * protected void insertToDatabase(final String cycle_id,final String
	 * uri,final String area_name) { // TODO Auto-generated method stub class
	 * SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
	 * 
	 * private Dialog loadingDialog;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); }
	 * 
	 * 
	 * @Override protected String doInBackground(String... params) { String
	 * paramCycleid = params[0]; String paramArea = params[1]; //String
	 * paramLongitude = params[2]; //String paramArea = params[3];
	 * 
	 * 
	 * 
	 * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * nameValuePairs.add(new BasicNameValuePair("cycle_id", cycle_id));
	 * //nameValuePairs.add(new BasicNameValuePair("cycle_latitude",
	 * myLatitude)); //nameValuePairs.add(new
	 * BasicNameValuePair("cycle_longitude", myLongitude));
	 * nameValuePairs.add(new BasicNameValuePair("area_name", area_name)); try {
	 * HttpClient httpClient = new DefaultHttpClient(); HttpPost httpPost = new
	 * HttpPost(uri); httpPost.setEntity(new
	 * UrlEncodedFormEntity(nameValuePairs));
	 * 
	 * HttpResponse response = httpClient.execute(httpPost);
	 * 
	 * HttpEntity entity = response.getEntity();
	 * 
	 * } catch (ClientProtocolException e) {
	 * 
	 * } catch (IOException e) {
	 * 
	 * } return "Success"; }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * super.onPostExecute(result);
	 * 
	 * Toast.makeText(getApplicationContext(), result,
	 * Toast.LENGTH_LONG).show(); } } SendPostReqAsyncTask sendPostAsync = new
	 * SendPostReqAsyncTask(); sendPostAsync.execute(cycle_id,uri,area_name); }
	 * 
	 * private void startMyRide(){ String uri = new
	 * String("http://myridepes.esy.es/startmyride.php");
	 * insertToDatabase(cycle_id, uri, get_area_name); }
	 * 
	 * private void endMyTrip() { String uri = new
	 * String("http://myridepes.esy.es/endmyride.php");
	 * insertToDatabase(cycle_id, uri, get_area_name); }
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_bike);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		// getData();
		myMap = mapFragment.getMap();

		myMap.setMyLocationEnabled(true);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		locTowers = lm.getBestProvider(criteria, true);
		Location location = lm.getLastKnownLocation(locTowers);
		if (location != null) {
			onLocationChanged(location);
		}
		lm.requestLocationUpdates(locTowers, 1000, 0, this);

		myMap.addMarker(new MarkerOptions().position(new LatLng(pointA_latitude, pointA_longitude)).title(pointA));
		myMap.addMarker(new MarkerOptions().position(new LatLng(pointB_latitude, pointB_longitude)).title(pointB));
		myMap.addMarker(new MarkerOptions().position(new LatLng(pointC_latitude, pointC_longitude)).title(pointC));

		areaNameEdit = (EditText) findViewById(R.id.areaName);

		Button startMyRide = (Button) findViewById(R.id.startRide);
		startMyRide.setOnClickListener(onClickListener);
		Button endMyRide = (Button) findViewById(R.id.endRide);
		endMyRide.setOnClickListener(onClickListener);

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);

		myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		myMap.animateCamera(CameraUpdateFactory.zoomTo(16));
		// String loc = "Lat: " + latitude + "\nLong: " + longitude;
		// Toast.makeText(getApplicationContext(), loc,
		// Toast.LENGTH_SHORT).show();
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
	/*
	 * @Override public void onMapReady(GoogleMap googleMap) { // TODO
	 * Auto-generated method stub
	 * 
	 * double[][] dock_lat_lng = null; int dock_count = 0;
	 * 
	 * for (int i = 0; i < 20; i++) { for (int j = 0; j < 2; j++) {
	 * dock_lat_lng[i][0] = Double.parseDouble(locationList[i][1]);
	 * dock_lat_lng[i][1] = Double.parseDouble(locationList[i][2]); }
	 * dock_count++; }
	 * 
	 * for (int i = 0; i < dock_count; i++) { googleMap.addMarker(new
	 * MarkerOptions().position(new LatLng(dock_lat_lng[i][0],
	 * dock_lat_lng[i][1])) .title(locationList[i][3])); } }
	 * 
	 */

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Button b = (Button) v;
			//area = areaNameEdit.getText().toString();
			switch (v.getId()) {
			case R.id.startRide:
				authenticate(user_aadhaar);
				if (authRide == 1) {
					area = areaNameEdit.getText().toString();
					if (area.equalsIgnoreCase(pointA) || area.equalsIgnoreCase(pointB) || area.equalsIgnoreCase(pointC)) {
						rideFlag = 1;
						if (rideFlag == 1) {
							start_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.format(Calendar.getInstance().getTime());
							Toast.makeText(getApplicationContext(), "Your ride has started...", Toast.LENGTH_SHORT)
									.show();
							if (area.equalsIgnoreCase("PESITM")) {
								ride_id++;
								ride_source = pointA;
							} else if (area.equalsIgnoreCase("PESITM SBM")) {
								ride_id++;
								ride_source = pointB;
							} else if (area.equalsIgnoreCase("Government School")) {
								ride_id++;
								ride_source = pointC;
							}
						} else {
							Toast.makeText(getApplicationContext(), "Please end your current ride...",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "Please enter source area source seen on map...",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please authenticate...", Toast.LENGTH_SHORT).show();
				}
				areaNameEdit.setText(null);
				break;
			case R.id.endRide:
				// endMyRide();
				area = areaNameEdit.getText().toString();
				if (rideFlag == 0) {
					Toast.makeText(getApplicationContext(), "Please Start a Ride...", Toast.LENGTH_SHORT).show();
				} else {
					// String area = areaname.getText().toString();
					// areaname.setText("");
					end_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
					if (area.equalsIgnoreCase("PESITM") || area.equalsIgnoreCase("PESITM SBM")
							|| area.equalsIgnoreCase("Government School")) {
						if (area.equalsIgnoreCase(pointA)) {
							String ride_id_st = Integer.toString(ride_id);
							ride_destination = pointA;
							ride_cycle_id = cycle_id;

							rideList.put("id", ride_id_st);
							rideList.put("start_time", start_time);
							rideList.put("source", ride_source);
							rideList.put("end_time", end_time);
							rideList.put("destination", ride_destination);
							rideList.put("cycle_id", ride_cycle_id);

							myRideList.add(rideList);
							Toast.makeText(getApplicationContext(), "Your ride has ended...Thank You!",
									Toast.LENGTH_SHORT).show();

						} else if (area.equalsIgnoreCase(pointB)) {
							String ride_id_st = Integer.toString(ride_id);
							ride_destination = pointB;
							ride_cycle_id = cycle_id;

							rideList.put("id", ride_id_st);
							rideList.put("start_time", start_time);
							rideList.put("source", ride_source);
							rideList.put("end_time", end_time);
							rideList.put("destination", ride_destination);
							rideList.put("cycle_id", ride_cycle_id);

							myRideList.add(rideList);
							Toast.makeText(getApplicationContext(), "Your ride has ended...Thank You!",
									Toast.LENGTH_SHORT).show();

						} else if (area.equalsIgnoreCase(pointC)) {
							String ride_id_st = Integer.toString(ride_id);
							ride_destination = pointC;
							ride_cycle_id = cycle_id;

							rideList.put("id", ride_id_st);
							rideList.put("start_time", start_time);
							rideList.put("source", ride_source);
							rideList.put("end_time", end_time);
							rideList.put("destination", ride_destination);
							rideList.put("cycle_id", ride_cycle_id);

							myRideList.add(rideList);
							Toast.makeText(getApplicationContext(), "Your ride has ended...Thank You!",
									Toast.LENGTH_SHORT).show();

						}
					} else {
						Toast.makeText(getApplicationContext(), "Please enter area destination seen on map...",
								Toast.LENGTH_SHORT).show();
					}
				}
				areaNameEdit.setText(null);
				break;
			}

		}
	};
}