/**
 * 
 */
package com.hoothub.android.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;
import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.types.User;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ProfileActivity extends BaseActivity {
	
	ImageView avatarImage;
	TextView fullNameField;
	Button saveButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		fullNameField = (TextView) findViewById(R.id.fullNameField);
		saveButton = (Button) findViewById(R.id.saveButton);
		avatarImage = (ImageView) findViewById(R.id.avatarImage);
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				try {
					((Hoothub) getApplication()).getApiClient().updateSelf(fullNameField.getText().toString());
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
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		try {
			User self = ((Hoothub) getApplication()).getApiClient().getSelf();
			fullNameField.setText(self.getName());
			Bitmap image = ((Hoothub) getApplication()).getUrlFetcher().fetchImage(self.getAvatarUrl());
			if (image != null) {
				avatarImage.setImageBitmap(image);
			}
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
}
