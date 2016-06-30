package org.flamering.sample.chat.data;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class ChatSession {

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
