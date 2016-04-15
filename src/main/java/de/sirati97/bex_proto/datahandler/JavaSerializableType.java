package de.sirati97.bex_proto.datahandler;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class JavaSerializableType<T extends Serializable> extends ObjType {
    private final JavaSerializableExtractor<T> extractor = new JavaSerializableExtractor<>();
    private final Class<T> clazz;
    private final String name;
    private final boolean register;

    protected JavaSerializableType(Class<T> clazz, String name, boolean register) {
        this.clazz = clazz;
        this.name = name;
        this.register = register;
        register();
    }


    public JavaSerializableType(Class<T> clazz) {
        this(clazz, "JavaSerializable", false);
    }


    @Override
    public JavaSerializableStream<T> createStream(Object obj) {
        return new JavaSerializableStream<>((T) obj);
    }

    @Override
    public StreamExtractor<T> getExtractor() {
        return extractor;
    }

    @Override
    public T[] createArray(int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    @Override
    protected boolean earlyRegister() {
        return false;
    }

    @Override
    protected void register() {
        if(register) {
            super.register();
        }
    }
}
