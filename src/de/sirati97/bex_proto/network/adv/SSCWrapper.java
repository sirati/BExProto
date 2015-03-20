package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.network.NetCreator;

public class SSCWrapper {

	private String clientName;
	private boolean generic;
	private int id;

	public SSCWrapper(ServerSideConnection ssc) {
		this(ssc.getClientName(), ssc.isGeneric(), ssc.getId());
	}
	
	public SSCWrapper(String clientName, boolean generic, int id) {
		this.clientName = clientName;
		this.generic = generic;
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public int getId() {
		return id;
	}
	

	public boolean isGeneric() {
		return generic;
	}
	
	public ServerSideConnection getSSC(NetCreator creator) {
		return ((AdvCreator) creator).getServerSideConnection(getClientName(), isGeneric(), getId());
	}
}
