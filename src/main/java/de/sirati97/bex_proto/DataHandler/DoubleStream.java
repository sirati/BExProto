package de.sirati97.bex_proto.DataHandler;

public class DoubleStream implements Stream {
	private double data;

	public DoubleStream(double data) {
		this.data = data;
	}

	@Override
	public byte[] getBytes() {
		return BExStatic.setDouble(data);
	}

}
