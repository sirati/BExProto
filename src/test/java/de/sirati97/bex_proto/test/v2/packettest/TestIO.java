package de.sirati97.bex_proto.test.v2.packettest;

import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.artifcon.IOHandler;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class TestIO implements IOHandler {
    public ArtifConnection receiver;

    @Override
    public void send(byte[] stream) {
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        receiver.read(stream);
    }
}
