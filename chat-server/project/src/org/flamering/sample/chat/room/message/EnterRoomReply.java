package org.flamering.sample.chat.room.message;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnterRoomReply {
	
	private String messageName = "EnterRoomReply";
	
	private String result = "Invalid Session";
	
	private String roomName = "";
	
	private List<String> users = new ArrayList<>();
	
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
	
	@JsonProperty("room_name")
	public String getRoomName() {
		return roomName;
	}
	@JsonProperty("room_name")
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	@JsonProperty("users")
	public List<String> getUsers() {
		return users;
	}
	@JsonProperty("users")
	public void setUsers(List<String> users) {
		this.users = users;
	}

}
