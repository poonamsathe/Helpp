package com.blakky.help123;

import java.util.ArrayList;

import com.blakky.helper.SQLiteHandler;
import com.blakky.helper.SessionManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] mColors;
	private ArrayList<ItemsModel> itemModel;
	private TypedArray itemImages;
	private DrawerAdapter drawerAdapter;
	private int lastIndex=1;
	private ImageView imgbtn;
	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerList=(ListView)findViewById(R.id.left_drawer);
		imgbtn=(ImageView)findViewById(R.id.menu_btn);
		itemModel=new ArrayList<ItemsModel>();
		
		View header=getLayoutInflater().inflate(R.layout.header, null);
		ImageView pro=(ImageView)header.findViewById(R.id.profile_image);
		TextView prouname=(TextView)header.findViewById(R.id.textView1);

		SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
		String name = sharedpreferences.getString("fullname",null);
	
		prouname.setText(name);
		pro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
			}
		});
		
		imgbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDrawerLayout.openDrawer(mDrawerList);
			}
		});
		mDrawerList.addHeaderView(header);
		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
        itemModel.add(new ItemsModel("Food",R.drawable.icon));
        itemModel.add(new ItemsModel("Add Food",R.drawable.icon));
        itemModel.add(new ItemsModel("Housing",R.drawable.icon));
        //Shabih's Modification - start
        itemModel.add(new ItemsModel("Add Housing",R.drawable.icon));
        //Shabih's Modification - Finish
        itemModel.add(new ItemsModel("Clothing",R.drawable.icon));
        //Shabih's Modification - start
        itemModel.add(new ItemsModel("Add Clothing",R.drawable.icon));
        //Shabih's Modification - Finish
        itemModel.add(new ItemsModel("Furniture",R.drawable.icon));
        //Shabih's Modification - start
        itemModel.add(new ItemsModel("Add Furniture",R.drawable.icon));
        //Shabih's Modification - Finish
        itemModel.add(new ItemsModel("Logout",R.drawable.icon));

        
        drawerAdapter=new DrawerAdapter(itemModel);
        mDrawerList.setAdapter(drawerAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());
		// session manager
		session = new SessionManager(getApplicationContext());

		FragmentManager fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FeedFragment()).commit();


		
	}

	
	private class DrawerAdapter extends BaseAdapter{
		ArrayList<ItemsModel> itemsmodel;
		public DrawerAdapter(ArrayList<ItemsModel> itemModel) {
			// TODO Auto-generated constructor stub
		this.itemsmodel=itemModel;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemsmodel.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemsmodel.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (view == null) {
	            LayoutInflater mInflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            view = mInflater.inflate(R.layout.list_adapter, null);
	        }
			//ImageView img=(ImageView)view.findViewById(R.id.img);
			TextView name=(TextView)view.findViewById(R.id.name);
			name.setText(itemsmodel.get(position).getItemName());
			//img.setImageResource(itemsmodel.get(position).getItemImage());
			return view;
		}
	}
	
	public class DrawerItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			// TODO Auto-generated method stub
			
			if(position != 0){
				lastIndex=position;
				selectItem(position);
			}else{
				mDrawerList.setItemChecked(lastIndex, true);
			}
		}
	}
	private void selectItem(int position){
		Log.d("POS", position+"");
		Fragment frag= null;
		switch (position) {
		case 1:
			frag = new FeedFragment();
			break;

		case 2:
			frag = new PostFragment();
			break;
		
		case 3:
			frag = new HousingFrag();
			break;
		
		case 4:
			frag = new PostHouseFragment();
			break;
		case 5:
			frag = new ClothFrag();
			break;
		case 6:
			frag = new PostClothFragment();
			break;
//Shabih's Modification - start	
		case 7:
			frag = new FurniFrag();
			break;
		
		case 8:
			frag = new PostFurnitureFragment();
			break;
		case 9:
			logoutUser();
		default:
			frag = null;
			break;
		}
		
		if(frag!=null){
			FragmentManager fragmentManager= getSupportFragmentManager();
	        fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
			mDrawerList.setItemChecked(position, true);	
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	private class ItemsModel{
		private String itemName;
	private int itemImage;
	public ItemsModel(String name, int image) {
		// TODO Auto-generated constructor stub
		this.itemName=name;
		this.itemImage=image;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemImage() {
		return itemImage;
	}
	public void setItemImage(int itemImage) {
		this.itemImage = itemImage;
	}
}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		//db.deleteUsers();

		SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putBoolean("isLoggedIn", false);
		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	
}
