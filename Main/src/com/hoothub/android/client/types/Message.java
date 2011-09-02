/**
 * 
 */
package com.hoothub.android.client.types;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class Message extends AbstractType {
	String subject, body;
	User sender, recipient;
	int createdAt;
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public User getSender() {
		return sender;
	}
	
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public User getRecipient() {
		return recipient;
	}
	
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
	
	public int getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(int createdAt) {
		this.createdAt = createdAt;
	}

	public long getRecipientId() {
		return recipient.getId();
	}

	public String getRecipientName() {
		return recipient.getName();
	}

	public String getRecipientLogin() {
		return recipient.getLogin();
	}

	public String getRecipientEmail() {
		return recipient.getEmail();
	}

	public long getSenderId() {
		return sender.getId();
	}

	public String getSenderName() {
		return sender.getName();
	}

	public String getSenderLogin() {
		return sender.getLogin();
	}

	public String getSenderEmail() {
		return sender.getEmail();
	}

	public String getSenderAvatarUrl() {
		return sender.getAvatarUrl();
	}
	
	
}
