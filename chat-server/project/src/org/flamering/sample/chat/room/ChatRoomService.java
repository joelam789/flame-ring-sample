package org.flamering.sample.chat.room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.transactions.Transaction;

import org.flamering.component.Grid;
import org.flamering.component.Json;
import org.flamering.service.BaseService;
import org.flamering.service.NetworkMessage;
import org.flamering.service.NetworkService;

import org.flamering.sample.chat.ChatServerApp;
import org.flamering.sample.chat.data.ChatRoom;
import org.flamering.sample.chat.data.ChatSession;
import org.flamering.sample.chat.data.RoomSummary;
import org.flamering.sample.chat.room.message.CreateRoomReply;
import org.flamering.sample.chat.room.message.CreateRoomRequest;
import org.flamering.sample.chat.room.message.EnterRoomReply;
import org.flamering.sample.chat.room.message.EnterRoomRequest;
import org.flamering.sample.chat.room.message.ExitRoomReply;
import org.flamering.sample.chat.room.message.ExitRoomRequest;
import org.flamering.sample.chat.room.message.SendMessageReply;
import org.flamering.sample.chat.room.message.SendMessageRequest;

public class ChatRoomService extends BaseService {
	
	public CreateRoomReply create(CreateRoomRequest request) {
		
		System.out.println("Got create-room request");
		
		CreateRoomReply reply = new CreateRoomReply();
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();

		if (userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return reply;
		}
		
		String roomName = request.getRoomName();
		if (roomName == null || roomName.length() <= 0) {
			reply.setResult("Invalid room name");
			return reply;
		}
		
		IgniteCache<String, ChatSession> sessionCache = Grid.getCache("chat-session");
		ChatSession session = sessionCache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {
			
			try {
				
				String sessionName = session.getSessionName();
				if (sessionName == null || sessionName.length() <= 0) {
					reply.setResult("Please enter the lobby first so that you can get a valid session name.");
					return reply;
				}
				
				if (session.getRoomName() != null && session.getRoomName().length() > 0) {
					reply.setResult("Users are not allowed to create a new chat room if they are still in some existing chat rooms");
					return reply;
				}
				
				IgniteCache<String, RoomSummary> summaryCache = Grid.getCache("room-summary");
				RoomSummary summary = summaryCache.get(roomName);
				if (summary != null) {
					reply.setResult("Room name [" + roomName + "] has been occupied.");
					return reply;
				}
				
				IgniteCache<String, ChatRoom> roomCache = Grid.getCache("chat-room");
				
				IgniteTransactions transactions = Grid.getGrid().transactions();
				try (Transaction tx = transactions.txStart()) {
					
					RoomSummary newSummary = new RoomSummary();
					newSummary.setCreator(userName);
					newSummary.setRoomName(roomName);
					newSummary.setUserCount(1); // the room creator
					
					if (!summaryCache.putIfAbsent(roomName, newSummary)) {
						reply.setResult("Room name [" + roomName + "] has been occupied.");
						return reply;
					}
					
					ChatRoom newRoom = new ChatRoom();
					newRoom.setCreator(userName);
					newRoom.setRoomName(roomName);
					newRoom.getUsers().add(userName); // add the user name of the room creator
					newRoom.getSessions().add(sessionName); // add the session name of the room creator
					
					if (!roomCache.putIfAbsent(roomName, newRoom)) {
						reply.setResult("Room name [" + roomName + "] has been occupied.");
						return reply;
					}
					
					ChatSession userSession = sessionCache.get(userName); // reload it to make sure what we get is the latest one
					if (userSession != null) {
						userSession.setRoomName(roomName);
						session.setRoomName(sessionCache.get(userName).getRoomName()); // need to reload? should be a bug ...
					}
					if (userSession == null || !sessionCache.replace(userName, session, userSession)) {
						reply.setResult("User session data is not in sync.");
						return reply;
					}
					
					tx.commit();
					
				}
				
				
				try (QueryCursor<Entry<String, RoomSummary>> cursor = summaryCache.query(
						new ScanQuery<String, RoomSummary>())) { // get all entries...
					
					for (Entry<String, RoomSummary> e : cursor) {
										
						RoomSummary roomInfo = e.getValue();
						reply.getRooms().add(roomInfo);
					}
					
				}
				
				reply.setRoomName(roomName);
				reply.getUsers().add(userName);
				reply.setResult("ok");
				
			} catch(Exception ex) {
				ex.printStackTrace();
				reply.setResult("Failed to create a new room");
			}
			
		}
		
		return reply;
	}
	
