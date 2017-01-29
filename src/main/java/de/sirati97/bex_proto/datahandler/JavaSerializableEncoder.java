package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class JavaSerializableEncoder<T extends Serializable>  extends EncoderBase<T> {

    @Override
    public void encode(T serializable, ByteBuffer buffer) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream serializer = null;
        try {
            serializer = new ObjectOutputStream(out);
            serializer.writeObject(serializable);
            BExStatic.setByteArray(out.toByteArray(), buffer);
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
