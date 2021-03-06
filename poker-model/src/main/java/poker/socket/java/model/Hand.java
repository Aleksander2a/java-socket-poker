package poker.socket.java.model;

import java.util.*;

/**
 * Class to represent value of player's cards
 */
public class Hand {
    private Ranking ranking;
    private Card leadingCard;
    private Card kicker;
    private List<Card> playerCards;

    public enum Ranking {
        HIGH_CARD, ONE_PAIR, TWO_PAIRS, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE,
        FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    /**
     * Initializes hand with given player's cards
     * @param cards Player's cards
     */
    public Hand(List<Card> cards) {
        playerCards = cards;
        ranking = Ranking.HIGH_CARD;
        leadingCard = cards.get(4);
        kicker = cards.get(3);
    }

    public Ranking getRanking() {
        return ranking;
    }

    public Card getLeadingCard() {
        return leadingCard;
    }

    public Card getKicker() {
        return kicker;
    }

    /**
     * Checks if there is one pair (if so, sets the leading card and a kicker).
     * @return True if there is a pair and false otherwise
     */
    public boolean hasOnePair() {
        boolean toReturn = false;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(playerCards.get(j).getRank()==playerCards.get(i).getRank() && !playerCards.get(j).equals(playerCards.get(i))) {
                    ranking = Ranking.ONE_PAIR;
                    leadingCard = playerCards.get(j);
                    toReturn = true;
                }
            }
        }
        for(int i=4; i>=0; i--) {
            if(playerCards.get(i).getRank() != leadingCard.getRank()) {
                kicker = playerCards.get(i);
                break;
            }
        }
        return toReturn;
    }

    /**
     * Checks if there are two pairs (if so, sets the leading card and a kicker).
     * @return True if there are two pairs and false otherwise
     */
    public boolean hasTwoPairs() {
        int countOfPairs = 0;
        Card pairOne = null;
        Card pairTwo = null;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(playerCards.get(j).getRank()==playerCards.get(i).getRank() && !playerCards.get(j).equals(playerCards.get(i))) {
                    if(pairOne == null) {
                        pairOne = playerCards.get(j);
                        countOfPairs++;
                    }
                    if(countOfPairs == 1 && playerCards.get(j).getRank() != pairOne.getRank() && pairOne != null) {
                        pairTwo = playerCards.get(j);
                        countOfPairs++;
                        ranking = Ranking.TWO_PAIRS;
                    }
                }
            }
        }
        if(pairTwo != null) {
            if(pairTwo.compareRankWith(pairOne) > 0) {
                leadingCard = pairTwo;
                kicker = pairOne;
            }
            else {
                leadingCard = pairOne;
                kicker = pairTwo;
            }
        }
        return pairTwo != null;
    }

    /**
     * Checks if there is three of a kind (if so, sets the leading card and a kicker).
     * @return True if there is three of a kind and false otherwise
     */
    public boolean hasThreeOfAKind() {
        boolean toReturn = false;
        int countOfCard = 1;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(playerCards.get(j).getRank()==playerCards.get(i).getRank() && !playerCards.get(j).equals(playerCards.get(i))) {
                    countOfCard++;
                    if(countOfCard==3) {
                        ranking = Ranking.THREE_OF_A_KIND;
                        leadingCard = playerCards.get(j);
                        toReturn = true;
                    }
                }
            }
            countOfCard = 0;
        }
        for(int i=4; i>=0; i--) {
            if(playerCards.get(i).getRank() != leadingCard.getRank()) {
                kicker = playerCards.get(i);
                break;
            }
        }
        return toReturn;
    }

    /**
     * Checks if there is a straight (if so, sets the leading card and a kicker).
     * @return True if there is a straight and false otherwise
     */
    public boolean hasStraight() {
        boolean toReturn = true;
        // Except case with ACE
        if(playerCards.get(4).getRank() != Card.Rank.ACE || playerCards.get(0).getRank() != Card.Rank.TWO) {
            for(int i=0; i<4; i++) {
                if(playerCards.get(i+1).getRank().ordinal()-playerCards.get(i).getRank().ordinal() != 1) {
                    toReturn = false;
                }
            }
        }
        // Case with an ACE !!!
        if(playerCards.get(4).getRank() == Card.Rank.ACE && playerCards.get(0).getRank() == Card.Rank.TWO) {
            for(int i=0; i<3; i++) {
                if(playerCards.get(i+1).getRank().ordinal()-playerCards.get(i).getRank().ordinal() != 1) {
                    toReturn = false;
                }
            }
        }
        if(toReturn) {
            ranking = Ranking.STRAIGHT;
            leadingCard = playerCards.get(4);
            kicker = playerCards.get(3);
        }
        return toReturn;
    }

    /**
     * Checks if there is a flush (if so, sets the leading card and a kicker).
     * @return True if there is a flush and false otherwise
     */
    public boolean hasFlush() {
        Card.Suit color = playerCards.get(0).getSuit();
        for(int i=1; i<5; i++) {
            if(playerCards.get(i).getSuit() != color) {
                return false;
            }
        }
        ranking = Ranking.FLUSH;
        leadingCard = playerCards.get(4);
        kicker = playerCards.get(3);
        return true;
    }

    /**
     * Checks if there is a full house (if so, sets the leading card and a kicker).
     * @return True if there is a full house and false otherwise
     */
    boolean hasFullHouse() {
        if(hasOnePair()) {
            Card pair = leadingCard;
            if(hasThreeOfAKind() && leadingCard.compareRankWith(pair) != 0) {
                    ranking = Ranking.FULL_HOUSE;
                    kicker = pair;
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a four of a kind (if so, sets the leading card and a kicker).
     * @return True if there is a four of a kind and false otherwise
     */
    boolean hasFourOfAKind() {
        Card first = playerCards.get(0);
        int firstCount = 1;
        Card different = null;
        int differentCount = 0;
        for(int i=1; i<5; i++) {
            if(playerCards.get(i).getRank() != first.getRank()) {
                if(different == null) {
                    different = playerCards.get(i);
                    differentCount++;
                }
                else if(playerCards.get(i).getRank() != different.getRank()) {
                    return false;
                }
            }
            else {
                firstCount++;
            }
        }
        ranking = Ranking.FOUR_OF_A_KIND;
        if(firstCount > differentCount) {
            leadingCard = first;
            kicker = different;
        }
        else {
            leadingCard = different;
            kicker = first;
        }
        return true;
    }

    /**
     * Checks if there is a straight flush (if so, sets the leading card and a kicker).
     * @return True if there is a straight flush and false otherwise
     */
    boolean hasStraightFlush() {
        if(hasFlush() && hasStraight()) {
            ranking = Ranking.STRAIGHT_FLUSH;
            leadingCard = playerCards.get(4);
            kicker = playerCards.get(3);
            return true;
        }
        return false;
    }

    /**
     * Checks if there is a royal flush (if so, sets the leading card and a kicker).
     * @return True if there is a royal flush and false otherwise
     */
    boolean hasRoyalFlush() {
        if(hasStraightFlush() && playerCards.get(4).getRank() == Card.Rank.ACE) {
            ranking = Ranking.ROYAL_FLUSH;
            leadingCard = playerCards.get(4);
            kicker = playerCards.get(3);
            return true;
        }
        return false;
    }

    public void hasHand() {
        hasOnePair();
        hasTwoPairs();
        hasThreeOfAKind();
        hasStraight();
        hasFlush();
        hasFullHouse();
        hasFourOfAKind();
        hasStraightFlush();
        hasRoyalFlush();
    }

    @Override
    public String toString() {
        return "Hand{" +
                "ranking=" + ranking +
                ", leadingCard=" + leadingCard +
                ", kicker=" + kicker +
                '}';
    }

    public String rankingToString() {
        String result = "";
        switch (ranking) {
            case HIGH_CARD:
                result += ranking + "," + leadingCard.getRank() + "," + kicker.getRank();
                break;
            case ONE_PAIR:
                result += ranking + "," + leadingCard.getRank() + "," + kicker.getRank();
                break;
            case TWO_PAIRS:
                result += ranking + "," + leadingCard.getRank() + "," + kicker.getRank();
                break;
            case THREE_OF_A_KIND:
                result += ranking + "," + leadingCard.getRank() + "," + kicker.getRank();
                break;
            case STRAIGHT:
                result += ranking + "," + leadingCard.getRank();
                break;
            case FLUSH:
                result += ranking + "," + leadingCard.getSuit() + "," + leadingCard.getRank();
                break;
            case FULL_HOUSE:
                result += ranking + "," + leadingCard.getRank() + "," + kicker.getRank();
                break;
            case FOUR_OF_A_KIND:
                result += ranking + "," + leadingCard.getRank() + "," + kicker.getRank();
                break;
            case STRAIGHT_FLUSH:
                result += ranking + "," + leadingCard.getSuit() + "," + leadingCard.getRank();
                break;
            case ROYAL_FLUSH:
                result += ranking + "," + leadingCard.getSuit() + "," + leadingCard.getRank();
                break;
        }
        return result;
    }

    public int compareTo(Hand otherHand) {
        if(ranking.ordinal() - otherHand.ranking.ordinal() > 0) {
            return 1;
        }
        else if(ranking.ordinal() - otherHand.ranking.ordinal() < 0) {
            return -1;
        }
        if(leadingCard.compareRankWith(otherHand.leadingCard) > 0) {
            return 1;
        }
        else if(leadingCard.compareRankWith(otherHand.leadingCard) < 0) {
            return -1;
        }
        if(kicker.compareRankWith(otherHand.kicker) > 0) {
            return 1;
        }
        else if(kicker.compareRankWith(otherHand.kicker) < 0) {
            return -1;
        }
        return 0;
    }

}
