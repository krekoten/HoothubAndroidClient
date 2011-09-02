/**
 * 
 */
package com.hoothub.android.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hoothub.android.activity.SignInActivity;
import com.hoothub.android.service.UpdaterService;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class SignOutReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context, UpdaterService.class);
		context.stopService(serviceIntent);
		
		Intent signInIntent = new Intent(context, SignInActivity.class);
		signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(signInIntent);
	}

}
