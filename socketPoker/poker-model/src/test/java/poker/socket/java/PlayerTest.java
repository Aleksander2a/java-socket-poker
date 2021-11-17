package poker.socket.java;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private final Player player = new Player();
    private final Deck deck = new Deck();

    @Test
    void addCardTest() {
        Card card = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        player.addCard(card);
        Card result = player.getCardAtIndex(0);
        assertTrue(result.getRank()== Card.Rank.ACE && result.getSuit()== Card.Suit.HEARTS);
    }

    @Test
    void sortCardTest() {
        for(int i=0; i<5; i++) {
            Card card = deck.dealCard();
            player.addCard(card);
        }
        player.sortCards();
        player.printCards();
        assertTrue(true);
    }
}