	public EnterRoomReply enter(EnterRoomRequest request) {
		
		System.out.println("Got enter-room request");
		
		EnterRoomReply reply = new EnterRoomReply();
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();

		if (userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return reply;
		}
		
		String roomName = request.getRoomName();
		if (roomName == null || roomName.length() <= 0) {
			reply.setResult("Invalid room name");
			return reply;
		}
		
		IgniteCache<String, ChatSession> sessionCache = Grid.getCache("chat-session");
		ChatSession session = sessionCache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {
			
			try {
				
				String sessionName = session.getSessionName();
				if (sessionName == null || sessionName.length() <= 0) {
					reply.setResult("Please enter the lobby first so that you can get a valid session name.");
					return reply;
				}
			
				if (session.getRoomName() != null && session.getRoomName().length() > 0) {
					reply.setResult("Users are allowed to enter a chat room just only when they are in the lobby");
					return reply;
				}
				
				IgniteCache<String, RoomSummary> summaryCache = Grid.getCache("room-summary");
				RoomSummary summary = summaryCache.get(roomName);
				if (summary == null) {
					reply.setResult("Room with the name [" + roomName + "] does not exist.");
					return reply;
				}
				
				IgniteCache<String, ChatRoom> roomCache = Grid.getCache("chat-room");
				ChatRoom chatRoom = null;
				
				List<String> userList = new ArrayList<>();
				
				IgniteTransactions transactions = Grid.getGrid().transactions();
				try (Transaction tx = transactions.txStart()) {
					
					RoomSummary roomSummary = summaryCache.get(roomName);
					if (roomSummary == null) {
						reply.setResult("Room data is not in sync.");
						return reply;
					}
					
					roomSummary.setUserCount(roomSummary.getUserCount() + 1);
					summaryCache.put(roomName, roomSummary);
					
					
					chatRoom = roomCache.get(roomName);
					if (chatRoom == null) {
						reply.setResult("Room data is not in sync.");
						return reply;
					}
					
					if (chatRoom.getUsers() == null) chatRoom.setUsers(new ArrayList<String>());
					if (chatRoom.getUsers().contains(userName)) {
						reply.setResult("The user has already been in the room [" + roomName + "].");
						return reply;
					}
					chatRoom.getUsers().add(userName);
					
					if (chatRoom.getSessions() == null) chatRoom.setSessions(new ArrayList<String>());
					chatRoom.getSessions().add(sessionName);
					
					roomCache.put(roomName, chatRoom);
					
					userList.addAll(chatRoom.getUsers());
					
					
					ChatSession userSession = sessionCache.get(userName); // reload it to make sure what we get is the latest one
					if (userSession != null) {
						userSession.setRoomName(roomName);
						session.setRoomName(sessionCache.get(userName).getRoomName()); // need to reload? should be a bug ...
					}
					if (userSession == null || !sessionCache.replace(userName, session, userSession)) {
						reply.setResult("User session data is not in sync.");
						return reply;
					}
					
					
					tx.commit();
					
				}
				
				
				reply.setRoomName(roomName);
				reply.setUsers(userList);
				
				reply.setResult("ok");
				
				if (chatRoom != null) broadcast(chatRoom, userName, userName + " entered.");
				
			} catch(Exception ex) {
				ex.printStackTrace();
				reply.setResult("Failed to enter a room");
			}
			
		}
		
		return reply;
		
	}
	
