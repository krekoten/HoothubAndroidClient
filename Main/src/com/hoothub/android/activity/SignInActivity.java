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

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class SignInActivity extends BaseActivity {
	
	TextView mLoginField, mPasswordField, mFullNameField, mEmailField;
	
	OnClickListener signUpButton = new OnClickListener() {
		
		public void onClick(View view) {
			Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
			SignInActivity.this.startActivity(intent);
		}
	};
	
	OnClickListener signInButton = new OnClickListener() {
		
		public void onClick(View view) {
			if (mLoginField.getText().toString().equals("") || mPasswordField.getText().toString().equals("")) {
				Toast.makeText(SignInActivity.this, R.string.fillInAllFieldsNotice, Toast.LENGTH_LONG).show();
				return;
			}

			try {
				boolean isAuthorized = obtainAuthorization(
					mLoginField.getText().toString(),
					mPasswordField.getText().toString()
				);
				
				if (isAuthorized) {
					SignInActivity.this.startActivity(new Intent(SignInActivity.this, HomeActivity.class));
				} else {
					Toast.makeText(SignInActivity.this, R.string.wrongSingInCredentials, Toast.LENGTH_LONG).show();
				}
			} catch (ClientProtocolException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "SignIn", exception);
				}
			} catch (IOException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "SignIn", exception);
				}
				Toast.makeText(SignInActivity.this, R.string.networkProblemNotice, Toast.LENGTH_LONG).show();
			}
		}
		
		public boolean obtainAuthorization(String username, String password) throws ClientProtocolException, IOException {
			Hoothub app = (Hoothub) SignInActivity.this.getApplication();
			try {
				String token = app.getApiClient().obtainAccessToken(username, password);
				if(token != null && !token.equals("")) {
					app.signIn(username, token);
					return true;
				}
			} catch (ClientRestException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "obtainAuthorization", exception);
				} 
			}
			return false;
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		
		mLoginField = (TextView) findViewById(R.id.loginField);
		mPasswordField = (TextView) findViewById(R.id.passwordField);
		
		((Button) findViewById(R.id.signUpButton)).setOnClickListener(signUpButton);
		((Button) findViewById(R.id.signInButton)).setOnClickListener(signInButton);
	}
	
	@Override
	void onSignIn(Intent intent) {
		finish();
	}
}
