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
}
