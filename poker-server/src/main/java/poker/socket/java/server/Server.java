package poker.socket.java.server;

import poker.socket.java.model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server class with main() function.
 * Contains all the games and players connected.
 */
public class Server {
    private static int nextPlayerId = 0;
    private static int nextGameId = 0;
    private static List<Game> games = new ArrayList<>();
    private static List<Player> clients = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger( Server.class.getName() );

    public static List<Player> getClients() {
        return clients;
    }

    public static List<Game> getGames() {
        return games;
    }

    public static int getNextGameId() {
        return nextGameId;
    }

    public static int getNextPlayerId() {
        return nextPlayerId;
    }

    public static void increaseNextPlayerId() {
        nextPlayerId++;
    }

    public static void increaseNextGameId() {
        nextGameId++;
    }

    /**
     * Opens connection and begins to wait for clients to join
     * @param args PortNumber
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        InetAddress host = InetAddress.getLocalHost();
        String hostName = host.getHostName();
        LOGGER.log( Level.INFO,"Welcome to server: {0}", hostName);

        if (args.length != 1) {
            LOGGER.info("Usage: java -jar poker-server-1.0-jar-with-dependencies <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            LOGGER.info("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
