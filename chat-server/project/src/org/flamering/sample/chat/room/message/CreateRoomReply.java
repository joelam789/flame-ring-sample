package org.flamering.sample.chat.room.message;

import java.util.ArrayList;
import java.util.List;

import org.flamering.sample.chat.data.RoomSummary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRoomReply {
	
	private String messageName = "CreateRoomReply";
	
	private String result = "Invalid Session";
	
	private String roomName = "";
	
	private List<String> users = new ArrayList<>();
	private List<RoomSummary> rooms = new ArrayList<>();
	
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
	
	@JsonProperty("rooms")
	public List<RoomSummary> getRooms() {
		return rooms;
	}
	@JsonProperty("rooms")
	public void setRooms(List<RoomSummary> rooms) {
		this.rooms = rooms;
	}

}
