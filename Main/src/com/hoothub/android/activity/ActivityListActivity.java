/**
 * 
 */
package com.hoothub.android.activity;

import java.util.Comparator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.types.Activity;
import com.hoothub.android.service.ActivitiesService;
import com.hoothub.android.widget.ActivityAdapter;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ActivityListActivity extends BaseActivity {
	
	ActivityAdapter adapter;
	long lastAddedActivityIndex = 0;
	
	Comparator<Activity> comparator = new Comparator<Activity>() {

		public int compare(Activity object1, Activity object2) {
			if (object1.getCreatedAt() > object2.getCreatedAt()) {
				return -1;
			} else if (object2.getCreatedAt() > object1.getCreatedAt()) {
				return 1;
			}
			return 0;
		}
		
	};
	
	BroadcastReceiver newActivity = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityListActivity.this.updateActivity();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		ListView listView = (ListView) findViewById(R.id.activityListView);
		adapter = new ActivityAdapter(getApplicationContext());
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(Hoothub.TAG, "Clicked row ID: " + new Long(id).toString());
				Intent intent = new Intent(ActivityListActivity.this, AcivityItemActivity.class);
				intent.putExtra("activityId", id);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateActivity();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Hoothub.intent.action.NEW_ACTIVITY);
		
		registerReceiver(newActivity, intentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(newActivity);
	}
	
	void updateActivity() {
		adapter.clear();
		for (Activity item : ((Hoothub) getApplication()).getActivityStorage()) {
			adapter.add(item);
		}
		adapter.sort(comparator);
		lastAddedActivityIndex = adapter.getCount() - 1;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.newActivity:
			startActivity(new Intent(this, NewActivityActivity.class));
			return true;
		case R.id.refreshActivity:
			startService(new Intent(this, ActivitiesService.class));
		}
		return false;
	}

}
