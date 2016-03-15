package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.Type;

/**
 * Created by sirati97 on 15.03.2016.
 */
public class Test {

    void fuu () {
        Packet p = new Packet(null);
        int i = p.get(0);
        long l = p.get(1);
        Type t = p.get(2);
    }

}
