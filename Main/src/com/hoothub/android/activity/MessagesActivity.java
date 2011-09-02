/**
 * 
 */
package com.hoothub.android.activity;

import java.io.IOException;
import java.util.Comparator;

import org.apache.http.client.ClientProtocolException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.types.Message;
import com.hoothub.android.service.MessagesService;
import com.hoothub.android.widget.MessageAdapter;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class MessagesActivity extends BaseActivity {
	
	MessageAdapter adapter;
	long mThreadId;
	
	TextView bodyTextView;
	Button postButton;
	
	Comparator<Message> comparator = new Comparator<Message>() {

		public int compare(Message object1, Message object2) {
			if (object1.getCreatedAt() > object2.getCreatedAt()) {
				return 1;
			} else if (object2.getCreatedAt() > object1.getCreatedAt()) {
				return -1;
			}
			return 0;
		}
		
	};
	
	BroadcastReceiver newMessage = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getLongExtra("threadId", 0) == mThreadId) {
				MessagesActivity.this.updateThreads(mThreadId);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages_list);
		
		ListView listView = (ListView) findViewById(R.id.messageThreadListView);
		adapter = new MessageAdapter(getApplicationContext());
		
		bodyTextView = (TextView) findViewById(R.id.bodyTextView);
		postButton = (Button) findViewById(R.id.postButton);
		
		postButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				Hoothub app = (Hoothub) getApplication();
				try {
					app.getApiClient().createMessage(mThreadId, bodyTextView.getText().toString());
					bodyTextView.setText("");
				} catch (ClientProtocolException exception) {
					if (Hoothub.DEBUG) {
						Log.e(Hoothub.TAG, "post message", exception);
					}
					Toast.makeText(MessagesActivity.this, R.string.errorPostingMessage, Toast.LENGTH_LONG).show();
					return;
				} catch (ClientRestException exception) {
					if (Hoothub.DEBUG) {
						Log.e(Hoothub.TAG, "post message", exception);
					}
					Toast.makeText(MessagesActivity.this, R.string.errorPostingMessage, Toast.LENGTH_LONG).show();
					return;
				} catch (IOException exception) {
					if (Hoothub.DEBUG) {
						Log.e(Hoothub.TAG, "post message", exception);
					}
					Toast.makeText(MessagesActivity.this, R.string.errorPostingMessage, Toast.LENGTH_LONG).show();
					return;
				}
				Toast.makeText(MessagesActivity.this, R.string.messagePosted, Toast.LENGTH_LONG).show();
				startService(new Intent(MessagesActivity.this, MessagesService.class));
			}
		});
		
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Intent createIntent = getIntent();
		if (createIntent.getExtras() == null) {
			finish();
		}
		
		mThreadId = (long) createIntent.getLongExtra("threadId", 0);
		
		updateThreads(mThreadId);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Hoothub.intent.action.NEW_MESSAGE);
		
		registerReceiver(newMessage, intentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(newMessage);
	}
	
	void updateThreads(Long threadId) {
		if (Hoothub.DEBUG) {
			Log.i(Hoothub.TAG, "updating thread ID: " + threadId.toString());
		}
		adapter.clear();

		for (Message item : ((Hoothub) getApplication()).getMessageThreadsStorage().get(threadId)) {
			adapter.add(item);
		}
		
		adapter.sort(comparator);
	}
}
