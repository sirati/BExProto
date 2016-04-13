package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.artifcon.IOHandler;
import de.sirati97.bex_proto.v2.module.internal.IHandshakeCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by sirati97 on 12.04.2016.
 */
public class ModularArtifConnection extends ArtifConnection {
    private final ModuleHandler moduleHandler;
    private boolean connectionEstablished=false;
    private final Map<IModule, Object> moduleData = new HashMap<>();
    private boolean canConnect=true;
    private final Object connectMutex = new Object();
    private String connectedWith=null;

    public ModularArtifConnection(String connectionName, AsyncHelper asyncHelper, IOHandler ioHandler, ModuleHandler moduleHandler) {
        super(connectionName, asyncHelper, ioHandler, moduleHandler.getLogger(), moduleHandler.getPacketHandler());
        this.moduleHandler = moduleHandler;
    }

    public ModuleHandler getModuleHandler() {
        return moduleHandler;
    }

    public boolean isConnectionEstablished() {
        return connectionEstablished;
    }

    void setConnectionEstablished(boolean connectionEstablished) {
        this.connectionEstablished = connectionEstablished;
    }

    Object getModuleData(IModule module) {
        return moduleData.get(module);
    }

    Object getOrCreateModuleData(IModule module) {
        Object result = moduleData.get(module);
        if (result == null) {
            result = module.createData(this);
            if (result == null) {
                throw new IllegalStateException("Data cannot be null");
            }
            moduleData.put(module, result);
        }
        return result;
    }

    Object removeModuleData(IModule module) {
        return moduleData.remove(module);
    }

    public void connect() throws TimeoutException, InterruptedException, HandshakeErroredException {
        if (!canConnect) {
            throw new IllegalStateException("Cannot connect twice");
        }
        synchronized (connectMutex) {
            if (!canConnect) { //test it again to be sure
                throw new IllegalStateException("Cannot connect twice");
            }
            HandshakeData data = new HandshakeData();
            moduleHandler.handshakeModule.sendHandshakeRequest(this, data);
            connect_wait(data);
            for (IModuleHandshake moduleHandshake:moduleHandler.handshakesInternalPriority) {
                data.activeModule = moduleHandshake;
                moduleHandshake.onHandshake(this, data);
                connect_wait(data);
            }
            moduleHandler.handshakeModule.sendHandshakeExchangeData(this);
            connect_wait(data);
            for (IModuleHandshake moduleHandshake:moduleHandler.handshakesHighPriority) {
                data.activeModule = moduleHandshake;
                moduleHandshake.onHandshake(this, data);
                connect_wait(data);
            }
            for (IModuleHandshake moduleHandshake:moduleHandler.handshakesLowPriority) {
                data.activeModule = moduleHandshake;
                moduleHandshake.onHandshake(this, data);
                connect_wait(data);
            }
            moduleHandler.handshakeModule.sendHandshakeFinished(this);
            connect_wait(data);

        }

    }

    public String getConnectedWith() {
        return connectedWith;
    }

    public void setConnectedWith(String connectedWith) {
        this.connectedWith = connectedWith;
    }

    private void connect_wait(HandshakeData data) throws InterruptedException, TimeoutException, HandshakeErroredException {
        synchronized (connectMutex) {
            data.yield = true;
            while (data.yield && !data.done && data.exception == null) {
                data.yield = false;
                data.nextYield = System.currentTimeMillis()+1000;
                connectMutex.wait(5000);
            }
            data.timeout |= !data.done;
            if (data.exception != null) {
                throw new HandshakeErroredException(data.exception);
            }
            if (data.timeout) {
                throw new TimeoutException("Connection timed out" + (data.activeModule==null?"":(" or "+data.activeModule.toString()+" executed for to long without calling IHandshakeCallback.yield() [" + (System.currentTimeMillis()-data.nextYield+1000)+ "ms since operation start or last yield]")));
            }
            data.reset();
        }
    }

    class HandshakeData implements IHandshakeCallback {
        public boolean done = false;
        public boolean yield = false;
        public Throwable exception;
        public boolean timeout = false;
        public IModuleHandshake activeModule;
        public long nextYield;

        @Override
        public synchronized void callback() {
            done = true;
            synchronized (connectMutex) {
                connectMutex.notifyAll();
            }

        }

        @Override
        public void error(Throwable cause) {
            exception = cause;
            yield=false;
            done=false;
            synchronized (connectMutex) {
                connectMutex.notifyAll();
            }
        }

        @Override
        public synchronized void yield() {
            if (System.currentTimeMillis()<nextYield) {
                return;
            }
            nextYield = System.currentTimeMillis()+500;
            yield = true;
            synchronized (connectMutex) {
                connectMutex.notifyAll();
            }
        }

        @Override
        public synchronized boolean hasTimeoutOccurred() {
            return timeout;
        }

        public void reset() {
            done = false;
            yield = false;
            timeout = false;
            exception = null;
            activeModule=null;
        }
    }

}
