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
        LinkedHashMap<String, String> check;

        switch (state) {
            case ENTERED:
                Player client = new Player(Server.nextPlayerId);
                Server.nextPlayerId++;
                Server.clients.add(client);
                //TODO: Add refresh option to see newly created games
                if(Server.games.isEmpty()) {
                    state = State.NEW_GAME;
                }
                else {
                    state = State.JOIN_OR_CREATE_GAME;
                }
                theOutput = "State:" + state + "-";
                theOutput += "PlayerID:" + client.getId() + "-";
                theOutput += "GameNumber:" + Server.games.size() + "-";
                if(state == State.JOIN_OR_CREATE_GAME) {
                    for(Game g : Server.games) {
                        theOutput += "Game" + g.getId() + ":" + g.getMaxPlayersNumber() + "-";
                    }
                }
                break;
            case JOIN_OR_CREATE_GAME:
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = ServerMessageHandler.answerToMessage(encodedFromClient);
                check = ServerMessageHandler.encode(theOutput);
                if(check.get("State").equals("NEW_GAME")) {
                    state = State.NEW_GAME;
                }
                else if(check.get("State").equals("IN_GAME")) {
                    state = State.IN_GAME;
                }
                else if(check.get("State").equals("WAITING_FOR_PLAYERS")) {
                    state = State.WAITING_FOR_PLAYERS;
                }
                break;
            //case JOIN_GAME:
                //break;
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
                check = ServerMessageHandler.encode(theOutput);
                if(check.get("State").equals("IN_GAME")) {
                    state = State.IN_GAME;
                }
                break;
            case IN_GAME:
                //System.out.println("Game started");
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = ServerMessageHandler.answerToMessage(encodedFromClient);
                //TODO
                //Game.Round gameRound =
                break;
        }


        return theOutput;
    }
}
