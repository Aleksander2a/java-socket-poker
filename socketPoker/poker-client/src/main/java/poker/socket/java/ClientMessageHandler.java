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
            Game.Round gameRound = Game.Round.valueOf(msg.get("GameRound"));
            ArrayList<Card> playerCards = new ArrayList<>();
            if(msg.containsKey("AllFolded")) {
                if(!msg.get("AllFolded").equals("00")) {
                    System.out.println("Player" + msg.get("AllFolded") + " wins the pot, as everyone else folded! Starting new set...");
                }
            }
            if(!msg.containsKey("Bankrupt") && !msg.containsKey("Winner")) {
                gameInfoEncoder(msg.get("GameInfo"));
                System.out.println("Your cards:");
                Pattern cardPattern = Pattern.compile("Card[0-4]");
                String handToPrint = "|";
                for (String key : msg.keySet()) {
                    Matcher cardMatcher = cardPattern.matcher(key);
                    if (cardMatcher.find()) {
                        handToPrint += msg.get(key) + "|";
                        playerCards.add(new Card(msg.get(key)));
                    }
                }
                System.out.println(handToPrint);
            }
            if(!gameRound.equals(Game.Round.COMPARING_CARDS) && !msg.get("Turn").equals(msg.get("PlayerID"))) {
                System.out.println("NOT your turn! Press ENTER to refresh");
                inputString = scanner.nextLine();
                answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
                        + "-Decision:Refresh";
                return answer;
            }
            switch (gameRound) {
                case FIRST_BETTING:
                    System.out.println("Your turn!");
                    //if(Integer.parseInt(msg.get("MaxBid")) > Integer.parseInt(msg.get("MyBid"))) {}
                    //if(msg.get("MyAction").equals("NONE")) {
                    //TODO: Game verifies if the player needs to make a move
                    //int maxBidDiffMyBid = Integer.parseInt(msg.get("MaxBid")) - Integer.parseInt(msg.get("MyBid"));
                    int minBid = 0;
                    if(Integer.parseInt(msg.get("MaxBid")) > Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))) {
                        minBid = Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"));
                    }
                    else {
                        minBid = Integer.parseInt(msg.get("MaxBid"));
                    }
                    System.out.println("What do you want do: Fold OR Bid ?");
                    inputString = scanner.nextLine();
                    while(!inputString.equals("Fold") && !inputString.equals("Bid")) {
                        System.out.println("What do you want do: Fold OR Bid ?");
                        inputString = scanner.nextLine();
                    }
                    switch (inputString) {
//                               case "Check":
//                                    answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
//                                            + "-Decision:Check";
//                                    break;
                        case "Fold":
                            answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
                                    + "-Decision:Fold";
                            break;
                        case "Bid":
                            answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
                                    + "-Decision:Bid";
                            System.out.println("How much do you want to bid? " + minBid + "-" + (Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))));
                            inputString = scanner.nextLine();
                            while(inputString.equals("") || (Integer.parseInt(inputString) > (Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))) || Integer.parseInt(inputString) < minBid)) {
                                System.out.println("Invalid amount. Provide number between: " + minBid + "-" + (Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))));
                                inputString = scanner.nextLine();
                            }
                            answer += "-Bid:" + inputString;
                            break;
                    }
                    break;
                case CHANGE_CARDS:
                    answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID");
                    System.out.println("How many cards do you want to change? (0-5)");
                    inputString = scanner.nextLine();
                    while(Integer.parseInt(inputString) > 5 || Integer.parseInt(inputString) < 0) {
                        System.out.println("Invalid amount. Provide number between (0-5) ");
                        inputString = scanner.nextLine();
                    }
                    answer += "-Decision:" + inputString;
                    int nrOfCardsToChange = Integer.parseInt(inputString);
                    for(int i=0; i<nrOfCardsToChange; i++) {
                        boolean validCard = false;
                        System.out.println("What card do you want to change? (Rank Suit)");
                        inputString = scanner.nextLine();
                        //TODO: Handle invalid input
                        Pattern validCardPattern = Pattern.compile("[A-Za-z] [A-Za-z]");
                        Matcher validMatcher = validCardPattern.matcher(inputString);
                        while(!validMatcher.find()) {
                            System.out.println("Invalid input. Try again. (Rank Suit)");
                            inputString = scanner.nextLine();
                            validMatcher = validCardPattern.matcher(inputString);
                        }
                        String[] cardInput = inputString.split(" ");
                        for(Card c : playerCards) {
                            if(cardInput[0].equals(String.valueOf(c.getRank())) && cardInput[1].equals(String.valueOf(c.getSuit()))) {
                                validCard = true;
                                break;
                            }
                        }
                        while(!validCard) {
                            System.out.println("No such card. Provide a card that you have");
                            inputString = scanner.nextLine();
                            //TODO: Handle invalid input
                            validMatcher = validCardPattern.matcher(inputString);
                            while(!validMatcher.find()) {
                                System.out.println("Invalid input. Try again. (Rank Suit)");
                                inputString = scanner.nextLine();
                                validMatcher = validCardPattern.matcher(inputString);
                            }
                            cardInput = inputString.split(" ");
                            for(Card c : playerCards) {
                                if(c.getRank().equals(Card.Rank.valueOf(cardInput[0])) && c.getSuit().equals(Card.Suit.valueOf(cardInput[1]))) {
                                    validCard = true;
                                    break;
                                }
                            }
                        }
                        //Add info on cards to change to answer
                        answer += "-ToChange" + i + ":" + cardInput[0] + "," + cardInput[1];
                    }
                    break;
                case SECOND_BETTING:
                    System.out.println("Your turn!");
                    //if(Integer.parseInt(msg.get("MaxBid")) > Integer.parseInt(msg.get("MyBid"))) {}
                    //if(msg.get("MyAction").equals("NONE")) {
                    //TODO: Game verifies if the player needs to make a move
                    //int maxBidDiffMyBid = Integer.parseInt(msg.get("MaxBid")) - Integer.parseInt(msg.get("MyBid"));
                    if(Integer.parseInt(msg.get("MaxBid")) > Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))) {
                        minBid = Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"));
                    }
                    else {
                        minBid = Integer.parseInt(msg.get("MaxBid"));
                    }
                    System.out.println("What do you want do: Fold OR Bid ?");
                    inputString = scanner.nextLine();
                    while(!inputString.equals("Fold") && !inputString.equals("Bid")) {
                        System.out.println("What do you want do: Fold OR Bid ?");
                        inputString = scanner.nextLine();
                    }
                    switch (inputString) {
//                               case "Check":
//                                    answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
//                                            + "-Decision:Check";
//                                    break;
                        case "Fold":
                            answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
                                    + "-Decision:Fold";
                            break;
                        case "Bid":
                            answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID")
                                    + "-Decision:Bid";
                            System.out.println("How much do you want to bid? " + minBid + "-" + (Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))));
                            inputString = scanner.nextLine();
                            while(inputString.equals("") || (Integer.parseInt(inputString) > (Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))) || Integer.parseInt(inputString) < minBid)) {
                                System.out.println("Invalid amount. Provide number between: " + minBid + "-" + (Integer.parseInt(msg.get("MyMoney")) + Integer.parseInt(msg.get("MyBid"))));
                                inputString = scanner.nextLine();
                            }
                            answer += "-Bid:" + inputString;
                            break;
                    }
                    break;
