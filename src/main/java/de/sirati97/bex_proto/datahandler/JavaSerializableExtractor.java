package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class JavaSerializableExtractor<T extends Serializable> implements StreamExtractor<T> {

    @Override
    public T extract(CursorByteBuffer dat) {
        int length = (Integer) Type.Integer.getExtractor().extract(dat);
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
