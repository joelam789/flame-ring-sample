package org.flamering.sample.chat.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ChatRoom implements Externalizable {
	
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

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(this.roomName);
		out.writeObject(this.creator);
		
		String[] userArray = {};
		userArray = this.users.toArray(userArray);
		String userString = StringUtils.join(userArray, '\n');
		out.writeObject(userString);
		
		String[] sessionArray = {};
		sessionArray = this.sessions.toArray(sessionArray);
		String sessionString = StringUtils.join(sessionArray, '\n');
		out.writeObject(sessionString);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		this.roomName = (String)in.readObject();
        this.creator = (String)in.readObject();
        
        String userString = (String)in.readObject();
        String[] userArray = StringUtils.split(userString, '\n');
        this.users = new ArrayList<String>();
        this.users.addAll(Arrays.asList(userArray));
        
        String sessionString = (String)in.readObject();
        String[] sessionArray = StringUtils.split(sessionString, '\n');
        this.sessions = new ArrayList<String>();
        this.sessions.addAll(Arrays.asList(sessionArray));
	}
	
	

}
