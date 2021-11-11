package poker.socket.java;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class to represent a deck of cards in the game
 */
public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();

    /**
     * Initialises Deck with all possible cards (52 cards)
     */
    public Deck() {
        for(Card.Suit s : Card.Suit.values()) {
            for(Card.Rank r : Card.Rank.values()) {
                cards.add(new Card(r, s));
            }
        }
    }

    /**
     * Returns random Card from all Cards in the deck
     */
    public Card dealCard() {
        // create instance of Random class
        Random rand = new Random();
        // Generate random integers in range 0 to cards.size()
        int index = rand.nextInt(cards.size());
        Card cardToReturn = cards.get(index);
        cards.remove(index);
        return cardToReturn;
    }

    /**
     * Adds given card to Deck
     * @param card Card to be added to Deck
     */
    public void addCard(Card card) {
        if(!cards.contains(card)) {
            cards.add(card);
        }
    }

    /**
     * Checks whether given Card is in the Deck
     * @param card Card to check
     * @return boolean
     */
    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    public int getDeckSize() {return cards.size();}

    /**
     * Prints out all cards in the Deck
     */
     public void printDeck() {
         for(Card card : cards) {
             System.out.println(card.toString());
         }
     }
}
