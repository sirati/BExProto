package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SSLSocketInputStream extends InputStream implements SocketDepended {
	private InputStream inner;
	private Integer last = null;
	private TLSv1_2SocketFactory socketFactory;
	private Socket socket;
	
	public SSLSocketInputStream(InputStream inner, Socket socket, TLSv1_2SocketFactory socketFactory) {
		this.inner = inner;
		this.socketFactory = socketFactory;
		this.socket = socket;
	}
	
	
	@Override
	public synchronized int read() throws IOException {
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
	public synchronized int available() throws IOException {
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
	
	public Socket getSocket() {
		return socket;
	}
	
	
	private int openCounter=0;
	public void open() {
		openCounter++;
	}
	
	@Override
	public void close() throws IOException {
		if (--openCounter<1) {
			socketFactory.unregister(this);
			super.close();
		}
	}

}
