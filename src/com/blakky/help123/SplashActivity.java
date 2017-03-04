package com.blakky.help123;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		/*
		 * Creates a thread to display the splash screen for 2 seconds and
		 * redirects to Ride preferences screen
		 */
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent i = new Intent(SplashActivity.this,LoginActivity.class);
					startActivity(i);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
