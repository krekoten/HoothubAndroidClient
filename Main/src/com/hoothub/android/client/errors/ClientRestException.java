/**
 * 
 */
package com.hoothub.android.client.errors;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class ClientRestException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ClientRestException(String message) {
		super(message);
	}
}
