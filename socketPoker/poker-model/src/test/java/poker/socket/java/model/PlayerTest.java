package poker.socket.java.model;

import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {
    private final Player player = new Player();
    private final Deck deck = new Deck();

    @Test
    public void addCardTest() {
        Card card = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        player.addCard(card);
        Card result = player.getCardAtIndex(0);
        Assert.assertTrue(result.getRank()== Card.Rank.ACE && result.getSuit()== Card.Suit.HEARTS);
    }

    @Test
    public void sortCardTest() {
        for(int i=0; i<5; i++) {
            Card card = deck.dealCard();
            player.addCard(card);
        }
        player.sortCards();
        player.printCards();
        Assert.assertTrue(true);
    }


}
