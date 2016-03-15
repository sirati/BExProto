package de.sirati97.bex_proto.v1.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.sirati97.bex_proto.datahandler_.ArrayType;
import de.sirati97.bex_proto.datahandler_.DynamicObj;
import de.sirati97.bex_proto.datahandler_.NullableType;
import de.sirati97.bex_proto.datahandler_.Stream;
import de.sirati97.bex_proto.datahandler_.Type;
import de.sirati97.bex_proto.datahandler_.TypeBase;
import de.sirati97.bex_proto.v1.command.CommandRegister;
import de.sirati97.bex_proto.threading.AdvThreadAsyncHelper;
import de.sirati97.bex_proto.v1.network.ISocketFactory;
import de.sirati97.bex_proto.v1.network.SocketFactory;
import de.sirati97.bex_proto.v1.network.adv.AdvClient;
import de.sirati97.bex_proto.v1.network.adv.AdvServer;
import de.sirati97.bex_proto.v1.network.adv.CryptoContainer;


public class Main {
	private static AdvThreadAsyncHelper asyncHelper;
	private static long sleepTime;
	public static void main(String[] args) throws InterruptedException {
		asyncHelper = new AdvThreadAsyncHelper();
		asyncHelper.runAsync(new Runnable() {
			public void run() {
				try {
					main();
				} catch (UnrecoverableKeyException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException | InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		}, "main");
	}
	
	public static void main() throws InterruptedException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
		//Auswerter der daten instanzieren
		
		CommandRegister commandRegister = new CommandRegister();
		MACommand maCommand = new MACommand();
		commandRegister.register(maCommand);
		final ICountCommand iCountCommand = new ICountCommand(2, "ICount Benchmark(With Reconnect):");
		commandRegister.register(iCountCommand);
		final ICountCommand iCount2Command = new ICountCommand(3, "ICount Benchmark(Without Reconnect):");
		commandRegister.register(iCount2Command);
		// Um neue Threads zu erstellen. Was ja auf bungee nicht direkt geht, deswegen diese klasse
		//Server & Client instanzieren
		
		CryptoContainer cryptoContainerServer = new CryptoContainer(new File("server2.jks"), "123456Server", "epicserver", "654321Server");
		CryptoContainer cryptoContainerClient = new CryptoContainer(new File("client2.jks"), "123456Client", "epicclient", "654321Client");
		final ISocketFactory socketFactoryServer = new SocketFactory();//new TLSv1_2SocketFactory(new File("server2.jks"), "123456Server", "654321Server", true);
		final ISocketFactory socketFactoryClient = new SocketFactory();//new TLSv1_2SocketFactory(new File("client2.jks"), "123456Client", "654321Client");
		
		
		//socketFactory1 = new SocketFactory();
		AdvServer server = new AdvServer(asyncHelper, 10000, commandRegister, socketFactoryServer);
		server.setCryptoContainer(cryptoContainerServer);
		final AdvClient client = new AdvClient(asyncHelper, "127.0.0.1", 10000, "TheSuperAwesomeClient", true, commandRegister, socketFactoryClient);
		client.setCryptoContainer(cryptoContainerClient);
		//		AdvClient client2 = new AdvClient(asyncHelper, "127.0.0.1", 10000, "TheSuperAwesomeClient", true, command);
		
		//Server & Client starten (server zuerst, weil sonst der client keine connection bekommen kann)
		server.start();
		client.start();
//		client2.start();

		
		
		
		final long startTime = System.currentTimeMillis();
		Stream stream;
		TypeBase type;
		//testdaten zu byte[] 
		type = new NullableType(new ArrayType(new NullableType(Type.Integer)));
		System.out.println(type.getTypeName());
		stream = maCommand.send(new DynamicObj(type, new Integer[]{1231, null, 0 ,1, Integer.MAX_VALUE, null, 0, Integer.MIN_VALUE}));
		
		//testdaten senden1
//		System.out.println(bytesToString(stream.getBytes()));
		maCommand.send(stream,client);
		

		
		//testdaten zu byte[] 
		type = Type.String_Utf_16;
		System.out.println(type.getTypeName());
		stream = maCommand.send(new DynamicObj(type, "Hallo liebe welt"));

		//testdaten senden
//		System.out.println(bytesToString(stream.getBytes()));
		maCommand.send(stream,client);
//		
//		//testdaten zu byte[] 
//		type = new NullableType(Type.Type);
//		System.out.println(type.getTypeName());
//		stream = maCommand.send(new DynamicObj(type, new NullableType(new ArrayType(new NullableType(Type.Integer)))));
//
//		//testdaten senden
////		System.out.println(bytesToString(stream.getBytes()));
//		maCommand.send(stream,client);
		

		
		long timeICount = 0;
		long stampICount = System.currentTimeMillis();
		
		for (int i=1;i<=1;i++) {
			client.reconnect();
			timeICount += System.currentTimeMillis() - stampICount;
			sleep(20);
			stampICount = System.currentTimeMillis();
			stream = iCountCommand.send(i*4-3);
			iCountCommand.send(stream, client);
			stream = iCountCommand.send(i*4-2);
			iCountCommand.send(stream, client);
			stream = iCountCommand.send(i*4-1);
			iCountCommand.send(stream, client);
			stream = iCountCommand.send(i*4);
			iCountCommand.send(stream, client);
		}
		timeICount += System.currentTimeMillis() - stampICount;
		final long finalTimeICount = timeICount;
		
		long stamp2ICount = System.currentTimeMillis();
		
//		asyncHelper.runAsync(new Runnable() {
//			public void run() {
//				for (int i=501;i<=1000;i++) {
//					iCount2Command.send(i, client);
//				}
//			}
//		}, "Async Test1");
//		
//
//		asyncHelper.runAsync(new Runnable() {
//			public void run() {
//				for (int i=1001;i<=1500;i++) {
//					iCount2Command.send(i, client);
//				}
//			}
//		}, "Async Test1");
//		
//
//		asyncHelper.runAsync(new Runnable() {
//			public void run() {
//				for (int i=1501;i<=2000;i++) {
//					iCount2Command.send(i, client);
//				}
//			}
//		}, "Async Test3");
//		
//		for (int i=1;i<=500;i++) {
//			iCount2Command.send(i, client);
//		}
		final long finalTime2ICount = System.currentTimeMillis() - stamp2ICount;
		sleep(300);
		//Server & Client stoppen
		Thread t = new Thread() {
			public void run() {
				
				try {
					Main.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(iCount2Command.toString());
				System.out.println("Needed " + finalTime2ICount + "ms for sending all iCount2 Commands.");
				System.out.println(iCountCommand.toString());
				System.out.println("Needed " + finalTimeICount + "ms for sending all iCount Commands.");
				
				Set<Thread> threads = new HashSet<>(asyncHelper.getActiveThreads());
				System.out.println("There are " + threads.size() + " threads active:");
				for (Thread thread:threads) {
					System.out.println(thread.getName() + " {id=" + thread.getId() + "}");
				}
//				System.out.println("Client: There are " + socketFactoryClient.getRegisteredInputStreams().size() + " inputstreams still registered!");
//				System.out.println("Server: There are " + socketFactoryServer.getRegisteredInputStreams().size() + " inputstreams still registered!");
				
				long time = System.currentTimeMillis() - startTime;
				System.out.println("Needed " + time + "ms. Test waited for " + sleepTime + "ms and worked for " + (time-sleepTime) + "ms.");
				if (threads.size()==0) {
					System.out.println("Shutting down jvm!");
					System.exit(0);
				}

			}
		};
		t.start();
		server.stop();
		client.stop();
		System.out.println("Main Thread finsihed.");
		
	}
	
	private static void sleep(int t) throws InterruptedException {
		final long startTime = System.currentTimeMillis();
		Thread.sleep(t);
		sleepTime += System.currentTimeMillis()-startTime;
	}
	
	public static String bytesToString(byte[] stream) {
		StringBuilder sb = new StringBuilder();
		for (byte b:stream) {
			sb.append(b);
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public static Map<Thread, Integer> trap = new HashMap<>();
	public static String getTrap() {
		int t = trap.get(Thread.currentThread());
		StringBuilder sb = new StringBuilder();
		for (int i = 0;i<t;i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public static void changeTrap(int i) {
		if (!trap.containsKey(Thread.currentThread())) {
			trap.put(Thread.currentThread(), i);
		} else {
			trap.put(Thread.currentThread(), trap.get(Thread.currentThread())+i);
		}
			
	}
			
	
	
	
}
