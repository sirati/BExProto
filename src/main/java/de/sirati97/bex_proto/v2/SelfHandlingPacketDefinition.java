package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.IType;

/**
 * Created by sirati97 on 16.04.2016.
 */
public abstract class SelfHandlingPacketDefinition extends PacketDefinition implements PacketHandler {
    public SelfHandlingPacketDefinition(short id, IPacketCollection parent, IType... types) {
        super(id, parent, true, null, types);
    }

    public SelfHandlingPacketDefinition(short id, IType... types) {
        super(id, null, true, null, types);
    }

    public SelfHandlingPacketDefinition(short id, IPacketCollection parent, Boolean requiresReliableConnection, IType... types) {
        super(id, parent, true, requiresReliableConnection, types);
    }

    public SelfHandlingPacketDefinition(short id, Boolean requiresReliableConnection, IType... types) {
        super(id, null, true, requiresReliableConnection, types);
    }
}