//                case SET_OVER:
//                    System.out.println();
//                    break;
                case COMPARING_CARDS:
                    if(msg.containsKey("Bankrupt")) {
                        if(msg.get("Bankrupt").equals("Yes")) {
                            System.out.println("You are out of the game! Want to play again? (yes/no)");
                            inputString = scanner.nextLine();
                            while(!inputString.equals("yes") && !inputString.equals("no")) {
                                System.out.println("Want to play again? (yes/no)");
                                inputString = scanner.nextLine();
                            }
                            if(inputString.equals("yes")) {
                                //go to join/create game
                                answer += "State:JOIN_OR_CREATE_GAME-";
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
                                    //TODO: list available games with refresh option
                                    answer += "Decision:join-";
                                    System.out.println("Which game do you want to join?");
                                    inputString = scanner.nextLine();
                                    while (!availableGames.contains(inputString)) {
                                        System.out.println("Provide a valid game that is available");
                                        inputString = scanner.nextLine();
                                    }
                                    String chosenGame =  "" + inputString.charAt(4);
                                    answer += "Joins:" + chosenGame + "-";
                                    return answer;
                                }
                                else if(inputString.equals("new")) {
                                    answer += "Decision:new-";
                                    return answer;
                                }
                            }
                            else {
                                System.out.println("Bye");
                                System.exit(0);
                            }
                        }
                    }
                    if(msg.containsKey("Winner")) {
                        if(msg.get("Winner").equals("Yes")) {
                            System.out.println("You won the game! Want to play again? (yes/no)");
                            inputString = scanner.nextLine();
                            while(!inputString.equals("yes") && !inputString.equals("no")) {
                                System.out.println("Want to play again? (yes/no)");
                                inputString = scanner.nextLine();
                            }
                            if(inputString.equals("yes")) {
                                //go to join/create game
                                answer += "State:JOIN_OR_CREATE_GAME-";
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
                                    //TODO: list available games with refresh option
                                    answer += "Decision:join-";
                                    System.out.println("Which game do you want to join?");
                                    inputString = scanner.nextLine();
                                    while (!availableGames.contains(inputString)) {
                                        System.out.println("Provide a valid game that is available");
                                        inputString = scanner.nextLine();
                                    }
                                    String chosenGame =  "" + inputString.charAt(4);
                                    answer += "Joins:" + chosenGame + "-";
                                    return answer;
                                }
                                else if(inputString.equals("new")) {
                                    answer += "Decision:new-";
                                    return answer;
                                }
                            }
                            else {
                                System.out.println("Bye");
                                System.exit(0);
                            }
                        }
                    }
                    if(!msg.containsKey("PotWinner")) {
                        answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID") + "-Decision:Unseen";
                        System.out.println("Show off your cards! Press ENTER to continue");
                        inputString = scanner.nextLine();
                    }
                    else {
                        System.out.println("Player" + msg.get("PotWinner") + " won the pot!");
                        System.out.println("Press ENTER to continue");
                        inputString = scanner.nextLine();
                        answer = "State:IN_GAME-PlayerID:" + msg.get("PlayerID") + "-GameID:" + msg.get("GameID") + "-Decision:Seen";
                    }
                    break;
            }
        }
        return answer;
    }
}
