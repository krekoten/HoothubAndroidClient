/**
 * 
 */
package com.hoothub.android.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.hoothub.android.Hoothub;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class BaseActivity extends Activity {
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Hoothub.intent.action.SIGN_IN)) {
				onSignIn(intent);
			}
			
			if (intent.getAction().equals(Hoothub.intent.action.SIGN_OUT)) {
				onSignOut(intent);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter intentFiter = new IntentFilter();
		intentFiter.addAction(Hoothub.intent.action.SIGN_IN);
		intentFiter.addAction(Hoothub.intent.action.SIGN_OUT);
		
		registerReceiver(broadcastReceiver, intentFiter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
	
	/**
	 * Called when user signs in or application started and user was signed in 
	 * @param intent
	 */
	void onSignIn(Intent intent) {
		
	}
	
	/**
	 * Called when user chooses to sign out from account
	 * @param intent
	 */
	void onSignOut(Intent intent) {
		finish();
	}
	
}
