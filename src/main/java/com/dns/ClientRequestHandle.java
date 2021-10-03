package com.dns;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRequestHandle {

    public void run(byte[] message, SocketAddress clientAddress, DatagramSocket socket) {
        Logger logger = Logger.getLogger(ClientRequestHandle.class.getName());
        try {
            DataInputStream request = new DataInputStream(new ByteArrayInputStream(message));
            // Ignora o header do datagrama dns(primeiros 12 bytes), desnecessários para o
            // momento.
            for (int i = 0; i < 6; i++) {
                request.readShort();
            }
            StringBuilder domain = new StringBuilder();
            int len;
            // Processa os bytes após o header, responsável por armazenar
            // o domínio a ser resolvido.
            while ((len = request.readByte()) > 0) {
                byte[] partialDomain = new byte[len];
                for (int i = 0; i < len; i++) {
                    partialDomain[i] = request.readByte();
                }
                domain.append(new String(partialDomain, StandardCharsets.UTF_8) + ".");
            }
            String domainString = domain.substring(0, domain.length() - 1);
            // Verifica se o domínio em questão está presente no banco de dados.
            boolean flag = new BlackListAnalyzer().analyzer(domainString);
            if (flag) {
                new BlockedResponse().response(clientAddress, message, socket);
                logger.log(Level.INFO, () -> "BLOQUEADO - >" + domainString);
            } else {
                new NonBlockedResponse().response(clientAddress, message, socket);
                logger.log(Level.INFO, () -> "PERMITIDO - >" + domainString);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
