package com.hoothub.android.client.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hoothub.android.client.types.Group;
import com.hoothub.android.client.types.HoothubType;

public abstract class AbstractParser<T extends HoothubType> implements HoothubParser<T> {

	public T parse(JSONObject object) throws JSONException, NotImplementedError {
		throw new NotImplementedError("item parser not implemented");
	}

	public Group<T> parse(JSONArray array) throws JSONException, NotImplementedError {
		throw new NotImplementedError("group parser not implemented");
	}

}
