package poker.socket.java;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class to represent a player in the game
 */
public class Player {
    private int id;
    private int money;
    private ArrayList<Card> cards = new ArrayList<>();
    private int currentGameId;

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

    /**
     * Prints out all player's cards
     */
    public void printCards() {
        for(Card card : cards) {
            System.out.println(card.toString());
        }
    }
}
