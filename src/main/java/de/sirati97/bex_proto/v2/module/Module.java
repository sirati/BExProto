package de.sirati97.bex_proto.v2.module;

import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.v2.IPacketDefinition;

import static de.sirati97.bex_proto.v2.module.ModuleHandler.assertConnectionModuleSupport;

/**
 * Created by sirati97 on 12.04.2016.
 */
public abstract class Module<ModuleDataType> implements IModule {
    private final short id;
    private final IPacketDefinition packet;

    public Module(short id) {
        this.id = id;
        this.packet = createPacket();
    }

    protected abstract IPacketDefinition createPacket();

    public final short getId() {
        return id;
    }

    public IPacketDefinition getPacket() {
        return packet;
    }


    protected ModuleDataType getModuleData(IConnection connection) {
        assertConnectionModuleSupport(connection);
        return (ModuleDataType) ((ModularArtifConnection) connection).getModuleData(this);
    }

    protected ModuleDataType getOrCreateModuleData(IConnection connection) {
        assertConnectionModuleSupport(connection);
        return (ModuleDataType) ((ModularArtifConnection) connection).getOrCreateModuleData(this);
    }

    protected ModuleDataType removeModuleData(IConnection connection) {
        assertConnectionModuleSupport(connection);
        return (ModuleDataType) ((ModularArtifConnection) connection).removeModuleData(this);
    }

    @Override
    public abstract ModuleDataType createData(ModularArtifConnection connection);
}
