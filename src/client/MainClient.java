package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainClient {

    public static void main (String[] args) {
        System.out.println("========== CLIENT ===========");
        try {
            InetAddress serverAddress = InetAddress.getByName(args[0]);
            DatagramSocket socket = new DatagramSocket();

            new Thread(new ResponseListener(socket,serverAddress)).start();

            for ( int i = 1; i < args.length; ++i) {
                int port = Integer.parseInt(args[i]);
                String message = "test";
                message = "@"+message;
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
                socket.send(packet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
