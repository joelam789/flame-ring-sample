package org.flamering.sample.chat.lobby.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnterLobbyReply {
	
	private String messageName = "EnterLobbyReply";
	
	private String result = "Invalid Session";
	
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

}
