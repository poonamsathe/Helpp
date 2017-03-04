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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class WishFrag extends Fragment implements OnClickListener {

	Intent intent;

	private CheckBox Food, Housing, Clothing, Furniture;
	Button SubmitWishList;

	private View rootview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.activity_wish_frag, container, false);

		Food = (CheckBox) rootview.findViewById(R.id.WishlistFood);
		Housing = (CheckBox) rootview.findViewById(R.id.WishlistHousing);
		Clothing = (CheckBox) rootview.findViewById(R.id.WishlistClothing);
		Furniture = (CheckBox) rootview.findViewById(R.id.WishlistFurniture);
		SubmitWishList = (Button) rootview.findViewById(R.id.SubmitWishList);

		CheckBox food = (CheckBox) rootview.findViewById(R.id.WishlistFood);
		food.setChecked(true);
		// checkButtonClick();
		SubmitWishList.setOnClickListener(this);
		return rootview;
	}

	// Run when button is clicked
	@Override
	public void onClick(View v) {

		/*
		 * StringBuffer result = new StringBuffer(); result.append(
		 * "Food check : ").append(Food.isChecked()); result.append(
		 * "\nHousing check : ").append(Housing.isChecked()); result.append(
		 * "\nClothing check :").append(Clothing.isChecked()); result.append(
		 * "\nFurniture check :").append(Furniture.isChecked());
		 * 
		 * Toast.makeText(getActivity(), result.toString(),
		 * Toast.LENGTH_LONG).show();
		 * 
		 */
		final String DEFAULT = "N/A";
		SharedPreferences preferences = this.getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
		String name = preferences.getString("name", DEFAULT);
		System.out.println("Email++++++++ " + name);

		String post_food = Food.isChecked() + " ";
		String post_housing = Housing.isChecked() + " ";
		String post_clothing = Clothing.isChecked() + " ";
		String post_furniture = Furniture.isChecked() + " ";

		System.out.println("Printing the result " + post_food);
		System.out.println("Printing the result " + post_housing);
		System.out.println("Printing the result " + post_clothing);
		System.out.println("Printing the result " + post_furniture);
		
		//Toast.makeText(getActivity(),"Your Wish List is Submited",Toast.LENGTH_SHORT).show(); 

		// UserValidation validates = new UserValidation();
		// validates.execute(email, post_food,post_clothing,post_furniture);

		CheckList cl = new CheckList();
		cl.execute(name, post_food.trim(), post_housing.trim(), post_clothing.trim(), post_furniture.trim());

	}

	private class CheckList extends AsyncTask<String, String, String> {

		HttpClient httpClient;
		@SuppressWarnings("unused")
		HttpResponse httpResponse;
		HttpPost httpPost;
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				String email = URLEncoder.encode(params[0], "UTF-8").replace("+", "%20");
				String w_food = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
				String w_house = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");
				String w_cloth = URLEncoder.encode(params[3], "UTF-8").replace("+", "%20");
				String w_furni = URLEncoder.encode(params[4], "UTF-8").replace("+", "%20");
				
				String toPostPHP = "email=" + email + "&w_food=" + w_food + "&w_house=" + w_house
						+ "&w_cloth=" + w_cloth + "&w_furni=" + w_furni;
				
				String fullURL = "http://omega.uta.edu/~sas4798/wishlist.php?" + toPostPHP;
				httpClient = new DefaultHttpClient();

				Log.i("PostActvitiy - ", "Created httpClient " + fullURL);
				httpPost = new HttpPost(fullURL);
				httpResponse = httpClient.execute(httpPost);
			}catch (ArrayIndexOutOfBoundsException e) {
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
			}
			
			System.out.println("wishlist working!!");
			return null;
		}

	}

}

/*
 * private void checkButtonClick() { // TODO Auto-generated method stub
 * SubmitWishList = (Button)rootview.findViewById(R.id.SubmitWishList);
 * SubmitWishList.setOnClickListener(this);
 * 
 * 
 * } public void onClick(View v) { // TODO Auto-generated method stub if
 * (((CheckBox) v).isChecked()) { Toast.makeText(getActivity(),
 * "Bro, try Food :)", Toast.LENGTH_LONG).show(); }
 * 
 * 
 * }
 */
