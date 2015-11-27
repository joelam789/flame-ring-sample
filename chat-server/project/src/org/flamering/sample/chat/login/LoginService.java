package org.flamering.sample.chat.login;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.apache.ignite.IgniteCache;

import org.flamering.component.Grid;
import org.flamering.service.BaseService;
import org.flamering.service.NetworkService;

import org.flamering.sample.chat.ChatServerApp;
import org.flamering.sample.chat.data.ChatSession;
import org.flamering.sample.chat.login.message.LoginReply;
import org.flamering.sample.chat.login.message.LoginRequest;
import org.flamering.sample.chat.login.message.LogoutRequest;
import org.flamering.sample.chat.room.message.ExitRoomRequest;

public class LoginService extends BaseService {
	
	protected boolean validateUser(String userName, String password) {
		return userName != null && userName.length() > 0; // skip checking password since it is just a demo
	}

	public LoginReply login(LoginRequest request, String requesterInfo) {
		
		System.out.println("Got login request from " + requesterInfo);
		
		LoginReply reply = new LoginReply();
		
		String userName = request.getUserName();
		
		if (userName == null || userName.length() <= 0) {
			reply.setResult("User name or password is not correct.");
			return reply;
		}
		
		String userToken = request.getUserToken();
		String password = request.getPassword();
		
		boolean isTokenOK = false;    // if re-login, need to check the token
		boolean isPasswordOK = false; // if new login, need to check the password
		
		boolean relogin = userToken != null && userToken.length() > 0;
							
		if (!relogin) isPasswordOK = validateUser(userName, password);
		
		if (!relogin && !isPasswordOK) {
			reply.setResult("User name or password is not correct.");
			return reply;
		}
		
		String serverName = "";
		String sessionName = "";
		
		IgniteCache<String, ChatSession> cache = Grid.getCache("chat-session");
		
		ChatSession existingSession = cache.get(userName);
		
		if (relogin) {
			isTokenOK = existingSession != null && userToken.equals(existingSession.getUserToken());
		}
		
		if (relogin && !isTokenOK) {
			reply.setResult("Failed to re-login with an invalid token.");
			return reply;
		}
		
		if (isTokenOK || isPasswordOK) {
			
			if (!relogin && existingSession != null) {
				
				serverName = existingSession.getServerName();
				sessionName = existingSession.getSessionName();
				
				// try to logout the old one ...
				if (existingSession.getUserName() != null && existingSession.getUserName().length() > 0
						&& existingSession.getUserToken() != null && existingSession.getUserToken().length() > 0) {
					LogoutRequest logoutRequest = new LogoutRequest();
					logoutRequest.setUserName(existingSession.getUserName());
					logoutRequest.setUserToken(existingSession.getUserToken());
					logout(logoutRequest);
				}
				
			}
			
		} else {
			
			reply.setResult("User name or password is not correct.");
			return reply;
			
		}
		
		boolean locked = false;
		Lock lock = cache.lock(userName); // need a lock because we would update/remove the session record
		
		try {
			
			if (lock != null) locked = lock.tryLock(10, TimeUnit.SECONDS);
			
			if (locked) {
				
				ChatSession session = relogin ? cache.get(userName) : new ChatSession();
				if (session == null) {
					relogin = false; // cannot execute re-login since the session lost
					session = new ChatSession();
				}
				
				session.setUserName(userName);
				
				if (!relogin) {
					session.setServerName("");
					session.setSessionName("");
					session.setRoomName("");
				} else {
					System.out.println(new Date().toString() + " - Got re-login request from: " + userName);
				}
				
				// always refresh the user token so that user cannot re-login again and again with the same token
				session.setUserToken(UUID.randomUUID().toString());
				
				cache.put(userName, session);
				
				System.out.println(new Date().toString() + " - Session cache refreshed: " + userName);
				
				reply.setResult("ok");
				
				reply.setServerUri(Grid.call(NetworkService.SERVICE_NAME, NetworkService.FUNC_SERVICE_ADDRESS, "", 
						Grid.getGroup(ChatServerApp.getServerGroupName("group-edge")).forRandom()));
				
				reply.setUserToken(session.getUserToken());
				
			} else {
				
				reply.setResult("System is busy now, please try again later");
				
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(lock != null && locked) lock.unlock();
		}
		
		// try to kick the old one if necessary
		// (this could be a reasonable logic as long as the user name could be protected by password)
		if (serverName != null && serverName.length() > 0
				&& sessionName != null && sessionName.length() > 0) {
			Grid.run(NetworkService.SERVICE_NAME, NetworkService.FUNC_DISCONNECT, sessionName, 
					Grid.getGroupByName(serverName));
		}
		
		return reply;
	}
	
	public String logout(LogoutRequest request) {
		
		String userName = request.getUserName();
		String userToken = request.getUserToken();
		
		if (userName == null || userName.length() <= 0
				|| userToken == null || userToken.length() <= 0) {
			return "";
		}
		
		IgniteCache<String, ChatSession> cache = Grid.getCache("chat-session");
		ChatSession session = cache.get(userName);
		
		if (session != null && session.getUserToken().equals(userToken)) {
			
			// need to leave the room if the user has joined one
			if (session.getRoomName() != null && session.getRoomName().length() > 0) {
				
				// going to call room::exit ...
				
				ExitRoomRequest requestToExit = new ExitRoomRequest();
				requestToExit.setUserName(userName);
				requestToExit.setUserToken(userToken);
				requestToExit.setRoomName(session.getRoomName());
				
				Grid.call("room-service", "exit", requestToExit, 
						Grid.getGroup(ChatServerApp.getServerGroupName("group-room")));
				
			}
			
			// then try to remove the cache of the session
			
			boolean locked = false;
			Lock lock = cache.lock(userName); // need a lock because we would update/remove the session record
			
			try {
				
				if (lock != null) locked = lock.tryLock(10, TimeUnit.SECONDS);
				
				if (locked) {
				
					ChatSession userSession = cache.get(userName); // reload cache after lock it to make sure what we get is the latest one
					
					if (userSession != null && userSession.getUserToken().equals(userToken)) {
						cache.remove(userName);
						System.out.println(new Date().toString() + " - Session cache removed: " + userName);
					}
				
				} else {
					
					// if failed to lock it we just give up removing the cache... 
					// this should not be a big issue because the cache would be refreshed once the same user login again
					System.out.println(new Date().toString() + " - System is too busy to remove the cache of the broken session: " + userName);
					
				}
				
			} catch(Exception ex) {
				ex.printStackTrace();
			} finally {
				if(lock != null && locked) lock.unlock();
			}
			
		}
		
		return "ok";
	}
	
}
