package de.sirati97.bex_proto.v2.modular.internal;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.IEncryptionContainer;
import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketHandler;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.events.TrustPublicKeyEvent;
import de.sirati97.bex_proto.v2.modular.HandshakeRejectedException;
import de.sirati97.bex_proto.v2.modular.IModuleHandshake;
import de.sirati97.bex_proto.v2.modular.ModularArtifConnectionService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import static de.sirati97.bex_proto.v2.modular.internal.BouncyCastleHelper.*;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class EncryptionModule extends InternalModule<EncryptionModule.EncryptionData> implements IModuleHandshake, PacketHandler {
    private static class EncryptionPacketDefinition extends PacketDefinition {
        public EncryptionPacketDefinition(short id, PacketHandler executor) {
            super(id, executor, Type.Byte, Type.Byte.asArray().asNullable(), Type.Byte.asArray().asNullable());
        }
    }
    static class EncryptionData {
        public boolean done = false;
        public ICallback callback;
        public PublicKey remotePublicKey;
        public Key secretKey;
        public byte[] vectorPartClient;
        public byte[] vectorPartServer;
    }
    private EncryptionPacketDefinition packetDefinition;
    private final IEncryptionContainer encryptionContainer;
    private final KeyGenerator keyGenerator;
    private final SecureRandom secureRandom;


    public EncryptionModule(IEncryptionContainer encryptionContainer) throws NoSuchAlgorithmException, NoSuchProviderException {
        super((short) -3);
        this.encryptionContainer = encryptionContainer;
        initBouncyCastle();
        keyGenerator = KeyGenerator.getInstance(SYMMETRIC_TYPE, PROVIDER);
        secureRandom = SecureRandom.getInstance("SHA1PRNG");
        keyGenerator.init(SYMMETRIC_SIZE);
    }

    @Override
    protected IPacketDefinition createPacket() {
        return packetDefinition==null?(packetDefinition=new EncryptionPacketDefinition(getId(), this)):packetDefinition;
    }

    @Override
    public EncryptionData createData(ModularArtifConnectionService connection) {
        return new EncryptionData();
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }

    protected void send(State state, IConnection connection) {
        send(state, null, connection);
    }

    protected void send(State state, byte[] data, IConnection connection) {
        send(state, data, null, connection);
    }

    protected void send(State state, byte[] data, byte[] data2, IConnection connection) {
        Packet packet = new Packet(packetDefinition, state.id, data, data2);
        packet.sendTo(connection);
    }

    protected void sendError(String error, IConnection connection) {
        send(State.Error, Type.String_US_ASCII.createStream(error).getByteBuffer().getBytes(), connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        byte state = packet.get(0);
        byte[] data = packet.get(1);
        byte[] data2 = packet.get(2);
        ModularArtifConnectionService connection = (ModularArtifConnectionService) packet.getSender();
        switch (State.getById(state)) {
            case Error:
                onError((String) Type.String_US_ASCII.getExtractor().extract(new CursorByteBuffer(data, connection)), connection);
                break;
            case ClientPublicKey:
                onClientPublicKey(data, connection);
                break;
            case ServerPublicKey:
                onServerPublicKey(data, connection);
                break;
            case TrustServer:
                onTrustServer(data, connection);
                break;
            case TrustServerAnswer:
                onTrustServerAnswer(data, connection);
                break;
            case RequestSecretKey:
                onRequestSecretKey(connection);
                break;
            case SecretKey:
                onSecretKey(connection, data, data2);
                break;
            case SecretKeyConfirm:
                onSecretKeyConfirm(connection, data, data2);
                break;
            case Success:
                onSuccess(connection);
                break;

        }
    }


    public enum State {
        Error(-1),ClientPublicKey(0),ServerPublicKey(1),TrustServer(2),TrustServerAnswer(3),RequestSecretKey(4),SecretKey(5),SecretKeyConfirm(6),Cancel(7), Success(8);

        private static int off;
        public final byte id;

        State(int id) {
            this.id = (byte)id;
        }

        static {
            int off = 0;
            for (State state:values()) {
                off=off>state.id?state.id:off;
            }
            State.off=off;
        }

        public static State getById(byte id) {
            return values()[id-off];
        }
    }

    @Override
    public void onHandshake(ModularArtifConnectionService connection, ICallback callback) throws Throwable {
        getOrCreateModuleData(connection).callback = callback;
        send(State.ClientPublicKey, encryptionContainer.getPublicKey().getEncoded(), connection);
    }

    @Override
    public void onHandshakeServerSide(ModularArtifConnectionService connection, ICallback callback) throws Throwable {
        getOrCreateModuleData(connection).callback = callback;
    }

    @Override
    public boolean completeHandshake(ModularArtifConnectionService connection) throws Throwable {
        return removeModuleData(connection).done;
    }


    private void closeWithError(String text, Throwable t, ModularArtifConnectionService con) {
        con.getLogger().error(text+" :", t);
        _closeWithError(text, con);
    }

    private void closeWithError(String text, ModularArtifConnectionService con) {
        con.getLogger().error(text+" :", new Exception("Unknown type"));
        _closeWithError(text, con);
    }


    private void _closeWithError(String text, ModularArtifConnectionService con) {
        sendError(text, con);
        //con.stop();
    }

    //Client/PeerConnecting only
    private void onServerPublicKey(byte[] key, ModularArtifConnectionService connection) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        PublicKey publicKey;
        try {
            publicKey = encryptionContainer.recoverFromData(key);
        } catch (InvalidKeySpecException e) {
            onError(e, connection);
            return;
        }

        boolean trusted = encryptionContainer.trust(publicKey);
        TrustPublicKeyEvent trustPublicKeyEvent =new TrustPublicKeyEvent(publicKey, trusted);
        connection.invokeEvent(trustPublicKeyEvent);
        trusted = trustPublicKeyEvent.isTrusted();

        if (trusted) {
            encryptionData.remotePublicKey=publicKey;
            byte[] iv = new byte[IV_LENGTH/2];
            secureRandom.nextBytes(iv);
            encryptionData.vectorPartClient = iv;

            try {
                Cipher cipherServerPublic = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
                cipherServerPublic.init(Cipher.ENCRYPT_MODE, encryptionData.remotePublicKey);
                byte[] iVectorClientBytes = cipherServerPublic.doFinal(iv);
                send(State.TrustServer, iVectorClientBytes, connection);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
                onError(e, connection);
            }
        } else {
            onError("Server is not trusted!", connection);
        }
    }

    private void onTrustServerAnswer(byte[] dataVector, ModularArtifConnectionService connection) {

        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        try {
            Cipher cipherClientPrivate = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherClientPrivate.init(Cipher.DECRYPT_MODE, encryptionContainer.getPrivateKey());
            byte[] iVectorBytesReceived = cipherClientPrivate.doFinal(dataVector);
            byte[] iVectorBytesLocal = encryptionData.vectorPartClient;
            if (iVectorBytesLocal.length != iVectorBytesReceived.length) {
                onError("Received invalid vector.", connection);
                return;
            }
            for (int i=0;i<iVectorBytesLocal.length;i++) {
                if (iVectorBytesLocal[i] != iVectorBytesReceived[i]) {
                    onError("Received invalid vector.", connection);
                    return;
                }
            }
            send(State.RequestSecretKey, connection);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
            onError(e, connection);
        }
    }

    private void onSecretKey(ModularArtifConnectionService connection, byte[] dataKey, byte[] dataVector) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        if (encryptionData.remotePublicKey == null || encryptionData.vectorPartClient == null) {
            onError("Unexpected Error! Handshake data is corrupted.", connection);
            return;
        }

        try {
            Cipher cipherServerPrivate = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherServerPrivate.init(Cipher.DECRYPT_MODE, encryptionContainer.getPrivateKey());
            byte[] secretKeyRawBytes = cipherServerPrivate.doFinal(dataKey);
            byte[] iVectorRawBytes = cipherServerPrivate.doFinal(dataVector);
            encryptionData.secretKey = new SecretKeySpec(secretKeyRawBytes, SYMMETRIC_TYPE);
            encryptionData.vectorPartServer = iVectorRawBytes;

            Cipher cipherClientPublic = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherClientPublic.init(Cipher.ENCRYPT_MODE, encryptionData.remotePublicKey);
            byte[] secretKeyBytes = cipherClientPublic.doFinal(secretKeyRawBytes);
            byte[] iVectorBytes = cipherClientPublic.doFinal(iVectorRawBytes);
            send(State.SecretKeyConfirm, secretKeyBytes, iVectorBytes, connection);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
            onError(e, connection);
        }
    }

    private void onSuccess(ModularArtifConnectionService connection) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        if (encryptionData.remotePublicKey == null || encryptionData.vectorPartClient == null || encryptionData.vectorPartServer == null || encryptionData.secretKey == null) {
            onError("Unexpected Error! Handshake data is corrupted.", connection);
            return;
        }

        try {
            byte[] iv = new byte[encryptionData.vectorPartClient.length+encryptionData.vectorPartServer.length];
            System.arraycopy(encryptionData.vectorPartClient,0,iv,0,encryptionData.vectorPartClient.length);
            System.arraycopy(encryptionData.vectorPartServer,0,iv,encryptionData.vectorPartClient.length,encryptionData.vectorPartServer.length);
            IvParameterSpec secretVector = new IvParameterSpec(iv);

            Cipher sendCipher = Cipher.getInstance(SYMMETRIC_PATTERN, PROVIDER);
            sendCipher.init(Cipher.ENCRYPT_MODE, encryptionData.secretKey, secretVector);
            Cipher receiveCipher = Cipher.getInstance(SYMMETRIC_PATTERN, PROVIDER);
            receiveCipher.init(Cipher.DECRYPT_MODE, encryptionData.secretKey, secretVector);
            connection.setReceiveCipher(receiveCipher);
            connection.setSendCipher(sendCipher);
            connection.setHashAlgorithm(MessageDigest.getInstance(HASH_ALGORITHM, PROVIDER));
            encryptionData.done = true;
            encryptionData.callback.callback();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            onError(e, connection);
        }
    }



    //Server/PeerConnectedTo only
    private void onClientPublicKey(byte[] key, ModularArtifConnectionService connection) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        PublicKey publicKey;
        try {
            publicKey = encryptionContainer.recoverFromData(key);
        } catch (InvalidKeySpecException e) {
            closeWithError(e.toString(), connection);
            return;
        }

        boolean trusted = encryptionContainer.trust(publicKey);
        TrustPublicKeyEvent trustPublicKeyEvent =new TrustPublicKeyEvent(publicKey, trusted);
        connection.invokeEvent(trustPublicKeyEvent);
        trusted = trustPublicKeyEvent.isTrusted();

        if (trusted) {
            encryptionData.remotePublicKey=publicKey;
            send(State.ServerPublicKey, encryptionContainer.getPublicKey().getEncoded(), connection);
        } else {
            closeWithError("Client is not trusted!", connection);
        }
    }

    private void onTrustServer(byte[] dataVector, ModularArtifConnectionService connection) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        if (encryptionData.remotePublicKey == null) {
            closeWithError("Unexpected Error!", connection);
            return;
        }

        try {
            Cipher cipherServerPrivate = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherServerPrivate.init(Cipher.DECRYPT_MODE, encryptionContainer.getPrivateKey());
            byte[] iv = cipherServerPrivate.doFinal(dataVector);
            encryptionData.vectorPartClient = iv;

            Cipher cipherClientPublic = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherClientPublic.init(Cipher.ENCRYPT_MODE, encryptionData.remotePublicKey);
            byte[] iVectorBytes = cipherClientPublic.doFinal(iv);
            send(State.TrustServerAnswer, iVectorBytes, connection);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
            closeWithError("Unexpected Error!", e, connection);
        }
    }

    private void onRequestSecretKey(ModularArtifConnectionService connection) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        if (encryptionData.remotePublicKey == null || encryptionData.vectorPartClient == null) {
            closeWithError("Unexpected Error!", connection);
            return;
        }
        Key secretKey = keyGenerator.generateKey();
        byte[] iv = new byte[IV_LENGTH/2];
        secureRandom.nextBytes(iv);
        encryptionData.secretKey = secretKey;
        encryptionData.vectorPartServer = iv;

        try {
            Cipher cipherClientPublic = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherClientPublic.init(Cipher.ENCRYPT_MODE, encryptionData.remotePublicKey);
            byte[] secretKeyBytes = cipherClientPublic.doFinal(secretKey.getEncoded());
            byte[] iVectorBytes = cipherClientPublic.doFinal(iv);
            send(State.SecretKey, secretKeyBytes, iVectorBytes, connection);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
            connection.getLogger().error("Unexpected Error while encryption handshake: ", e);
            closeWithError("Unexpected Error!", e, connection);
        }
    }

    private void onSecretKeyConfirm(ModularArtifConnectionService connection, byte[] dataKey, byte[] dataVector) {
        EncryptionData encryptionData = getModuleData(connection);
        encryptionData.callback.yield(YieldCause.PacketReceived);
        if (encryptionData.remotePublicKey == null || encryptionData.vectorPartClient == null || encryptionData.vectorPartServer == null || encryptionData.secretKey == null) {
            closeWithError("Unexpected Error!", connection);
            return;
        }

        try {
            Cipher cipherServerPrivate = Cipher.getInstance(ASYMMETRIC_PATTERN, PROVIDER);
            cipherServerPrivate.init(Cipher.DECRYPT_MODE, encryptionContainer.getPrivateKey());
            byte[] secretKeyBytesReceived = cipherServerPrivate.doFinal(dataKey);
            byte[] iVectorBytesReceived = cipherServerPrivate.doFinal(dataVector);
            byte[] secretKeyBytesLocal = encryptionData.secretKey.getEncoded();
            byte[] iVectorBytesLocal = encryptionData.vectorPartServer;
            if (secretKeyBytesLocal.length != secretKeyBytesReceived.length) {
                closeWithError("Unexpected Error!", connection);
                return;
            }
            if (iVectorBytesLocal.length != iVectorBytesReceived.length) {
                closeWithError("Unexpected Error!", connection);
                return;
            }
            for (int i=0;i<secretKeyBytesLocal.length;i++) {
                if (secretKeyBytesLocal[i] != secretKeyBytesReceived[i]) {
                    closeWithError("Unexpected Error!", connection);
                    return;
                }
            }
            for (int i=0;i<iVectorBytesLocal.length;i++) {
                if (iVectorBytesLocal[i] != iVectorBytesReceived[i]) {
                    closeWithError("Unexpected Error!", connection);
                    return;
                }
            }

            byte[] iv = new byte[encryptionData.vectorPartClient.length+encryptionData.vectorPartServer.length];
            System.arraycopy(encryptionData.vectorPartClient,0,iv,0,encryptionData.vectorPartClient.length);
            System.arraycopy(encryptionData.vectorPartServer,0,iv,encryptionData.vectorPartClient.length,encryptionData.vectorPartServer.length);
            IvParameterSpec secretVector = new IvParameterSpec(iv);

            Cipher sendCipher = Cipher.getInstance(SYMMETRIC_PATTERN, PROVIDER);
            sendCipher.init(Cipher.ENCRYPT_MODE, encryptionData.secretKey, secretVector);
            Cipher receiveCipher = Cipher.getInstance(SYMMETRIC_PATTERN, PROVIDER);
            receiveCipher.init(Cipher.DECRYPT_MODE, encryptionData.secretKey, secretVector);
            send(State.Success, connection);
            connection.setReceiveCipher(receiveCipher);
            connection.setSendCipher(sendCipher);
            connection.setHashAlgorithm(MessageDigest.getInstance(HASH_ALGORITHM, PROVIDER));
            encryptionData.done = true;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            closeWithError("Unexpected Error!", e, connection);
        }
    }



    //Both

    private void onError(String message, ModularArtifConnectionService connection) {
        getModuleData(connection).callback.error(new HandshakeRejectedException(message));
    }
    private void onError(Throwable t, ModularArtifConnectionService connection) {
        getModuleData(connection).callback.error(new HandshakeRejectedException(t));
    }
}
