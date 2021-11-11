package poker.socket.java;

import java.util.ArrayList;

public class Deck {
    public ArrayList<Card> deck = new ArrayList<>();

    public Deck() {
        for(Card.Suit s : Card.Suit.values()) {
            for(Card.Rank r : Card.Rank.values()) {
                deck.add(new Card(r, s));
            }
        }
    }
}
