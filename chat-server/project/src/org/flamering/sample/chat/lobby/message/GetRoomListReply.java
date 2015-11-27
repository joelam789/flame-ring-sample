package org.flamering.sample.chat.lobby.message;

import java.util.ArrayList;
import java.util.List;

import org.flamering.sample.chat.data.RoomSummary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetRoomListReply {
	
	private String messageName = "GetRoomListReply";
	
	private String result = "Invalid Session";
	
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
	
	@JsonProperty("rooms")
	public List<RoomSummary> getRooms() {
		return rooms;
	}
	@JsonProperty("rooms")
	public void setRooms(List<RoomSummary> rooms) {
		this.rooms = rooms;
	}

}
