package com.dns;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(App.class.getName());
        try {
            if (args.length > 0 && args[0].equals("att")) {
                RefreshDB.refresh();
            }
            try (DatagramSocket socket = new DatagramSocket(53);) {
                socket.setSoTimeout(0);
                byte[] message = new byte[512];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                do {
                    socket.receive(packet);
                    long start = System.nanoTime();
                    ClientRequestHandle clientRequestHandle = new ClientRequestHandle();
                    clientRequestHandle.run(message, packet.getSocketAddress(), socket);
                    long end = System.nanoTime();
                    logger.log(Level.INFO, () -> Long.toString(TimeUnit.NANOSECONDS.toMillis(end - start)));
                } while (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
