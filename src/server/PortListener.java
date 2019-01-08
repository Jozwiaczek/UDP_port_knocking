package server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PortListener implements Runnable {

    private int port;
    private static Set<ClientConnection> processedTCP = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public PortListener (int port) {
        this.port=port;
    }

    @Override
    public void run ( ) {
        try ( DatagramSocket socket = new DatagramSocket(port)) {
            while (true) {
                byte data[] = new byte[2048];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("On port: " + port + "  |  Get message: " + message);

                ClientConnection clientConnection = new ClientConnection(packet.getAddress(), packet.getPort());
                if (!message.startsWith("@"))
                    continue;

                if(!processedTCP.add(clientConnection))
                    continue;

                ServerSocket serverSocket = new ServerSocket(0);
                Integer localPort = serverSocket.getLocalPort();
                byte[] tabLocalPort = localPort.toString().getBytes();

                socket.send(new DatagramPacket(tabLocalPort, tabLocalPort.length, packet.getAddress(), packet.getPort()));
                serverSocket.setSoTimeout(1000);

                Socket socket1;
                ClientConnection tcpClientConnection;
                do {
                    socket1 = serverSocket.accept();
                    tcpClientConnection = new ClientConnection(socket1.getInetAddress(), socket1.getPort());
                } while (!tcpClientConnection.equals(clientConnection));


                BufferedReader reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
                String receivedMessage = reader.readLine().toUpperCase();
                System.out.println("\nRecieived: " + receivedMessage);

                int alfaLength = 'Z'-'A'+1;
                int kluczCezara = 5%(alfaLength);
                char[] tab = receivedMessage.toCharArray();
                for (int i = 0; i<tab.length; i++){
                    tab[i] += kluczCezara;
                    if (tab[i] > 'Z')
                        tab[i] -=alfaLength;
                    if (tab[i] < 'A')
                        tab[i] +=alfaLength;
                }

                String hashMessage = String.copyValueOf(tab);

                writer.write(hashMessage+"\n");
                writer.flush();

                processedTCP.remove(clientConnection);
                System.out.println("-----------------------------");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
