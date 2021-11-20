package poker.socket.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ClientMessageHandlerTest {
    @Test
    void encodeTest() {
        String s = "State:ENTERED-PlayerID:0-";
        LinkedHashMap<String, String> result = ClientMessageHandler.encode(s);
        System.out.println(result);
        assertTrue(true);
    }
}
