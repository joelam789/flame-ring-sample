package org.flamering.sample.chat;

import java.util.Collection;

import org.apache.ignite.cluster.ClusterNode;

import org.flamering.app.ConsoleApp;
import org.flamering.component.Grid;
import org.flamering.service.NetworkService;

public class ChatServerApp extends ConsoleApp {
	
	public String version() {
		return getAppVersion();
	}
	
	public String edge() {
		String result = "";
		Collection<ClusterNode> nodes = Grid.getGroup(getServerGroupName("group-edge")).nodes();
		for (ClusterNode node : nodes) {
			String nodeName = node.attributes().get(Grid.NODE_NAME).toString();
			String serviceAddress = Grid.call(NetworkService.SERVICE_NAME, NetworkService.FUNC_SERVICE_ADDRESS, "", Grid.getGroupByName(nodeName));
			String clientCount = Grid.call(NetworkService.SERVICE_NAME, NetworkService.FUNC_CLIENT_COUNT, "", Grid.getGroupByName(nodeName));
			result += nodeName + ", " +  serviceAddress + ", " + clientCount + "\n";
		}
		return result;
	}
	
	public static String getServerGroupName(String key) {
		return getAppSettings().getExtra().get(key);
	}

	public static void main(String[] args) {
		
		ConsoleApp app = new ChatServerApp();
		if (app.init(args) >= 0) app.run();
		
	}

}
