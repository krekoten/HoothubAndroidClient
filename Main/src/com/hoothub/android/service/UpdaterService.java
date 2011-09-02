/**
 * 
 */
package com.hoothub.android.service;

import com.hoothub.android.Hoothub;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class UpdaterService extends Service {

	Updater worker = new Updater();
	boolean runFlag = false;

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Hoothub.intent.action.START_UPDATER)) {
				worker.start();
			}
			if (intent.getAction().equals(Hoothub.intent.action.STOP_UPDATER)) {
				worker.stop();
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(broadcastReceiver);
		
		setRunFlagOff();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Hoothub.intent.action.START_UPDATER);
		filter.addAction(Hoothub.intent.action.STOP_UPDATER);
		registerReceiver(broadcastReceiver, filter);
		
		synchronized (this) {
			if (!runFlag) {
				setRunFlagOn();
				worker.start();
			}
		}
		
		return START_STICKY;
	}
	
	synchronized void setRunFlagOn() {
		runFlag = true;
	}
	
	synchronized void setRunFlagOff() {
		runFlag = false;
	}

	class Updater extends Thread {
		
		public Updater() {
			super("UpdaterService Updater");
		}
		
		public void run() {
			while (runFlag) {
				
				startService(new Intent(UpdaterService.this, ActivitiesService.class));
				startService(new Intent(UpdaterService.this, FriendsService.class));
				startService(new Intent(UpdaterService.this, FriendInvitesService.class));
				startService(new Intent(UpdaterService.this, MessagesService.class));
				
				Log.i(Hoothub.TAG, "Working...");
				try {
					sleep(((Hoothub) getApplication()).getSettings().getLong("updateTimeout", 1) * 1000 * 60);
				} catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
	
}
