/**
 * 
 */
package com.hoothub.android.client;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.hoothub.android.client.errors.ClientRestException;
import com.hoothub.android.client.http.REST;
import com.hoothub.android.client.parsers.AccessTokenParser;
import com.hoothub.android.client.parsers.ActivityParser;
import com.hoothub.android.client.parsers.FriendInviteParser;
import com.hoothub.android.client.parsers.GroupParser;
import com.hoothub.android.client.parsers.MessageParser;
import com.hoothub.android.client.parsers.UserParser;
import com.hoothub.android.client.types.AccessToken;
import com.hoothub.android.client.types.Activity;
import com.hoothub.android.client.types.FriendInvite;
import com.hoothub.android.client.types.Group;
import com.hoothub.android.client.types.Message;
import com.hoothub.android.client.types.User;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class HoothubApi{
	
	String mClientId, mClientSecret;
	REST mRestClient;

	public HoothubApi(String baseUrl, String clientId, String clientSecret) {
		mRestClient = new REST(baseUrl);
		mRestClient.addDefaultHeader("Accept", "application/json");
		mClientId = clientId;
		mClientSecret = clientSecret;
	}
	
	public void setAccessToken(String token) {
		mRestClient.addDefaultHeader("Authorization", "Bearer " + token);
	}
	
	public void removeAccessToken() {
		mRestClient.removeDefaultKey("Authorization");
	}

	public String obtainAccessToken(String username, String password) throws ClientRestException, ClientProtocolException, IOException {
    	
		AccessToken token = (AccessToken) mRestClient.post("/oauth/access_token", new AccessTokenParser(),
			new BasicNameValuePair("grant_type", "password"),
			new BasicNameValuePair("client_id", mClientId),
			new BasicNameValuePair("client_secret", mClientSecret),
			new BasicNameValuePair("username", username),
			new BasicNameValuePair("password", password)
		);
		
		return token.getToken();
	}

	public String createUser(String login, String password, String email, String fullName) throws ClientRestException, ClientProtocolException, IOException {
		AccessToken token = (AccessToken) mRestClient.post("/users", new AccessTokenParser(),
				new BasicNameValuePair("client_id", mClientId),
				new BasicNameValuePair("client_secret", mClientSecret),
				new BasicNameValuePair("user[login]", login),
				new BasicNameValuePair("user[password]", password),
				new BasicNameValuePair("user[email]", email),
				new BasicNameValuePair("user[full_name]", fullName)
		);
			
		return token.getToken();
	}

	@SuppressWarnings("unchecked")
	public Group<Activity> getActivities() throws ClientProtocolException, ClientRestException, IOException {
		return (Group<Activity>)mRestClient.get("/activities", new GroupParser<Activity>(new ActivityParser()));
	}
	
	@SuppressWarnings("unchecked")
	public Group<User> getFriends() throws ClientProtocolException, ClientRestException, IOException {
		return (Group<User>) mRestClient.get("/friends", new GroupParser<User>(new UserParser()));
	}
	
	@SuppressWarnings("unchecked")
	public Group<FriendInvite> getFriendInvites() throws ClientProtocolException, ClientRestException, IOException {
		return (Group<FriendInvite>) mRestClient.get("/friend_invites", new GroupParser<FriendInvite>(new FriendInviteParser()));
	}

	@SuppressWarnings("unchecked")
	public Group<Message> getMessages() throws ClientProtocolException, ClientRestException, IOException {
		return (Group<Message>) mRestClient.get("/messages", new GroupParser<Message>(new MessageParser()));
	}

	public void createActivity(String body) throws ClientProtocolException, ClientRestException, IOException {
		mRestClient.post("/activities", new BasicNameValuePair("activity[body]", body));
	}

	public void acceptFriendship(Long requestId) throws ClientProtocolException, ClientRestException, IOException {
		mRestClient.post("/friend_invites/" + requestId.toString() + "/accept");
	}
	
	public void denyFriendship(Long requestId) throws ClientProtocolException, ClientRestException, IOException {
		mRestClient.post("/friend_invites/" + requestId.toString() + "/deny");
	}
	
	public User getSelf() throws ClientProtocolException, ClientRestException, IOException {
		return (User) mRestClient.get("/user", new UserParser());
	}

	public void createMessage(Long threadId, String body) throws ClientProtocolException, ClientRestException, IOException {
		mRestClient.post("/messages", new BasicNameValuePair("message[body]", body),
										new BasicNameValuePair("message[recipient_id]", threadId.toString()));
	}
}
