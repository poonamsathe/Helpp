package com.blakky.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.util.Log;

public class UserFunctions {

	private Json jsonParser;
	public static final String feedurl = "http://omega.uta.edu/~sas4798/food_get.php";
	public static final String clothurl = "http://omega.uta.edu/~sas4798/cloth_get.php";
	public static final String housingurl = "http://omega.uta.edu/~sas4798/house_get.php";
	public static final String furniurl = "http://omega.uta.edu/~sas4798/furni_get.php";
	public static final String feedurlbyid = "http://omega.uta.edu/~sas4798/food_getbyid.php";

	public JSONArray getDetails() throws Exception{
		// getting JSON Object
		JSONArray json = jsonParser.getJson(feedurl);
		Log.i("tag", json+"");
		return json;
		
	}

	public JSONArray getClothDetails() throws Exception{
		// getting JSON Object
		JSONArray json = jsonParser.getJson(clothurl);
		Log.i("tag", json+"");
		return json;
		
	}
	
	public JSONArray getHousingDetails() throws Exception{
		// getting JSON Object
		JSONArray json = jsonParser.getJson(housingurl);
		Log.i("tag", json+"");
		return json;
		
	}
	
	public JSONArray getFurniDetails() throws Exception{
		// getting JSON Object
		JSONArray json = jsonParser.getJson(furniurl);
		Log.i("tag", json+"");
		return json;
		
	}
	
	public JSONArray getFeedDetails(String userid) throws Exception{
		// getting JSON Object
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid",userid));

        JSONParser jsonpar = new JSONParser();
        // getting JSON Object
        String json = jsonpar.getJSONFromUrl(feedurlbyid, params);
		
        JSONArray array = new JSONArray(json);
		Log.i("tag", json+"");
		return array;
		
	}
}
