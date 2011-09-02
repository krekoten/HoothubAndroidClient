/**
 * 
 */
package com.hoothub.android.widget;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.types.FriendInvite;
import com.hoothub.android.service.FriendInvitesService;
import com.hoothub.android.service.FriendsService;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendInviteAdapter extends ArrayAdapter<FriendInvite> {
	public FriendInviteAdapter(Context context) {
		super(context, R.layout.friend_invite_row);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.friend_invite_row, null);
		}
		
		FriendInvite item = (FriendInvite) getItem(position);
		
		if (item != null) {
			TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
			ImageView avatarImage = (ImageView) view.findViewById(R.id.avatarImage);
			Button acceptButton = (Button) view.findViewById(R.id.acceptButton);
			Button denyButton = (Button) view.findViewById(R.id.denyButton);
			
			
			acceptButton.setTag(new FriendInviteRow(position, item.getId()));
			acceptButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View view) {
					FriendInviteRow  request = (FriendInviteRow) ((Button) view).getTag();
					Hoothub app = (Hoothub) getContext();
					Toast.makeText(getContext(), R.string.requestAccepted, Toast.LENGTH_LONG).show();
					try {
						app.getApiClient().acceptFriendship(request.inviteId);
						remove(getItem(request.rowId));
						getContext().startService(new Intent(getContext(), FriendsService.class));
						getContext().startService(new Intent(getContext(), FriendInvitesService.class));
						
					} catch (ClientProtocolException exception) {
						if (Hoothub.DEBUG) {
							Log.e(Hoothub.TAG, "accept invite", exception);
						}
						Toast.makeText(getContext(), R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
					} catch (ClientRestException exception) {
						if (Hoothub.DEBUG) {
							Log.e(Hoothub.TAG, "accept invite", exception);
						}
						Toast.makeText(getContext(), R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
					} catch (IOException exception) {
						if (Hoothub.DEBUG) {
							Log.e(Hoothub.TAG, "accept invite", exception);
						}
						Toast.makeText(getContext(), R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
					}
				}
			});
			
			denyButton.setTag(new FriendInviteRow(position, item.getId()));
			denyButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View view) {
					FriendInviteRow  request = (FriendInviteRow) ((Button) view).getTag();
					Hoothub app = (Hoothub) getContext();
					
					try {
						app.getApiClient().denyFriendship(request.inviteId);
						remove(getItem(request.rowId));
						getContext().startService(new Intent(getContext(), FriendInvitesService.class));
						Toast.makeText(getContext(), R.string.requestDenied, Toast.LENGTH_LONG).show();
					} catch (ClientProtocolException exception) {
						if (Hoothub.DEBUG) {
							Log.e(Hoothub.TAG, "accept invite", exception);
						}
						Toast.makeText(getContext(), R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
					} catch (ClientRestException exception) {
						if (Hoothub.DEBUG) {
							Log.e(Hoothub.TAG, "accept invite", exception);
						}
						Toast.makeText(getContext(), R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
					} catch (IOException exception) {
						if (Hoothub.DEBUG) {
							Log.e(Hoothub.TAG, "accept invite", exception);
						}
						Toast.makeText(getContext(), R.string.errorPostingActivity, Toast.LENGTH_LONG).show();
					}
				}
			});
			
			nameTextView.setText(item.getUserName());
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
		return ((FriendInvite) getItem(position)).getId();
	}
	
	public class FriendInviteRow {
		public Long inviteId;
		public int rowId;
		public FriendInviteRow(int rowId, Long inviteId) {
			this.rowId = rowId;
			this.inviteId = inviteId;
		}
	}
}
