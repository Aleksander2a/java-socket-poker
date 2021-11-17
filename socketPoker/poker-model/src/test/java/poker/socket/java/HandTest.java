package poker.socket.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    private final Player player = new Player();
    private final Deck deck = new Deck();

    @Test
    void hasOnePairTrueTest() {
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));

//        for(int i=0; i<5; i++) {
//            Card card = deck.dealCard();
//            player.addCard(card);
//        }
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasOnePair() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasOnePair());
    }

    @Test
    void hasOnePairFalseTest() {
        player.addCard(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));

//        for(int i=0; i<5; i++) {
//            Card card = deck.dealCard();
//            player.addCard(card);
//        }
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasOnePair() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasOnePair());
    }

    @Test
    void hasTwoPairsTrueTest() {
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasTwoPairs() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasTwoPairs());
    }

    @Test
    void hasTwoPairsFalseTest() {
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasTwoPairs() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasTwoPairs());
    }

    @Test
    void hasThreeOfAKindTrueTest() {
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasThreeOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasThreeOfAKind());
    }

    @Test
    void hasThreeOfAKindFalseTest() {
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.EIGHT, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasThreeOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasThreeOfAKind());
    }

    @Test
    void hasStraightTrueTest() {
        player.addCard(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraight() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasStraight());
    }

    @Test
    void hasStraightTrueWithAceTest() {
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraight() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasStraight());
    }

    @Test
    void hasStraightFalseTest() {
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraight() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasStraight());
    }

    @Test
    void hasFlushTrueTest() {
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasFlush());
    }

    @Test
    void hasFlushFalseTest() {
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasFlush());
    }

    @Test
    void hasFullHouseTrueTest() {
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFullHouse() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasFullHouse());
    }

    @Test
    void hasFullHouseFalseTest() {
        player.addCard(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFullHouse() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasFullHouse());
    }

    @Test
    void hasFourOfAKindTrueTest() {
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFourOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasFourOfAKind());
    }

    @Test
    void hasFourOfAKindFalseTest() {
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasFourOfAKind() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasFourOfAKind());
    }

    @Test
    void hasStraightFlushTrueTest() {
        player.addCard(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraightFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasStraightFlush());
    }

    @Test
    void hasStraightFlushFalseTest() {
        player.addCard(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.NINE, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasStraightFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasStraightFlush());
    }

    @Test
    void hasRoyalFlushTrueTest() {
        player.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasRoyalFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertTrue(hand.hasRoyalFlush());
    }

    @Test
    void hasRoyalFlushFalseTest() {
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        player.setHand();
        player.printCards();
        Hand hand = player.getHand();
        System.out.println(hand.hasRoyalFlush() + "|" + hand.getRanking() + "|" + hand.getLeadingCard().toString() + "|" + hand.getKicker().toString());
        assertFalse(hand.hasRoyalFlush());
    }
}
