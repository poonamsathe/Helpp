
package com.blakky.help123;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.blakky.helper.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener, OnItemSelectedListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner spinner_usertype;
/*    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;*/
	private String[] state = { "Refugee", "Charity" };
	static final String SENDER_ID = "423067181619"; 
	static String regId;
	String name;
	AsyncTask<Void, Void, Void> mRegisterTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        
    	// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);
		System.out.println(regId);
		Log.i("reg", regId+"");
		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(this, SENDER_ID);
			regId = GCMRegistrar.getRegistrationId(this);
		} else {
			regId = GCMRegistrar.getRegistrationId(this);
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.				
				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {
				//post(name, regId);
			}
		}
	
        
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        spinner_usertype = (Spinner) findViewById(R.id.spinner_usertype);
        
        btnRegister.setOnClickListener(this);
        
       
       /* // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        // Register Button Click event
        /*btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });*/

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        spinner_usertype = (Spinner) findViewById(R.id.spinner_usertype);
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				state);
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_usertype.setAdapter(adapter_state);
		spinner_usertype.setOnItemSelectedListener(this);

    }

    public static void updateTable(String regid){
    	regId = regid;
    	Log.e("Reg", "> " + regId);
    }
    
    
    private  void post(String name,String regID)
            throws IOException {   	

    	final String endpoint = "http://omega.uta.edu/~sas4798/save_reg.php";
        final Map<String, String> map = new HashMap<String, String>();
        map.put("reg_id", regID);
        map.put("username", name);
        
        
    	mRegisterTask = new AsyncTask<Void, Void, Void>(){
    		 
             
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
			       URL url;
			        try {
			            url = new URL(endpoint);
			        } catch (MalformedURLException e) {
			            throw new IllegalArgumentException("invalid url: " + endpoint);
			        }
			        StringBuilder bodyBuilder = new StringBuilder();
			        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			        // constructs the POST body using the parameters
			        while (iterator.hasNext()) {
			            Entry<String, String> param = iterator.next();
			            bodyBuilder.append(param.getKey()).append('=')
			                    .append(param.getValue());
			            if (iterator.hasNext()) {
			                bodyBuilder.append('&');
			            }
			        }
			        String body = bodyBuilder.toString();
			        Log.v(TAG, "Posting '" + body + "' to " + url);
			        byte[] bytes = body.getBytes();
			        HttpURLConnection conn = null;
			        try {
			        	Log.e("URL", "> " + url);
			            conn = (HttpURLConnection) url.openConnection();
			            conn.setDoOutput(true);
			            conn.setUseCaches(false);
			            conn.setFixedLengthStreamingMode(bytes.length);
			            conn.setRequestMethod("POST");
			            conn.setRequestProperty("Content-Type",
			                    "application/x-www-form-urlencoded;charset=UTF-8");
			            // post the request
			            OutputStream out = conn.getOutputStream();
			            out.write(bytes);
			            out.close();
			            // handle the response
			            int status = conn.getResponseCode();
			            if (status != 200) {
			              throw new IOException("Post failed with error code " + status);
			            }
			        } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
			            if (conn != null) {
			                conn.disconnect();
			            }
			        }				
				
				return null;
			}
    		
    	};
    	
    	mRegisterTask.execute();
 
      }
    
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString("message");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			//lblMessage.append(newMessage + "\n");			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			generateNotification(context, newMessage);
			// Releasing wake lock
			WakeLocker.release();
		}
	};

	
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = context.getString(R.string.app_name);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        
        
        Intent notificationIntent = new Intent(context, MainActivity_Refugee.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        mBuilder.setContentIntent(intent);
        
        notificationManager.notify(0, mBuilder.build());      

    }
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	System.out.println("Entered onClick");
    	String name = inputFullName.getText().toString().trim();
    	System.out.println("Printing name" + name);
    	String email = inputEmail.getText().toString().trim();
    	System.out.println("Printing email" + email);
    	String password = inputPassword.getText().toString().trim();
    	String usertype = (String) spinner_usertype.getSelectedItem();
    	System.out.println("Usertype selected" + usertype);
    	
    	//validation sequence for all the inputs
    	if(name.isEmpty()){
			Toast.makeText(getApplicationContext(), "Enter your full name",
					Toast.LENGTH_SHORT).show();
		}else if (password.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter values in password field",
					Toast.LENGTH_SHORT).show();
		}else if (email.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Enter values in email field",
					Toast.LENGTH_SHORT).show();
		} else if (password.length() < 8) {
			Toast.makeText(getApplicationContext(),
					"Password must contain atleast 8 characters",
					Toast.LENGTH_SHORT).show();
		} else if (!email.contains("@gmail.com")) {
			Toast.makeText(getApplicationContext(), "Enter a valid gmail ID",
					Toast.LENGTH_SHORT).show();
		}else {
			EnterValues enter = new EnterValues();
			enter.execute(name, email, password, usertype,regId);
			
			if(regId != ""){
				GCMRegistrar.isRegisteredOnServer(RegisterActivity.this);
			}
			updateTable(regId);

			
			Toast.makeText(
					RegisterActivity.this,
					"Please Login", Toast.LENGTH_SHORT)
					.show();
			
			 Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
         	 startActivity(i);
         	 finish();
		}
	}
    
    private class EnterValues extends AsyncTask<String, Void, String> {

    	HttpClient httpClient;
		@SuppressWarnings("unused")
		HttpResponse httpResponse;
		HttpPost httpPost;
		public void execute(String name, String password, String email) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String name = URLEncoder.encode(params[0], "UTF-8").replace("+", "%20");
				String email = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
				String password = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");
				String usertype = URLEncoder.encode(params[3], "UTF-8").replace("+", "%20");
				String gcm_reg = URLEncoder.encode(params[4], "UTF-8").replace("+", "%20");


				String toPostPHP = "uname=" + name + "&email=" + email + "&password=" + password
						+ "&usertype=" + usertype + "&gcm_regid="+gcm_reg;

				String fullURL = "http://omega.uta.edu/~sas4798/create_user_profile.php?" + toPostPHP;
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
			}

			System.out.println("wooooooooo hooo");
			
			return null;
		}
    	
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	spinner_usertype.setSelection(position);

		String selState = (String) spinner_usertype.getSelectedItem();
		System.out.println(selState);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
   /* private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        
        pDialog.setMessage("Registering ...");
        showDialog();

        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() { 
             public void run() { 
                 // Inserting row in users table
                 db.addUser(name, email, password,System.currentTimeMillis()+"");
                 hideDialog();
                 Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
             	 startActivity(i);
             	 finish();
             } 
        }, 3000);
        
        

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

	
}