	public ExitRoomReply exit(ExitRoomRequest request) {
		
		ExitRoomReply reply = new ExitRoomReply();
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();

		if (userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return reply;
		}
		
		String roomName = request.getRoomName();
		if (roomName == null || roomName.length() <= 0) {
			reply.setResult("Invalid room name");
			return reply;
		}
		
		IgniteCache<String, ChatSession> sessionCache = Grid.getCache("chat-session");
		ChatSession session = sessionCache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {
			
			try {
				
				String sessionName = session.getSessionName();
				if (sessionName == null || sessionName.length() <= 0) {
					reply.setResult("Please enter the lobby first so that you can get a valid session name.");
					return reply;
				}
			
				if (session.getRoomName() == null || !session.getRoomName().equals(roomName)) {
					reply.setResult("The user is not in the room [" + roomName + "].");
					return reply;
				}
				
				IgniteCache<String, RoomSummary> summaryCache = Grid.getCache("room-summary");
				RoomSummary summary = summaryCache.get(roomName);
				if (summary == null) {
					reply.setResult("Room with the name [" + roomName + "] does not exist.");
					return reply;
				}
				
				IgniteCache<String, ChatRoom> roomCache = Grid.getCache("chat-room");
				ChatRoom chatRoom = null;
				
				IgniteTransactions transactions = Grid.getGrid().transactions();
				try (Transaction tx = transactions.txStart()) {
					
					RoomSummary roomSummary = summaryCache.get(roomName);
					if (roomSummary == null) {
						reply.setResult("Room data is not in sync.");
						return reply;
					}
					
					roomSummary.setUserCount(roomSummary.getUserCount() - 1);
					summaryCache.put(roomName, roomSummary);
					
					if (roomSummary.getUserCount() > 0) {
						chatRoom = roomCache.get(roomName);
						if (chatRoom == null) {
							reply.setResult("Room data is not in sync.");
							return reply;
						}
						if (chatRoom.getUsers() != null) chatRoom.getUsers().remove(userName);
						if (chatRoom.getSessions() != null) chatRoom.getSessions().remove(sessionName);
						roomCache.put(roomName, chatRoom);
					} else {
						if (!summaryCache.remove(roomName) || !roomCache.remove(roomName)) {
							reply.setResult("Room data is not in sync.");
							return reply;
						}
						System.out.println(new Date().toString() + " - Empty room removed: " + roomName);
					}
					
					
					ChatSession userSession = sessionCache.get(userName); // reload it to make sure what we get is the latest one
					if (userSession != null) {
						userSession.setRoomName("");
						session.setRoomName(sessionCache.get(userName).getRoomName()); // need to reload? should be a bug ...
					}
					if (userSession == null || !sessionCache.replace(userName, session, userSession)) {
						reply.setResult("User session data is not in sync.");
						return reply;
					}
					
					tx.commit();
					
				}
				
				
				try (QueryCursor<Entry<String, RoomSummary>> cursor = summaryCache.query(
						new ScanQuery<String, RoomSummary>())) { // get all entries...
					
					for (Entry<String, RoomSummary> e : cursor) {
										
						RoomSummary roomInfo = e.getValue();
						reply.getRooms().add(roomInfo);
					}
					
				}
				
				reply.setResult("ok");
				
				if (chatRoom != null) broadcast(chatRoom, userName, userName + " left.");
				
			} catch(Exception ex) {
				ex.printStackTrace();
				reply.setResult("Failed to exit room");
			}
			
		}
		
		return reply;

	}
	
