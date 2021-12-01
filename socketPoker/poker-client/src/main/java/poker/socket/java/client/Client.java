package poker.socket.java.client;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;

/**
 * The main class on the Client-side of the app.
 *
 */
public class Client
{

    private static final Logger LOGGER = Logger.getLogger( Client.class.getName() );

    public static void main( String[] args ) throws IOException
    {
        PropertyConfigurator.configure("D:\\Studia AGH\\Programowanie zaawansowane 1\\Zadanie-1\\socketPoker\\poker-client\\src\\main\\resources\\log4j.properties");

        if (args.length != 2) {
            LOGGER.info("Usage: java -jar poker-client-1.0-jar-with-dependencies <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        ) {
            String fromServer;
            String fromClient;
            LinkedHashMap<String, String> encodedFromServer;

            while ((fromServer = in.readLine()) != null) {
                // encode fromServer
                // print message from server
                encodedFromServer = ClientMessageHandler.encode(fromServer);
                // react
                fromClient = ClientMessageHandler.answerToMessage(encodedFromServer);
                if (fromClient.equals("Quit"))
                    break;

                // get response from Client - OLD KK VERSION
                // print message from client
                out.println(fromClient);
            }
        } catch (UnknownHostException e) {
            LOGGER.info("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.info("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }


    }
}
