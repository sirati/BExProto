package de.sirati97.bex_proto.network.adv;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.sirati97.bex_proto.network.NetConnection;

public class ConnectionManager {
	private Map<String, Set<AdvConnection>> connections = new HashMap<String, Set<AdvConnection>>();
	private Map<NetConnection,AdvConnection> netAdvMap = new HashMap<NetConnection, AdvConnection>();
	
	public void register(AdvConnection advConnection) {
		if (connections.containsKey(advConnection.getClientName())) {
			Set<AdvConnection> connections2 = connections.get(advConnection.getClientName());
			if (connections.size() == 1) {
				boolean generic = false;
				AdvConnection advConnection2 = null;
				for (AdvConnection advConnection3:connections2) {
					generic = advConnection3.isGeneric();
					advConnection2 = advConnection3;
				}
				if (!generic) {
					connections2.clear();
					advConnection2.closeConnection();
					netAdvMap.remove(advConnection2.getNetConnection());
				}
			}
			if (!advConnection.isGeneric()) {
				for (AdvConnection advConnection2:connections2) {
					advConnection2.closeConnection();
					netAdvMap.remove(advConnection2.getNetConnection());
				}
				connections2.clear();
			}
			connections2.add(advConnection);
		} else {
			Set<AdvConnection> connections2 = new HashSet<AdvConnection>();
			connections2.add(advConnection);
			connections.put(advConnection.getClientName(), connections2);
		}
		netAdvMap.put(advConnection.getNetConnection(), advConnection);
	}
	

	public void unregister(AdvConnection advConnection) {
		if (connections.containsKey(advConnection.getClientName())) {
			Set<AdvConnection> connections2 = connections.get(advConnection.getClientName());
			connections2.remove(advConnection);
			if (connections2.size() < 1) {
				connections.remove(advConnection.getClientName());
			}
		}
		netAdvMap.remove(advConnection.getNetConnection());
		advConnection.closeConnection();
	}
	
	public Set<AdvConnection> getConnections(String clientName) {
		if (!connections.containsKey(clientName)) return new HashSet<AdvConnection>();
		return Collections.unmodifiableSet(connections.get(clientName));
	}
	

	public AdvConnection getAdvConnection(NetConnection connection) {
		return netAdvMap.get(connection);
	}
}
