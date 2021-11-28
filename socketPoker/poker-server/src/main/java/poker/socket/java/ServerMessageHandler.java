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
        String infoIfAllFolded = "00";
        int playersToCompleteSet = 0;
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
                    game.setRound(Game.Round.FIRST_BETTING);
                    gameRound = String.valueOf(game.getRound());
                    playerTurnId = game.getPlayerTurn().getId();
                    maxBid = game.getMaxBid();
                    for(Player p : game.gamePlayers()) {
                        game.addActivePlayer(p);
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
                game.setRound(Game.Round.FIRST_BETTING);
                gameRound = String.valueOf(game.getRound());
                playerTurnId = game.getPlayerTurn().getId();
                maxBid = game.getMaxBid();
                for(Player p : game.gamePlayers()) {
                    game.addActivePlayer(p);
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
            if(game != null) {
                for (Player p : game.gamePlayers()) {
                    if (p.getId() == Integer.parseInt(msg.get("PlayerID"))) {
                        player = p;
                    }
                }
            }
            gameRound = String.valueOf(game.getRound());
            if(msg.get("Decision").equals("Refresh")) {
                answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                        + "-GameID:" + msg.get("GameID") + "-GameRound:" + gameRound + "-AllFolded:" + game.isAllFolded() + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                        + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                        + "-" + player.handToString();
                return answer;
            }
            else {
//                int countOfNone = 0;
//                for(Player p : game.activePlayers()) {
//                    if(p.getAction().equals(Player.Action.NONE)) {
//                        countOfNone++;
//                    }
//                }
//                if(countOfNone==game.activePlayers().size()) {
//                    game.setAllFolded("00");
//                }
                switch (gameRound) {
                    case "FIRST_BETTING":
                        String playerDecision = msg.get("Decision");
                        switch (playerDecision){
//                            case "Check":
//                                break;
                            case "Fold":
                                player.setAction(Player.Action.FOLD);
                                game.setAllFolded("00");
                                //game.playerFolds(player);
                                // TODO: Proceed game status
                                //game.proceedBettingRound();
                                break;
                            case "Bid":
                                int playerBid = Integer.parseInt(msg.get("Bid"));
                                int diff = player.getBid() - playerBid;
                                player.setBid(playerBid);
                                player.updateInPot(-diff);
                                player.updateMoney(diff);
                                player.setAction(Player.Action.BID);
                                game.setMaxBid(Math.max(playerBid, game.getMaxBid()));
                                game.updatePot();
                                game.setAllFolded("00");
                                //TODO: Proceed game status
                                //game.proceedBettingRound();
                                break;
                        }
                        //game.nextTurn();
                        // TODO: Proceed game status
                        game.proceedBettingRound();
                        if(!game.isAllFolded().equals("00")) {
                            game.setRound(Game.Round.FIRST_BETTING);
                            for(int i=0; i<game.gamePlayers().size(); i++) {
                                game.gamePlayers().get(i).setAction(Player.Action.NONE);
                            }
                        }
                        answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                + "-GameID:" + msg.get("GameID") + "-GameRound:" + game.getRound() + "-AllFolded:" + game.isAllFolded() + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                                + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                                + "-" + player.handToString();
                        break;
                    case "CHANGE_CARDS":
                        game.setAllFolded("00");
                        player.setAction(Player.Action.NONE);
                        int nrOfCardsToChange = Integer.parseInt(msg.get("Decision"));
                        player.setCardsChangeCompleted(true);
                        player.setSetCompleted(false);
                        for(int i=0; i<nrOfCardsToChange; i++) {
                            Card newCard = game.deck.dealCard();
                            Card oldCard = player.removeCard(msg.get("ToChange"+ i));
                            game.deck.addCard(oldCard);
                            player.addCard(newCard);
                        }
                        player.setHand();
                        boolean roundCompleted = true;
                        for(Player p : game.activePlayers()) {
                            if(!p.isCardsChangeCompleted()) {
                                roundCompleted = false;
                            }
                        }
                        if(!roundCompleted) {
                            int index=0;
                            for(int i=0; i<game.activePlayers().size(); i++) {
                                if(game.activePlayers().get(i).getId()==game.getPlayerTurn().getId()) {
                                    index = (i+1)%game.activePlayers().size();
                                }
                            }
                            game.setPlayerTurn(game.activePlayers().get(index));
                        }
                        else {
                            game.setRound(Game.Round.SECOND_BETTING);
                            game.resetTurnOnNewPhase();
                        }
                        gameRound = String.valueOf(game.getRound());
                        answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                + "-GameID:" + msg.get("GameID") + "-GameRound:" + gameRound + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                                + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                                + "-" + player.handToString();
                        break;
                    case "SECOND_BETTING":
                        player.setCardsChangeCompleted(false);
                        playerDecision = msg.get("Decision");
                        switch (playerDecision){
//                            case "Check":
//                                break;
                            case "Fold":
                                player.setAction(Player.Action.FOLD);
                                game.playerFolds(player);
                                // TODO: Proceed game status
                                //game.proceedBettingRound();
                                break;
                            case "Bid":
                                int playerBid = Integer.parseInt(msg.get("Bid"));
                                int diff = player.getBid() - playerBid;
                                player.setBid(playerBid);
                                player.updateInPot(-diff);
                                player.updateMoney(diff);
                                player.setAction(Player.Action.BID);
                                game.setMaxBid(Math.max(playerBid, game.getMaxBid()));
                                game.updatePot();
                                //TODO: Proceed game status
                                //game.proceedBettingRound();
                                break;
                        }
                        //game.nextTurn();
                        // TODO: Proceed game status
                        game.proceedBettingRound();
                        if(!game.isAllFolded().equals("00")) {
                            game.setRound(Game.Round.FIRST_BETTING);
                            for(int i=0; i<game.gamePlayers().size(); i++) {
                                game.gamePlayers().get(i).setAction(Player.Action.NONE);
                            }
                        }
                        answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                + "-GameID:" + msg.get("GameID") + "-GameRound:" + game.getRound() + "-AllFolded:" + game.isAllFolded() + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                                + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                                + "-" + player.handToString();
                        break;
                    case "COMPARING_CARDS":
                        game.setAllFolded("00");
                        if(msg.get("Decision").equals("Unseen")) {
                            game.comparePlayersCards();
                            game.distributePot();
                            gameRound = String.valueOf(game.getRound());
                            answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                    + "-GameID:" + msg.get("GameID") + "-GameRound:" + gameRound + "-PotWinner:" + game.getPotWinner().getId() + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                                    + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                                    + "-" + player.handToString();
                        }
                        else {
                            //TODO: proceed after comparing cards (New set or game over)
                            player.setSetCompleted(true);
                            int count = 0;
                            for(Player p : game.activePlayers()) {
                                if(p.isSetCompleted()) {
                                    count++;
                                }
                            }
                            playersToCompleteSet = count;
                            if(player.getMoney()==0) {
                                answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                        + "-GameRound:" + gameRound + "-Bankrupt:Yes";
                            }
                            if(!game.isSetProceeded()) {
                                game.proceedAfterComparingCards();
                                game.setSetProceeded(true);
                            }
                            if(game.activePlayers().size() == 1) {
                                if(game.getWinner().getId()==Integer.parseInt(msg.get("PlayerID"))) {
                                    answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID")
                                            + "-GameRound:COMPARING_CARDS" + "-Winner:Yes";
                                }
                                //Server.games.remove(game);
                            }
                            else {
                                //while(playersToCompleteSet!=game.activePlayers().size()) {;}
                                game.setRound(Game.Round.FIRST_BETTING);
                                gameRound = String.valueOf(game.getRound());
                                answer = "State:IN_GAME-" + "PlayerID:" + msg.get("PlayerID")
                                        + "-GameID:" + msg.get("GameID") + "-GameRound:FIRST_BETTING" + "-Turn:" + game.getPlayerTurn().getId() + "-MyMoney:" + player.getMoney() + "-MaxBid:"
                                        + game.getMaxBid() + "-MyBid:" + player.getBid() + "-MyAction:" + player.getAction() + "-GameInfo:" + game.gameInfo()
                                        + "-" + player.handToString();
                                //game.setRound(Game.Round.COMPARING_CARDS);
                                if(playersToCompleteSet == game.activePlayers().size()) {
                                    game.setRound(Game.Round.FIRST_BETTING);
                                    playersToCompleteSet = 0;
                                    game.setSetProceeded(false);
                                }
                            }
                        }
                        break;
                }
            }
        }
        return answer;
    }
}
