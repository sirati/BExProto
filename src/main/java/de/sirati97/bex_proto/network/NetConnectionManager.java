package de.sirati97.bex_proto.network;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NetConnectionManager {
	private Set<NetConnection> connections = new HashSet<>();
	
	public void add(NetConnection connection) {
		connections.add(connection);
	}
	
	public void remove(NetConnection connection) {
		connections.remove(connection);
	}
	
	public Set<NetConnection> getConnections() {
		return Collections.unmodifiableSet(connections);
	}
}
