package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.v1.network.NetCreator;

public class SSCWrapper {

	private String clientName;
	private boolean generic;
	private int id;

	public SSCWrapper(IServerSideConnection ssc) {
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
	
	public IServerSideConnection getSSC(NetCreator creator) {
		return ((AdvCreator) creator).getServerSideConnection(getClientName(), isGeneric(), getId());
	}
	
//	public Encoder getStream() {
//		return Type.SSCWrapper.getEncoder(this);
//	}
	
	@Override
	public String toString() {
		return super.toString() + " Clientname=" + clientName + ",generic=" + generic + ",id=" + id;
	}
}
