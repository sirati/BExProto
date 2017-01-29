package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

import java.util.ArrayList;
import java.util.List;

public class TypeEncoder extends EncoderBase<IType> {

    @Override
    public void encode(IType data, ByteBuffer buffer) {
        boolean isDerived = (data instanceof IDerivedType);
        if (isDerived) {
            List<IDerivedType> derivedTypesList = new ArrayList<>();
            byte[] derivedTypes;
            IType temp = data;
            while (temp instanceof IDerivedType) {
                IDerivedType derivedType = (IDerivedType) temp;
                temp = derivedType.getInnerType();
                derivedTypesList.add(derivedType);
            }
            derivedTypes = new byte[derivedTypesList.size()];
            for (int i=0;i<derivedTypes.length;i++) {
                derivedTypes[derivedTypes.length-i-1]=derivedTypesList.get(i).getDerivedID();
            }
            Type.Boolean.getEncoder().encode(true, buffer);
            Type.String_US_ASCII.getEncoder().encode(temp.getTypeName(), buffer);
            BExStatic.setByteArray(derivedTypes, buffer);
        } else {
            Type.Boolean.getEncoder().encode(false, buffer);
            Type.String_US_ASCII.getEncoder().encode(data.getTypeName(), buffer);
        }
    }
}
