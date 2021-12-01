package poker.socket.java.server;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

/**
 * Unit test for simple Server.
 */
public class ServerMessageHandlerTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void encodeTest() {
        String s = "State:JOIN_OR_CREATE_GAME-PlayerID:0-Decision:refresh-";
        LinkedHashMap<String, String> result = ServerMessageHandler.encode(s);
        System.out.println(result);
        Assert.assertTrue(true);
    }
    @Test
    public void answerToMessageTest1() {
        String fromClient = "State:JOIN_OR_CREATE_GAME-PlayerID:0-Decision:refresh-";
        LinkedHashMap<String, String> encodedFromClient = ServerMessageHandler.encode(fromClient);
        String answer = ServerMessageHandler.answerToMessage(encodedFromClient);
        //Assert.assertTrue(true);
        Assert.assertEquals("State:JOIN_OR_CREATE_GAME-PlayerID:0-GameNumber:0-", answer);
    }
    @Test
    public void answerToMessageTest2() {
        String fromClient = "State:NEW_GAME-PlayerID:0-Players:2-StartingMoney:500-Ante:50-";
        LinkedHashMap<String, String> encodedFromClient = ServerMessageHandler.encode(fromClient);
        String answer = ServerMessageHandler.answerToMessage(encodedFromClient);
        //Assert.assertTrue(true);
        Assert.assertEquals("State:WAITING_FOR_PLAYERS-PlayerID:0-GameID:0-NrOfPlayers:0-MaxPlayers:2", answer);
    }
//    @Test
//    public void answerToMessageTest3() {
//        String fromClient = " State:JOIN_OR_CREATE_GAME-PlayerID:1-Decision:join-Joins:0-";
//        LinkedHashMap<String, String> encodedFromClient = ServerMessageHandler.encode(fromClient);
//        ServerMessageHandler.answerToMessage(encodedFromClient);
//        Assert.assertTrue(true);
//    }
}
