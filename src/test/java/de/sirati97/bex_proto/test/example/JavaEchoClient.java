package de.sirati97.bex_proto.test.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by sirati97 on 07.02.2017 for BexProto.
 */
public class JavaEchoClient {
    public static void main(String... args) throws Throwable {
        JavaEchoClient client = new JavaEchoClient();
        client.start();
    }

    private Socket socket;
    private boolean started = false;

    public void start() throws Throwable {
        if (started) {
            throw new IllegalStateException("Client is already running");
        }
        started = true;
        socket = new Socket(InetAddress.getLocalHost(), 12312);
        Thread socketReaderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (started) {
                    if (!read() && started) {
                        throw new IllegalStateException("Reading resulted in exception");
                    }
                }
            }
        });
        socketReaderThread.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {
            System.out.print("Input: ");
            input = in.readLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            sendMessage(input);
        }
        started = false;
        socket.close();
        socket.getInputStream().close();
        socketReaderThread.interrupt();
        Thread.sleep(100);
        socketReaderThread.stop();
    }



    private boolean read() {
        try {
            long result;
            byte[] header = new byte[4];
            result = socket.getInputStream().read(header);
            if (result < 4) {
                return false;
            }
            result = socket.getInputStream().skip(4);
            if (result < 4) {
                return false;
            }
            int packetLength = ByteBuffer.wrap(header).getInt()-4; //message is always 4 bytes shorter than packet
            byte[] packet = new byte[packetLength];
            result = socket.getInputStream().read(packet);
            if (result < packetLength) {
                return false;
            }
            String message = new String(packet, StandardCharsets.UTF_8);
            receivedMessage(message);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private void receivedMessage(String in) {
        System.out.println("Received message from server: " + in);
    }


    private void sendMessage(String in) throws Throwable {
        byte[] packet = in.getBytes(StandardCharsets.UTF_8);
        socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(packet.length+4).array()); //packet length
        socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(packet.length).array()); //message length
        socket.getOutputStream().write(packet);
        socket.getOutputStream().flush();
    }



}
