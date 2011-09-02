/**
 * 
 */
package com.hoothub.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.types.Message;
import com.hoothub.android.utils.TimeHelper;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class MessageThreadAdapter extends ArrayAdapter<Message> {
	
	public MessageThreadAdapter(Context context) {
		super(context, R.layout.message_thread_row);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.message_thread_row, null);
		}
		
		Message item = (Message) getItem(position);
		view.setTag(item.getSenderId() == ((Hoothub) getContext()).userId() ? item.getRecipientId() : item.getSenderId());
		
		if (item != null) {
			TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView),
					chatWithUserName = (TextView) view.findViewById(R.id.chatWithUserName),
					bodyTextView = (TextView) view.findViewById(R.id.bodyTextView),
					dateTextView = (TextView) view.findViewById(R.id.dateTextView);
			
			ImageView avatarImage = (ImageView) view.findViewById(R.id.avatarImage);
			
			String otherUserName = item.getSenderId() == ((Hoothub) getContext()).userId() ? item.getRecipientName() : item.getSenderName();
			
			chatWithUserName.setText(otherUserName);
			nameTextView.setText(item.getSenderName());
			bodyTextView.setText(item.getBody());
			dateTextView.setText(TimeHelper.unixTimestampToDate(getContext(), item.getCreatedAt()));
			Bitmap bitmap = ((Hoothub) getContext()).getUrlFetcher().fetchImage(item.getSenderAvatarUrl());
			if (bitmap != null) {
				avatarImage.setImageBitmap(bitmap);
			}
		}
		
		return view;
	}
	
	@Override
	public
	long getItemId(int position) {
		return ((Message) getItem(position)).getId();
	}
}
