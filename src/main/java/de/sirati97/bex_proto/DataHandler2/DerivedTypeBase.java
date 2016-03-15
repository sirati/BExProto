package de.sirati97.bex_proto.DataHandler2;

import java.util.HashMap;
import java.util.Map;

public interface DerivedTypeBase extends TypeBase {
	byte getDerivedID();
	TypeBase getInnerType();
	ArrayType getInnerArray();
	boolean isBasePremitive();
	DerivedFactory getFactory();
	Object toPremitiveArray(Object obj);
	public static class Register {
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
	public interface DerivedFactory {
		byte getDerivedID();
		DerivedTypeBase create(TypeBase inner);
	}
}
