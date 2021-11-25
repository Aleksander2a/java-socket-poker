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
        String playerHand = "";
        String gameRound= "";
        int playerTurnId = -1;
        int money = 0;
        int maxBid = 0;
        int myBid = 0;
        Player.Action action = Player.Action.NONE;
        //answer += "State:" + msg.get("State") + "|";
        //answer += "ID:" + msg.get("ID") + "|";
        if(msg.get("State").equals("JOIN_OR_CREATE_GAME")) {
            if(msg.get("Decision").equals("join")) {
                Game game=null;
                for (Game g : Server.games) {
                    if(g.getId() == Integer.parseInt(msg.get("Joins"))) {
                        game = g;
                    }
                }
                for(Player p : Server.clients) {
                    if(p.getId() == Integer.parseInt(msg.get("PlayerID"))) {
                        game.addPlayer(p);
                        p.setCurrentGameId(game.getId());
                    }
                }
                if(game.getPlayersNumber() == game.getMaxPlayersNumber()) {
                    //TODO: add info about game when IN_GAME
                    game.initializeGame();
                    gameRound = String.valueOf(game.getRound());
                    playerTurnId = game.getPlayerTurn().getId();
                    maxBid = game.getMaxBid();
                    for(Player p : game.gamePlayers()) {
                        if(p.getId()==Integer.parseInt(msg.get("PlayerID"))) {
                            playerHand = p.handToString();
                            money = p.getMoney();
                            myBid = p.getBid();
                            action = p.getAction();
                        }
                    }
                    answer += "State:IN_GAME-" + "PlayerID:" + msg.get("PlayerID") + "-GameID:" + game.getId()
                            + "-GameRound:" + gameRound + "-Turn:" + playerTurnId + "-MyMoney:" + money + "-MaxBid:"
                            + maxBid + "-MyBid:" + myBid + "-MyAction:" + action + "-GameInfo:" + game.gameInfo() + "-" + playerHand;
                }
                else {
                    answer = "State:WAITING_FOR_PLAYERS-" + "PlayerID:" + msg.get("PlayerID") + "-"
                            + "GameID:" + game.getId() + "-NrOfPlayers:" + String.valueOf(game.getPlayersNumber()) +
                            "-MaxPlayers:" + String.valueOf(game.getMaxPlayersNumber());
                }
            }
            else if(msg.get("Decision").equals("new")) {
                answer += "State:NEW_GAME-" + "PlayerID:" + msg.get("PlayerID") + "-";
            }
        }
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
                    p.setCurrentGameId(game.getId());
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
                //TODO: add info about game when IN_GAME
                game.initializeGame();
                gameRound = String.valueOf(game.getRound());
                playerTurnId = game.getPlayerTurn().getId();
                maxBid = game.getMaxBid();
                for(Player p : game.gamePlayers()) {
                    if(p.getId()==Integer.parseInt(msg.get("PlayerID"))) {
                        playerHand = p.handToString();
                        money = p.getMoney();
                        myBid = p.getBid();
                        action = p.getAction();
                    }
                }
                answer = "State:IN_GAME-" + "PlayerID:" + msg.get("PlayerID")
                        + "-GameID:" + msg.get("GameID") + "-GameRound:" + gameRound + "-Turn:" + playerTurnId + "-MyMoney:" + money + "-MaxBid:"
                        + maxBid + "-MyBid:" + myBid + "-MyAction:" + action + "-GameInfo:" + game.gameInfo()
                        + "-" + playerHand;
            }
            else {
                answer = "State:WAITING_FOR_PLAYERS-" + "PlayerID:" + msg.get("PlayerID") + "-"
                        + "GameID:" + msg.get("GameID") + "-NrOfPlayers:" + String.valueOf(game.getPlayersNumber()) +
                        "-MaxPlayers:" + String.valueOf(game.getMaxPlayersNumber());
            }
        }
        if(msg.get("State").equals("IN_GAME")) {
            //TODO
            Game game=null;
            Player player = null;
            for (Game g : Server.games) {
                if (g.getId() == Integer.parseInt(msg.get("GameID"))) {
                    game = g;
                }
            }
            for (Player p : game.gamePlayers()) {
                if (p.getId() == Integer.parseInt(msg.get("PlayerID"))) {
                    player = p;
                }
            }
            gameRound = String.valueOf(game.getRound());
            if(msg.get("Decision").equals("Refresh")) {
                answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                        + "-GameID:" + msg.get("GameID") + "-GameRound:" + gameRound + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                        + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                        + "-" + player.handToString();
            }
            else {
                switch (gameRound) {
                    case "FIRST_BETTING":
                        String playerDecision = msg.get("Decision");
                        switch (playerDecision){
//                            case "Check":
//                                break;
                            case "Fold":
                                player.setAction(Player.Action.FOLD);
                                game.playerFolds(player);
                                // TODO: Proceed game status
                                break;
                            case "Bid":
                                int playerBid = Integer.parseInt(msg.get("Bid"));
                                player.setBid(playerBid);
                                player.updateMoney(-playerBid);
                                player.updateInPot(playerBid);
                                player.setAction(Player.Action.BID);
                                game.setMaxBid(Math.max(playerBid, game.getMaxBid()));
                                //TODO: Proceed game status
                                break;
                        }
                        game.nextTurn();
                        gameRound = String.valueOf(game.getRound());
                        answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                + "-GameID:" + msg.get("GameID") + "-GameRound:" + gameRound + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                                + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                                + "-" + player.handToString();
                        break;
                    case "CHANGE_CARDS":
                        break;
                    case "SECOND_BETTING":
                        break;
                    case "COMPARING_CARDS":
                        break;
                }
            }
        }
        return answer;
    }
}
