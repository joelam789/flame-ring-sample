package org.flamering.sample.chat.data;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
	
	private String roomName = "";
	
	private String creator = "";
	
	private List<String> users = new ArrayList<>();
	private List<String> sessions = new ArrayList<>();

	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	public List<String> getSessions() {
		return sessions;
	}
	public void setSessions(List<String> sessions) {
		this.sessions = sessions;
	}

}
