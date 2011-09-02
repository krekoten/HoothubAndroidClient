/**
 * 
 */
package com.hoothub.android.client.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hoothub.android.client.types.Group;
import com.hoothub.android.client.types.HoothubType;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public interface HoothubParser<T extends HoothubType> {
	public T parse(JSONObject object) throws JSONException, NotImplementedError;
	public Group<T> parse(JSONArray array) throws JSONException, NotImplementedError;
}
