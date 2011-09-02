/**
 * 
 */
package com.hoothub.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.types.User;
import com.hoothub.android.widget.FriendAdapter;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendListActivity extends BaseActivity {
	
	FriendAdapter adapter;
	
	BroadcastReceiver newFriend = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			FriendListActivity.this.updateFriends();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		
		ListView listView = (ListView) findViewById(R.id.friendsListView);
		adapter = new FriendAdapter(getApplicationContext());
		
		listView.setAdapter(adapter);
	}
	
	public void onResume() {
		super.onResume();
		updateFriends();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Hoothub.intent.action.NEW_FRIEND);
		
		registerReceiver(newFriend, intentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(newFriend);
	}
	
	void updateFriends() {
		adapter.clear();
		for (User item : ((Hoothub) getApplication()).getFriendsStorage()) {
			adapter.add(item);
		}
	}
}
