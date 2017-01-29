package de.sirati97.bex_proto.v1.network.adv;

import de.sirati97.bex_proto.datahandler.ObjType;

public class SSCWrapperType extends ObjType<SSCWrapper> {

    public SSCWrapperType() {
        super(new SSCWrapperEncoder(), new SSCWrapperDecoder());
    }

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return obj instanceof SSCWrapper;
    }

	@Override
	public Class<SSCWrapper> getType() {
		return SSCWrapper.class;
	}

	@Override
	public String getTypeName() {
		return "SSCWrapper";
	}

}
