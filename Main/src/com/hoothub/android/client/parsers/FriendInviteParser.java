/**
 * 
 */
package com.hoothub.android.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoothub.android.client.types.FriendInvite;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendInviteParser extends AbstractParser<FriendInvite> {

	@Override
	public FriendInvite parse(JSONObject object) throws JSONException {
		FriendInvite friendInvite = new FriendInvite();
		JSONObject item = object.getJSONObject("friend_invite");
		
		friendInvite.setId(item.getInt("id"));
		friendInvite.setUser(new UserParser().parse(item.getJSONObject("from")));
		
		return friendInvite;
	}

}
