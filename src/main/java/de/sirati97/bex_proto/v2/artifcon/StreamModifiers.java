package de.sirati97.bex_proto.v2.artifcon;

import de.sirati97.bex_proto.datahandler.IEncryptionStreamModifier;
import de.sirati97.bex_proto.datahandler.IHashingStreamModifier;
import de.sirati97.bex_proto.datahandler.IStreamModifier;
import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sirati97 on 29.01.2017 for BexProto.
 */
public class StreamModifiers {
    private IEncryptionStreamModifier encryptionModifier;
    private IHashingStreamModifier hashingModifier;
    private IStreamModifier integrityCheck;
    private List<IStreamModifier> utilModifiers = new ArrayList<>();

    public void setEncryptionModifier(IEncryptionStreamModifier encryptionModifier) {
        this.encryptionModifier = encryptionModifier;
    }

    public void setHashingModifier(IHashingStreamModifier hashingModifier) {
        this.hashingModifier = hashingModifier;
    }

    public IEncryptionStreamModifier getEncryptionModifier() {
        return encryptionModifier;
    }

    public IHashingStreamModifier getHashingModifier() {
        return hashingModifier;
    }

    public void setIntegrityCheck(IStreamModifier integrityCheck) {
        this.integrityCheck = integrityCheck;
    }

    public List<IStreamModifier> getUtilModifiers() {
        return utilModifiers;
    }


    public ByteBuffer apply(ByteBuffer buffer) {
        for (IStreamModifier streamModifier:utilModifiers) {
            buffer = streamModifier.apply(buffer);
        }
        buffer = integrityCheck==null?buffer:integrityCheck.apply(buffer);
        buffer = hashingModifier ==null?buffer: hashingModifier.apply(buffer);
        return encryptionModifier ==null?buffer: encryptionModifier.apply(buffer);
    }


    public byte[] unapply(byte[] stream) {
        stream = encryptionModifier ==null?stream: encryptionModifier.unapply(stream);
        stream = hashingModifier ==null?stream: hashingModifier.unapply(stream);
        stream = integrityCheck==null?stream:integrityCheck.unapply(stream);
        for (IStreamModifier streamModifier:utilModifiers) {
            stream = streamModifier.unapply(stream);
        }
        return stream;
    }
    
}
