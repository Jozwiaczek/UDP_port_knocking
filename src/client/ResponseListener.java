package client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ResponseListener implements Runnable {

    DatagramSocket datagramSocket;
    InetAddress sAdres;

    public ResponseListener (DatagramSocket datagramSocket, InetAddress sAdres) {
        this.datagramSocket = datagramSocket;
        this.sAdres=sAdres;
    }

    @Override
    public void run () {
        try{
            byte[] buffer = new byte[2048];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            datagramSocket.setSoTimeout(10000);
            datagramSocket.receive(response);
            String responseS = new String(response.getData(), 0, response.getLength());
            System.out.println("Obtained TCP port: " + responseS);

            Socket tcpSocket = new Socket(sAdres, Integer.parseInt(responseS), null, datagramSocket.getLocalPort());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            writer.write("ABCD");
            writer.newLine();
            writer.flush();
            String reqestResponse = reader.readLine();
            System.out.println("Response: "+reqestResponse);
            tcpSocket.close();


        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
