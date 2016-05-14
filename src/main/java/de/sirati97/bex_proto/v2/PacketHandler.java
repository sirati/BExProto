package de.sirati97.bex_proto.v2;

/**
 * Created by sirati97 on 15.03.2016.
 */
public interface PacketHandler {
    void execute(ReceivedPacket packet);
}
