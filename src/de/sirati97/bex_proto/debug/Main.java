package de.sirati97.bex_proto.debug;

import de.sirati97.bex_proto.ArrayType;
import de.sirati97.bex_proto.DynamicObj;
import de.sirati97.bex_proto.NullableType;
import de.sirati97.bex_proto.Stream;
import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.TypeBase;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetServer;
import de.sirati97.bex_proto.network.ThreadAsyncHelper;
import de.sirati97.bex_proto.network.adv.AdvClient;
import de.sirati97.bex_proto.network.adv.AdvServer;


public class Main {

	public static void main(String[] args) throws InterruptedException {
		//Auswerter der daten instanzieren
		MACommand command = new MACommand();
		// Um neue Threads zu erstellen. Was ja auf bungee nicht direkt geht, deswegen diese klasse
		AsyncHelper asyncHelper = new ThreadAsyncHelper();
		//Server & Client instanzieren
		NetServer server = new AdvServer(asyncHelper, 10000, command);
		AdvClient client = new AdvClient(asyncHelper, "127.0.0.1", 10000, "TheSuperAwesomeClient", true, command);
//		AdvClient client2 = new AdvClient(asyncHelper, "127.0.0.1", 10000, "TheSuperAwesomeClient", true, command);
		
		//Server & Client starten (server zuerst, weil sonst der client keine connection bekommen kann)
		server.start();
		client.start();
//		client2.start();
		
		//testdaten zu byte[] 
//		Stream stream = command.send("ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", "ABCabcÄÖÜäöü^°123óò", 1L, 2, (short)3, (byte)4, 3.5, new int[][]{{9},{8, 1000000000},{7}});
		TypeBase type = new NullableType(new ArrayType(new NullableType(Type.Integer)));
		System.out.println(type.getTypeName());
		Stream stream = command.send(new DynamicObj(type, new Integer[]{1231, null, 0 ,1, Integer.MAX_VALUE, null, 0, Integer.MIN_VALUE}));
		
		//testdaten senden
		System.out.println(bytesToString(stream.getBytes()));
		command.send(stream,client);
		
		Thread.sleep(100);
		//Server & Client stoppen
		server.stop();
		client.stop();
		Thread.sleep(100);
		//System.exit(0);
		
		
		
		
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
	
	

	
	
	
}
