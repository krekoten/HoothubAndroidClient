/**
 * 
 */
package com.hoothub.android.client.errors;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ServerError extends ClientRestException {

	public ServerError(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
}
