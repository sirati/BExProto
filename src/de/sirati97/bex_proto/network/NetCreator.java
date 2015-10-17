package de.sirati97.bex_proto.network;

public interface NetCreator {
	void onSocketClosed(NetConnection connection);
	void sendPing(NetConnection connection);
}
