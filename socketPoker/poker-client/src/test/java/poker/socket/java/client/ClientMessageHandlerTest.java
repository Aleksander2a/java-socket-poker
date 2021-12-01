package poker.socket.java.client;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ClientMessageHandlerTest {
    @Test
    public void encodeTest() {
        String s = "State:ENTERED-PlayerID:0-";
        LinkedHashMap<String, String> result = ClientMessageHandler.encode(s);
        System.out.println(result);
        Assert.assertTrue(true);
    }

    @Test
    public void encodeGameInfoTest() {
        String s = "Round=FIRST_BETTING|Pot=0,|Player0|Money=0|InGame=false|Dealer,|Player1|Money=0|InGame=false|Turn,";
        ClientMessageHandler.gameInfoEncoder(s);
        Assert.assertTrue(true);
    }

    @Test
    public void handRankEncoderHighCardTest() {
        String s = "HIGH_CARD,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("HIGH_CARD of ACE with kicker KING", result);
    }

    @Test
    public void handRankEncoderOnePairTest() {
        String s = "ONE_PAIR,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("ONE_PAIR of ACEs with kicker KING", result);
    }

    @Test
    public void handRankEncoderTwoPairsTest() {
        String s = "TWO_PAIRS,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("TWO_PAIRS of ACEs and KINGs", result);
    }

    @Test
    public void handRankEncoderThreeOfAKindTest() {
        String s = "THREE_OF_A_KIND,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("THREE_OF_A_KIND of ACEs with kicker KING", result);
    }

    @Test
    public void handRankEncoderStraightTest() {
        String s = "STRAIGHT,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("STRAIGHT up to ACE", result);
    }

    @Test
    public void handRankEncoderFlushTest() {
        String s = "FLUSH,HEARTS,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("FLUSH of HEARTS with high card KING", result);
    }

    @Test
    public void handRankEncoderFullHouseTest() {
        String s = "FULL_HOUSE,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("FULL_HOUSE of three ACEs and two KINGs", result);
    }

    @Test
    public void handRankEncoderFourOfAKindTest() {
        String s = "FOUR_OF_A_KIND,ACE,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("FOUR_OF_A_KIND of ACEs with kicker KING", result);
    }

    @Test
    public void handRankEncoderStraightFlushTest() {
        String s = "STRAIGHT_FLUSH,HEARTS,KING";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("STRAIGHT_FLUSH of HEARTS with high card KING", result);
    }

    @Test
    public void handRankEncoderRoyalFlushTest() {
        String s = "ROYAL_FLUSH,HEARTS,ACE";
        String result = ClientMessageHandler.handRankEncoder(s);
        Assert.assertEquals("ROYAL_FLUSH of HEARTS with high card ACE", result);
    }

//    @Test
//    public void answerToMessageTest1() {
//        String fromServer = "";
//        LinkedHashMap<String, String> encodedFromServer = ClientMessageHandler.encode(fromServer);
//        ClientMessageHandler.answerToMessage(encodedFromServer);
//        Assert.assertTrue(true);
//    }
}
