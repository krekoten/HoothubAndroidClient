/**
 * 
 */
package com.hoothub.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.types.User;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendAdapter extends ArrayAdapter<User> {
	public FriendAdapter(Context context) {
		super(context, R.layout.friend_row);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.friend_row, null);
		}
		
		User item = (User) getItem(position);
		
		if (item != null) {
			TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
			ImageView avatarImage = (ImageView) view.findViewById(R.id.avatarImage);
			
			nameTextView.setText(item.getName());
			Log.i(Hoothub.TAG, "Downloading: " + item.getAvatarUrl());
			
			Bitmap bitmap = ((Hoothub) getContext()).getUrlFetcher().fetchImage(item.getAvatarUrl());
			if (bitmap != null) {
				avatarImage.setImageBitmap(bitmap);
			}
		}
		
		return view;
	}
	
	@Override
	public
	long getItemId(int position) {
		return ((User) getItem(position)).getId();
	}
}
