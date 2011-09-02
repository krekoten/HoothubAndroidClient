/**
 * 
 */
package com.hoothub.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class HomeActivity extends BaseActivity {
	
	OnClickListener activityListButton = new OnClickListener() {
		
		public void onClick(View view) {
			HomeActivity.this.startActivity(new Intent(HomeActivity.this, ActivityListActivity.class));
		}
	};
	
	OnClickListener messageThreadListButton = new OnClickListener() {
		
		public void onClick(View view) {
			HomeActivity.this.startActivity(new Intent(HomeActivity.this, MessageThreadActivity.class));
		}
	};
	
	OnClickListener friendListButton = new OnClickListener() {
		
		public void onClick(View view) {
			HomeActivity.this.startActivity(new Intent(HomeActivity.this, FriendListActivity.class));
		}
	};
	
	OnClickListener friendInviteListButton = new OnClickListener() {
		
		public void onClick(View view) {
			HomeActivity.this.startActivity(new Intent(HomeActivity.this, FriendInviteListActivity.class));
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		((Button) findViewById(R.id.showActivityList)).setOnClickListener(activityListButton);
		((Button) findViewById(R.id.showMessageThreadList)).setOnClickListener(messageThreadListButton);
		((Button) findViewById(R.id.showFriendList)).setOnClickListener(friendListButton);
		((Button) findViewById(R.id.showFriendInviteList)).setOnClickListener(friendInviteListButton);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		Hoothub app = (Hoothub) getApplication();
		switch (menuItem.getItemId()) {
		case R.id.userProfileMenu:
			startActivity(new Intent(this, ProfileActivity.class));
			break;
		case R.id.signOutMenu:
			app.signOut();
			break;
		case R.id.quitMenu:
			app.quitApplication();
			break;
		}
		return false;
	}
}
