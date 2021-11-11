package poker.socket.java;

import java.io.*;
import java.net.*;

public class MultiServer {
    public static void main(String[] args) throws IOException
    {
        InetAddress host = InetAddress.getLocalHost();
        String hostName = host.getHostName();
        System.out.println( "MultiServer " + hostName + " welcomes You!" );

        if (args.length != 1) {
            System.err.println("Usage: java KKMultiServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new MultiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
