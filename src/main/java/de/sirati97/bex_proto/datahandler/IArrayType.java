package de.sirati97.bex_proto.datahandler;

/**
 * Created by sirati97 on 24.04.2016.
 */
public interface IArrayType<InnerType> extends DerivedTypeBase<InnerType[],InnerType> {
    TypeBase<?> getBase();
    int getDimensions();
}
