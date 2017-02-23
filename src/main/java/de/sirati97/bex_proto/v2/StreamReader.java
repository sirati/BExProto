package de.sirati97.bex_proto.v2;

import de.sirati97.bex_proto.datahandler.BExStatic;
import de.sirati97.bex_proto.threading.IAsyncHelper;
import de.sirati97.bex_proto.util.CursorByteBuffer;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

public class StreamReader {
	private IPacketDefinition packet;
	
	public StreamReader(IPacketDefinition packet) {
		this.packet = packet;
	}
	
	public byte[] read(StreamChannel channel, byte[] bytes, BasicService sender, IAsyncHelper asyncHelper, String name) {
	    synchronized (channel) {
            int location = 0;
            do {
                int streamLength = -1;
                if (bytes.length-location>4) {
                    streamLength = BExStatic.getInteger(bytes, location);
                }

                location +=4;
                if (streamLength==-1 || (location +streamLength > bytes.length)) {
                    location -=4;
                    byte[] overflow = new byte[bytes.length-location];
                    System.arraycopy(bytes, location, overflow, 0, bytes.length-location);
                    return overflow;
                }

                byte[] stream = new byte[streamLength];

                System.arraycopy(bytes, location, stream, 0, streamLength);
                location +=streamLength;
                stream = sender.getStreamModifiers().unapply(stream);
                final CursorByteBuffer buf = new CursorByteBuffer(stream, sender);
                execute(buf, asyncHelper, name);

            } while (location < bytes.length);
            return null;
        }
	}
	
	public void execute(final CursorByteBuffer buf, IAsyncHelper asyncHelper, String name) {
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
