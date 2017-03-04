package com.blakky.help123;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blakky.helper.DirectionsJSONParser;
import com.blakky.helper.JSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;


public class Map extends Activity implements LocationListener {
	
	
	// Google Map
    private static GoogleMap googleMap;
    private JSONParser jsonparser;
    private String address;
    static double latitude = 0;
    static double longitude = 0;
    
    static double destlat = 0;
    static double destlon = 0;
	
    // flag for GPS status
	boolean isGPSEnabled = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	// flag for network status
	boolean isNetworkEnabled = false;

	boolean checksettings = false;
	// flag for GPS status
	boolean canGetLocation = false;
 		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
            // Loading map
            //initilizeMap();
            jsonparser = new JSONParser();
            address = getIntent().getStringExtra("address");
            Log.i("oncre", ""+address);    
			address = address.replaceAll(" ","%20");
			new getAddress().execute(address);
		            
    }

    class getAddress extends AsyncTask<String, Void, LatLng>{

		@Override
		protected LatLng doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			
			LatLng point = null;
			  String uri = "http://maps.google.com/maps/api/geocode/json?address="
		                + params[0] + "&sensor=false";

		        HttpGet httpGet = new HttpGet(uri);

		        HttpClient client = new DefaultHttpClient();
		        HttpResponse response;
		        StringBuilder stringBuilder = new StringBuilder();

		        try {
		            response = client.execute(httpGet);
		            HttpEntity entity = response.getEntity();

		            InputStream stream = entity.getContent();

		            int byteData;
		            while ((byteData = stream.read()) != -1) {
		                stringBuilder.append((char) byteData);
		            }

		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		        double lat = 0.0, lng = 0.0;

		        JSONObject jsonObject;
		        try {
		            jsonObject = new JSONObject(stringBuilder.toString());
		            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
		                    .getJSONObject("geometry").getJSONObject("location")
		                    .getDouble("lng");
		            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
		                    .getJSONObject("geometry").getJSONObject("location")
		                    .getDouble("lat");
		            point = new LatLng(lat,lng);
		        } catch (JSONException e) {
		            e.printStackTrace();
		        }
		        
		        
			return point;
		}

		@Override
		protected void onPostExecute(LatLng result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.e("map", ""+result);
			destlat = result.latitude;
			destlon = result.longitude;
			showLocation();
		
		}
    	
		
    }

    
    
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Map.this);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        
        alertDialog.setCancelable(false);
        // Showing Alert Message
        alertDialog.show();
	}

	
	private void showLocation(){

		 int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(Map.this);

	        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	           Toast.makeText(Map.this, "Update Your Google play services", Toast.LENGTH_LONG).show();

	        }else {
	        	
	        	
	        	LocationManager locationManager;
	    		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

	    		// getting GPS status
	    		isGPSEnabled = locationManager
	    				.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    		// getting network status
	    		isNetworkEnabled = locationManager
	    				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	    		
	    		
	    		if (!isGPSEnabled && !isNetworkEnabled) {
	    			// no network provider is enabled
	    		//	showSettingsAlert();
	    		} else if(isGPSEnabled || isNetworkEnabled) {
	    			
	    			
	    			// Google Play Services are available
	    			
					// Initializing 
				
					
					// Getting reference to SupportMapFragment of the activity_main
	    			
	    			
	    			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
	                        R.id.map)).getMap();
	    			
					
					// Enable MyLocation Button in the Map
					googleMap.setMyLocationEnabled(true);

					GoogleMapOptions options = new GoogleMapOptions();
				
					options.mapType(GoogleMap.MAP_TYPE_NORMAL)
				    .compassEnabled(true)
				    .rotateGesturesEnabled(true)
				    .tiltGesturesEnabled(false)
				    .zoomControlsEnabled(false)
				    .zoomGesturesEnabled(true);
				 

			
			        // Creating a criteria object to retrieve provider
			      //  Criteria criteria = new Criteria();
			
			        // Getting the name of the best provider
			       // String provider = locationManager.getBestProvider(criteria, true);
			
			        // Getting Current Location From GPS
			        Location location = null;
			        
			        if(isGPSEnabled){
			        	if (location == null) {
							locationManager.requestLocationUpdates(
									LocationManager.GPS_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
							if (locationManager != null) {
								location = locationManager
										.getLastKnownLocation(LocationManager.GPS_PROVIDER);
								if (location != null) {
					                onLocationChanged(location);
								}
							}
						}
			        }
			        
			        
			        
			     
			        
			        if(location == null){
			        	   if(isNetworkEnabled){
					        	locationManager.requestLocationUpdates(
										LocationManager.NETWORK_PROVIDER,
										MIN_TIME_BW_UPDATES,
										MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
								Log.d("Network", "Network");
								if (locationManager != null) {
									location = locationManager
											.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
									if (location != null) {
						                onLocationChanged(location);
									}
								}
					        }
			        
			      }
			        
			      latitude =  location.getLatitude();
			      longitude =  location.getLongitude();
			        
			    LatLng point;    

			    Log.i(latitude+"", longitude+"longitude");
			    Log.i(destlat+"", destlon+"");
				if(destlat == 0 || destlon == 0){
					point = new LatLng(latitude,longitude);
				}else{
					point = new LatLng(destlat,destlon);
				}
				googleMap.clear();
				LatLng startPoint = new LatLng(latitude, longitude);
				drawMarker(point);
				
				getDirection(latitude,longitude);
	    			
	    		} else {
	    			
	    			showSettingsAlert();
	    		}
	        	
	        								
							
					
	        }		
		
	}
	
	
	private void getDirection(double mLatitude,double mLongitude){
		
		
		LatLng origin = new LatLng(mLatitude, mLongitude);
		LatLng dest =  new LatLng(destlat,destlon);
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);				
		DownloadTask downloadTask = new DownloadTask();
		// Start downloading json data from Google Directions API
		downloadTask.execute(url);
		
	}
	
	
	private String getDirectionsUrl(LatLng origin,LatLng dest){
		
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;			
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;		
		
		return url;
	}
	
	
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
				data = null;
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			if(result != null){
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
			}else{
				showSettingsAlert1();
			}
		}		
	}
	
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            	routes = null;
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			
			if(result != null){
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);					
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(10);
				lineOptions.color(Color.BLUE);	
				
			}
			
			// Drawing polyline in the Google Map for the i-th route
				if(lineOptions != null){
					googleMap.addPolyline(lineOptions);
				}else{
					showLocation();
				}
			
			}else{
				showSettingsAlert1();
			}
			
			
		}			
    }   
    
	
	
	private void drawMarker(LatLng point){
	

		// Add new marker to the Google Map Android API V2
		googleMap.addMarker(new MarkerOptions()
								.position(point)
								.snippet(address+"")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_icon))
								);		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng point = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());        
        getDirection(latitude, longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	   /**
			 * Function to show settings alert dialog
			 * On pressing Settings button will lauch Settings Options
			 * */
			public void showSettingsAlert1(){
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(Map.this);
		   	 
		        // Setting Dialog Title
		        alertDialog.setTitle("Network Error");
		 
		        // Setting Dialog Message
		        alertDialog.setMessage("Network is too slow. Please Try again");
		 
		        // On pressing Settings button
		        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		                onBackPressed();
		            }
		        });
		 
		        // on pressing cancel button
		        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            dialog.cancel();
		            }
		        });
		        
		        alertDialog.setCancelable(false);
		        // Showing Alert Message
		        alertDialog.show();
			}
	
	
    
        @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("ondes", "");
		googleMap.clear();
	}
        
    	public void onDestroyView() {

 		   
 		}


		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			Log.i("onpause", "");
		}




		@Override
    protected void onResume() {
        super.onResume();
        Log.i("onres", "");

    }
    
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap != null) {
        	googleMap = null;
        }
        	
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        
    }
 
     
}
