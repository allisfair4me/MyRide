package com.example.myridetrial;

import android.support.v7.app.ActionBarActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R.string;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private EditText myAadhar;
	private EditText myPassword;

	private boolean haveConnectedWifi = false;
	private boolean haveConnectedMobile = false;

	public static final String USER_AADHAAR = "user_aadhaar";

	public String user_aadhaar;
	public String user_password;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button myLogin = (Button) findViewById(R.id.myLogin);
		Button myForgotPwd = (Button) findViewById(R.id.myForgot);
		Button myRegister = (Button) findViewById(R.id.myRegister);
		Button myWork = (Button) findViewById(R.id.myWork);
		myAadhar = (EditText) findViewById(R.id.myAadhar);
		myPassword = (EditText) findViewById(R.id.myPassword);

		myLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user_aadhaar = myAadhar.getText().toString();
				user_password = myPassword.getText().toString();

				Pattern aadhaar_pattern = Pattern.compile("[0-9]{12}");
				Matcher m1 = aadhaar_pattern.matcher(user_aadhaar);
				Boolean aadhaar_matches = m1.matches();
				
				haveNetworkConnection();

				if (user_aadhaar == null || user_password == null) {
					Toast.makeText(getApplicationContext(), " Please enter aadhaar number or password... ",
							Toast.LENGTH_SHORT).show();
				} else if (user_aadhaar.equalsIgnoreCase("admin")) {
					if (user_password.equalsIgnoreCase("admin")) {
						Intent openAdminUse = new Intent("com.example.myridetrial.ADMIN_USE");
						Toast.makeText(getApplicationContext(), " Welcome Admin! ", Toast.LENGTH_SHORT).show();
						startActivity(openAdminUse);
					}
				} else if (aadhaar_matches == false) {
					Toast.makeText(getApplicationContext(), " Please check aadhaar number or password... ",
							Toast.LENGTH_SHORT).show();
				} else if(haveConnectedWifi == true || haveConnectedMobile == true){
					login(user_aadhaar, user_password);
				} else {
					Toast.makeText(getApplicationContext(), "Please check your Internet connection...", Toast.LENGTH_SHORT).show();
					//Intent openuserlogin = new Intent("com.example.myridetrial.USER_LOGIN");
					//Toast.makeText(getApplicationContext(), " You have logged in successfully! ", Toast.LENGTH_SHORT)
					//		.show();
					//startActivity(openuserlogin);
				}
			}
		});

		myRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openUserRegister = new Intent("com.example.myridetrial.USER_REGISTER");
				startActivity(openUserRegister);
			}
		});

	}

	private void login(final String user_aadhaar, String user_password) {
		class LoginAsync extends AsyncTask<String, Void, String> {

			private Dialog loadingDialog;
			String result = null;

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String u_aadhaar = params[0];
				String u_password = params[1];

				InputStream is = null;
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_aadhaar", u_aadhaar));
				nameValuePairs.add(new BasicNameValuePair("user_password", u_password));

				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://myridepes.esy.es/login-check.php"); // give
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
				loadingDialog = ProgressDialog.show(MainActivity.this, "Please Wait", "Loading...");
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				// System.out.println(result);
				String s = result.trim();
				loadingDialog.dismiss();
				if (s.equalsIgnoreCase("failure")) {
					// finish();
					Toast.makeText(getApplicationContext(), "Invalid User Name or Password " + s, Toast.LENGTH_LONG)
							.show();
				} else if(s.equalsIgnoreCase("success")){
					Toast.makeText(getApplicationContext(), " You have logged in successfully! " + s,
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent("com.example.myridetrial.USER_LOGIN");
					finish();
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), " Welcome Admin! " + s,
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent("com.example.myridetrial.ADMIN_USE");
					finish();
					startActivity(intent);
				}
			}
		}

		LoginAsync la = new LoginAsync();
		la.execute(user_aadhaar, user_password);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
