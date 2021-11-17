package poker.socket.java;

import java.util.*;

public class Hand {
    private Ranking ranking;
    private Card leadingCard;
    private Card kicker;
    private ArrayList<Card> playerCards;

    public enum Ranking {
        HIGH_CARD, ONE_PAIR, TWO_PAIRS, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE,
        FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    public Hand(ArrayList<Card> cards) {
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

    public boolean hasOnePair() {
        boolean toReturn = false;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(playerCards.get(j).getRank()==playerCards.get(i).getRank() && !playerCards.get(j).equals(playerCards.get(i))) {
                    ranking = Ranking.ONE_PAIR;
                    kicker = leadingCard;
                    leadingCard = playerCards.get(j);
                    toReturn = true;
                }
            }
        }
        return toReturn;
    }

    public boolean hasTwoPairs() {
        boolean toReturn = false;
        int countOfPairs = 0;
        Card.Rank pairOne = null;
        Card.Rank pairTwo = null;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(playerCards.get(j).getRank()==playerCards.get(i).getRank() && !playerCards.get(j).equals(playerCards.get(i))) {
                    if(pairOne == null) {
                        pairOne = playerCards.get(j).getRank();
                        countOfPairs++;
                    }
                    if(countOfPairs == 1 && playerCards.get(j).getRank() != pairOne && pairOne != null) {
                        pairTwo = playerCards.get(j).getRank();
                        countOfPairs++;
                        ranking = Ranking.TWO_PAIRS;
                        kicker = leadingCard;
                        leadingCard = playerCards.get(j);
                    }
                }
            }
        }
        return pairTwo != null;
        //return toReturn;
    }

    public boolean hasThreeOfAKind() {
        boolean toReturn = false;
        int countOfCard = 1;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(playerCards.get(j).getRank()==playerCards.get(i).getRank() && !playerCards.get(j).equals(playerCards.get(i))) {
                    countOfCard++;
                    if(countOfCard==3) {
                        ranking = Ranking.THREE_OF_A_KIND;
                        kicker = leadingCard;
                        leadingCard = playerCards.get(j);
                        toReturn = true;
                    }
                }
            }
            countOfCard = 0;
        }
        return toReturn;
    }

    public boolean hasStraight() {
        // TODO: think of an ACE !!!
        for(int i=0; i<4; i++) {
            if(playerCards.get(i+1).getRank().ordinal()-playerCards.get(i).getRank().ordinal() != 1) {
                return false;
            }
        }
        ranking = Ranking.STRAIGHT;
            leadingCard = playerCards.get(4);
            kicker = playerCards.get(3);
        return true;
    }

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

    //// TODO:
    boolean hasFullHouse() {
        return (hasOnePair() && hasThreeOfAKind());
    }
    ////

    boolean hasFourOfAKind() {
        Card.Rank first = playerCards.get(0).getRank();
        Card.Rank different = null;
        for(int i=1; i<5; i++) {
            if(playerCards.get(i).getRank() != first) {
                if(different == null) {
                    different = playerCards.get(i).getRank();
                }
                else if(playerCards.get(i).getRank() != different) {
                    return false;
                }
            }
        }
        ranking = Ranking.FOUR_OF_A_KIND;
        leadingCard = playerCards.get(4);
        kicker = playerCards.get(3);
        return true;
    }

    boolean hasStraightFlush() {
        if(hasFlush() && hasStraight()) {
            ranking = Ranking.STRAIGHT_FLUSH;
            leadingCard = playerCards.get(4);
            kicker = playerCards.get(3);
            return true;
        }
        return false;
    }

    boolean hasRoyalFlush() {
        if(hasStraightFlush() && playerCards.get(4).getRank() == Card.Rank.ACE) {
            ranking = Ranking.ROYAL_FLUSH;
            leadingCard = playerCards.get(4);
            kicker = playerCards.get(3);
            return true;
        }
        return false;
    }


}
