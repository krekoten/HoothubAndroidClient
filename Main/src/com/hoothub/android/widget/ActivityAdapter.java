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
import com.hoothub.android.client.types.Activity;
import com.hoothub.android.utils.TimeHelper;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ActivityAdapter extends ArrayAdapter<Activity> {

	public ActivityAdapter(Context context) {
		super(context, R.layout.activity_row);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.activity_row, null);
		}
		
		Activity item = (Activity) getItem(position);
		
		if (item != null) {
			TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView),
					bodyTextView = (TextView) view.findViewById(R.id.bodyTextView),
					dateTextView = (TextView) view.findViewById(R.id.dateTextView);
			ImageView avatarImage = (ImageView) view.findViewById(R.id.avatarImage);
			
			nameTextView.setText(item.getUserName());
			bodyTextView.setText(item.getBody());
			dateTextView.setText(TimeHelper.unixTimestampToDate(getContext(), item.getCreatedAt()));
			
			Log.i(Hoothub.TAG, "Downloading: " + item.getUserAvatarUrl());
				
			Bitmap bitmap = ((Hoothub) getContext()).getUrlFetcher().fetchImage(item.getUserAvatarUrl());
			if (bitmap != null) {
				avatarImage.setImageBitmap(bitmap);
			}
		}
		
		return view;
	}
	
	@Override
	public
	long getItemId(int position) {
		return ((Activity) getItem(position)).getId();
	}
}
