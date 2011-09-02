/**
 * 
 */
package com.hoothub.android.client.types;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class Activity extends AbstractType {
	String title, body;
	User user;
	long createdAt;

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	
	/* Delegate methods to user object */
	
	public String getUserName() {
		return user.getName();
	}
	
	public long getUserId() {
		return user.getId();
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
