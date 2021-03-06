package poker.socket.java.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class to represent a player in the game
 */
public class Player {
    private int id;
    private int money;
    private int inPot;
    private int bid;
    private boolean active;
    private Action action;
    private List<Card> cards = new ArrayList<>();
    private int currentGameId;
    private Hand hand;
    private boolean cardsChangeCompleted = false;
    private boolean setCompleted = false;

    private static final Logger LOGGER = Logger.getLogger( Player.class.getName() );

    public enum Action {
        NONE, FOLD, BID
    }

    public Player() {}

    /**
     * Creates a player with unique id on the server
     * @param playerId Server-Unique number to be player's id
     */
    public Player(int playerId) {
        this.id = playerId;
        this.currentGameId = -1;
    }

    public int getId() {
        return id;
    }

    public void setCurrentGameId(int gameId) {this.currentGameId = gameId;}

    public void setMoney(int amount) {
        this.money = amount;
    }

    public int getMoney() {
        return money;
    }

    public void updateMoney(int value) {
        this.money += value;
    }

    public int getInPot() {
        return inPot;
    }

    public void setInPot(int inPot) {
        this.inPot = inPot;
    }

    public void updateInPot(int value) {
        this.inPot += value;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int inBid) {
        this.bid = inBid;
    }

    public boolean isCardsChangeCompleted() {
        return cardsChangeCompleted;
    }

    public void setCardsChangeCompleted(boolean cardsChangeCompleted) {
        this.cardsChangeCompleted = cardsChangeCompleted;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isSetCompleted() {
        return setCompleted;
    }

    public void setSetCompleted(boolean setCompleted) {
        this.setCompleted = setCompleted;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Removes given Card from player's cards and returns it
     * @param card Card to be removed
     * @return Removed card
     */
    public Card removeCard(Card card) {
        cards.remove(card);
        return card;
    }

    /**
     * Adds given card to player's cards
     * @param card Card to be added to player's cards
     */
    public void addCard(Card card) {
        if(!cards.contains(card)) {
            cards.add(card);
        }
    }

    public Card getCardAtIndex(int i) {
        return cards.get(i);
    }

    /**
     * Prints out all player's cards
     */
    public void printCards() {
        String s = "";
        for(Card card : cards) {
            s = card.toString();
            LOGGER.info(s);
        }
    }

    /**
     * Sorts cards by their rank
     */
    public void sortCards() {
        cards.sort(new SortByRankSuit());
    }

    /**
     * Helper class to sort cards by their rank
     */
    static class SortByRankSuit implements Comparator<Card> {
        @Override
        public int compare(Card o1, Card o2) {
            if(o1.compareRankWith(o2)!=0) {
                return o1.compareRankWith(o2);
            }
            else {
                if(o1.compareSuitWith(o2)!=0) {
                    return o1.compareSuitWith(o2);
                }
            }
                return 0;
        }
    }

    /**
     * Find player's best hand
     */
    public void setHand() {
        sortCards();
        hand = new Hand(cards);
        hand.hasHand();
    }

    /**
     * Codes player's hand to String, so it can be easily printed out
     * @return Player's hand coded into a String
     */
    public String handToString() {
        StringBuilder result = new StringBuilder();
        for(int i=0; i<5; i++) {
            result.append("Card").append(i).append(":").append(cards.get(i).getRank()).append(",").append(cards.get(i).getSuit()).append("-");
        }

        return result.toString();
    }

    /**
     * Pops from player's cards a card given as a String from
     * @param s Card given as a String
     * @return Card given as a String (no longer in player's cards)
     */
    public Card removeCard(String s) {
        String[] cardInString = s.split(",");
        Card toReturn = null;
        for(int i=0; i<cards.size(); i++) {
            if(String.valueOf(cards.get(i).getRank()).equals(cardInString[0]) && String.valueOf(cards.get(i).getSuit()).equals(cardInString[1])) {
                toReturn = cards.get(i);
                cards.remove(cards.get(i));
            }
        }
        return toReturn;
    }
}
