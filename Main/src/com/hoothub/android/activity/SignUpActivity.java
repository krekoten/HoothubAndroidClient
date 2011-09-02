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
public class SignUpActivity extends BaseActivity {
	
	TextView mLoginField, mEmailField, mFullNameField, mPasswordField;
	
	OnClickListener signUpButton = new OnClickListener() {
		
		public void onClick(View view) {
			
			String login = mLoginField.getText().toString(),
					password = mPasswordField.getText().toString(),
					email = mEmailField.getText().toString(),
					fullName = mFullNameField.getText().toString();
			
			if (login.equals("") || password.equals("") || email.equals("") || fullName.equals("")) {
				
				Toast.makeText(SignUpActivity.this, R.string.fillInAllFieldsNotice, Toast.LENGTH_LONG).show();
				return;
			}
			try {
				if (createUser(login, password, email, fullName)) {
					SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
					// TODO: Send message about sign in
				} else {
					/**
					 * TODO: Handle errors and show them. Requires API changes too.
					 */
					Log.e(Hoothub.TAG, "Error occured during sign up. Fix sign up handling errors");
				}
			} catch (ClientProtocolException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "SignUp", exception);
				}
			} catch (IOException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "SignUp", exception);
				}
				Toast.makeText(SignUpActivity.this, R.string.networkProblemNotice, Toast.LENGTH_LONG).show();
			}
		}

		private boolean createUser(String login, String password, String email,
				String fullName) throws ClientProtocolException, IOException {
			
			Hoothub app = (Hoothub) SignUpActivity.this.getApplication();
			
			try {
				String token = app.getApiClient().createUser(login, password, email, fullName);
				if (token != null && !token.equals("")) {
					app.signIn(login, token);
					return true;
				}
			} catch (ClientRestException exception) {
				if (Hoothub.DEBUG) {
					Log.e(Hoothub.TAG, "createUser", exception);
				}
			}
			
			return false;
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		mLoginField = (TextView) findViewById(R.id.loginField);
		mPasswordField = (TextView) findViewById(R.id.passwordField);
		mFullNameField = (TextView) findViewById(R.id.fullNameField);
		mEmailField = (TextView) findViewById(R.id.emailField);
		
		((Button) findViewById(R.id.signUpButton)).setOnClickListener(signUpButton);
	}
	
	@Override
	void onSignIn(Intent intent) {
		finish();
	}
	
	@Override
	void onSignOut(Intent intent) {
		// Do nothing, we should not close on sign out
	}
}
