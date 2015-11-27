package org.flamering.sample.chat.room.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnterRoomRequest {
	
	private String messageName = "EnterRoomRequest";
	
	private String userName = "";
	
	private String userToken = "";
	
	private String roomName = "";
	
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

}
