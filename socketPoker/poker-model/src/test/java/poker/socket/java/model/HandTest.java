package poker.socket.java.model;

import org.junit.Test;

import org.junit.Assert;

/**
 * Unit test for Hand.
 */
public class HandTest {
    private final Player player = new Player();
    private final Deck deck = new Deck();


    @Test
    public void hasOnePairTrueTest() {
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasOnePair() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasOnePair());
    }

    @Test
    public void hasOnePairFalseTest() {
        player.addCard(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasOnePair() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasOnePair());
    }

    @Test
    public void hasTwoPairsTrueTest() {
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasTwoPairs() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasTwoPairs());
    }

    @Test
    public void hasTwoPairsFalseTest() {
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasTwoPairs() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasTwoPairs());
    }

    @Test
    public void hasThreeOfAKindTrueTest() {
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasThreeOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasThreeOfAKind());
    }

    @Test
    public void hasThreeOfAKindFalseTest() {
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasThreeOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasThreeOfAKind());
    }

    @Test
    public void hasStraightTrueTest() {
        player.addCard(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraight() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasStraight());
    }

    @Test
    public void hasStraightTrueWithAceTest() {
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraight() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasStraight());
    }

    @Test
    public void hasStraightFalseTest() {
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraight() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasStraight());
    }

    @Test
    public void hasFlushTrueTest() {
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasFlush());
    }

    @Test
    public void hasFlushFalseTest() {
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasFlush());
    }

    @Test
    public void hasFullHouseTrueTest() {
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFullHouse() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasFullHouse());
    }

    @Test
    public void hasFullHouseFalseTest() {
        player.addCard(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFullHouse() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasFullHouse());
    }

    @Test
    public void hasFourOfAKindTrueTest() {
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFourOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasFourOfAKind());
    }

    @Test
    public void hasFourOfAKindFalseTest() {
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFourOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasFourOfAKind());
    }

    @Test
    public void hasStraightFlushTrueTest() {
        player.addCard(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraightFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        System.out.println(player.getHand().rankingToString());
        Assert.assertTrue(hand.hasStraightFlush());
    }

    @Test
    public void hasStraightFlushFalseTest() {
        player.addCard(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraightFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasStraightFlush());
    }

    @Test
    public void hasRoyalFlushTrueTest() {
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasRoyalFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertTrue(hand.hasRoyalFlush());
    }

    @Test
    public void hasRoyalFlushFalseTest() {
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasRoyalFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        Assert.assertFalse(hand.hasRoyalFlush());
    }

    @Test
    public void hasHandTest() {
        for(int i=0; i<5; i++) {
            Card card = deck.dealCard();
            player.addCard(card);
        }
        player.setHand();
        player.printCards();
        System.out.println(player.getHand().toString());
        Assert.assertTrue(true);
    }

    @Test
    public void compareToTest() {
        System.out.println("Player ----------------------");
        for(int i=0; i<5; i++) {
            Card card = deck.dealCard();
            player.addCard(card);
        }
        player.setHand();
        player.printCards();
        System.out.println(player.getHand().toString());

        System.out.println("Player1 ----------------------");
        Player player1 = new Player();
        for(int i=0; i<5; i++) {
            Card card = deck.dealCard();
            player1.addCard(card);
        }
        player1.setHand();
        player1.printCards();
        System.out.println(player1.getHand().toString());

        System.out.println("Compare Player to Player1 ----------------------");
        System.out.println(player.getHand().compareTo(player1.getHand()));
        Assert.assertTrue(true);
    }
}
