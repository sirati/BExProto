package de.sirati97.bex_proto.datahandler;

import java.io.Serializable;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class JavaSerializableType<T extends Serializable> extends ObjType<T> {
    private final Class<T> clazz;
    private final String name;
    private final boolean register;

    protected JavaSerializableType(Class<T> clazz, String name, boolean register) {
        super(new JavaSerializableEncoder<T>(), new JavaSerializableDecoder<T>());
        this.clazz = clazz;
        this.name = name;
        this.register = register;
        register();
    }

    public JavaSerializableType(Class<T> clazz) {
        this(clazz, "JavaSerializable", false);
    }

    @Override
    public boolean isEncodable(Object obj, boolean platformIndependent) {
        return !platformIndependent && clazz.isInstance(obj);
    }

    @Override
    public boolean isEncodable(Class clazz, boolean platformIndependent) {
        return !platformIndependent && super.isEncodable(clazz, false);
    }

    @Override
    public Class<T> getType() {
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
