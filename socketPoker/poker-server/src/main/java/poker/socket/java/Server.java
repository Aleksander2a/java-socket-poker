package poker.socket.java;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static int nextPlayerId = 0;
    public static int nextGameId = 0;
    public static ArrayList<Game> games = new ArrayList<>();
    public static ArrayList<Game> players = new ArrayList<>();

    public static void main(String[] args) throws IOException
    {
        InetAddress host = InetAddress.getLocalHost();
        String hostName = host.getHostName();
        System.out.println( "Server " + hostName + " welcomes You!" );

        if (args.length != 1) {
            System.err.println("Usage: java -jar poker-server-1.0-jar-with-dependencies <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
