package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class DoubleStream implements Stream {
	private double data;

	public DoubleStream(double data) {
		this.data = data;
	}

	@Override
	public ByteBuffer getBytes() {
		return BExStatic.setDouble(data);
	}

}
