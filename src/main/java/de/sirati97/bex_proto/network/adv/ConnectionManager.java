package de.sirati97.bex_proto.network.adv;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.sirati97.bex_proto.network.NetConnection;

public class ConnectionManager {
	private Map<String, ConnectionData> connections = new HashMap<String, ConnectionData>();
	private Map<NetConnection,AdvConnection> netAdvMap = new HashMap<NetConnection, AdvConnection>();
	
	public void register(AdvConnection advConnection) {
		synchronized(this) {
			if (connections.containsKey(advConnection.getClientName())) {
				ConnectionData connectionData = connections.get(advConnection.getClientName());
				if (connections.size() == 1) {
					boolean generic = false;
					AdvConnection advConnection2 = null;
					for (AdvConnection advConnection3:connectionData.connections) {
						generic = advConnection3.isGeneric();
						advConnection2 = advConnection3;
					}
					if (!generic) {
						connectionData.connections.clear();
						connectionData.highestId = 0;
						advConnection2.closeConnection();
						netAdvMap.remove(advConnection2.getNetConnection());
					}
				}
				if (!advConnection.isGeneric()) {
					for (AdvConnection advConnection2:connectionData.connections) {
						advConnection2.closeConnection();
						netAdvMap.remove(advConnection2.getNetConnection());
					}
					connectionData.connections.clear();
					connectionData.highestId = 0;
				}
				connectionData.connections.add(advConnection);
				advConnection.setId(connectionData.highestId++);
			} else {
				ConnectionData connectionData = new ConnectionData();
				connectionData.connections.add(advConnection);
				advConnection.setId(connectionData.highestId++);
				connections.put(advConnection.getClientName(), connectionData);
			}
			netAdvMap.put(advConnection.getNetConnection(), advConnection);	
		}
	}
	

	public void unregister(NetConnection netConnection) {
		unregister(getAdvConnection(netConnection));
	}

	public void unregister(AdvConnection advConnection) {
		if (advConnection == null)return;
		synchronized(this) {
			if (connections.containsKey(advConnection.getClientName())) {
				Set<AdvConnection> connections2 = connections.get(advConnection.getClientName()).connections;
				connections2.remove(advConnection);
				if (connections2.size() < 1) {
					connections.remove(advConnection.getClientName());
				}
			}
			netAdvMap.remove(advConnection.getNetConnection());
			advConnection.closeConnection();
		}
	}
	
	public Set<AdvConnection> getConnections(String clientName) {
		if (!connections.containsKey(clientName)) return new HashSet<AdvConnection>();
		return Collections.unmodifiableSet(connections.get(clientName).connections);
	}
	
	public AdvConnection getAdvConnection(String clientName, int id) {
		if (!connections.containsKey(clientName)) return null;
		Set<AdvConnection> connections = getConnections(clientName);
		for (AdvConnection connection:connections) {
			if (connection.getId()==id)return connection;
		}
		return null;
	}
	

	public Set<AdvConnection> getConnections() {
		Set<AdvConnection> result = new HashSet<>();
		for (AdvConnection connection:netAdvMap.values()) {
			result.add(connection);
		}
		return Collections.unmodifiableSet(result);
	}
	

	public AdvConnection getAdvConnection(NetConnection connection) {
		return netAdvMap.get(connection);
	}
	
	private static class ConnectionData {
		public int highestId = 0;
		public Set<AdvConnection> connections = new HashSet<AdvConnection>();;
	}
}
