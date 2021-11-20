package poker.socket.java;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The main class on the Client-side of the app.
 *
 */
public class Client
{
    public static void main( String[] args ) throws IOException
    {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java -jar poker-client-1.0-jar-with-dependencies <host name> <port number>");
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
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromClient;
            LinkedHashMap<String, String> encodedFromServer;

            while ((fromServer = in.readLine()) != null) {
                // uncode fromServer
                System.out.println("Server: " + fromServer);
                encodedFromServer = ClientMessageHandler.encode(fromServer);
                // react
                fromClient = ClientMessageHandler.answerToMessage(encodedFromServer);
                if (fromServer.equals("Bye."))
                    break;

                // get response from Client - OLD KK VERSION
                //fromClient = stdIn.readLine();
                if (fromClient != null) {
                    System.out.println("Client: " + fromClient);
                    out.println(fromClient);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }


    }
}
