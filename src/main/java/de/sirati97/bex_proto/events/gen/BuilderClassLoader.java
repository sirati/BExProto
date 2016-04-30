package de.sirati97.bex_proto.events.gen;

/**
 * Created by sirati97 on 30.04.2016.
 */
public class BuilderClassLoader extends ClassLoader {

    public Class defineClass(String name, byte[] bytes) {
        return this.defineClass(name, bytes, 0, bytes.length);
    }

}
