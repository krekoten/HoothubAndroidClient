/**
 * 
 */
package com.hoothub.android.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoothub.android.client.types.AccessToken;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class AccessTokenParser extends AbstractParser<AccessToken> {
	
	static final String TAG = "AccessTokenParser";
	
	@Override
	public AccessToken parse(JSONObject object) throws JSONException {
		AccessToken token = new AccessToken();
		if(object.has("access_token")) {
			token.setToken(object.getString("access_token"));
		}
		return token;
	}
}
