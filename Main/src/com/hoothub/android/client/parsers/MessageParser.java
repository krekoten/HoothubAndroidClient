/**
 * 
 */
package com.hoothub.android.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoothub.android.client.types.Message;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class MessageParser extends AbstractParser<Message> {
	@Override
	public Message parse(JSONObject object) throws JSONException {
		Message message = new Message();
		
		JSONObject item = object.getJSONObject("message");
		
		message.setId(item.getInt("id"));
		if (item.has("subject")) {
			message.setSubject(item.getString("subject"));
		}
		message.setBody(item.getString("body"));
		message.setCreatedAt(item.getInt("created_at"));
		
		message.setSender(new UserParser().parse(item.getJSONObject("sender")));
		message.setRecipient(new UserParser().parse(item.getJSONObject("recipient")));
		
		return message;
	}
}
