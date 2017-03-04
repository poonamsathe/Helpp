package com.blakky.help123;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.blakky.help123.FeedClothFragment.RowItem;
import com.blakky.helper.UserFunctions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FeedFragment extends Fragment{

	private View rootview;
	private ListView lv;
	private RowItem row;	
	private List<RowItem> rowItems;
	private ProgressDialog pDialog;
	private CustomListViewAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.frag_feed, container,false);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

		lv = (ListView)rootview.findViewById(R.id.dash_feed_list);
		
		// TextFilter
        lv.setTextFilterEnabled(true);
        final EditText editTxt = (EditText)rootview. findViewById(R.id.edittextfilter);          

        editTxt.addTextChangedListener(new TextWatcher() {
        
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
					String text = editTxt.getText().toString().toLowerCase(Locale.getDefault());

					adapter.filter(text);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		
		new FeedAync().execute();
		
		return rootview;
	}

	class FeedAync extends AsyncTask<String, Void, List<RowItem>>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog.show();
		}


		@Override
		protected List<RowItem> doInBackground(String... params) {
			// TODO Auto-generated method stub
			rowItems = new ArrayList<RowItem>();
			UserFunctions userFunction = new UserFunctions();
			JSONArray json = null;
			try {
				json = userFunction.getDetails();
				Log.i("tag", json+"");
				if(json!=null){
					//JSONArray spec_aray= json.getJSONArray("");
					for(int i=0;i<json.length();i++)
					{
						JSONObject json1= json.getJSONObject(i);
						String name=json1.getString("food_des");
						String desc=json1.getString("food_detail");
						String address= json1.getString("food_add");
						String contact_no = json1.getString("food_ph");
						
						
						RowItem item = new RowItem(name, desc, address, contact_no);
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
			pDialog.dismiss();
			adapter = new CustomListViewAdapter(getActivity(), R.layout.list_news, result);
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

	    }


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			rowitem=getItem(position);
			
			 LayoutInflater mInflater = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			
			if(convertView == null)
			{
				convertView = mInflater.inflate(R.layout.list_news, parent,false);
	            holder = new ViewHolder();	
				holder.food_name=(TextView)convertView.findViewById(R.id.feed_name);
				holder.food_desc=(TextView)convertView.findViewById(R.id.feed_descp);
				holder.food_address = (TextView)convertView.findViewById(R.id.feed_address);
				holder.food_contact = (TextView)convertView.findViewById(R.id.feed_contact);
				holder.food_img = (ImageView)convertView.findViewById(R.id.feed_img);
				holder.food_map = (ImageView)convertView.findViewById(R.id.feed_map);

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
					Intent i = new Intent(getActivity(),Map.class);
					i.putExtra("address", adaprowitems.get(position).faddress);
					startActivity(i);
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
		
		/*		
		
		 * We create our filter	
		 
		
		@Override
		public Filter getFilter() {
			if (rFilter == null)
				rFilter = new CustomFilter();
			
			return rFilter;
		}

		
		private class CustomFilter extends Filter {

			
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				// We implement here the filter logic
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = origitemList;
					results.count = origitemList.size();
				}
				else {
					// We perform filtering operation
					List<RowItem> nPlanetList = new ArrayList<RowItem>();
					
					for (RowItem p : adaprowitems) {
						if (p.getFname().toUpperCase().startsWith(constraint.toString().toUpperCase()))
							nPlanetList.add(p);
					}
					
					results.values = nPlanetList;
					results.count = nPlanetList.size();

				}
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				
				// Now we have to inform the adapter about the new list filtered
				if (results.count == 0)
					notifyDataSetInvalidated();
				else {
					adaprowitems = (List<RowItem>) results.values;
					notifyDataSetChanged();
				}
				
			}
			
		}
		
*/		
	}
	
	
	
	class RowItem 
	{
		private String fname;
		private String fdetail;
		private String faddress;
		private String fcontact;
		public RowItem(String fname, String fdetail, String faddress, String fcontact) {
			super();
			this.fname = fname;
			this.fdetail = fdetail;
			this.faddress = faddress;
			this.fcontact = fcontact;
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

	

