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
        for(Card card : deck.deck) {
            System.out.println(card.rank + " " + card.suit);
        }
    }
}
