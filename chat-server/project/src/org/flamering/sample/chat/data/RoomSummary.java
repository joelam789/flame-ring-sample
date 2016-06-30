package org.flamering.sample.chat.data;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomSummary {
	
	private String roomName = "";
	
	private String creator = "";
	
	private int userCount = 0;

	@JsonProperty("room_name")
	public String getRoomName() {
		return roomName;
	}

	@JsonProperty("room_name")
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@JsonProperty("creator")
	public String getCreator() {
		return creator;
	}

	@JsonProperty("creator")
	public void setCreator(String creator) {
		this.creator = creator;
	}

	@JsonProperty("user_count")
	public int getUserCount() {
		return userCount;
	}

	@JsonProperty("user_count")
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != getClass()) return false;
		
		RoomSummary summary = (RoomSummary) obj;
		return new EqualsBuilder()
					.append(roomName, summary.roomName)
					.append(creator, summary.creator)
					.append(userCount, summary.userCount)
					.isEquals();
	}

}
