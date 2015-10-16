package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.io.InputStream;

public class SSLSocketInputStream extends InputStream {
	private InputStream inner;
	private Integer last = null;
	
	public SSLSocketInputStream(InputStream inner) {
		this.inner = inner;
	}
	
	
	@Override
	public int read() throws IOException {
		if (last != null) {
			try {
				return last;
			} finally {
				last = null;
			}
		}
		return inner.read();
	}
	
	@Override
	public int available() throws IOException {
		if (inner.available() != 0) {
			if (last == null) {
				return inner.available();
			} else {
				return inner.available()+1;
			}
		}
		int i = inner.read();
		if (i==-1)return 0;
		last = i;
		return inner.available()+1;
	}

}
