/**
 * 
 */
package com.hoothub.android.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hoothub.android.client.types.Activity;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ActivityParser extends AbstractParser<Activity> {

	BroadcastReceiver newActivity = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
	};
	
	@Override
	public Activity parse(JSONObject object) throws JSONException {
		Activity activity = new Activity();
		
		JSONObject item = object.getJSONObject("activity");
		activity.setId(item.getInt("id"));
		if (item.has("title")) {
			activity.setTitle(item.getString("title"));
		}
		if (item.has("body")) {
			activity.setBody(item.getString("body"));
		}
		activity.setCreatedAt(item.getLong("created_at"));
		activity.setUser(new UserParser().parse(item.getJSONObject("author")));
		
		return activity;
	}
}
