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
import com.hoothub.android.client.types.FriendInvite;
import com.hoothub.android.widget.FriendInviteAdapter;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendInviteListActivity extends BaseActivity {
	FriendInviteAdapter adapter;
	
	BroadcastReceiver newFriendInvite = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			FriendInviteListActivity.this.updateFriendInvites();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_invite_list);
		
		ListView listView = (ListView) findViewById(R.id.friendInvitesListView);
		adapter = new FriendInviteAdapter(getApplicationContext());
		
		listView.setAdapter(adapter);
	}
	
	public void onResume() {
		super.onResume();
		updateFriendInvites();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Hoothub.intent.action.NEW_FRIEND_INVITE);
		
		registerReceiver(newFriendInvite, intentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(newFriendInvite);
	}
	
	void updateFriendInvites() {
		adapter.clear();
		for (FriendInvite item : ((Hoothub) getApplication()).getFriendInvitesStorage()) {
			adapter.add(item);
		}
	}
}
