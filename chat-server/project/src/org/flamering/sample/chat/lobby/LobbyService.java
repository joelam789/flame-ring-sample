package org.flamering.sample.chat.lobby;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.transactions.Transaction;

import org.flamering.component.Grid;
import org.flamering.service.BaseService;
import org.flamering.service.ServiceRequester;

import org.flamering.sample.chat.data.ChatRoom;
import org.flamering.sample.chat.data.ChatSession;
import org.flamering.sample.chat.data.RoomSummary;
import org.flamering.sample.chat.lobby.message.EnterLobbyReply;
import org.flamering.sample.chat.lobby.message.EnterLobbyRequest;
import org.flamering.sample.chat.lobby.message.GetRoomListReply;
import org.flamering.sample.chat.lobby.message.GetRoomListRequest;

public class LobbyService extends BaseService {
	
	public EnterLobbyReply enter(EnterLobbyRequest request, ServiceRequester requester) {
		
		System.out.println("Got enter-lobby request from " + requester.getRemoteAddress() + " (" + requester.getProtocol() + ")");
		
		EnterLobbyReply reply = new EnterLobbyReply();
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();
		
		if (userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return reply;
		}
		
		IgniteCache<String, ChatSession> sessionCache = Grid.getCache("chat-session");
		ChatSession session = sessionCache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {	
			
			try {
			
				IgniteTransactions transactions = Grid.getGrid().transactions();
				try (Transaction tx = transactions.txStart()) {
					
					ChatSession userSession = sessionCache.get(userName); // reload cache after lock it to make sure what we get is the latest one
					
					if (userSession != null && userSession.getUserToken().equals(userToken)) {
						
						String sessionName = userSession.getSessionName();
						String roomName = userSession.getRoomName();
						
						userSession.setRemoteAddress(requester.getRemoteAddress());
						userSession.setServerName(requester.getServerName());
						userSession.setSessionName(requester.getSessionName());
						sessionCache.put(userName, userSession);
						
						if (sessionName != null && sessionName.length() > 0
								&& roomName != null && roomName.length() > 0) {
							
							// should need to update chat room info too
							
							IgniteCache<String, ChatRoom> roomCache = Grid.getCache("chat-room");
							ChatRoom chatRoom = roomCache.get(roomName);
							if (chatRoom != null 
									&& chatRoom.getUsers() != null && chatRoom.getUsers().contains(userName)
									&& chatRoom.getSessions() != null) {
								chatRoom.getSessions().remove(sessionName); // try to remove the old one 
								chatRoom.getSessions().add(requester.getSessionName()); // and then add the new one
								roomCache.put(roomName, chatRoom); // update room info
							}
						}
						
						tx.commit();
						
						reply.setResult("ok");
					}
					
				}
			
			} catch(Exception ex) {
				ex.printStackTrace();
				reply.setResult("Failed to enter the lobby");
			}
			
		}
		
		return reply;
	}
	
	public GetRoomListReply getRoomList(GetRoomListRequest request) {
		
		GetRoomListReply reply = new GetRoomListReply();
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();
		
		if (userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return reply;
		}
		
		IgniteCache<String, ChatSession> sessionCache = Grid.getCache("chat-session");
		ChatSession session = sessionCache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {
			
			IgniteCache<String, RoomSummary> cache = Grid.getCache("room-summary");
			
			try (QueryCursor<Entry<String, RoomSummary>> cursor = cache.query(
					new ScanQuery<String, RoomSummary>())) { // get all entries...
				
				for (Entry<String, RoomSummary> e : cursor) {
									
					RoomSummary summary = e.getValue();
					reply.getRooms().add(summary);
				}
				
			}
			
			reply.setResult("ok");
			
		}
		
		return reply;
	}

}
