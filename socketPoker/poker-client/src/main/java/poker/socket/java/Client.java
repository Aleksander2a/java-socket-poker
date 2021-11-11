package poker.socket.java;

import java.io.*;
import java.net.*;

/**
 * Hello world!
 *
 */
public class Client
{
    public static void main( String[] args ) throws IOException
    {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
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

//        System.out.println( "hello world!" );
//        System.out.println( "Szablon projektu z metodą main i zależnościami wbudowanymi w wykonywalny jar" );
//        //wywołanie metody generującej hash SHA-512
//        System.out.println("HASH 512 dla słowa test: " + TextUtils.sha512Hash("test"));
//        Deck deck = new Deck();
//        deck.printDeck();
//        System.out.println(deck.getDeckSize());
//        //System.out.println(Card.Rank.KING.compareTo(Card.Rank.ACE));
//        Card randomCard = deck.dealCard();
//        System.out.println("--------");
//        System.out.println(randomCard.toString());
//        System.out.println(deck.getDeckSize());
//        System.out.println(deck.hasCard(randomCard));
//        deck.addCard(randomCard);
//        System.out.println(deck.getDeckSize());
//        System.out.println(deck.hasCard(randomCard));
    }
}
