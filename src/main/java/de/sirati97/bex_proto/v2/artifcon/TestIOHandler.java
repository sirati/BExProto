package de.sirati97.bex_proto.v2.artifcon;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class TestIOHandler implements IOHandler {
    public ArtifConnection receiver;

    @Override
    public void send(byte[] stream) {
        System.out.println(bytesToString(stream));
        receiver.read(stream);
    }



    public static String bytesToString(byte[] stream) {
        StringBuilder sb = new StringBuilder();
        for (byte b:stream) {
            String num = String.valueOf((int)b & 0x000000FF);
            sb.append(StringUtils.leftPad(num, 4 , ' '));
        }
        return sb.toString();
    }


}
