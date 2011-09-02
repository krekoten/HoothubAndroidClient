/**
 * 
 */
package com.hoothub.android.client.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hoothub.android.Hoothub;
import com.hoothub.android.client.parsers.GroupParser;
import com.hoothub.android.client.parsers.HoothubParser;
import com.hoothub.android.client.parsers.NotImplementedError;
import com.hoothub.android.client.types.HoothubType;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class JSONUtils {
	public static HoothubType consume(String body, HoothubParser<? extends HoothubType> parser) throws JSONException {
		try {
			if (parser instanceof GroupParser) {
				return parser.parse(new JSONArray(body));
			} else {
				return parser.parse(new JSONObject(body));
			}
		} catch (NotImplementedError exception) {
			if (Hoothub.DEBUG) {
				Log.e(Hoothub.TAG, "JSONUtils consume", exception);
			}
			return null;
		}
	}
}
