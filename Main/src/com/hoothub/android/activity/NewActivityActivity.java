/**
 * 
 */
package com.hoothub.android.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.service.ActivitiesService;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class NewActivityActivity extends BaseActivity {
	
	TextView bodyTextView;
	
	OnClickListener postButtonAction = new OnClickListener() {
		
		public void onClick(View view) {
			Hoothub app = (Hoothub) getApplication();
			
			try {
				app.getApiClient().createActivity(bodyTextView.getText().toString());
			} catch (ClientProtocolException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "post activity", exception);
				}
				Toast.makeText(NewActivityActivity.this, R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
				return;
			} catch (ClientRestException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "post activity", exception);
				}
				Toast.makeText(NewActivityActivity.this, R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
				return;
			} catch (IOException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "post activity", exception);
				}
				Toast.makeText(NewActivityActivity.this, R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
				return;
			}
			
			Toast.makeText(NewActivityActivity.this, R.string.activityPosted, Toast.LENGTH_LONG).show();
			startService(new Intent(NewActivityActivity.this, ActivitiesService.class));
			finish();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_activity);
		
		bodyTextView = (TextView) findViewById(R.id.bodyTextView);
		((Button) findViewById(R.id.postButton)).setOnClickListener(postButtonAction);
	}
}
