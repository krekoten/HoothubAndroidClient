/**
 * 
 */
package com.hoothub.android.client.errors;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class NotAuthorized extends ClientRestException {

	public NotAuthorized(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
