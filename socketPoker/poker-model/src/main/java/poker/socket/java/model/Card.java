package poker.socket.java.model;

import java.util.Objects;

/**
 * A class to represent a card in the game
 *
 */
public class Card
{
    private Rank rank;
    private Suit suit;

    /**
     * Possible card values (from ONE to ACE)
     */
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }

    /**
     * Possible card colors (4 of them)
     */
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    public Card(){}

    /**
     * Creates new Card object with given Rank and Suit
     * @param r Rank
     * @param s Suit
     */
    public  Card(Rank r, Suit s){
        rank = r;
        suit = s;
    }

    public Card(String x) {
        String[] values = x.split(",");
        rank = Rank.valueOf(values[0]);
        suit = Suit.valueOf(values[1]);

    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    /**
     * Compares card's rank with given card's rank
     * @param card Card to compare
     * @return positive/negative integer or 0 when equal
     */
    public int compareRankWith(Card card) {
        return Integer.compare(this.rank.compareTo(card.rank), 0);
    }

    /**
     * Compares card's suit with given card's suit
     * @param card Card to compare
     * @return positive/negative integer or 0 when equal
     */
    public int compareSuitWith(Card card) {
        return Integer.compare(this.suit.compareTo(card.suit), 0);
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

    /**
     * Makes printing card's value to console easier
     * @return String
     */
    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", suit=" + suit +
                '}';
//        String result = this.rank.toString();
//        result += ' ';
//        switch (this.suit) {
//            case CLUBS:
//                result += (char)'\u2663';
//                return result;
//            case DIAMONDS:
//                result += (char)'\u2666';
//                return result;
//            case HEARTS:
//                result += (char)'\u2665';
//                return result;
//            case SPADES:
//                result += (char)'\u2660';
//                return result;
//        }
//        return "";
    }
}
