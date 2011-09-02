/**
 * 
 */
package com.hoothub.android.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hoothub.android.Hoothub;
import com.hoothub.android.service.UpdaterService;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class SignInReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context, UpdaterService.class);
		intent.setAction(Hoothub.intent.action.START_UPDATER);
		
		context.startService(serviceIntent);
	}

}
