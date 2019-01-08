package server;

public class MainServer {

    public static void main(String[] args) {
        System.out.println("========== SERVER ===========");
        System.out.println("---- Available UDP ports ----");
        for (int i = 0; i < args.length; ++i) {
            System.out.println("Port UDP: "+args[i]);
            PortListener listener = new PortListener(Integer.parseInt(args[i]));
            new Thread(listener).start();
        }
        System.out.println("-----------------------------");
    }
}
