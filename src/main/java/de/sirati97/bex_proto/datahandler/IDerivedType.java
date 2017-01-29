package de.sirati97.bex_proto.datahandler;

import java.util.HashMap;
import java.util.Map;

public interface IDerivedType<Type,InnerType> extends IType<Type> {
	byte getDerivedID();
	IType<InnerType> getInnerType();
	IArrayType getInnerArray();
	Class<Type> getType();
	boolean isBasePrimitive();
	DerivedFactory getFactory();
	Object toPrimitiveArray(Object obj);
	class Register {
		private static Map<Byte, DerivedFactory> types = new HashMap<Byte, DerivedFactory>();
		public static final ArrayTypeFactory ARRAY_TYPE_FACTORY;
		public static final NullableTypeFactory NULLABLE_TYPE_FACTORY; 
		
		static {
			ARRAY_TYPE_FACTORY = new ArrayTypeFactory();
			register(ARRAY_TYPE_FACTORY);
			NULLABLE_TYPE_FACTORY = new NullableTypeFactory();
			register(NULLABLE_TYPE_FACTORY);
		}
		
		public static void register(DerivedFactory type) {
			types.put(type.getDerivedID(), type);
		}
		
		public static DerivedFactory get(byte id) {
			return types.get(id);
		}
	}
	interface DerivedFactory {
		byte getDerivedID();
		IDerivedType create(IType inner);
	}
}
