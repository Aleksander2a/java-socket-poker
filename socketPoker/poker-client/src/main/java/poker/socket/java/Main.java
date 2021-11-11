package poker.socket.java;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        System.out.println( "hello world!" );
        System.out.println( "Szablon projektu z metodą main i zależnościami wbudowanymi w wykonywalny jar" );
        //wywołanie metody generującej hash SHA-512
        System.out.println("HASH 512 dla słowa test: " + TextUtils.sha512Hash("test"));
        Deck deck = new Deck();
        deck.writeDeck();
        System.out.println(deck.getDeckSize());
        //System.out.println(Card.Rank.KING.compareTo(Card.Rank.ACE));
        Card randomCard = deck.dealCard();
        System.out.println("--------");
        System.out.println(randomCard.toString());
        System.out.println(deck.getDeckSize());
        System.out.println(deck.hasCard(randomCard));
        deck.addCard(randomCard);
        System.out.println(deck.getDeckSize());
        System.out.println(deck.hasCard(randomCard));
    }
}
