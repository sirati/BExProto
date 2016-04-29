package de.sirati97.bex_proto.test.v2;

/**
 * Created by sirati97 on 16.04.2016.
 */
public class TestException extends Exception {
    public TestException(String message) {
        super(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestException)) {
            return false;
        }
        return getMessage().equals(((TestException) obj).getMessage());
    }
}
