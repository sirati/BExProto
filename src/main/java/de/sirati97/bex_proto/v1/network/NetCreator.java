package de.sirati97.bex_proto.v1.network;

public interface NetCreator {
	void onSocketClosed(NetConnection connection);
	void sendPing(NetConnection connection);
}
