package poker.socket.java;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Protocol {

    public enum State {
        ENTERED, NEW_GAME, JOIN_OR_CREATE_GAME, JOIN_GAME, CHOOSE_GAME, WAITING_FOR_PLAYERS, IN_GAME
    }

    public State state = State.ENTERED;


    public String processInput(String theInput) {
        String theOutput = null;
        LinkedHashMap<String, String> encodedFromClient;

        switch (state) {
            case ENTERED:
                Player player = new Player(Server.nextPlayerId);
                Server.nextPlayerId++;
                Server.clients.add(player);
                if(Server.games.isEmpty()) {
                    state = State.NEW_GAME;
                }
                else {
                    state = State.JOIN_OR_CREATE_GAME;
                }
                theOutput = "State:" + state + "-";
                theOutput += "PlayerID:" + player.getId() + "-";
                break;
            case NEW_GAME:
                // encode theInput
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = ServerMessageHandler.answerToMessage(encodedFromClient);
                //Game game = new Game();
                //game.setId(Server.nextGameId++);
                //game.setPlayersNumber(Integer.parseInt(theInput));
                //theOutput = "How much money do you want for start?";
                state = State.WAITING_FOR_PLAYERS;
                break;
            case WAITING_FOR_PLAYERS:
                // uncode theInput
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = ServerMessageHandler.answerToMessage(encodedFromClient);
                LinkedHashMap<String, String> check = ServerMessageHandler.encode(theOutput);
                if(check.get("State").equals("IN_GAME")) {
                    state = State.IN_GAME;
                }
                break;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        if (state == WAITING) {
//            theOutput = "Knock! Knock!";
//            state = SENTKNOCKKNOCK;
//        } else if (state == SENTKNOCKKNOCK) {
//            if (theInput.equalsIgnoreCase("Who's there?")) {
//                theOutput = clues[currentJoke];
//                state = SENTCLUE;
//            } else {
//                theOutput = "You're supposed to say \"Who's there?\"! " +
//                        "Try again. Knock! Knock!";
//            }
//        } else if (state == SENTCLUE) {
//            if (theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
//                theOutput = answers[currentJoke] + " Want another? (y/n)";
//                state = ANOTHER;
//            } else {
//                theOutput = "You're supposed to say \"" +
//                        clues[currentJoke] +
//                        " who?\"" +
//                        "! Try again. Knock! Knock!";
//                state = SENTKNOCKKNOCK;
//            }
//        } else if (state == ANOTHER) {
//            if (theInput.equalsIgnoreCase("y")) {
//                theOutput = "Knock! Knock!";
//                if (currentJoke == (NUMJOKES - 1))
//                    currentJoke = 0;
//                else
//                    currentJoke++;
//                state = SENTKNOCKKNOCK;
//            } else {
//                theOutput = "Bye.";
//                state = WAITING;
//            }
//        }
        return theOutput;
    }
}
