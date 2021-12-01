package poker.socket.java.server;

import poker.socket.java.model.*;
import java.util.*;

public class ServerMessageHandler {
    private static final String STATE = "State";
    private static final String DECISION = "Decision";
    private static final String PLAYER_ID = "PlayerID";
    private static final String STATE_IN_GAME = "State:IN_GAME-";
    private static final String PLAYER_ID_KEY = "PlayerID:";
    private static final String GAME_ID_KEY = "-GameID:";
    private static final String GAME_ROUND_KEY = "-GameRound:";
    private static final String TURN_KEY = "-Turn:";
    private static final String MY_MONEY = "-MyMoney:";
    public static final String MAX_BID = "-MaxBid:";
    private static final String MY_BID = "-MyBid:";
    private static final String MY_ACTION = "-MyAction:";
    private static final String GAME_INFO_KEY = "-GameInfo:";
    private static final String PLAYER_HAND_RANK = "PlayerHandRank:";
    private static final String STATE_WAITING= "State:WAITING_FOR_PLAYERS-";
    private static final String NR_OF_PLAYERS_KEY = "-NrOfPlayers:";
    private static final String MAX_NR_OF_PLAYERS_KEY = "-MaxPlayers:";
    private static final String GAME_ID = "GameID";
    private static final String STATE_IN_GAME_PLAYER_ID = "State:IN_GAME-PlayerID:";
    private static final String ALL_FOLDED = "-AllFolded:";
    private static final String MAIN_POT_WON = "-MainPotWon:";
    private static final String WINNER_HAND = "-WinnerHand:";


