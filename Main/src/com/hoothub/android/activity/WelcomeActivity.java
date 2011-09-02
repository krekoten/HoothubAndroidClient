package com.hoothub.android.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hoothub.android.Hoothub;
import com.hoothub.android.R;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class WelcomeActivity extends BaseActivity {
	
	/**
	 * TODO: Add splash screen http://www.anddev.org/simple_splash_screen-t811.html
	 */
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.welcome);
		
		Intent intent;
		
		if (!((Hoothub)getApplication()).userAuthorized()) {
			intent = new Intent(this, SignInActivity.class);
		} else {
			intent = new Intent(this, HomeActivity.class);
		}
		startActivity(intent);
        finish();
	}
}
