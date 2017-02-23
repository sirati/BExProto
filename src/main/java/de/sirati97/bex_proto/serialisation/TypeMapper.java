package de.sirati97.bex_proto.serialisation;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.datahandler.IType;
import de.sirati97.bex_proto.util.exception.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sirati97 on 07.07.2016 for BexProto.
 */
public class TypeMapper {
    private final Map<IType, Integer> typeId = new HashMap<>();
    private final List<IType> idType = new ArrayList<>();
    private final boolean platformIndependent;

    public TypeMapper(boolean platformIndependent) {
        this.platformIndependent = platformIndependent;
    }

    public int getMappedType(Object o) {
        IType type = Types.getByInstance(o, platformIndependent, true);

        throw new NotImplementedException("");
    }

    public Types getMappedTypeById(int id) {
        throw new NotImplementedException("");

    }
}
