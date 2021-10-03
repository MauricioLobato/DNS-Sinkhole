package com.dns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

public class NonBlockedResponse {

    public void response(SocketAddress clientAddress, byte[] message, DatagramSocket socket) throws IOException {
        InetAddress dns = InetAddress.getByName("8.8.8.8");
        DatagramPacket reqPacket = new DatagramPacket(message, message.length, dns, 53);
        try (DatagramSocket reqSocket = new DatagramSocket();) {
            reqSocket.send(reqPacket);
            byte[] buffer = new byte[1024];
            DatagramPacket respPacket = new DatagramPacket(buffer, buffer.length);
            reqSocket.setSoTimeout(1000);
            reqSocket.receive(respPacket);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientAddress);
            socket.send(packet);
        }
    }
}