	public String chat(SendMessageRequest request) {
		
		System.out.println("Got chat request");
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();
		
		String words = request.getWords();

		if (words == null || words.length() <= 0
				|| userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return "";
		}
		
		String roomName = request.getRoomName();
		if (roomName == null || roomName.length() <= 0) {
			return "";
		}
		
		IgniteCache<String, ChatSession> sessionCache = Grid.getCache("chat-session");
		ChatSession session = sessionCache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {
			
			if (session.getRoomName() == null || !session.getRoomName().equals(roomName)) {
				return "";
			}
			
			IgniteCache<String, ChatRoom> roomCache = Grid.getCache("chat-room");
			ChatRoom room = roomCache.get(roomName);
			if (room == null) {
				return "";
			}
			
			List<String> userList = room.getUsers();
			if (userList == null || !userList.contains(userName)) {
				return "";
			}
			
			String target = request.getTarget() == null ? "" : request.getTarget();
			
			SendMessageReply reply = new SendMessageReply();
			reply.setFromSystem(false);
			reply.setPrivateChat(request.isPrivateChat());
			reply.setRoomName(roomName);
			reply.setTarget(target);
			reply.setUserName(userName);
			reply.setWords(words);
			
			if (request.isPrivateChat()) {
				
				String userServerName = session.getServerName();
				String userSessionName = session.getSessionName();
				
				if (userServerName != null && userServerName.length() > 0
						&& userSessionName != null && userSessionName.length() > 0) {
					
					NetworkMessage msg = new NetworkMessage();
					msg.setMessage(Json.toJsonString(reply));
					String[] arraySessionNames = new String[]{userSessionName};
					msg.setEndpoints(Arrays.asList(arraySessionNames));
					
					Grid.run(NetworkService.BEAN_NAME, NetworkService.FUNC_SEND, msg, 
							Grid.getGroupByName(userServerName));
				}
				
				ChatSession targetSession = target.length() > 0 ? sessionCache.get(target) : null;
				if (targetSession != null) {
					
					String targetServerName = targetSession.getServerName();
					String targetSessionName = targetSession.getSessionName();
					
					if (targetServerName != null && targetServerName.length() > 0
							&& targetSessionName != null && targetSessionName.length() > 0) {
						
						NetworkMessage msg = new NetworkMessage();
						msg.setMessage(Json.toJsonString(reply));
						String[] arraySessionNames = new String[]{targetSessionName};
						msg.setEndpoints(Arrays.asList(arraySessionNames));
						
						Grid.run(NetworkService.BEAN_NAME, NetworkService.FUNC_SEND, msg, 
								Grid.getGroupByName(targetServerName));
					}
					
				}
				
			} else {
				
				List<String> sessionNames = room.getSessions();
				if (sessionNames != null && sessionNames.size() > 0) {
					
					NetworkMessage msg = new NetworkMessage();
					msg.setMessage(Json.toJsonString(reply));
					msg.setEndpoints(sessionNames);
					
					Grid.broadcast(NetworkService.BEAN_NAME, NetworkService.FUNC_SEND, msg, 
							Grid.getGroup(ChatServerApp.getServerGroupName("group-edge")));
					
				}
			}
			
		}
		
		return "";
	}
	
	protected String broadcast(ChatRoom room, String userName, String message) {
		
		if (room == null || message == null || message.length() <= 0) {
			return "";
		}
		
		SendMessageReply reply = new SendMessageReply();
		reply.setFromSystem(true);
		reply.setPrivateChat(false);
		reply.setRoomName(room.getRoomName());
		reply.setTarget("");
		reply.setUserName(userName);
		reply.setWords(message);
		
		List<String> sessionNames = room.getSessions();
		if (sessionNames != null && sessionNames.size() > 0) {
			
			NetworkMessage msg = new NetworkMessage();
			msg.setMessage(Json.toJsonString(reply));
			msg.setEndpoints(sessionNames);
			
			Grid.broadcast(NetworkService.BEAN_NAME, NetworkService.FUNC_SEND, msg, 
					Grid.getGroup(ChatServerApp.getServerGroupName("group-edge")));
			
		}
		
		return "";
		
	}

}
