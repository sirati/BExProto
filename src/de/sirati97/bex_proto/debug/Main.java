package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetClient;
import de.sirati97.bex_proto.network.NetServer;
import de.sirati97.bex_proto.network.adv.AdvClient;
import de.sirati97.bex_proto.network.adv.AdvServer;


public class Main {

	public static void main(String[] args) throws InterruptedException {
		//Auswerter der daten instanzieren
		TestCommand command = new TestCommand();
		// Um neue Threads zu erstellen. Was ja auf bungee nicht direkt geht, deswegen diese klasse
		AsyncHelper asyncHelper = new AsyncHelperImpl();
		//Server & Client instanzieren
		NetServer server = new AdvServer(asyncHelper, 10000, command);
		NetClient client = new AdvClient(asyncHelper, "127.0.0.1", 10000, "TheSuperAwesomeClient", true, command);
		//Server & Client starten (server zuerst, weil sonst der client keine connection bekommen kann)
		server.start();
		client.start();
		//testdaten zu byte[] 
		Stream stream = command.send("ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", 1L, 2, (short)3, (byte)4, 3.5, new int[][]{{9},{8, 1000000000},{7}});
		//testdaten senden
//		System.out.println(bytesToString(stream.getBytes()));
		command.send(stream,client);
		
		//Server & Client stoppen
		server.stop();
		client.stop();
		Thread.sleep(100);
		System.exit(0);
		
		
		
		
//		//Auswerter der daten instanzieren
//		TestCommand command = new TestCommand();
//		StreamReader streamReader = new StreamReader(command);
//		// Um neue Threads zu erstellen. Was ja auf bungee nicht direkt geht, deswegen diese klasse
//		AsyncHelper asyncHelper = new AsyncHelperImpl();
//		//Server & Client instanzieren
//		NetServer server = new NetServer(asyncHelper, 10000, streamReader);
//		NetClient client = new NetClient(asyncHelper, "127.0.0.1", 10000, streamReader);
//		//Server & Client starten (server zuerst, weil sonst der client keine connection bekommen kann)
//		server.start();
//		client.start();
//		//testdaten zu byte[] 
//		byte[] stream = new SendStream(command.send("ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", 1L, 2, (short)3, (byte)4, 3.5, new int[][]{{9},{8, 1000000000},{7}})).getBytes();
//		//testdaten senden
//		client.send(stream);
//		//Server & Client stoppen
//		server.stop();
//		client.stop();
//		Thread.sleep(100);
//		System.exit(0);
		
		
////		MACommand command = new MACommand();
//		TestCommand command = new TestCommand();
//		
//		byte[] stream = new SendStream(command.send("ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", 1L, 2, (short)3, (byte)4, 3.5, new int[][]{{9},{8, 1000000000},{7}})).getBytes();
////		byte[] stream = new SendStream(command.send(new byte[][]{{9},{8, 8},{7}}, null, null, null, null, null, null, null, null, null)).getBytes();
//		StringBuilder sb = new StringBuilder();
//		for (byte b:stream) {
//			sb.append(b);
//			sb.append(' ');
//		}
//		System.out.println(sb.toString());
//		try {
//			System.out.write(stream);
//			System.out.println();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		StreamReader reader = new StreamReader(command);
//		reader.read(stream);
		
	}
	
	public static String bytesToString(byte[] stream) {
		StringBuilder sb = new StringBuilder();
		for (byte b:stream) {
			sb.append(b);
			sb.append(' ');
		}
		return sb.toString();
	}
	
	static class AsyncHelperImpl implements AsyncHelper{

		@Override
		public AsyncTaskImpl runAsync(Runnable runnable) {
			Thread thread = new Thread(runnable);
			thread.start();
			return new AsyncTaskImpl(thread);
		}
		
		static class AsyncTaskImpl implements AsyncTask {
			private Thread thread;

			public AsyncTaskImpl(Thread thread) {
				this.thread = thread;
			}
			
			@Override
			public void stop() {
				thread.interrupt();
				
			}
			
		}
	}

	
	
	
}
