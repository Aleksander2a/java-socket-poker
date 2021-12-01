package poker.socket.java.server;

import poker.socket.java.model.*;

import java.util.LinkedHashMap;

public class Protocol {

    private static  final String STATE_STRING = "State";

    public enum State {
        ENTERED, NEW_GAME, JOIN_OR_CREATE_GAME, WAITING_FOR_PLAYERS, IN_GAME
    }

    private State state = State.ENTERED;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String processInput(String theInput) {
        StringBuilder theOutput = new StringBuilder();
        LinkedHashMap<String, String> encodedFromClient;
        LinkedHashMap<String, String> check;

        switch (state) {
            case ENTERED:
                Player client = new Player(Server.getNextPlayerId());
                Server.increaseNextPlayerId();
                Server.getClients().add(client);
                state = State.JOIN_OR_CREATE_GAME;
                theOutput = new StringBuilder("State:" + state + "-");
                theOutput.append("PlayerID:").append(client.getId()).append("-");
                theOutput.append("GameNumber:").append(Server.getGames().size()).append("-");
                for(Game g : Server.getGames()) {
                    theOutput.append("Game").append(g.getId()).append(":").append(g.getMaxPlayersNumber()).append("-");
                }
                break;
            case JOIN_OR_CREATE_GAME:
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = new StringBuilder(ServerMessageHandler.answerToMessage(encodedFromClient));
                check = ServerMessageHandler.encode(theOutput.toString());
                if(check.get(STATE_STRING).equals("NEW_GAME")) {
                    state = State.NEW_GAME;
                }
                else if(check.get(STATE_STRING).equals("IN_GAME")) {
                    state = State.IN_GAME;
                }
                else if(check.get(STATE_STRING).equals("WAITING_FOR_PLAYERS")) {
                    state = State.WAITING_FOR_PLAYERS;
                }
                break;
            case NEW_GAME:
                // encode theInput
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = new StringBuilder(ServerMessageHandler.answerToMessage(encodedFromClient));
                state = State.WAITING_FOR_PLAYERS;
                break;
            case WAITING_FOR_PLAYERS:
                // uncode theInput
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = new StringBuilder(ServerMessageHandler.answerToMessage(encodedFromClient));
                check = ServerMessageHandler.encode(theOutput.toString());
                if(check.get(STATE_STRING).equals("IN_GAME")) {
                    state = State.IN_GAME;
                }
                break;
            case IN_GAME:
                encodedFromClient = ServerMessageHandler.encode(theInput);
                // react
                theOutput = new StringBuilder(ServerMessageHandler.answerToMessage(encodedFromClient));
                break;
            default:
                break;
        }


        return theOutput.toString();
    }
}
