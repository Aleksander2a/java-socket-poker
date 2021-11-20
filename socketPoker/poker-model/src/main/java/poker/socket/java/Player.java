package poker.socket.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * A class to represent a player in the game
 */
public class Player {
    private int id;
    private int money;
    private int inPot;
    private int Bid;
    private ArrayList<Card> cards = new ArrayList<>();
    private int currentGameId;
    private Hand hand;


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

    public int getCurrentGameId() {return currentGameId;}

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
        return Bid;
    }

    public void setBid(int inBid) {
        this.Bid = inBid;
    }

    public void updateBid(int value) {
        this.Bid += value;
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
        for(Card card : cards) {
            System.out.println(card.toString());
        }
    }

    public void sortCards() {
        cards.sort(new SortByRankSuit());
    }

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

    public void setHand() {
        sortCards();
        hand = new Hand(cards);
        hand.hasHand();
    }

    public Hand getHand() {
        return hand;
    }
}
