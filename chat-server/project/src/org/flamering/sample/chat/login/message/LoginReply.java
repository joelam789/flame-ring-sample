package org.flamering.sample.chat.login.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginReply {
	
	private String messageName = "LoginReply";
	
	private String result = "Invalid User";
	
	private String serverUri = "";
	
	private String userToken = "";
	
	@JsonProperty("msg")
	public String getMessageName() {
		return messageName;
	}
	@JsonProperty("msg")
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	
	@JsonProperty("result")
	public String getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(String result) {
		this.result = result;
	}
	
	@JsonProperty("server_uri")
	public String getServerUri() {
		return serverUri;
	}

	@JsonProperty("server_uri")
	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
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
