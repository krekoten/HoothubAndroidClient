/**
 * 
 */
package com.hoothub.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.types.Activity;
import com.hoothub.android.utils.TimeHelper;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class AcivityItemActivity extends BaseActivity {
	
	long activityId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);

		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			activityId = intent.getLongExtra("activityId", 0);
			
			if (activityId == 0) {
				finish();
			}
			
			Activity item = null;
			for (Activity activity : ((Hoothub) getApplication()).getActivityStorage()) {
				if (activity.getId() == activityId) {
					item = activity;
					break;
				}
			}
			
			
			if (item != null) {
				((TextView) findViewById(R.id.nameTextView)).setText(item.getUserName());
				((TextView) findViewById(R.id.dateTextView)).setText(
					TimeHelper.unixTimestampToDate(getApplicationContext(), item.getCreatedAt())
				);
				((TextView) findViewById(R.id.bodyTextView)).setText(item.getBody());
			}
		}
	}
}
