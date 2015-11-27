package org.flamering.sample.chat.room.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendMessageRequest {
	
	private String messageName = "SendMessageRequest";
	
	private String userName = "";
	
	private String userToken = "";
	
	private String roomName = "";
	
	private String target = "";
	
	private String words = "";
	
	private boolean privateChat = false;
	
	
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
	
	@JsonProperty("room_name")
	public String getRoomName() {
		return roomName;
	}
	@JsonProperty("room_name")
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	@JsonProperty("target")
	public String getTarget() {
		return target;
	}
	@JsonProperty("target")
	public void setTarget(String target) {
		this.target = target;
	}
	
	@JsonProperty("words")
	public String getWords() {
		return words;
	}
	@JsonProperty("words")
	public void setWords(String words) {
		this.words = words;
	}
	
	@JsonProperty("is_private")
	public boolean isPrivateChat() {
		return privateChat;
	}
	@JsonProperty("is_private")
	public void setPrivateChat(boolean privateChat) {
		this.privateChat = privateChat;
	}

}
