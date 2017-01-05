package de.sirati97.bex_proto.v2.networkmodell;

/**
 * Created by sirati97 on 04.01.2017 for BexProto.
 */
interface INamed<T extends INamed<T>> {
    String getName();
    boolean equals(T obj);
}
