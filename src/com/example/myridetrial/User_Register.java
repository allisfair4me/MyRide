package com.example.myridetrial;

import java.io.IOException;
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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class User_Register extends Activity {

	private EditText aadhaar;
	private EditText mobile_number;
	private EditText email_id;
	private EditText password;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		final EditText aadhaar = (EditText) findViewById(R.id.aadharNo);
		final EditText mobile_number = (EditText) findViewById(R.id.mobileNo);
		final EditText email_id = (EditText) findViewById(R.id.emailAdd);
		final EditText password = (EditText) findViewById(R.id.userPwd);

		Button clickRegister = (Button) findViewById(R.id.register);

		final Intent openMyself = new Intent("com.example.myridetrial.USER_REGISTRATION");

		clickRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String aadhaar_number = aadhaar.getText().toString();
				String mobile_num = mobile_number.getText().toString();
				String email = email_id.getText().toString();
				String user_password = password.getText().toString();
				
				Pattern aadhaar_pattern = Pattern.compile("[0-9]{12}");
				Matcher m1 = aadhaar_pattern.matcher(aadhaar_number);
				Boolean aadhaar_matches = m1.matches();
				Pattern mobile_pattern = Pattern.compile("[7|8|9][0-9]{9}");
				Matcher m2 = mobile_pattern.matcher(mobile_num);
				Boolean mobile_matches = m2.matches();
				Pattern email_pattern = Pattern.compile("[a-z|A-Z|0-9]*[@].*");
				Matcher m3 = mobile_pattern.matcher(email);
				Boolean email_matches = m2.matches();

				if (aadhaar_number == null || mobile_num == null || email == null || user_password == null) {
					Toast.makeText(getApplicationContext(), " Please fill all required details... ", Toast.LENGTH_SHORT)
							.show();
				} else if(aadhaar_matches == false){
					Toast.makeText(getApplicationContext(), " Invalid Aadhaar Number ", Toast.LENGTH_SHORT)
					.show();
				} else if(mobile_matches == false){
					Toast.makeText(getApplicationContext(), " Invalid Mobile Number ", Toast.LENGTH_SHORT)
					.show();
				}else if(email_matches == false){
					Toast.makeText(getApplicationContext(), " Invalid Email Address ", Toast.LENGTH_SHORT)
					.show();
				}else{
					insertToDatabase(aadhaar_number, mobile_num, email, user_password);
				}
			}
		});

	}

	protected void insertToDatabase(final String aadhaar_number, final String mobile_num, final String email,
			final String user_password) {
		// TODO Auto-generated method stub
		class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

			private Dialog loadingDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingDialog = ProgressDialog.show(User_Register.this, "Please wait", "Loading...");
			}

			@Override
			protected String doInBackground(String... params) {
				String paramAadhaar = params[0];
				String paramMobile = params[1];
				String paramEmail = params[2];
				String paramPassword = params[3];
				String paramAdmin = "N";

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("aadhaar_number", aadhaar_number));
				nameValuePairs.add(new BasicNameValuePair("mobile_number", mobile_num));
				nameValuePairs.add(new BasicNameValuePair("email_id", email));
				nameValuePairs.add(new BasicNameValuePair("password", user_password));
				nameValuePairs.add(new BasicNameValuePair("admin", paramAdmin));

				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://myridepes.esy.es/register-user.php");
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();

				} catch (ClientProtocolException e) {

				} catch (IOException e) {

				}
				return "Success";
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				loadingDialog.dismiss();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				Intent openUserLogin = new Intent("com.example.myridetrial.USER_LOGIN");
				startActivity(openUserLogin);
			}
		}
		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(aadhaar_number, mobile_num, email, user_password);
	}

}
