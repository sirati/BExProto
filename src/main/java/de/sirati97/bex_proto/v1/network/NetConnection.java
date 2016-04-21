package de.sirati97.bex_proto.v1.network;

import de.sirati97.bex_proto.datahandler.SendStream;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.threading.AsyncHelper.AsyncTask;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.util.exception.NotImplementedException;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v1.StreamReader;
import de.sirati97.bex_proto.v1.command.ConnectionInfo;

import javax.crypto.Cipher;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class NetConnection implements NetCreator, ConnectionInfo, IConnection {
	private AsyncHelper asyncHelper;
	private Socket socket;
	private boolean enabled = false;
	private NetConnectionManager netConnectionManager;
	private StreamReader streamReader;
	private AsyncTask readerTask;
	private NetCreator creator;
	private boolean readingLocked = false;
	private boolean registered = true;
	private boolean stoped = false;
	private Cipher sendCipher;
	private Cipher receiveCipher;
	
	private ISocketFactory socketFactory;
	private int reconnectID=-1;
	private NetConnection passAlong;
	private Set<AsyncTask> tasks = new HashSet<>();
	private String subnet = "";
	
	public NetConnection(AsyncHelper asyncHelper, Socket socket,
			NetConnectionManager netConnectionManager, StreamReader streamReader, NetCreator creator, ISocketFactory socketFactory) {
		this.asyncHelper = asyncHelper;
		this.socket = socket;
		this.netConnectionManager = netConnectionManager;
		this.streamReader = streamReader;
		this.creator = creator==null?this:creator;
		this.socketFactory = socketFactory;
	}

//	InputStream inNew = socketFactory.createSocketInputStream(socket);
//	socketFactory.switchInputStream(in, inNew);
//	in = inNew;
	
	public void start() {
		if (enabled)
			return;
		enabled = true;
		netConnectionManager.add(this);
		if (socket instanceof SSLSocket) {
			try {
				((SSLSocket) socket).startHandshake();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		readerTask = asyncHelper.runAsync(new Runnable() {
			
			public void run() {
				byte[] overflow=null;
				try {
					InputStream in = socketFactory.createSocketInputStream(socket);
					while (enabled && !Thread.interrupted()) {
						if (!socketFactory.isSocketInputStream(socket, in)) {
							InputStream inNew = socketFactory.createSocketInputStream(socket);
							socketFactory.switchInputStream(in, inNew);
							in = inNew;
						}
//						if (in instanceof SocketDepended && ((SocketDepended) in).getSocket()!=socket) {
//							System.err.println("create new");
//							in.close();
//							in = socketFactory.createSocketInputStream(socket);
//						} else if (in != socketFactory.createSocketInputStream(socket)) { //guess what! Memory Leak
//							in = socketFactory.createSocketInputStream(socket);
//						}
						
						try {
							overflow = read(overflow, in);
						} catch(SSLException e) {
							stop();
						} catch (IOException e) {
							if (!socketFactory.isSocketInputStream(socket, in)) { //if the old sockets get closed and it didnt already started reading.
								InputStream inNew = socketFactory.createSocketInputStream(socket);
								socketFactory.switchInputStream(in, inNew);
								in = inNew;
								continue;
							}
							if (!socket.isClosed())e.printStackTrace();
						} catch (InterruptedException e) {
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (!isPassAlong()) {
							socket.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}, "Socket Reader Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
	}
	
	protected byte[] read(byte[] overflow, InputStream in) throws SSLException, IOException, InterruptedException {
		if (!isReadingLocked() && (socket.isClosed() || isPassAlong() || in.available() > 0)) {
			if (socket.isClosed() || isPassAlong()) {
				if(isPassAlong())System.out.println("Cancelled reading because other thread is also reading! overflow lenght=" + (overflow==null?0:overflow.length));
				return overflow;
			}
			int available = in.available();
			if (socket.isClosed() || isPassAlong()) {
				if(isPassAlong())System.out.println("Cancelled reading because other thread is also reading! overflow lenght=" + (overflow==null?0:overflow.length));
				return overflow;
			}
			
			byte[] buffer = new byte[available];
			in.read(buffer);
			if (overflow != null) {
				byte[] buffer2 = new byte[overflow.length + buffer.length];
				System.arraycopy(overflow, 0, buffer2, 0, overflow.length);
				System.arraycopy(buffer, 0, buffer2, overflow.length, buffer.length);
				buffer = buffer2;
				overflow = null;
			}
			if (passAlong==null) {
				overflow = exercuteInput(buffer);
			} else {
				passAlong.exercuteInput(buffer);
			}
			
		} else {
			Thread.sleep(0, 1);
		}
		return overflow;
	}
	
	private Object exercuteInputMetux = new Object();
	protected synchronized byte[] exercuteInput(byte[] received) {
		synchronized (exercuteInputMetux) {
//			System.out.println("Exercute: " + Main.bytesToString(received));
			return streamReader.read(received, NetConnection.this, asyncHelper, "Stream Exercuter Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		}
	}
	
	public void exercuteInput(CursorByteBuffer dat) {
		streamReader.exercute(dat, NetConnection.this, asyncHelper, "Stream Exercuter Thread for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
	}
	
	private Object sendMutex = new Object();
	public void send(byte[] stream) {
		synchronized (sendMutex) {
			if (socket.isClosed()) {
				stop();
				return;
			}
			
			try {
				socket.getOutputStream().write(stream);
				socket.getOutputStream().flush();
				
			} catch (SocketException e) {
				if (!registered);
				e.printStackTrace();
				stop();
				return;
			}	catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	
	private long pingTimestamp=0;
	private long pingSendTimestamp=0;
	
	private int ping=-1;
	public void receivedPong(int ping) {
		this.ping = ping;
		this.pingTimestamp = System.currentTimeMillis();
	}
	
	private Object getPingMicroMutex = new Object();
	public int getPingMicro() {
		synchronized (getPingMicroMutex) {
			long currentTimestamp = System.currentTimeMillis();
			
			while (currentTimestamp/1000>pingTimestamp/1000) {
				if (currentTimestamp/100>pingSendTimestamp/100) {
					_sendPing();
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return ping;
		}
	}
	

	public int getPing() {
		return getPingMicro()/1000;
	}
	private void _sendPing() {
		pingSendTimestamp = System.currentTimeMillis();
		getCreator().sendPing(this);
	}
	
	public void sendPing(NetConnection connection) {
		throw new NotImplementedException("Ping is not supported by this connection");
	}

	public void stop() {
		if (stoped)return;
		synchronized (this) {
			stoped = true;
			enabled = false;
			readerTask.stop();
			for (AsyncTask task:tasks) {
				task.stop();
			}
//			try {
//				socket.shutdownOutput();
//			} catch (IOException e) {}
			getCreator().onSocketClosed(this);
			try {
				socket.close();
				//in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	protected StreamReader getStreamReader() {
		return streamReader;
	}
	
	public NetCreator getCreator() {
		return creator;
	}
	
	protected void setCreator(NetCreator creator) {
		this.creator = creator;
	}

	@Override
	public void onSocketClosed(NetConnection connection) {}

	public boolean isReadingLocked() {
		return readingLocked;
	}
	
	public void setReadingLocked(boolean readingLocked) {
		this.readingLocked = readingLocked;
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}
	
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return super.toString() + "{ip=" + socket.getInetAddress().getHostAddress() + ",port=" + socket.getPort() + "}";
	}
	
	public Cipher getSendCipher() {
		return sendCipher;
	}
	
	public void setSendCipher(Cipher sendCipher) {
		this.sendCipher = sendCipher;
	}
	
	public Cipher getReceiveCipher() {
		return receiveCipher;
	}
	
	public void setReceiveCipher(Cipher receiveCipher) {
		this.receiveCipher = receiveCipher;
	}

	
	public void setReconnectID(int reconnectID) {
		this.reconnectID = reconnectID;
	}
	
	public int getReconnectID() {
		return reconnectID;
	}

	public void passAlong(NetConnection connection) {
		synchronized (this) {
			passAlong = connection;
			stoped = true;
			enabled = false;
		}
	}
	
	public boolean isPassAlong() {
		return passAlong!=null;
	}

	public NetConnection getPassAlong() {
		return passAlong;
	}
	
	public void reconnectWith(Socket newSocket) {
		final Socket oldSocket = getSocket();
		setSocket(newSocket);

		class TaskWrap {
			public AsyncHelper.AsyncTask task;
		}
		final TaskWrap task = new TaskWrap();

		final long timpstampShutdown = System.currentTimeMillis()+10;
		task.task = asyncHelper.runAsync(new Runnable() {
			public void run() {
				byte[] overflow=null;
				InputStream in = null;
				try {
					in = socketFactory.createSocketInputStream(oldSocket);
					while (enabled&& timpstampShutdown>System.currentTimeMillis() && !Thread.interrupted()) {
						try {
							overflow = read(overflow, in);
						} catch(IOException | InterruptedException e) {
							break;
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						//if(in!=null)in.close();
						oldSocket.close();
						System.out.println("Disconnected old socket (" + oldSocket.getInetAddress().getHostAddress() + ":" + oldSocket.getPort() + ")");
					} catch (IOException e) {
						e.printStackTrace();
					}
					tasks.remove(task.task);
				}
			}
		}, "Socket Reader(Reconnect anti packet loss) Thread for " + oldSocket.getInetAddress().getHostAddress() + ":" + oldSocket.getPort());
		tasks.add(task.task);
//
		final TaskWrap task2 = new TaskWrap();
		task2.task = asyncHelper.runAsync(new Runnable() {
			public void run() {
				try {
					while (timpstampShutdown>System.currentTimeMillis()) {
						try {
							Thread.sleep(100);
						} catch(InterruptedException e) {
							break;
						}
					}
					System.out.println("Closed old socked(" + oldSocket.getInetAddress().getHostAddress() + ":" + oldSocket.getPort() + ")");
					oldSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					tasks.remove(task2.task);
				}
			}
		}, "Socket Destroyer Thread for " + oldSocket.getInetAddress().getHostAddress() + ":" + oldSocket.getPort());
		tasks.add(task2.task);
	}
	
	public String getSubnet() {
		return subnet;
	}
	
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	@Override
	public void send(SendStream stream, boolean reliable) {
		send(stream);
	}

	@Override
	public void send(SendStream stream) {
		send(stream.getByteBuffer().getBytes());
	}

	@Override
	public ILogger getLogger() {
		throw new NotImplementedException("online available in BExProto v2");
	}
}






















