/**
 * 
 */
package com.hoothub.android.client.errors;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class BadRequest extends ClientRestException {

	public BadRequest(String message) {
		super(message);
	}

	public BadRequest() {
		this(null);
	}

	private static final long serialVersionUID = 1L;

}
