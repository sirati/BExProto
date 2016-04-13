package de.sirati97.bex_proto.v2.module.internal;

import de.sirati97.bex_proto.v2.module.IModuleHandshake;

/**
 * Created by sirati97 on 13.04.2016.
 */
public abstract class CrytoModule extends InternalModule implements IModuleHandshake {
    CrytoModule(short id) {
        super(id); //TODO: This module
    }
}
