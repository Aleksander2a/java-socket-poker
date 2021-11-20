package poker.socket.java;

import java.util.*;

public class ClientMessageHandler {
    static LinkedHashMap<String, String> encode(String input) {
        String[] elements = input.split("-");
        List<String> list = Arrays.asList(elements);
        // Create map for e.g. key=State:value=ENTERED etc.
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for(String element : list) {
            String[] pair = element.split(":");
            result.put(pair[0], pair[1]);
        }
        return result;
    }

    static String answerToMessage(LinkedHashMap<String, String> msg) {
        Scanner scanner = new Scanner(System.in);
        String answer = "";
        String inputString = "";
        if(msg.get("State").equals("NEW_GAME")) {
            answer += "State:" + msg.get("State") + "-";
            answer += "PlayerID:" + msg.get("PlayerID") + "-";
            // Choose playerNumber
            System.out.println("How many players do you want to play? (2-4)");
            inputString = scanner.nextLine();;
            while (Integer.parseInt(inputString) > 4 || Integer.parseInt(inputString) < 2) {
                System.out.println("Give a valid number from 2 to 4");
                inputString = scanner.nextLine();
            }
            answer += "Players:" + inputString + "-";
            // Choose starting money
            System.out.println("How much money do you want for start? (500-1500)");
            inputString = scanner.nextLine();;
            while (Integer.parseInt(inputString) > 1500 || Integer.parseInt(inputString) < 500) {
                System.out.println("Give a valid number from 500 to 1500");
                inputString = scanner.nextLine();
            }
            answer += "StartingMoney:" + inputString + "-";
            // Choose ante
            System.out.println("How much money do you want for ante? (50-150)");
            inputString = scanner.nextLine();;
            while (Integer.parseInt(inputString) > 150 || Integer.parseInt(inputString) < 50) {
                System.out.println("Give a valid number from 50 to 150");
                inputString = scanner.nextLine();
            }
            answer += "Ante:" + inputString + "-";
        }
        if(msg.get("State").equals("WAITING_FOR_PLAYERS")) {
            int playersNeeded = Integer.parseInt(msg.get("MaxPlayers")) - Integer.parseInt(msg.get("NrOfPlayers"));
            System.out.println("GameID: " + msg.get("GameID") + " .Waiting for " + playersNeeded + " more player(s), press ENTER to refresh");
            inputString = scanner.nextLine();
            answer = "State:WAITING_FOR_PLAYERS-PlayerID:" + msg.get("PlayerID") + "-" + "GameID:" + msg.get("GameID");
        }
        return answer;
    }
}
