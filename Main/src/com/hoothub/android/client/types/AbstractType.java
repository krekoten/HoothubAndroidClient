/**
 * 
 */
package com.hoothub.android.client.types;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public abstract class AbstractType implements HoothubType {
	long mId;
	
	public long getId() {
		return mId; 
	}
	
	public void setId(long id) {
		mId = id;
	}
	
	@Override
	public boolean equals(Object other) {
		return getClass().equals(other.getClass()) && getId() == ((AbstractType) other).getId();
	}
}
