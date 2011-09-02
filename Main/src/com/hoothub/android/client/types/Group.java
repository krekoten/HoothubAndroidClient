/**
 * 
 */
package com.hoothub.android.client.types;

import java.util.ArrayList;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class Group<T extends HoothubType> extends ArrayList<T> implements HoothubType {
	private static final long serialVersionUID = 1L;
}
