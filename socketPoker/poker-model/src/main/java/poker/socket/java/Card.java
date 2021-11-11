package poker.socket.java;

import java.util.Objects;

/**
 * A class to represent a card in the game
 *
 */
public class Card
{
    public Rank rank;
    public Suit suit;

    /**
     * Possible card values (from ONE to ACE)
     */
    public enum Rank {
        ONE, TWO, THREE, FOUR, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    /**
     * Possible card colors (4 of them)
     */
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    public Card(){}
    public  Card(Rank r, Suit s){
        rank = r;
        suit = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
