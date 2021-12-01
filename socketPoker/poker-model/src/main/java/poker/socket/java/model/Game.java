package poker.socket.java.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class to represent a single game on the server
 */
public class Game {
    private int id;
    private int ante;
    private int maxPlayersNumber;
    private int startingMoney;

    private int pot;
    private int maxBid;
    private Deck deck = new Deck();
    private Player dealer;
    private Player playerTurn;
    private List<Player> players = new ArrayList<>();
    private List<Player> activePlayers = new ArrayList<>();
    private List<Player> playersByHand = new ArrayList<>();
    private Round round;
    private boolean initialized = false;
    private Player potWinner = null;
    private boolean finished = false;
    private boolean setProceeded = false;
    private String allFolded = "00";
    private boolean roundProceeded = false;
    private int playersToProceedRound = 0;
    private String winningHand = "none";
    private int mainPotWon = 0;

    public enum Round {
        FIRST_BETTING, CHANGE_CARDS, SECOND_BETTING, COMPARING_CARDS, SET_OVER, GAME_OVER
    }

    public Game() {}

    public void setId(int gameId) {this.id = gameId;}

    public int getId() {
        return id;
    }

    public int getStartingMoney() {
        return startingMoney;
    }

    public void setAnte(int gameAnte) {this.ante = gameAnte;}

    public int getAnte() {
        return ante;
    }

    public void setPot(int gamePot) {this.pot = gamePot;}

    public int getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(int maxBid) {
        this.maxBid = maxBid;
    }

    public void setMaxPlayersNumber(int maxPlayers) {this.maxPlayersNumber = maxPlayers;}

    public int getMaxPlayersNumber() {return this.maxPlayersNumber;}

    public void setStartingMoney(int money) {this.startingMoney = money;}

    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public String isAllFolded() {
        return allFolded;
    }

