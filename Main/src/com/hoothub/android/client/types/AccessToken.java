/**
 * 
 */
package com.hoothub.android.client.types;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class AccessToken implements HoothubType {

	String mToken;
	
	public void setToken(String token) {
		mToken = token;
	}
	
	public String getToken() {
		return mToken;
	}
	
}
