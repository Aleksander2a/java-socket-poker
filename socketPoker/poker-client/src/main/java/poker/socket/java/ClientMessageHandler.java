package poker.socket.java;

import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    static void gameInfoEncoder(String gameInfo) {
        String[] elements = gameInfo.split(",");
        for(String s : elements) {
            System.out.println(s);
        }
    }

    static String answerToMessage(LinkedHashMap<String, String> msg) {
        Scanner scanner = new Scanner(System.in);
        String answer = "";
        String inputString = "";
        if(msg.get("State").equals("JOIN_OR_CREATE_GAME")) {
            answer += "State:" + msg.get("State") + "-";
            answer += "PlayerID:" + msg.get("PlayerID") + "-";
            System.out.println("There are " + msg.get("GameNumber") + " game(s) to join:");
            Pattern gamePattern = Pattern.compile("Game[0-9]");
            Set<String> availableGames = new HashSet<>();
            for (String key : msg.keySet()) {
                Matcher gameMatcher = gamePattern.matcher(key);
                if(gameMatcher.find()) {
                    System.out.println(key + ": Max players: " + msg.get(key));
                    availableGames.add(key);
                }
            }
            System.out.println("Do you want to join an existing game or create new game?(join/new)");
            inputString = scanner.nextLine();
            while (!inputString.equals("join") && !inputString.equals("new")) {
                System.out.println("Provide valid input: \"join\" or \"new\"");
                inputString = scanner.nextLine();
            }
            if(inputString.equals("join")) {
                answer += "Decision:join-";
                System.out.println("Which game do you want to join?");
                inputString = scanner.nextLine();
                while (!availableGames.contains(inputString)) {
                    System.out.println("Provide a valid game that is available");
                    inputString = scanner.nextLine();
                }
                String chosenGame =  "" + inputString.charAt(4);
                answer += "Joins:" + chosenGame + "-";
            }
            else if(inputString.equals("new")) {
                answer += "Decision:new-";
            }
        }
        if(msg.get("State").equals("NEW_GAME")) {
            answer += "State:" + msg.get("State") + "-";
            answer += "PlayerID:" + msg.get("PlayerID") + "-";
            // Choose playerNumber
            System.out.println("How many players do you want to play? (2-4)");
            inputString = scanner.nextLine();
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
        if(msg.get("State").equals("IN_GAME")) {
            //TODO: receives info about game and reacts accordingly
            //System.out.println(msg.get("GameInfo"));
            //Game.Round gameRound =
            gameInfoEncoder(msg.get("GameInfo"));
            System.out.println("Your cards:");
            Pattern cardPattern = Pattern.compile("Card[0-4]");
            ArrayList<Card> playerCards = new ArrayList<>();
            String handToPrint = "|";
            for (String key : msg.keySet()) {
                Matcher cardMatcher = cardPattern.matcher(key);
                if(cardMatcher.find()) {
                    handToPrint += msg.get(key) + "|";
                    playerCards.add(new Card(msg.get(key)));
                }
            }
            System.out.println(handToPrint);
        }
        return answer;
    }
}
