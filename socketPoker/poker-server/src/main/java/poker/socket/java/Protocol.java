package poker.socket.java;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Protocol {
    private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUMJOKES = 5;

    public enum State {
        ENTERED, JOINING_GAME, CREATING_GAME, SET_START_MONEY, SET_ANTE, JOIN_OR_CREATE_GAME, WAITING_FOR_GAME, IN_GAME
    }

    public int state = WAITING;
    private int currentJoke = 0;

    private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
    private String[] answers = { "Turnip the heat, it's cold in here!",
            "I didn't know you could yodel!",
            "Bless you!",
            "Is there an owl in here?",
            "Is there an echo in here?" };

    public String processInput(String theInput) {
        String theOutput = null;

//        switch (state) {
//            case ENTERED:
//                Player player = new Player(Server.nextPlayerId);
//                Server.nextPlayerId++;
//                theOutput = "Welcome! Your ID is: " + String.valueOf(player.getId()) + ". ";
//                if(Server.games.isEmpty()) {
//                    theOutput += "There are no games right now. How many players do You want for next game? (2-4)";
//                    state = State.CREATING_GAME;
//                }
//                else {
//                    theOutput += "There are currently " + String.valueOf(Server.games.size()) + "games. Do you want to join game or create a game? (join/create)";
//                    state = State.JOIN_OR_CREATE_GAME;
//                }
//                break;
//            case CREATING_GAME:
//                Game game = new Game();
//                game.setId(Server.nextGameId++);
//                game.setPlayersNumber(Integer.parseInt(theInput));
//                theOutput = "How much money do you want for start?";
//                state = State.SET_START_MONEY;
//                break;
//            case SET_START_MONEY:
//
//                break;
//        }

        if (state == WAITING) {
            theOutput = "Knock! Knock!";
            state = SENTKNOCKKNOCK;
        } else if (state == SENTKNOCKKNOCK) {
            if (theInput.equalsIgnoreCase("Who's there?")) {
                theOutput = clues[currentJoke];
                state = SENTCLUE;
            } else {
                theOutput = "You're supposed to say \"Who's there?\"! " +
                        "Try again. Knock! Knock!";
            }
        } else if (state == SENTCLUE) {
            if (theInput.equalsIgnoreCase(clues[currentJoke] + " who?")) {
                theOutput = answers[currentJoke] + " Want another? (y/n)";
                state = ANOTHER;
            } else {
                theOutput = "You're supposed to say \"" +
                        clues[currentJoke] +
                        " who?\"" +
                        "! Try again. Knock! Knock!";
                state = SENTKNOCKKNOCK;
            }
        } else if (state == ANOTHER) {
            if (theInput.equalsIgnoreCase("y")) {
                theOutput = "Knock! Knock!";
                if (currentJoke == (NUMJOKES - 1))
                    currentJoke = 0;
                else
                    currentJoke++;
                state = SENTKNOCKKNOCK;
            } else {
                theOutput = "Bye.";
                state = WAITING;
            }
        }
        return theOutput;
    }
}
