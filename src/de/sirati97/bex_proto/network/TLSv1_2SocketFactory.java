package de.sirati97.bex_proto.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;

public class TLSv1_2SocketFactory implements ISocketFactory {
	private SSLContext sslContext;
	private boolean needClientAuth;
	
	public TLSv1_2SocketFactory(File certificate, String certPass, String keyPass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
		this(certificate, certPass, keyPass, false);
	}
		
		
	public TLSv1_2SocketFactory(File certificate, String certPass, String keyPass, boolean needClientAuth) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keyStore.load(new FileInputStream(certificate), certPass.toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
	            .getDefaultAlgorithm());
		kmf.init(keyStore, keyPass.toCharArray());
		sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		this.needClientAuth = needClientAuth;
	}
	
	@Override
	public Socket createSocket(String host, int port) throws UnknownHostException, IOException {
		return sslContext.getSocketFactory().createSocket(host, port);
	}

	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		SSLServerSocket result = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port);
		result.setNeedClientAuth(needClientAuth);
		return result;
	}

	@Override
	public ServerSocket createServerSocket(int port, InetAddress address) throws IOException {
		SSLServerSocket result = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port, -1, address);
		result.setNeedClientAuth(needClientAuth);
		return result;
	}

	@Override
	public InputStream getSocketInputStream(Socket socket) throws IOException {
		return new SSLSocketInputStream(socket.getInputStream());
	}

}
