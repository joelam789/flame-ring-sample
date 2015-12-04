package org.flamering.sample.chat.common;

import java.util.Date;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;

import org.flamering.component.Grid;
import org.flamering.service.NetworkEventService;
import org.flamering.sample.chat.ChatServerApp;
import org.flamering.sample.chat.data.ChatSession;
import org.flamering.sample.chat.login.message.LogoutRequest;

public class NetworkEventHandler extends NetworkEventService {
	
	@Override
	public String onOpen(String sessionName, String remoteAddress) {
		System.out.println(new Date().toString() + " - New client connected: " + sessionName + "," + remoteAddress);
		return "";
	}
	
	@Override
	public String onClose(String sessionName, String remoteAddress) {
		
		System.out.println(new Date().toString() + " - Client disconnected: " + sessionName + "," + remoteAddress);
		
		String userName = "";
		String userToken = "";
		
		try {
			
			IgniteCache<String, ChatSession> cache = Grid.getCache("chat-session");
		
			//try (QueryCursor<Entry<String, ChatSession>> cursor = cache.query(
			//		new ScanQuery<String, ChatSession>((k, p) -> p.getSessionName().equals(sessionName)))) {
			
			SqlQuery<String, ChatSession> sql = new SqlQuery<>(ChatSession.class, "sessionName = ?");
			try (QueryCursor<Entry<String, ChatSession>> cursor = cache.query(sql.setArgs(sessionName))) {
				  
				for (Entry<String, ChatSession> e : cursor) {
					
					ChatSession session = e.getValue();
					if (session != null) {
						userName = session.getUserName();
						userToken = session.getUserToken();
					}	  
					break; // normally should get only one matched
				}
			}
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if (userName != null && userName.length() > 0
				&& userToken != null && userToken.length() > 0) {
			
			LogoutRequest request = new LogoutRequest();
			request.setUserName(userName);
			request.setUserToken(userToken);
			
			Grid.run("login-service", "logout", request, 
					Grid.getGroup(ChatServerApp.getServerGroupName("group-login")));
		}
		
		return "";
	}

}
