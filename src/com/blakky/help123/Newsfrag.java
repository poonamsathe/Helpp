package com.blakky.help123;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Newsfrag extends Fragment {
	
	private View rootview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.activity_wish_frag, container, false);

		if (savedInstanceState == null) {
			addRssFragment();
		}
		return rootview;
	}

	private void addRssFragment() {
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		RssFragment fragment = new RssFragment();
		transaction.add(R.id.fragment_container, fragment);
		transaction.commit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("fragment_added", true);
	}
}
