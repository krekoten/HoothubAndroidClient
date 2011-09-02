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
import com.hoothub.android.client.types.Activity;
import com.hoothub.android.client.types.Group;

/**
 * 
 */

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ActivitiesService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(Hoothub.TAG, "Activities Service Started");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.i(Hoothub.TAG, "Activities Service Stoped");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.i(Hoothub.TAG, "Activities Service Recieved onStartCommand");
		
		new Updater(startId).start();
		
		return START_NOT_STICKY;
	}
	
	class Updater extends Thread {
		int mStartId;
		
		public Updater(int startId) {
			super("ActivitiesService Updater");
			mStartId = startId;
		}
		
		@Override
		public void run() {
			Hoothub app = (Hoothub) getApplication();
			
			if (app.userAuthorized()) {
				try {
					
					Group<Activity> activities = app.getApiClient().getActivities();
					
					for (Activity activity : activities) {
						app.addActivity(activity);
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
