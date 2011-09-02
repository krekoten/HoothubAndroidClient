/**
 * 
 */
package com.hoothub.android.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hoothub.android.Hoothub;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.errors.NotAuthorized;
import com.hoothub.android.client.types.FriendInvite;
import com.hoothub.android.client.types.Group;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendInvitesService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Hoothub.TAG, "Friend Invites Service Started");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.i(Hoothub.TAG, "Friend Invites Service Stoped");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(Hoothub.TAG, "Friend Invites Service Recieved onStartCommand");
		
		new Updater(startId).start();
		
		return START_NOT_STICKY;
	}
	
	class Updater extends Thread {
		int mStartId;
		
		public Updater(int startId) {
			super("FriendInvitesService Updater");
			mStartId = startId;
		}
		
		@Override
		public void run() {
			Hoothub app = (Hoothub) getApplication();
			
			if (app.userAuthorized()) {
				try {
					
					Group<FriendInvite> invites = app.getApiClient().getFriendInvites();
					app.getFriendInvitesStorage().clear();
					for (FriendInvite item : invites) {
						app.addFriendInvite(item);
					}
					
				} catch (ClientProtocolException exception) {
					if (Hoothub.DEBUG) {
						Log.e(Hoothub.TAG, getName(), exception);
					}
				} catch (NotAuthorized exception) {
					if (Hoothub.DEBUG) {
						Log.w(Hoothub.TAG, "Got Unauthorized response. Signing out.");
					}
					sendBroadcast(new Intent(Hoothub.intent.action.SIGN_OUT));
				} catch (ClientRestException exception) {
					if (Hoothub.DEBUG) {
						Log.e(Hoothub.TAG, getName(), exception);
					}
				} catch (IOException exception) {
					if (Hoothub.DEBUG) {
						Log.e(Hoothub.TAG, getName(), exception);
					}
				}
			}
			
			stopSelf(mStartId);
		}
	}

}
