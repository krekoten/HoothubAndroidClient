/**
 * 
 */
package com.hoothub.android.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoothub.android.client.types.User;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class UserParser extends AbstractParser<User> {

	@Override
	public User parse(JSONObject object) throws JSONException {
		User user = new User();
		
		JSONObject item = object.getJSONObject("user");
		if (item.has("id")) {
			user.setId(item.getInt("id"));
		}
		if (item.has("login")) {
			user.setLogin(item.getString("login"));
		}
		if (item.has("name")) {
			user.setName(item.getString("name"));
		}
		if (item.has("email")) {
			user.setEmail(item.getString("email"));
		}
		if (item.has("avatar_url")) {
			user.setAvatarUrl(item.getString("avatar_url"));
		}
		
		
		return user;
	}
	
}
