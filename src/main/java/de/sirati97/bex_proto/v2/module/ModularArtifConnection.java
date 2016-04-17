package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.util.exception.NotImplementedException;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;
import de.sirati97.bex_proto.v2.io.IOHandler;
import de.sirati97.bex_proto.v2.module.internal.ICallback;
import de.sirati97.bex_proto.v2.module.internal.YieldCause;
import de.sirati97.bex_proto.v2.module.internal.connectionhandler.ConnectionHandlerModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    public ModularArtifConnection(String connectionName, IOHandler ioHandler, ModuleHandler moduleHandler) {
        super(connectionName, moduleHandler.getAsyncHelper(), ioHandler, moduleHandler.getLogger(), moduleHandler.getPacketHandler());
        this.moduleHandler = moduleHandler;
    }

    public ModuleHandler getModuleHandler() {
        return moduleHandler;
    }

    public boolean isConnectionEstablished() {
        return connectionEstablished;
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

    public void connectAsync(final IConnectResult result) {
        getAsyncHelper().runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    connect();
                    if (result != null) {
                        result.onConnected(ModularArtifConnection.this);
                    }
                } catch (Exception e) {
                    if (result != null) {
                        result.onException(ModularArtifConnection.this, e);
                    }
                }

            }
        },"Client Side Handshake Thread");
    }

    @Override
    public void connect() throws TimeoutException, InterruptedException, IOException {
        if (!canConnect) {
            throw new IllegalStateException("Cannot connect twice");
        }
        synchronized (connectMutex) {
            if (!canConnect) { //test it again to be sure
                throw new IllegalStateException("Cannot connect twice");
            }
            super.connect();
            CallbackData data = new CallbackData();
            moduleHandler.connectionHandlerModule.sendHandshakeRequest(this, data);
            connect_wait(data);

            onHandshake(data, moduleHandler.handshakesInternalPriority, false);

            moduleHandler.connectionHandlerModule.sendHandshakeExchangeData(this);
            connect_wait(data);

            onHandshake(data, moduleHandler.handshakesHighPriority, false);
            onHandshake(data, moduleHandler.handshakesLowPriority, false);

            moduleHandler.connectionHandlerModule.sendHandshakeFinished(this);
            connect_wait(data);

            checkHandshakeCompleted(moduleHandler.handshakesInternalPriority);
            checkHandshakeCompleted(moduleHandler.handshakesHighPriority);
            checkHandshakeCompleted(moduleHandler.handshakesLowPriority);
            connectionEstablished = true;
        }
    }

    private void checkHandshakeCompleted(List<IModuleHandshake> handshakes) throws HandshakeException {
        for (IModuleHandshake moduleHandshake:handshakes) {
            try {
                if (!moduleHandshake.completeHandshake(this)) {
                    throw new HandshakeIncompleteException(moduleHandshake);
                }
            } catch (Throwable t) {
                throw (t instanceof HandshakeIncompleteException)?(HandshakeIncompleteException)t:new HandshakeModuleException(moduleHandshake, t);
            }
        }
    }

    private void onHandshake(ICallbackData data, List<IModuleHandshake> handshakes, boolean serverSide) throws HandshakeException, TimeoutException, InterruptedException {
        for (IModuleHandshake moduleHandshake:handshakes) {
            if (!serverSide) {
                data.setActiveModule(moduleHandshake);
            }
            try {
                if (serverSide) {
                    moduleHandshake.onHandshakeServerSide(this, data);
                } else {
                    moduleHandshake.onHandshake(this, data);
                }
            } catch (Throwable t) {
                throw new HandshakeModuleException(moduleHandshake, t);
            }
            if (!serverSide) {
                connect_wait(data.getRoot());
            }
        }
    }

    /**Internal do not call*/
    public final void internal_OnHandshakeStarted(Class clazz){
        if (clazz != ConnectionHandlerModule.class) {
            throw new IllegalStateException("This method should not be called by user code");
        }
        if (!canConnect) {
            throw new IllegalStateException("Cannot start handshake twice");
        }
        synchronized (connectMutex) {
            if (!canConnect) { //test it again to be sure
                throw new IllegalStateException("Cannot start handshake twice");
            }
            final CallbackData data = new CallbackData();
            final CallbackData initLock = new CallbackData();

            getAsyncHelper().runAsync(new Runnable() {
                @Override
                public void run() {
                    try {
                        ModularArtifConnection.super.connect();

                        data.yieldChild = initLock;
                        ServerCallbackDataWrapper serverData = new ServerCallbackDataWrapper(data);
                        moduleHandler.connectionHandlerModule.initServerSide(ModularArtifConnection.this, data);
                        onHandshake(serverData, moduleHandler.handshakesInternalPriority, true);
                        data.yieldChild = null;
                        initLock.callback();
                        connect_wait(data); //wait until internal handshake finishes

                        onHandshake(serverData, moduleHandler.handshakesHighPriority, true);
                        onHandshake(serverData, moduleHandler.handshakesLowPriority, true);
                        connect_wait(data); //wait until handshake finished

                        checkHandshakeCompleted(moduleHandler.handshakesInternalPriority);
                        checkHandshakeCompleted(moduleHandler.handshakesHighPriority);
                        checkHandshakeCompleted(moduleHandler.handshakesLowPriority);

                        connectionEstablished = true;
                        moduleHandler.connectionHandlerModule.finishServerSide(ModularArtifConnection.this);

                    } catch (Throwable e) {
                        getLogger().error("Handshake failed. Exception occurred:", e);
                        if (e instanceof IOException || e instanceof InterruptedException || e instanceof TimeoutException) {
                            moduleHandler.connectionHandlerModule.sendHandshakeError(e, ModularArtifConnection.this);
                        }
                    }

                }
            },"Server Side Handshake Thread");
            try {
                connect_wait(initLock);
            } catch (InterruptedException | TimeoutException | HandshakeException e) {
                data.error(e);
            }
        }
    }

    public String getConnectedWith() {
        return connectedWith;
    }

    public void setConnectedWith(String connectedWith) {
        this.connectedWith = connectedWith;
    }

    private void connect_wait(CallbackData data) throws InterruptedException, TimeoutException, HandshakeException {
        synchronized (connectMutex) {
            data.yield = true;
            while (data.yield && !data.done && data.exception == null) {
                data.yield = false;
                data.nextYield = System.currentTimeMillis()+1000;
                connectMutex.wait(5000);
            }
            data.timeout |= !data.done;
            if (data.exception != null) {
                throw data.exception instanceof HandshakeRemoteException?new HandshakeCancelledException(data.exception):new HandshakeException(data.exception);
            }
            if (data.timeout) {
                throw new TimeoutException("Connection timed out" + (data.activeModule==null?"":(" or "+data.activeModule.toString()+" executed for to long without calling IHandshakeCallback.yield() [" + (System.currentTimeMillis()-data.nextYield+1000)+ "ms since operation start or last yield]")));
            }
            data.reset();
        }
    }

    interface ICallbackData extends ICallback {
        IModuleHandshake getActiveModule();
        void setActiveModule(IModuleHandshake activeModule);
        CallbackData getRoot();
    }

    class CallbackData implements ICallbackData {
        public boolean done = false;
        public boolean yield = false;
        public Throwable exception;
        public boolean timeout = false;
        public IModuleHandshake activeModule;
        public long nextYield;
        public CallbackData yieldChild;

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
        public synchronized void yield(YieldCause cause) {
            if (yieldChild != null) {
                yieldChild.yield(YieldCause.PacketReceived);
            }
            if (System.currentTimeMillis()<nextYield) {
                return;
            }
            if (cause == YieldCause.KeepAlive) {
                moduleHandler.connectionHandlerModule.yieldRemote(ModularArtifConnection.this);
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

        public IModuleHandshake getActiveModule() {
            return activeModule;
        }

        public void setActiveModule(IModuleHandshake activeModule) {
            this.activeModule = activeModule;
        }

        @Override
        public CallbackData getRoot() {
            return this;
        }
    }

    class ServerCallbackDataWrapper implements ICallbackData {
        private final ICallbackData parent;

        ServerCallbackDataWrapper(ICallbackData parent) {
            this.parent = parent;
        }

        @Override
        public void callback() {
            throw new NotImplementedException("This method can only  be called clientside / on the connecting peer");
        }

        @Override
        public void error(Throwable cause) {
            parent.error(cause);
        }

        @Override
        public void yield(YieldCause cause) {
            parent.yield(cause);
        }

        @Override
        public boolean hasTimeoutOccurred() {
            return parent.hasTimeoutOccurred();
        }

        @Override
        public IModuleHandshake getActiveModule() {
            return parent.getActiveModule();
        }

        @Override
        public void setActiveModule(IModuleHandshake activeModule) {
            parent.setActiveModule(activeModule);
        }

        @Override
        public CallbackData getRoot() {
            return parent.getRoot();
        }
    }

}
