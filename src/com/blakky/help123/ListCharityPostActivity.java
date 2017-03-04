package com.blakky.help123;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ListCharityPostActivity extends Activity implements OnItemClickListener {
	String uid;
	private RowItem row;	
	private List<RowItem> rowItems;
	private ProgressDialog pDialog;
	private CustomListViewAdapter adapter;
	private ListView lv;
	AlertDialog.Builder alertDialogBuilder;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_charity_post);

		SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
		uid = sharedpreferences.getString("fullname",null);
	
		lv = (ListView) findViewById(R.id.listViewCharityPosts);

		LinkToOmega omegaData = new LinkToOmega();
		omegaData.execute();


		alertDialogBuilder = new AlertDialog.Builder(ListCharityPostActivity.this);
		alertDialogBuilder.setTitle("Delete");
		alertDialogBuilder.setMessage("Do you want to delete this item? ");
		alertDialogBuilder.setCancelable(false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String item = (String) parent.getItemAtPosition(position);
		Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG).show();

		String[] sendData = item.split(";");
		Intent i = new Intent(ListCharityPostActivity.this, DetailsCharityPostActivity.class);
		i.putExtra("textOmegaID", sendData[0]);
		i.putExtra("textOmegaHeader", sendData[1]);
		i.putExtra("textOmegaDescription", sendData[2]);
		i.putExtra("textOmegaAddress", sendData[3]);
		i.putExtra("textOmegaPhoneNumber", sendData[4]);
		startActivity(i);
	}


	private class LinkToOmega extends AsyncTask<String, String, List<RowItem>> {

		SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
		String uid = sharedpreferences.getString("uid",null);
	
		HttpClient httpClient;
		@SuppressWarnings("unused")
		HttpResponse httpResponse;
		HttpPost httpPost;
		InputStream is = null;

		@Override
		protected List<RowItem> doInBackground(String... params) {
			// Create Http request and response objects to connect to Omega
			try {

				rowItems = new ArrayList<RowItem>();

				String fullURL = "http://omega.uta.edu/~sas4798/food_getbyid.php?userid=" + uid;
				 HttpClient client = new DefaultHttpClient();
		         HttpGet request = new HttpGet();
		         request.setURI(new URI(fullURL));
		         HttpResponse response = client.execute(request);
			
		         BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		         
		         StringBuffer sb = new StringBuffer("");
		           String line="";
		           
		           while ((line = in.readLine()) != null) {
		              sb.append(line);
		              break;
		            }
		            in.close();
		       	String result = sb.toString();
			
			
			JSONArray json = null;
			
				json = new JSONArray(result);
				Log.i("tag"+uid, json+"");
				if(json!=null){
					//JSONArray spec_aray= json.getJSONArray("");
					for(int i=0;i<json.length();i++)
					{
						JSONObject json1= json.getJSONObject(i);
						String fid=json1.getString("food_id");
						String name=json1.getString("food_des");
						String desc=json1.getString("food_detail");
						String address= json1.getString("food_add");
						String contact_no = json1.getString("food_ph");
						
						
						RowItem item = new RowItem(fid,name, desc, address, contact_no);
						rowItems.add(item);
						
					}
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				rowItems = null;
				e1.printStackTrace();
				Log.i("tag", e1+"");
			}
			
			return rowItems;
		}

		@Override
		protected void onPostExecute(List<RowItem> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter = new CustomListViewAdapter(ListCharityPostActivity.this, R.layout.list_news, result);
			lv.setAdapter(adapter);
		}
		
		
		
	}

	
	public class CustomListViewAdapter extends ArrayAdapter<RowItem> implements Filterable{

		List<RowItem> adaprowitems;
		private Context context;
		private Filter rFilter;
		private List<RowItem> origitemList;
		
		ViewHolder holder = null;
		RowItem rowitem;
		
		private ArrayList<RowItem> arraylist;

		
		int[] array_img = {R.drawable.hero1,R.drawable.hero2,R.drawable.hero3,R.drawable.hero4
				,R.drawable.hero5,R.drawable.hero6,R.drawable.hero7};
		
	
		
		//List<RowItem> rowitems;
		
		public CustomListViewAdapter(Context context, int resource, List<RowItem> rowitems) {
			super(context, resource, rowitems);
			// TODO Auto-generated constructor stub
			this.adaprowitems = rowitems;
			this.context = context;
			this.origitemList = rowitems;
			this.arraylist = new ArrayList<RowItem>();
			this.arraylist.addAll(adaprowitems);
		}
		

		/*private view holder class*/
	    private class ViewHolder {
	       public ImageView food_img;
	       public TextView food_name;
	       public TextView food_desc;
	       public TextView  food_address;
	       public TextView  food_contact;
	       public ImageView food_map;
	       public ImageView food_delete;

	    }


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			rowitem=getItem(position);
			
			 LayoutInflater mInflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			
			if(convertView == null)
			{
				convertView = mInflater.inflate(R.layout.activity_myposts, parent,false);
	            holder = new ViewHolder();	
				holder.food_name=(TextView)convertView.findViewById(R.id.post_name);
				holder.food_desc=(TextView)convertView.findViewById(R.id.post_descp);
				holder.food_address = (TextView)convertView.findViewById(R.id.post_address);
				holder.food_contact = (TextView)convertView.findViewById(R.id.post_contact);
				holder.food_img = (ImageView)convertView.findViewById(R.id.post_img);
				holder.food_map = (ImageView)convertView.findViewById(R.id.post_map);
				holder.food_delete = (ImageView)convertView.findViewById(R.id.post_delete);

				convertView.setTag(holder);
			}else {
				 holder = (ViewHolder) convertView.getTag();
			}
			
			holder.food_name.setText(adaprowitems.get(position).fname);
			holder.food_desc.setText(adaprowitems.get(position).fdetail);
			holder.food_address.setText("Address: "+adaprowitems.get(position).faddress);
			holder.food_contact.setText("Contact No.:  "+adaprowitems.get(position).fcontact);
		
			int i = getRandom(7);
			holder.food_img.setImageResource(array_img[i]);
			holder.food_map.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(ListCharityPostActivity.this,Map.class);
					i.putExtra("address", adaprowitems.get(position).faddress);
					startActivity(i);
				}
			});
	
			
			holder.food_delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					
					alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							 for (int j = 0; j < adaprowitems.size(); j++) {
								RowItem item = adaprowitems.get(j);
								if(adaprowitems.get(position).fid == item.fid){
									adaprowitems.remove(item);
									notifyDataSetChanged();
									Toast.makeText(ListCharityPostActivity.this, "Post deleted Sucessfully", Toast.LENGTH_LONG).show();
									return;
								}
							}
						}
					  });
					alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
					
				}
			});
			
			
			return convertView;
		}
	    
		public  int getRandom(int max){
			return (int) (Math.random()*max);
		}
		
		public void resetData() {
			adaprowitems = origitemList;
			notifyDataSetChanged();
		}
		
		// Filter Class
		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());
			adaprowitems.clear();
			if (charText.length() == 0) {
				adaprowitems.addAll(arraylist);
			} else {
				for (RowItem wp : arraylist) {
					if (wp.getFname().toLowerCase(Locale.getDefault())
							.contains(charText)) {
						adaprowitems.add(wp);
					}
				}
			}
			notifyDataSetChanged();
		}
		
	
	}
	
	class RowItem 
	{
		private String fid;
		private String fname;
		private String fdetail;
		private String faddress;
		private String fcontact;
		public RowItem(String fid, String fname, String fdetail, String faddress, String fcontact) {
			super();
			this.fid = fid;
			this.fname = fname;
			this.fdetail = fdetail;
			this.faddress = faddress;
			this.fcontact = fcontact;
		}
		public String getFid() {
			return fid;
		}
		public void setFid(String fid) {
			this.fid = fid;
		}
		public String getFname() {
			return fname;
		}
		public void setFname(String fname) {
			this.fname = fname;
		}
		public String getFdetail() {
			return fdetail;
		}
		public void setFdetail(String fdetail) {
			this.fdetail = fdetail;
		}
		public String getFaddress() {
			return faddress;
		}
		public void setFaddress(String faddress) {
			this.faddress = faddress;
		}
		public String getFcontact() {
			return fcontact;
		}
		public void setFcontact(String fcontact) {
			this.fcontact = fcontact;
		}
		
		
	
	}
}