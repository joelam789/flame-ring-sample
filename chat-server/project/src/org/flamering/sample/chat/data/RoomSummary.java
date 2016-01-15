package org.flamering.sample.chat.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomSummary implements Binarylizable, Externalizable {
	
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
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeObject(this.roomName);
		out.writeObject(this.creator);
		out.writeInt(this.userCount);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
        this.roomName = (String)in.readObject();
        this.creator = (String)in.readObject();
        this.userCount = in.readInt();
	}
	
	@Override
	public void writeBinary(BinaryWriter writer) throws BinaryObjectException {
		// TODO Auto-generated method stub
		writer.writeString("roomName", this.roomName);
		writer.writeString("creator", this.creator);
		writer.writeInt("userCount", this.userCount);
	}

	@Override
	public void readBinary(BinaryReader reader) throws BinaryObjectException {
		// TODO Auto-generated method stub
		this.roomName = reader.readString("roomName");
        this.creator = reader.readString("creator");
        this.userCount = reader.readInt("userCount");
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
