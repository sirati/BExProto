package de.sirati97.bex_proto.test.example;

import java.util.Collections;
import java.util.HashSet;

/**
 * Created by sirati97 on 15.02.2017 for BexProto.
 */
public class Foo {


    public static void main(String... args) throws Throwable {
        System.out.println(Collections.unmodifiableCollection(new HashSet<>()).getClass().getName());
    }
}
