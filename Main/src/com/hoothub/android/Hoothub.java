package com.hoothub.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.util.Log;

import com.hoothub.android.client.HoothubApi;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.types.Activity;
import com.hoothub.android.client.types.FriendInvite;
import com.hoothub.android.client.types.Message;
import com.hoothub.android.client.types.User;
import com.hoothub.android.utils.URLFetcher;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public final class Hoothub extends Application {
	
	public static class intent {
		public static class action {
			/**
			 * User signed in
			 */
			public static final String SIGN_IN = "com.hoothub.android.intent.action.SIGN_IN";
			/**
			 * User signed out
			 */
			public static final String SIGN_OUT = "com.hoothub.android.intent.action.SIGN_OUT";
			/**
			 * Start update event broadcasting
			 */
			public static final String START_UPDATER = "com.hoothub.android.intent.action.START_UPDATER";
			/**
			 * Stop update event broadcasting
			 */
			public static final String STOP_UPDATER = "com.hoothub.android.intent.action.STOP_UPDATER";
			public static final String NEW_ACTIVITY = "com.hoothub.android.intent.action.NEW_ACTIVITY";
			public static final String NEW_FRIEND = "com.hoothub.android.intent.action.NEW_FRIEND";
			public static final String NEW_FRIEND_INVITE = "com.hoothub.android.intent.action.NEW_FRIEND_INVITE";
			public static final String NEW_MESSAGE = "com.hoothub.android.intent.action.NEW_MESSAGE";
			public static final String NEW_MESSAGE_THREAD = "com.hoothub.android.intent.action.NEW_MESSAGE_THREAD";
		}
	}
	
	public static final boolean DEBUG = true;
	public static final String TAG = "Hoothub";
	
	public static final String CLIENT_ID = "1";
	public static final String CLIENT_SECRET = "beta_secret";
	
	//public final static String BASE_URL = "http://10.0.2.2:3000/api/v1";
	public final static String BASE_URL = "http://hoothub.com/api/v1";
	//public final static String BASE_URL = "http://192.168.0.101:3000/api/v1";
	//public final static String BASE_URL = "http://172.16.0.101:3000/api/v1";
	
	HoothubApi mApiClient;
	
	List<Activity> activityStorage = new ArrayList<Activity>();
	List<User> friendStorage = new ArrayList<User>();
	List<FriendInvite> friendInviteStorage = new ArrayList<FriendInvite>();
	List<Message> messageStorage = new ArrayList<Message>();
	
	Map<Long, List<Message>> messageThreadStorage = new HashMap<Long, List<Message>>();
	
	String login;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (userAuthorized()) {
			// Hey, we are online :)
			notifySignedIn(getAccessToken());
		}
	}
	
	
	public SharedPreferences getSettings() {
		return getSharedPreferences("hoothub", MODE_PRIVATE);
	}
	
	public SharedPreferences getUserSettings() {
		if (login != null) {
			return getSharedPreferences(login, MODE_PRIVATE);
		}
		return null;
	}
	
	public void signOut() {
		this.login = null;
		sendBroadcast(new Intent(intent.action.SIGN_OUT));
		
		activityStorage.clear();
		friendStorage.clear();
		friendInviteStorage.clear();
		messageStorage.clear();
		messageThreadStorage.clear();
		mUserId = 0;
		
		getSettings().edit().remove("userToken").commit();
	}
	
	public void signIn(String login, String token) {
		this.login = login;
		setAccessToken(token);
		notifySignedIn(token);
	}
	
	void notifySignedIn(String token) {
		sendBroadcast(new Intent(intent.action.SIGN_IN));
		getApiClient().setAccessToken(token);
	}
	
	public boolean userAuthorized() {
		return getAccessToken() != null;
	}
	
	public void quitApplication() {
		Process.killProcess(Process.myPid());
	}
	
	String getAccessToken() {
		return getSettings().getString("userToken", null);
	}
	
	void setAccessToken(String accessToken) {
		getSettings().edit().putString("userToken", accessToken).commit();
	}
	
	public synchronized HoothubApi getApiClient() {
		if (mApiClient == null) {
			mApiClient = new HoothubApi(BASE_URL, CLIENT_ID, CLIENT_SECRET);
		}
		return mApiClient;
	}

	public List<Activity> getActivityStorage() {
		return activityStorage;
	}
	
	synchronized public void addActivity(Activity item) {
		if (!activityStorage.contains(item)) {
			activityStorage.add(item);
			sendBroadcast(new Intent(intent.action.NEW_ACTIVITY));
		}
	}


	public List<User> getFriendsStorage() {
		return friendStorage;
	}


	synchronized public void addFriend(User item) {
		if (!friendStorage.contains(item)) {
			friendStorage.add(item);
			sendBroadcast(new Intent(intent.action.NEW_FRIEND));
		}
	}


	public List<FriendInvite> getFriendInvitesStorage() {
		return friendInviteStorage;
	}
	
	synchronized public void addFriendInvite(FriendInvite item) {
		if (!friendInviteStorage.contains(item)) {
			friendInviteStorage.add(item);
			sendBroadcast(new Intent(intent.action.NEW_FRIEND_INVITE));
		}
	}
	
	public List<Message> getMessagesStorage() {
		return messageStorage;
	}
	
	public Map<Long, List<Message>> getMessageThreadsStorage() {
		return messageThreadStorage;
	}
	
	long mUserId = 0;
	
	public synchronized long userId() {
		if (mUserId == 0) {
			try {
				mUserId = getApiClient().getSelf().getId();
			} catch (ClientProtocolException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			} catch (ClientRestException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		}
		return mUserId;
	}
	
	public void makeThread(Message item) {
		long userId = userId();
		long otherUserId = item.getSenderId() == userId ? item.getRecipientId() : item.getSenderId();
		
		ArrayList<Message> list;
		
		if (!getMessageThreadsStorage().containsKey(otherUserId)) {
			list = new ArrayList<Message>();
			getMessageThreadsStorage().put(otherUserId, list);
			sendBroadcast(new Intent(Hoothub.intent.action.NEW_MESSAGE_THREAD));
		} else {
			list = (ArrayList<Message>) getMessageThreadsStorage().get(otherUserId);
		}
		list.add(item);
		if (DEBUG) {
			Log.i(TAG, "List: " + list.toString());
		}
		Intent intent = new Intent(Hoothub.intent.action.NEW_MESSAGE);
		intent.putExtra("threadId", otherUserId);
		sendBroadcast(intent);
	}
	
	synchronized public void addMessage(Message item) {
		if (!messageStorage.contains(item)) {
			messageStorage.add(item);
			makeThread(item);
		}
	}
	
	URLFetcher mUrlFetcher;
	
	synchronized public URLFetcher getUrlFetcher() {
		if (mUrlFetcher == null) {
			mUrlFetcher = new URLFetcher();
		}
		return mUrlFetcher;
	}
}
