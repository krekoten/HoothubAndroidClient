/**
 * 
 */
package com.hoothub.android.client.types;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class FriendInvite extends AbstractType {
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return user.getId();
	}

	public String getUserName() {
		return user.getName();
	}

	public String getUserLogin() {
		return user.getLogin();
	}

	public String getUserEmail() {
		return user.getEmail();
	}

	public String getUserAvatarUrl() {
		return user.getAvatarUrl();
	}
}
