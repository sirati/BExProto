package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class JavaSerializableDecoder<T extends Serializable> extends DecoderBase<T> {

    @Override
    public T decode(CursorByteBuffer dat, boolean header) {
        int length = Type.Integer.getDecoder().decode(dat);
        byte[] stream = dat.getMulti(length);
        ObjectInputStream deserializer = null;
        try {
            deserializer = new ObjectInputStream(new ByteArrayInputStream(stream));
            return (T) deserializer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } finally {
            if (deserializer != null) {
                try {
                    deserializer.close();
                } catch (IOException e) {}
            }
        }
    }
}