    private ServerMessageHandler() {}

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
        StringBuilder answer = new StringBuilder();
        String playerHand = "";
        String playerHandRank = "";
        String gameRound= "";
        int playerTurnId = -1;
        int money = 0;
        int maxBid = 0;
        int myBid = 0;
        int playersToCompleteSet = 0;
        Player.Action action = Player.Action.NONE;
        if(msg.get(STATE).equals("JOIN_OR_CREATE_GAME")) {
            if(msg.get(DECISION).equals("join")) {
                Game game=null;
                for (Game g : Server.getGames()) {
                    if(g.getId() == Integer.parseInt(msg.get("Joins"))) {
                        game = g;
                    }
                }
                for(Player p : Server.getClients()) {
                    if(p.getId() == Integer.parseInt(msg.get(PLAYER_ID))) {
                        game.addPlayer(p);
                        p.setCurrentGameId(game.getId());
                    }
                }
                if(game.getPlayersNumber() == game.getMaxPlayersNumber()) {
                    game.initializeGame();
                    game.setRound(Game.Round.FIRST_BETTING);
                    gameRound = String.valueOf(game.getRound());
                    playerTurnId = game.getPlayerTurn().getId();
                    maxBid = game.getMaxBid();
                    for(Player p : game.gamePlayers()) {
                        game.addActivePlayer(p);
                        if(p.getId()==Integer.parseInt(msg.get(PLAYER_ID))) {
                            playerHand = p.handToString();
                            playerHandRank = p.getHand().rankingToString();
                            money = p.getMoney();
                            myBid = p.getBid();
                            action = p.getAction();
                        }
                    }
                    answer.append(STATE_IN_GAME + PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append(GAME_ID_KEY).append(game.getId()).append(GAME_ROUND_KEY).append(gameRound).append(TURN_KEY).append(playerTurnId).append(MY_MONEY).append(money).append(MAX_BID).append(maxBid).append(MY_BID).append(myBid).append(MY_ACTION).append(action).append(GAME_INFO_KEY).append(game.gameInfo()).append("-").append(playerHand).append(PLAYER_HAND_RANK).append(playerHandRank);
                }
                else {
                    answer = new StringBuilder(STATE_WAITING + PLAYER_ID_KEY + msg.get(PLAYER_ID) + "-"
                            + "GameID:" + game.getId() + NR_OF_PLAYERS_KEY + game.getPlayersNumber() +
                            MAX_NR_OF_PLAYERS_KEY + game.getMaxPlayersNumber());
                }
            }
            else if(msg.get(DECISION).equals("new")) {
                answer.append("State:NEW_GAME-" + PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append("-");
            }
            else if(msg.get(DECISION).equals("refresh")) {
                answer = new StringBuilder("State:JOIN_OR_CREATE_GAME-");
                answer.append(PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append("-");
                answer.append("GameNumber:").append(Server.getGames().size()).append("-");
                for(Game g : Server.getGames()) {
                    answer.append("Game").append(g.getId()).append(":").append(g.getMaxPlayersNumber()).append("-");
                }
            }
        }
        if(msg.get(STATE).equals("NEW_GAME")) {
            // Choose playerNumber
            Game game = new Game();
            game.setMaxPlayersNumber(Integer.parseInt(msg.get("Players")));
            game.setStartingMoney(Integer.parseInt(msg.get("StartingMoney")));
            game.setAnte(Integer.parseInt(msg.get("Ante")));
            game.setId(Server.getNextGameId());
            Server.increaseNextGameId();
            for(Player p : Server.getClients()) {
                if(p.getId() == Integer.parseInt(msg.get(PLAYER_ID))) {
                    game.addPlayer(p);
                    p.setCurrentGameId(game.getId());
                }
            }
            Server.getGames().add(game);
            answer = new StringBuilder(STATE_WAITING + PLAYER_ID_KEY + msg.get(PLAYER_ID)
                    + GAME_ID_KEY + String.valueOf(game.getId()) + NR_OF_PLAYERS_KEY + String.valueOf(game.getPlayersNumber())
                    + MAX_NR_OF_PLAYERS_KEY + String.valueOf(game.getMaxPlayersNumber()));
        }
        if(msg.get(STATE).equals("WAITING_FOR_PLAYERS")) {
            Game game=null;
            for (Game g : Server.getGames()) {
                if(g.getId() == Integer.parseInt(msg.get(GAME_ID))) {
                    game = g;
                }
            }
            if(game.getPlayersNumber()==game.getMaxPlayersNumber()) {
                game.initializeGame();
                game.setRound(Game.Round.FIRST_BETTING);
                gameRound = String.valueOf(game.getRound());
                playerTurnId = game.getPlayerTurn().getId();
                maxBid = game.getMaxBid();
                for(Player p : game.gamePlayers()) {
                    game.addActivePlayer(p);
                    if(p.getId()==Integer.parseInt(msg.get(PLAYER_ID))) {
                        playerHand = p.handToString();
                        playerHandRank = p.getHand().rankingToString();
                        money = p.getMoney();
                        myBid = p.getBid();
                        action = p.getAction();
                    }
                }
                answer = new StringBuilder(STATE_IN_GAME + PLAYER_ID_KEY + msg.get(PLAYER_ID)
                        + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + gameRound + TURN_KEY + playerTurnId + MY_MONEY + money + MAX_BID
                        + maxBid + MY_BID + myBid + MY_ACTION + action + GAME_INFO_KEY + game.gameInfo()
                        + "-" + playerHand + PLAYER_HAND_RANK + playerHandRank);
            }
            else {
                answer = new StringBuilder(STATE_WAITING + PLAYER_ID_KEY + msg.get(PLAYER_ID) + "-"
                        + "GameID:" + msg.get(GAME_ID) + NR_OF_PLAYERS_KEY + game.getPlayersNumber() +
                        MAX_NR_OF_PLAYERS_KEY + game.getMaxPlayersNumber());
            }
        }
        if(msg.get(STATE).equals("IN_GAME")) {
            Game game=null;
            Player player = null;
            for (Game g : Server.getGames()) {
                if (g.getId() == Integer.parseInt(msg.get(GAME_ID))) {
                    game = g;
                }
            }
            if(game != null) {
                for (Player p : game.gamePlayers()) {
                    if (p.getId() == Integer.parseInt(msg.get(PLAYER_ID))) {
                        player = p;
                    }
                }
            }
            gameRound = String.valueOf(game.getRound());
            if(msg.get(DECISION).equals("Refresh")) {
                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                        + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + gameRound + ALL_FOLDED + game.isAllFolded() + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                        + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                        + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString());
                return answer.toString();
            }
            else {
                switch (gameRound) {
                    case "FIRST_BETTING":
                        String playerDecision = msg.get(DECISION);
                        switch (playerDecision){
                            case "Fold":
                                player.setAction(Player.Action.FOLD);
                                game.setAllFolded("00");
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
                                break;
                            default:
                                break;
                        }

                        boolean allNone = true;
                        for(Player p : game.activePlayers()) {
                            if(!p.getAction().equals(Player.Action.NONE)) {
                                allNone = false;
                            }
                        }
                        if(!allNone) {
                            game.proceedBettingRound();
                        }
                        boolean allWithNoInPot = true;
                        for(Player p : game.activePlayers()) {
                            if(p.getInPot()!=0) {
                                allWithNoInPot = false;
                            }
                        }
                        if(allWithNoInPot) {
                            game.takeAnteFromPlayer();
                        }
                        if(!game.isAllFolded().equals("00")) {
                            game.setRound(Game.Round.FIRST_BETTING);
                            for(int i=0; i<game.gamePlayers().size(); i++) {
                                game.gamePlayers().get(i).setAction(Player.Action.NONE);
                            }
                        }
                        answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + game.getRound() + ALL_FOLDED + game.isAllFolded() + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                                + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                                + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString() + MAIN_POT_WON + game.getMainPotWon() + WINNER_HAND + game.getWinningHand());
                        break;
                    case "CHANGE_CARDS":
                        game.setAllFolded("00");
                        player.setAction(Player.Action.NONE);
                        int nrOfCardsToChange = Integer.parseInt(msg.get(DECISION));
                        player.setCardsChangeCompleted(true);
                        player.setSetCompleted(false);
                        for(int i=0; i<nrOfCardsToChange; i++) {
                            Card newCard = game.getDeck().dealCard();
                            Card oldCard = player.removeCard(msg.get("ToChange"+ i));
                            game.getDeck().addCard(oldCard);
                            player.addCard(newCard);
                        }
                        player.setHand();
                        boolean roundCompleted = true;
                        for(Player p : game.activePlayers()) {
                            if(!p.isCardsChangeCompleted() && !p.getAction().equals(Player.Action.FOLD)) {
                                roundCompleted = false;
                            }
                        }
                        if(!roundCompleted) {
                            int index=0;
                            int indexOfTurn = 0;
                            boolean newTurnFound = false;
                            for(int i=0; i<game.activePlayers().size(); i++) {
                                if(game.activePlayers().get(i).getId()==game.getPlayerTurn().getId()) {
                                    indexOfTurn = i;
                                }
                            }
                            index = indexOfTurn + 1;
                            while(!newTurnFound) {
                                if(!game.activePlayers().get(index%game.activePlayers().size()).getAction().equals(Player.Action.FOLD)) {
                                    game.setPlayerTurn(game.activePlayers().get(index%game.activePlayers().size()));
                                    newTurnFound = true;
                                }
                                index++;
                            }
                        }
                        else {
                            game.setRound(Game.Round.SECOND_BETTING);
                            game.resetTurnOnNewPhase();
                        }
                        gameRound = String.valueOf(game.getRound());
                        answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + gameRound + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                                + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                                + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString());
                        break;
                    case "SECOND_BETTING":
                        player.setCardsChangeCompleted(false);
                        playerDecision = msg.get(DECISION);
                        switch (playerDecision){
                            case "Fold":
                                player.setAction(Player.Action.FOLD);
                                game.setAllFolded("00");
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
                                break;
                            default:
                                break;
                        }
                        allNone = true;
                        for(Player p : game.activePlayers()) {
                            if(!p.getAction().equals(Player.Action.NONE)) {
                                allNone = false;
                            }
                        }
                        if(!allNone) {
                            game.proceedBettingRound();
                        }
                        allWithNoInPot = true;
                        for(Player p : game.activePlayers()) {
                            if(p.getInPot()!=0) {
                                allWithNoInPot = false;
                            }
                        }
                        if(allWithNoInPot) {
                            game.takeAnteFromPlayer();
                        }
                        if(!game.isAllFolded().equals("00")) {
                            game.setRound(Game.Round.FIRST_BETTING);
                            for(int i=0; i<game.gamePlayers().size(); i++) {
                                game.gamePlayers().get(i).setAction(Player.Action.NONE);
                            }
                        }
                        answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + game.getRound() + ALL_FOLDED + game.isAllFolded() + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                                + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                                + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString() + MAIN_POT_WON + game.getMainPotWon() + WINNER_HAND + game.getWinningHand());
                        break;
                    case "COMPARING_CARDS":
                        game.setAllFolded("00");
                        if(msg.get(DECISION).equals("Unseen")) {
                            game.comparePlayersCards();
                            game.distributePot();
                            gameRound = String.valueOf(game.getRound());
                            answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                    + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + gameRound + "-PotWinner:" + game.getPotWinner().getId() + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                                    + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                                    + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString() + MAIN_POT_WON + game.getMainPotWon() + WINNER_HAND + game.getWinningHand());
                        }
                        else {
                            player.setSetCompleted(true);
                            int count = 0;
                            for(Player p : game.activePlayers()) {
                                if(p.isSetCompleted()) {
                                    count++;
                                }
                            }
                            playersToCompleteSet = count;
                            if(player.getMoney()==0) {
                                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                        + GAME_ROUND_KEY + gameRound + "-Bankrupt:Yes");
                            }
                            if(playersToCompleteSet == game.activePlayers().size()) {
                                game.proceedAfterComparingCards();
                                game.setSetProceeded(true);
                            }
                            if(game.activePlayers().size() == 1) {
                                if(game.getPotWinner().getId()==Integer.parseInt(msg.get(PLAYER_ID))) {
                                    answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                            + "-GameRound:COMPARING_CARDS" + "-Winner:Yes");
                                }
                            }
                            else if(playersToCompleteSet == game.activePlayers().size()) {
                                boolean anteTaken = true;
                                for(Player p : game.activePlayers()) {
                                    if(p.getInPot()==0) {
                                        anteTaken = false;
                                    }
                                }
                                if(!anteTaken) {
                                    game.takeAnteFromPlayer();
                                }
                                game.setRound(Game.Round.FIRST_BETTING);
                                answer = new StringBuilder(STATE_IN_GAME + PLAYER_ID_KEY + msg.get(PLAYER_ID)
                                        + GAME_ID_KEY + msg.get(GAME_ID) + "-GameRound:FIRST_BETTING" + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                                        + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                                        + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString() + MAIN_POT_WON + game.getMainPotWon() + WINNER_HAND + game.getWinningHand());
                                if(playersToCompleteSet == game.activePlayers().size()) {
                                    game.setRound(Game.Round.FIRST_BETTING);
                                    game.setSetProceeded(false);
                                }
                            }
                            else {
                                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID)
                                        + GAME_ID_KEY + msg.get(GAME_ID) + GAME_ROUND_KEY + gameRound + "-PotWinner:" + game.getPotWinner().getId() + TURN_KEY + game.getPlayerTurn().getId() + MY_MONEY + player.getMoney() + MAX_BID
                                        + game.getMaxBid() + MY_BID + player.getBid() + MY_ACTION + player.getAction() + GAME_INFO_KEY + game.gameInfo()
                                        + "-" + player.handToString() + PLAYER_HAND_RANK + player.getHand().rankingToString() + MAIN_POT_WON + game.getMainPotWon() + WINNER_HAND + game.getWinningHand());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return answer.toString();
    }
}
