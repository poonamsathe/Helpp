package com.blakky.help123;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpRetryException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PostFurnitureFragment extends Fragment implements OnItemSelectedListener, OnClickListener{

	Intent intent;
	TextView textViewAddress, textViewPhone, textViewDetails;
	EditText editTextDescription, editTextAddress, editTextPhone, editTextHeader;
	Button buttonPost, buttonMyPosts, buttonReset;
	String category = null;
	String header = null;
	String address = null;
	String description = null;
	String phone = null;
	private String[] state = { "Food", "Housing", "Clothing", "Furniture" };
	private View rootview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.activity_post_fragment, container,false);
		System.out.println(state.length);

		editTextDescription = (EditText)rootview.findViewById(R.id.editTextDescription);
		editTextAddress = (EditText)rootview. findViewById(R.id.editTextAddress);
		editTextPhone = (EditText)rootview. findViewById(R.id.editTextPhone);
		editTextHeader = (EditText)rootview. findViewById(R.id.editTextHeader);
		buttonPost = (Button)rootview. findViewById(R.id.buttonPost);
		buttonMyPosts = (Button)rootview. findViewById(R.id.buttonMyPosts);

		buttonPost.setOnClickListener(this);
		buttonMyPosts.setOnClickListener(this);


		
		return rootview;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.buttonPost:
			header = editTextHeader.getText().toString().trim();
			address = editTextAddress.getText().toString().trim();
			description = editTextDescription.getText().toString().trim();
			phone = editTextPhone.getText().toString().trim();

			final String DEFAULT = "N/A";
			SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
			String name = sharedpreferences.getString("name", DEFAULT);

			System.out.println(address);
			System.out.println(description);
			System.out.println(phone);

			System.out.println("Entered button post");
			// System.out.println("selectedStartTime"+selectedStartTime);
			if (editTextHeader == null) {
				Toast.makeText(getActivity(), "Enter values in Header feild field!", Toast.LENGTH_SHORT)
						.show();
			} else if (editTextAddress == null) {
				Toast.makeText(getActivity(), "Please enter the address!", Toast.LENGTH_SHORT).show();
			} else if (editTextDescription == null) {
				Toast.makeText(getActivity(), "Please enter the description!", Toast.LENGTH_SHORT).show();
			} else if (editTextPhone == null) {
				Toast.makeText(getActivity(), "Enter the phone number", Toast.LENGTH_SHORT).show();
			} else {

				new SendData().execute(name, category, header, description, address, phone);
				
				editTextDescription.setText(" ");
				editTextAddress.setText(" ");
				editTextPhone.setText(" ");
				editTextHeader.setText(" ");
			}
			break;

		case R.id.buttonMyPosts:
			intent = new Intent(getActivity(),ListCharityPostActivity.class);
			startActivity(intent);
			break;

		case R.id.buttonReset:
			editTextDescription.setText(" ");
			editTextAddress.setText(" ");
			editTextPhone.setText(" ");
			editTextHeader.setText(" ");
			break;
		}

	}

	private class SendData extends AsyncTask<String, String, String> {
		HttpClient httpClient;
		@SuppressWarnings("unused")
		HttpResponse httpResponse;
		HttpPost httpPost;

		@Override
		protected String doInBackground(String... params) {
			try {
				String name = URLEncoder.encode(params[0], "UTF-8").replace("+", "%20");
				String category = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
				String header = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");
				String Description = URLEncoder.encode(params[3], "UTF-8").replace("+", "%20");
				String Address = URLEncoder.encode(params[4], "UTF-8").replace("+", "%20");
				String Phone = URLEncoder.encode(params[5], "UTF-8").replace("+", "%20");

				String toPostPHP = "postuname=" + name + "&postheader=" + header
						+ "&postdescription=" + Description + "&postaddress=" + Address + "&postphone=" + Phone;

				String fullURL = "http://omega.uta.edu/~sas4798/furni_post.php?" + toPostPHP;
				httpClient = new DefaultHttpClient();

				Log.i("PostActvitiy - ", "Created httpClient " + fullURL);
				httpPost = new HttpPost(fullURL);
				httpResponse = httpClient.execute(httpPost);
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.e("PostActvitiy - ", "Error in ArrayIndexOutOfBoundsException - " + e.toString());
			} catch (ClientProtocolException e) {
				Log.e("PostActvitiy - ", "Error in ClientProtocolException - " + e.toString());
			} catch (UnsupportedEncodingException e) {
				Log.e("PostActivity URL Encode - ", e.toString());
			} catch (IllegalArgumentException e) {
				Log.e("PostActivity Illegal Args - ", e.toString());
			} catch (HttpRetryException e) {
				Log.e("PostActivity Connection - ", e.toString());
			} catch (IOException e) {
				Log.e("PostActivity IO - ", e.toString());
			}catch (Exception e) {
				// TODO: handle exception
				Log.e("EXCE{P IO - ", e.toString());
			}

			System.out.println("wooooooooo hooo");
			return null;
		}
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
			}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	
}
