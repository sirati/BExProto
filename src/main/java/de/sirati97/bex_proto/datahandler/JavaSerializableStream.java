package de.sirati97.bex_proto.datahandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class JavaSerializableStream<T extends Serializable> implements Stream {
    private final T serializable;

    public JavaSerializableStream(T serializable) {
        this.serializable = serializable;
    }

    @Override
    public byte[] getBytes() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream serializer = null;
        try {
            serializer = new ObjectOutputStream(out);
            serializer.writeObject(serializable);
            return BExStatic.setByteArray(out.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {}
            if (serializer != null) {
                try {
                    serializer.close();
                } catch (IOException e) {}
            }
        }
    }
}
