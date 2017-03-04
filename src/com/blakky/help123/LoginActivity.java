package com.blakky.help123;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blakky.helper.SQLiteHandler;
import com.blakky.helper.SessionManager;

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements OnClickListener {
	// private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	EditText inputEmail = null;
	EditText inputPassword = null;
/*	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;*/
	String email, password, result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		final String DEFAULT = "N/A";
		SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
		String name = sharedpreferences.getString("name", DEFAULT);
		String usertype = sharedpreferences.getString("usertype", DEFAULT);
		boolean isLoggedIn = sharedpreferences.getBoolean("isLoggedIn", false);
		if (isLoggedIn) {
			if(usertype.equals("Refugee")){
			Intent openActivity = new Intent(LoginActivity.this,MainActivity_Refugee.class);
			startActivity(openActivity);
			finish();
			}else{
				Intent openActivity = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(openActivity);
				finish();
			}
			
		}
		/*
		 * // Progress dialog pDialog = new ProgressDialog(this);
		 * pDialog.setCancelable(false);
		 * 
		 * // SQLite database handler db = new
		 * SQLiteHandler(getApplicationContext());
		 * 
		 * // Session manager session = new
		 * SessionManager(getApplicationContext());
		 * 
		 * // Check if user is already logged in or not if
		 * (session.isLoggedIn()) { // User is already logged in. Take him to
		 * main activity Intent intent = new Intent(LoginActivity.this,
		 * MainActivity.class); startActivity(intent); finish(); }
		 */

		// Login button Click Event
		btnLogin.setOnClickListener(this);

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.print("clickrd");
		switch (v.getId()) {

		case R.id.btnLogin:
			System.out.println("Entered OnClick");
			/*
			 * Intent openActivity; openActivity = new
			 * Intent(LoginActivity.this, MainActivity_Refugee.class); //Log.i(
			 * "LOGIN ACTIVIY", "ref 2"); startActivity(openActivity); //Log.i(
			 * "LOGIN ACTIVIY", "ref 3"); finish();
			 */
			 email = inputEmail.getText().toString().trim();
			password = inputPassword.getText().toString();

			if (email.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
			} else if (password.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Enter the password!", Toast.LENGTH_SHORT).show();
			} else {

				UserValidation validates = new UserValidation();
				validates.execute(email, password);
			}
			break;

		case R.id.btnLinkToRegisterScreen:
			Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
			startActivity(i);
			finish();
			break;
		}

	}

	private class UserValidation extends AsyncTask<String, String, String> {

		HttpClient httpClient;
		HttpResponse httpResponse;
		HttpPost httpPost;
		HttpEntity entity;
		InputStream isr;
		BufferedReader bReader;
		String line;
		String data;

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... params) {
			String username = params[0];
			String password1 = params[1];
			try {
//				String email = URLEncoder.encode(params[0].trim(), "UTF-8");
//				String password = URLEncoder.encode(params[1].trim(), "UTF-8");

				String toVerify = "email=" + username + "&password=" + password1;

				String fullURL = "http://omega.uta.edu/~sas4798/verify_password.php?" + toVerify;

				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(fullURL);
				System.out.println("httpPost is done");

				httpResponse = httpClient.execute(httpPost);
				System.out.println(httpResponse);

				entity = httpResponse.getEntity();
				if (entity != null) {
					isr = entity.getContent();
					System.out.println("byte - " + isr.available());
				}
			} catch (UnsupportedEncodingException e) {
				Log.e("log_tag", " Error in UnsupportedEncodingException - " + e.toString());
			} catch (ClientProtocolException e) {
				Log.e("log_tag", " Error in ClientProtocolException - " + e.toString());
			} catch (IOException e) {
				Log.e("log_tag", " Error in IOException - " + e.toString());
			} catch (Exception e) {
				Log.e("log_tag", " Error in Connection" + e.toString());
			}

			try {
				bReader = new BufferedReader(new InputStreamReader(isr), 8);
				line = null;
				while ((line = bReader.readLine()) != null) {
					data = line;
					Log.i("LoginActivity - ", "Data from omega " + line);
				}
				return data;
			} catch (IOException e) {
				Log.e("LoginActivity - ", "Error in ISR to String conversion - " + e.toString());
				return null;
			}
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("Printing the result " + result);

			Log.i("Login Activity", "on Post " + result);

			if (result.equalsIgnoreCase("0000")) {
				Toast.makeText(getApplicationContext(), "Username or Password does not exist!", Toast.LENGTH_LONG)
						.show();
			} else {

				Toast.makeText(getApplicationContext(), "Logging in...",
						Toast.LENGTH_SHORT).show();
				/*System.out.println("Exeucuting shared preferences");
				SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("name", inputEmail.getText().toString().trim());
				editor.putString("pass", inputPassword.getText().toString());
				editor.commit();*/
				System.out.println(result);

				String uid = result.split(",")[0].trim();
				String fullname = result.split(",")[1].trim();
				System.out.println(result);
				String username = result.split(",")[2].trim();
				String password = result.split(",")[3].trim();
				String usertype = result.split(",")[4].trim();
				String food = result.split(",")[5].trim();
				String house = result.split(",")[6].trim();
				String cloth = result.split(",")[7].trim();
				String furni = result.split(",")[8].trim();
				

				Log.i("Login Activity", "on Post " + username);
				Log.i("Login Activity", "on Post " + password);
				Log.i("Login Activity", "on Post " + usertype);

				Intent openActivity;
				if (usertype.equalsIgnoreCase("Charity")) {
					System.out.println("Exeucuting shared preferences");
					SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putString("name", inputEmail.getText().toString().trim());
					editor.putString("pass", inputPassword.getText().toString());
					editor.putString("usertype", usertype);
					editor.putString("uid", uid);
					editor.putString("fullname", fullname);
					editor.putBoolean("isLoggedIn", true);
					
					editor.commit();
					openActivity = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(openActivity);
					finish();
				} else if (usertype.equalsIgnoreCase("Refugee")) {
					// Log.i("LOGIN ACTIVIY", "ref 1");
					// openActivity = new
					// Intent("com.blakky.help.MAINACTIVITY_REFUGEE");
					System.out.println(usertype);
					System.out.println("Exeucuting shared preferences");
					SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putString("name", inputEmail.getText().toString().trim());
					editor.putString("pass", inputPassword.getText().toString());
					editor.putString("uid", uid);
					editor.putString("usertype", usertype);
					editor.putBoolean("isLoggedIn", true);
					editor.putString("fullname", fullname);
					editor.commit();
					openActivity = new Intent(LoginActivity.this, MainActivity_Refugee.class);
					// Log.i("LOGIN ACTIVIY", "ref 2");
					startActivity(openActivity);
					// Log.i("LOGIN ACTIVIY", "ref 3");
					finish();
				}

		}
	}
}
}