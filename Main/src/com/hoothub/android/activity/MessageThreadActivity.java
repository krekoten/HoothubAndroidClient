/**
 * 
 */
package com.hoothub.android.activity;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.types.Message;
import com.hoothub.android.widget.MessageThreadAdapter;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class MessageThreadActivity extends BaseActivity {
	
	MessageThreadAdapter adapter;
	
	Comparator<Message> comparator = new Comparator<Message>() {

		public int compare(Message object1, Message object2) {
			if (object1.getCreatedAt() > object2.getCreatedAt()) {
				return -1;
			} else if (object2.getCreatedAt() > object1.getCreatedAt()) {
				return 1;
			}
			return 0;
		}
		
	};
	
	BroadcastReceiver newMessage = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			MessageThreadActivity.this.updateThreads();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_thread_list);
		
		ListView listView = (ListView) findViewById(R.id.messageThreadListView);
		adapter = new MessageThreadAdapter(getApplicationContext());
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(Hoothub.TAG, "Clicked row ID: " + new Long(id).toString());
				Intent intent = new Intent(MessageThreadActivity.this, MessagesActivity.class);
				intent.putExtra("threadId", (Long)view.getTag());
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateThreads();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Hoothub.intent.action.NEW_MESSAGE);
		
		registerReceiver(newMessage, intentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(newMessage);
	}
	
	void updateThreads() {
		if (Hoothub.DEBUG) {
			Log.i(Hoothub.TAG, "updating threads");
		}
		adapter.clear();

		for (Long threadId : ((Hoothub) getApplication()).getMessageThreadsStorage().keySet()) {
			if (Hoothub.DEBUG) {
				Log.i(Hoothub.TAG, "updating thread id: " + threadId.toString());
			}
			
			ArrayList<Message> messages = (ArrayList<Message>) ((Hoothub) getApplication()).getMessageThreadsStorage().get(threadId);
			Message item = messages.get(messages.size() - 1);
			adapter.add(item);
		}
		
		adapter.sort(comparator);
	}
}
