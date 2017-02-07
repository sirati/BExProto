package de.sirati97.bex_proto.v2.service.modular.internal;

import de.sirati97.bex_proto.v2.service.modular.Module;

/**
 * Created by sirati97 on 13.04.2016.
 */
public abstract class InternalModule<ModuleDataType> extends Module<ModuleDataType> {
    public InternalModule(short id) {
        super(id);
        if (id >= -1) {
            throw new IllegalStateException("Id has to be smaller than -1");
        }
    }
}
