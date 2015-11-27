package org.flamering.sample.chat.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class ChatSession implements Externalizable {

	private String userName = "";
	
	private String userRole = "";
	
	private String userToken = "";
	
	private String roomName = "";
	
	private String serverName = "";
	
	private String sessionName = "";
	
	private String remoteAddress = "";
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(this.userName);
		out.writeObject(this.userRole);
		out.writeObject(this.userToken);
		out.writeObject(this.roomName);
		out.writeObject(this.serverName);
		out.writeObject(this.sessionName);
		out.writeObject(this.remoteAddress);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		this.userName = (String)in.readObject();
        this.userRole = (String)in.readObject();
        this.userToken = (String)in.readObject();
        this.roomName = (String)in.readObject();
        this.serverName = (String)in.readObject();
        this.sessionName = (String)in.readObject();
        this.remoteAddress = (String)in.readObject();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != getClass()) return false;
		
		ChatSession session = (ChatSession) obj;
		return new EqualsBuilder()
					.append(userName, session.userName)
					.append(userRole, session.userRole)
					.append(userToken, session.userToken)
					.append(roomName, session.roomName)
					.append(serverName, session.serverName)
					.append(sessionName, session.sessionName)
					.append(remoteAddress, session.remoteAddress)
					.isEquals();
	}

	
}
