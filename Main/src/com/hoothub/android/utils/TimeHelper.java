/**
 * 
 */
package com.hoothub.android.utils;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class TimeHelper {
	public static String unixTimestampToDate(Context context, long timestamp) {
		return DateFormat.getDateFormat(context).format(new Date(timestamp * 1000));
	}
}