    public void setAllFolded(String allFolded) {
        this.allFolded = allFolded;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public Deck getDeck() {
        return deck;
    }

    public Player getPlayerAtIndex(int i) {
        return players.get(i);
    }

    public List<Player> gamePlayers() {
        return  players;
    }

    public List<Player> activePlayers() {
        return  activePlayers;
    }

    public Player getPotWinner() {
        return potWinner;
    }

    public void setPotWinner(Player potWinner) {
        this.potWinner = potWinner;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isSetProceeded() {
        return setProceeded;
    }

    public void setSetProceeded(boolean setProceeded) {
        this.setProceeded = setProceeded;
    }

    public boolean isRoundProceeded() {
        return roundProceeded;
    }

    public void setRoundProceeded(boolean roundProceeded) {
        this.roundProceeded = roundProceeded;
    }

    public int getPlayersToProceedRound() {
        return playersToProceedRound;
    }

    public void setPlayersToProceedRound(int playersToProceedRound) {
        this.playersToProceedRound = playersToProceedRound;
    }

    public void updatePlayersToProceedRound(int playersToProceedRound) {
        this.playersToProceedRound += playersToProceedRound;
    }

    public int getMainPotWon() {
        return mainPotWon;
    }

    public String getWinningHand() {
        return winningHand;
    }

    public int getPot() {
        return pot;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public List<Player> getPlayersByHand() {
        return playersByHand;
    }

    public Player getDealer() {
        return dealer;
    }

    public void takeAnteFromPlayer() {
        pot = 0;
        for(Player p : players) {
            if(p.getMoney() != 0 ) {
                int pMoney = p.getMoney();
                p.setMoney(Math.max(pMoney - ante, 0));
                p.setInPot(pMoney - p.getMoney());
                pot += p.getInPot();
                p.setBid(0);
            }
            else {
                p.setActive(false);
            }
        }
    }

    public void dealCardsToActivePlayers() {
        for(Player p : activePlayers) {
            for(int i=0; i<5; i++) {
                p.addCard(deck.dealCard());
            }
            p.setHand();
        }
    }

    public String gameInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Round=").append(round).append("|Pot=").append(pot).append(",");
        for(Player p : players) {
            info.append("|Player").append(p.getId()).append("|Money=").append(p.getMoney()).append("|InGame=").append(p.isActive());
            if(p.isActive()) {
                info.append("|Action=").append(p.getAction()).append("|InPot=").append(p.getInPot()).append("|Bid=").append(p.getBid());
            }
            if(dealer.getId()==p.getId()) {
                info.append("|Dealer");
            }
            if(playerTurn.getId()==p.getId()) {
                info.append("|Turn");
            }
            info.append(",");
        }

        return info.toString();
    }

    public void addActivePlayer(Player p) {
        if (!activePlayers.contains(p)) {
            activePlayers.add(p);
        }
    }

    public void updatePot() {
        pot = 0;
        for(Player p : players) {
            pot += p.getInPot();
        }
    }

    public void giveMoneyToPlayers() {
        for(Player p : players) {
            p.setMoney(startingMoney);
        }
    }

    public void initializeGame() {
        if(!initialized) {
            setDealer(players.get(0));
            setPlayerTurn(players.get(1));
            giveMoneyToPlayers();
            takeAnteFromPlayer();
            for(Player p : players) {
                for(int i=0; i<5; i++) {
                    p.addCard(deck.dealCard());
                }
                p.setHand();
            }
            for (Player p : players) {
                p.setActive(true);
                p.setAction(Player.Action.NONE);
                p.setHand();
            }
            maxBid = 0;
            initialized = true;
        }
    }

    public void nextPhase() {
        switch (round){
            case FIRST_BETTING:
                round = Round.CHANGE_CARDS;
                break;
            case CHANGE_CARDS:
                round = Round.SECOND_BETTING;
                break;
            case SECOND_BETTING:
                round = Round.COMPARING_CARDS;
                break;
            default:
                break;
        }
    }

    public void proceedBettingRound() {
        int biddingPlayersCount = 0;
        for(Player p : activePlayers) {
            if(p.isActive() && !p.getAction().equals(Player.Action.FOLD)) {
                biddingPlayersCount++;
            }
        }
        if(biddingPlayersCount>1) {
            boolean areEqual = false;
            int countOfTrue = 0;
            for(Player p : activePlayers) {
                if((p.getBid()==maxBid && !p.getAction().equals(Player.Action.NONE)) || (p.getInPot()!=0 && p.getMoney()==0) || p.getAction().equals(Player.Action.FOLD)) {
                    countOfTrue++;
                }
            }
            if(countOfTrue==activePlayers.size()) {
                areEqual = true;
            }
            if(areEqual) {
                maxBid = 0;
                for(Player p : players) {
                    p.setBid(0);
                }
                nextPhase();
                resetTurnOnNewPhase();
            }
            else {
                int index=0;
                int indexOfTurn = 0;
                boolean newTurnFound = false;
                for(int i=0; i<activePlayers.size(); i++) {
                    if(activePlayers.get(i).getId()==playerTurn.getId()) {
                        indexOfTurn = i;
                    }
                }
                index = indexOfTurn + 1;
                while(!newTurnFound) {
                    if(!activePlayers.get(index%activePlayers.size()).getAction().equals(Player.Action.FOLD)) {
                        playerTurn = activePlayers.get(index%activePlayers.size());
                        newTurnFound = true;
                    }
                    index++;
                }

            }
        }
        else {
            round = Round.SET_OVER;
            for(int i=0; i<activePlayers.size(); i++) {
                if(activePlayers.get(i).getAction().equals(Player.Action.BID)) {
                    activePlayers.get(i).updateMoney(pot);
                    allFolded = String.valueOf(activePlayers.get(i).getId());
                    mainPotWon = pot;
                }
                activePlayers.get(i).setInPot(0);
                activePlayers.get(i).setBid(0);
                for(int j=0; j<5; j++) {
                    Card activePlayerCard = activePlayers.get(i).getCardAtIndex(0);
                    deck.addCard(activePlayerCard);
                    activePlayers.get(i).removeCard(activePlayerCard);
                }
                for(int n=0; n<5; n++) {
                    activePlayers.get(i).addCard(deck.dealCard());
                }
                activePlayers.get(i).setHand();
                }

            maxBid = 0;
            pot = 0;
            findNextDealerAndTurn();
        }
    }

    private void findNextDealerAndTurn() {
        int index = 0;
        for(int i=0; i<activePlayers.size(); i++) {
            if(activePlayers.get(i).getId()==dealer.getId()) {
                index = i;
            }
        }
        boolean newDealerFound = false;
        index++;
        while(!newDealerFound) {
            if(activePlayers.get((index) % activePlayers.size()).isActive()) {
                dealer = activePlayers.get((index) % activePlayers.size());
                newDealerFound = true;
            }
            index++;
        }
        boolean newTurnFound = false;
        while(!newTurnFound) {
            if(activePlayers.get((index) % activePlayers.size()).isActive()) {
                playerTurn = activePlayers.get((index) % activePlayers.size());
                newTurnFound = true;
            }
            index++;
        }
    }

    public void resetTurnOnNewPhase() {
        int index = 0;
        boolean newTurnFound = false;
        for(int i=0; i<activePlayers.size(); i++) {
            if(activePlayers.get(i).getId()==dealer.getId()) {
                index = i;
            }
        }
        index++;
        while(!newTurnFound) {
            if(!activePlayers.get(index%activePlayers.size()).getAction().equals(Player.Action.FOLD)) {
                playerTurn = activePlayers.get(index%activePlayers.size());
                newTurnFound = true;
            }
            index++;
        }
    }

    static class SortPlayersByHand implements Comparator<Player> {
        @Override
        public int compare(Player o1, Player o2) {
            return -o1.getHand().compareTo(o2.getHand());
        }
    }

    public void comparePlayersCards() {
        playersByHand = activePlayers;
        playersByHand.sort(new SortPlayersByHand());
    }

    public void distributePot() {
        int playerInPot = 0;
        for(Player p : activePlayers) {
            if(p.getInPot()!=0) {
                playerInPot++;
            }
        }
        int moneyToDivide = pot;
        for(int i=0; i<playersByHand.size(); i++) {
            if(!playersByHand.get(i).getAction().equals(Player.Action.FOLD)) {
                int playersPot = playersByHand.get(i).getInPot();
                playersByHand.get(i).updateMoney(Math.min(moneyToDivide, playersPot*playerInPot));
                if(i==0) {
                    mainPotWon = Math.min(moneyToDivide, playersPot*playerInPot);
                    winningHand = playersByHand.get(i).getHand().rankingToString();
                }
                moneyToDivide -= Math.min(moneyToDivide, playersPot*playerInPot);
            }
        }
        setPot(0);
        for(Player p : players) {
            p.setInPot(0);
            p.setAction(Player.Action.NONE);
        }
        potWinner = playersByHand.get(0);
    }

    public void proceedAfterComparingCards() {
        activePlayers.clear();
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getMoney()==0  && players.get(i).isActive()) {
                players.get(i).setCurrentGameId(-1);
                players.get(i).setActive(false);
                for(int j=0; j<5; j++) {
                    Card inActivePlayerCard = players.get(i).getCardAtIndex(0);
                    deck.addCard(inActivePlayerCard);
                    players.get(i).removeCard(inActivePlayerCard);
                }
            }
            if(players.get(i).isActive()) {
                activePlayers.add(players.get(i));
            }
        }
        if(activePlayers.size() > 1) {
            if(!isRoundProceeded()) {
                findNextDealerAndTurn();
            }

            for(int i=0; i<activePlayers.size(); i++) {
                for(int j=0; j<5; j++) {
                    Card activePlayerCard = activePlayers.get(i).getCardAtIndex(0);
                    deck.addCard(activePlayerCard);
                    activePlayers.get(i).removeCard(activePlayerCard);
                }
            }
            //new card deal
            maxBid = 0;
            dealCardsToActivePlayers();
            for(int n=0 ;n<activePlayers.size(); n++) {
                activePlayers.get(n).setHand();
            }

        }
        else {
            //game over
            finished = true;
        }
    }
}
