package poker.socket.java;

import java.util.*;

public class ServerMessageHandler {
    static LinkedHashMap<String, String> encode(String input) {
        String[] elements = input.split("-");
        List<String> list = Arrays.asList(elements);
        // Create map for e.g. key=State:value=ENTERED etc.
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for(String element : list) {
            String[] pair = element.split(":");
            result.put(pair[0], pair[1]);
        }
        return result;
    }

    static String answerToMessage(LinkedHashMap<String, String> msg) {
        Scanner scanner = new Scanner(System.in);
        String answer = "";
        //answer += "State:" + msg.get("State") + "|";
        //answer += "ID:" + msg.get("ID") + "|";
        if(msg.get("State").equals("NEW_GAME")) {
            // Choose playerNumber
            Game game = new Game();
            game.setMaxPlayersNumber(Integer.parseInt(msg.get("Players")));
            game.setStartingMoney(Integer.parseInt(msg.get("StartingMoney")));
            game.setAnte(Integer.parseInt(msg.get("Ante")));
            game.setId(Server.nextGameId++);
            for(Player p : Server.clients) {
                if(p.getId() == Integer.parseInt(msg.get("PlayerID"))) {
                    game.addPlayer(p);
                }
            }
            Server.games.add(game);
            answer = "State:WAITING_FOR_PLAYERS-" + "PlayerID:" + msg.get("PlayerID")
                    + "-GameID:" + String.valueOf(game.getId()) + "-NrOfPlayers:" + String.valueOf(game.getPlayersNumber())
                    + "-MaxPlayers:" + String.valueOf(game.getMaxPlayersNumber());
        }
        if(msg.get("State").equals("WAITING_FOR_PLAYERS")) {
            Game game=null;
            for (Game g : Server.games) {
                if(g.getId() == Integer.parseInt(msg.get("GameID"))) {
                    game = g;
                }
            }
            if(game.getPlayersNumber()==game.getMaxPlayersNumber()) {
                answer = "State:IN_GAME-" + "PlayerID:" + msg.get("PlayerID") + "-"
                        + "GameID:" + msg.get("GameID");
            }
            else {
                answer = "State:WAITING_FOR_PLAYERS-" + "PlayerID:" + msg.get("PlayerID") + "-"
                        + "GameID:" + msg.get("GameID") + "-NrOfPlayers:" + String.valueOf(game.getPlayersNumber()) +
                        "-MaxPlayers:" + String.valueOf(game.getMaxPlayersNumber());
            }

        }
        return answer;
    }
}
