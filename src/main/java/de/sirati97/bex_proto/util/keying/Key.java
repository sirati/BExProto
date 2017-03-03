package de.sirati97.bex_proto.util.keying;

/**
 * Created by sirati97 on 03.03.2017 for BexProto.
 */
public class Key {
    private final int id;
    private final KeyEnvironment environment;

    public Key(KeyEnvironment environment) {
        id = environment.getNext();
        this.environment = environment;
    }

    public void checkEnvironment(KeyEnvironment environment) {
        if (!this.environment.equals(environment)) {
            throw new WrongKeyException();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == hashCode() && obj instanceof Key;
    }
}
