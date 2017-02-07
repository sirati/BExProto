package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.threading.AsyncHelper;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

public class StreamReader {
	private IPacketDefinition packet;
	
	public StreamReader(IPacketDefinition packet) {
		this.packet = packet;
	}
	
	public byte[] read(byte[] bytes, BasicService sender, AsyncHelper asyncHelper, String name) {
		int location = 0;
		streamLoop: do {
			int streamLength = BExStatic.getInteger(bytes, location);
//            System.out.println("streamLength="+streamLength+", location="+location +", bytes.length="+bytes.length);
            location +=4;
			if (location +streamLength > bytes.length) {
                location -=4;
				byte[] overflow = new byte[bytes.length-location];
				System.arraycopy(bytes, location, overflow, 0, bytes.length-location);
//				System.out.println("Overflow: "+ Main.bytesToString(overflow));
				return overflow;
			}
//            System.out.println("no overflow");
            byte[] stream = new byte[streamLength];
			
			System.arraycopy(bytes, location, stream, 0, streamLength);
			location +=streamLength;
			stream = sender.getStreamModifiers().unapply(stream);
			final CursorByteBuffer buf = new CursorByteBuffer(stream, sender);
			execute(buf, asyncHelper, name);
			
		} while (location < bytes.length);
		return null;
	}
	
	public void execute(final CursorByteBuffer buf, AsyncHelper asyncHelper, String name) {
		asyncHelper.runAsync(new Runnable() {
			public void run() {
				packet.extract(buf);
			}
		}, name);
	}
	
	public IPacketDefinition getPacket() {
		return packet;
	}
	
}
