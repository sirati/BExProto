package de.sirati97.bex_proto.v1.debug;

import de.sirati97.bex_proto.datahandler_.DynamicObj;
import de.sirati97.bex_proto.datahandler_.Type;
import de.sirati97.bex_proto.v1.command.BEx1Command;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class MACommand extends BEx1Command<DynamicObj> {

	
	public MACommand() {
		super((short)1, Type.DynamicObj);
	}
	
	@Override
	public void receive(DynamicObj arg1, NetConnection sender) {
		System.out.println(arg1.getValue()==null?"Received object is null":"Received object type is " + arg1.getValue().getClass().toString());
		if (arg1.getValue()!=null) {
			try {
				Object[] array = (Object[])arg1.getValue();
				System.out.println("Received object is a non primitiv array.");
				for (int i=0;i<array.length;i++) {
					if (array[i]==null) {
						System.out.println("Array[" + i + "]=null");
					} else {
						System.out.println("Array[" + i + "]=" + array[i].toString());
					}
				}
			} catch (Exception e) {
				System.out.println("Received object is not a non primitiv array.");
				System.out.println(".toString()="+arg1.getValue().toString());
			}
		}
	}
}
