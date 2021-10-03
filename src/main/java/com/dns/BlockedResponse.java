package com.dns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class BlockedResponse {
    public void response(SocketAddress clientAddress, byte[] message, DatagramSocket socket) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        // Repassa os dois primeiros bytes do datagrama dns da requisição para o
        // datagrama dns da resposta. Precisam ser iguais.
        dos.write(message[0]);
        dos.write(message[1]);
        // O primeiro bit deste terceiro byte precisa ser 1, indicando uma resposta de
        // requisição. O resto deste terceiro bit mais o próximo(quarto) bit são
        // irrelevantes para o momento.
        dos.writeShort(0xFFFF);
        // Especifica o número de domínios resolvidos presentes neste datagrama. Para
        // este caso, o valor sempre será 1.
        dos.writeShort(0x0001);
        // Especifíca o número de "resoruce records" presente neste datagrma. Para este
        // caso, o valor sempre será 1.
        dos.writeShort(0x0001);
        // Próximos 4 bytes são irrelevantes para o momento.
        dos.writeShort(0x0000);
        dos.writeShort(0x0000);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(message));
        // Ignora os 12 primeiros bytes do datagrama dns de requisição.
        for (int i = 0; i < 6; i++) {
            dis.readShort();
        }
        int len;
        StringBuilder name = new StringBuilder();
        // Processa os próximos bytes, que armazenam o domínio a ser resolvido(Tamanho
        // em bytes variável).
        while ((len = dis.readByte()) > 0) {
            name.append(len);
            for (int i = 0; i < len; i++) {
                name.append(dis.readByte());
            }
        }
        // Insere valor zero no final da String de domínio, requisitado para indicar o
        // final de um domínio em um datagrama dns.
        name.append(0);
        // Copia o domínio enviado na requisição dns para para o datagrama de respostaa.
        dos.writeChars(name.toString());
        // Copia os próximos 4 bytes do datagrama de requisição para o datagrama de
        // resposta.
        dos.writeShort(dis.readShort());
        dos.writeShort(dis.readShort());
        // Copia novamente a string de domínio para o datagrama de requisição(
        // necessário de acordo com as especificações de formatação de datagrama dns).
        dos.writeChars(name.toString());
        // Próximos dois bits representam o tipo de "recurso" presente na resposta.
        // Nesse caso, um enderço ip de 32 bits. Escreve-se o valor 1.
        dos.writeShort(1);
        // Próximos dois bits representam a classe do recurso. Nesse caso,
        // "IN"(Internet). Escreve-se o valor 1.
        dos.writeShort(1);
        // Próximos 4 bytes irrelevantes para o momento
        dos.writeInt(0);
        // Tamanho do recurso, no caso, tamnho de um endereço ipv4.
        dos.writeShort("0.0.0.0".length());
        // O próprio recurso, a resposta da requisição da resolução de nomes enviada pelo
        // cliente. Como a requisição foi bloqueada, envia-se um endereço inválido.
        dos.writeChars("0.0.0.0");
        byte[] dnsFrame = baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(dnsFrame, dnsFrame.length, clientAddress);
        socket.send(packet);
    }
}
