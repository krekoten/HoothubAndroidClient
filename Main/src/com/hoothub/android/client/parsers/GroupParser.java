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
public class GroupParser<T extends HoothubType> extends AbstractParser<T> {
	
	HoothubParser<T> mItemParser;
	
	public GroupParser(HoothubParser<T> itemParser) {
		mItemParser = itemParser;
	}

	@Override
	public Group<T> parse(JSONArray array) throws JSONException, NotImplementedError {
		Group<T> group = new Group<T>();
		
		for (int i = 0, l = array.length(); i < l; i++) {
			JSONObject item = array.getJSONObject(i);
			group.add(mItemParser.parse(item));
		}
		
		return group;
	}

}
