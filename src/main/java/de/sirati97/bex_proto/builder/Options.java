package de.sirati97.bex_proto.builder;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;

/**
 * Created by sirati97 on 05.01.2017 for BexProto.
 */
public class Options {
    protected final static String NAME = "name";

    private TMap<String, Object> map = new THashMap<>();

    public Options add(String key, Object value) {
        map.put(key.toLowerCase(), value);
        return this;
    }

    public Options name(String name) {
        map.put(NAME, name);
        return this;
    }

    public <T> T get(String key) {
        //noinspection unchecked
        return (T)map.get(key.toLowerCase());
    }

    public String getName() {
        return (String) map.get(NAME);
    }

    public boolean hasName() {
        return map.containsKey(NAME);
    }

    public boolean hasKey(String key) {
        return map.containsKey(key.toLowerCase());
    }


}
