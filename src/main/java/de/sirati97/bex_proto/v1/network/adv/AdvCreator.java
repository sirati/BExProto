package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.v1.network.NetCreator;

public interface AdvCreator extends NetCreator {
	ServerSideConnection getServerSideConnection(String clientName, boolean generic, int id);
	CryptoContainer getCryptoContainer();
}
