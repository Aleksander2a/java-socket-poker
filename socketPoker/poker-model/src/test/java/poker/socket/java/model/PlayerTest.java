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
        Assert.assertTrue(result.getRank()==Card.Rank.ACE && result.getSuit()== Card.Suit.HEARTS);
    }

    @Test
    public void removeCardCardTest() {
        Card card = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        player.addCard(card);
        player.removeCard(card);
        int sizeAfter = player.getCards().size();
        Assert.assertEquals(0, sizeAfter);
    }

    @Test
    public void removeCardString() {
        Card card = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        player.addCard(card);
        player.removeCard("ACE,HEARTS");
        int sizeAfter = player.getCards().size();
        Assert.assertEquals(0, sizeAfter);
    }

    @Test
    public void setHandTest() {
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.setHand();
        Assert.assertEquals(player.getCards().get(0).getRank(), Card.Rank.FOUR);
    }

    @Test
    public void handToStringTest() {
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        String result = player.handToString();
        String expected = "Card0:FIVE,HEARTS-Card1:JACK,SPADES-Card2:ACE,CLUBS-Card3:FIVE,DIAMONDS-Card4:FOUR,HEARTS-";
        Assert.assertEquals(result, expected);
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
