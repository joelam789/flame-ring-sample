package org.flamering.sample.chat.login.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogoutRequest {
	
	private String messageName = "LogoutRequest";
	
	private String userName = "";
	
	private String userToken = "";
	
	@JsonProperty("msg")
	public String getMessageName() {
		return messageName;
	}
	@JsonProperty("msg")
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	
	@JsonProperty("user_name")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("user_name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonProperty("user_token")
	public String getUserToken() {
		return userToken;
	}

	@JsonProperty("user_token")
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
