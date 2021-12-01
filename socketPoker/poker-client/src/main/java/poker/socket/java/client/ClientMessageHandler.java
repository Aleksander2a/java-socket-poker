package poker.socket.java.client;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import poker.socket.java.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageHandler {

    private static final Logger LOGGER = Logger.getLogger( ClientMessageHandler.class.getName() );

    private static final String BANKRUPT = "Bankrupt";
    private static final String MAX_BID = "MaxBid";
    private static final String DECISION_BID_BID = "-Decision:Bid-Bid:";
    private static final String DECISION_FOLD = "-Decision:Fold";
    private static final String STATE_IN_GAME_PLAYER_ID = "State:IN_GAME-PlayerID:";
    private static final String INVALID_AMOUNT_PROVIDE_NUMBER_BETWEEN = "Invalid amount. Provide number between: ";
    private static final String HOW_MUCH_DO_YOU_WANT_TO_BID = "How much do you want to bid? ";
    private static final String GAME_ID_KEY = "-GameID:";
    private static final String GAME_ID = "GameID";
    private static final String BID = "-Bid:";
    private static final String DO_YOU_WANT_TO_PLAY_ALL_IN_YES_NO = "Do you want to play ALL-IN? (yes/no)";
    private static final String WINNER = "Winner";
    private static final String WHAT_DO_YOU_WANT_DO_FOLD_OR_BID = "What do you want do: Fold OR Bid ?";
    private static final String DECISION_BID = "-Decision:Bid";
    private static final String S_WITH_KICKER = "s with kicker ";
    private static final String WITH_HIGH_CARD = " with high card ";
    private static final String STATE = "State";
    private static final String PLAYER_ID_KEY = "PlayerID:";
    private static final String PLAYER_ID = "PlayerID";
    private static final String DECISION_REFRESH = "Decision:refresh-";
    private static final String ALL_FOLDED = "AllFolded";
    private static final String MY_MONEY = "MyMoney";
    private static final String MY_BID = "MyBid";
    private static final String TYPE_BID_TO_BID_FROM_0 = "Type \"Bid\" to bid from 0-";

    private ClientMessageHandler() {}

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
        PropertyConfigurator.configure("D:\\Studia AGH\\Programowanie zaawansowane 1\\Zadanie-1\\socketPoker\\poker-client\\src\\main\\resources\\log4j.properties");
        String[] elements = gameInfo.split(",");
        for(String s : elements) {
            LOGGER.info(s);
        }
    }

    static String handRankEncoder(String handRankInfo) {
        String result = "";
        String[] elements = handRankInfo.split(",");
        Hand.Ranking rank = Hand.Ranking.valueOf(elements[0]);
        switch (rank) {
            case HIGH_CARD:
                result += elements[0] + " of " + elements[1] + " with kicker " + elements[2];
                break;
            case ONE_PAIR:
                result += elements[0] + " of " + elements[1] + S_WITH_KICKER + elements[2];
                break;
            case TWO_PAIRS:
                result += elements[0] + " of " + elements[1] + "s and " + elements[2] +"s";
                break;
            case THREE_OF_A_KIND:
                result += elements[0] + " of " + elements[1] + S_WITH_KICKER + elements[2];
                break;
            case STRAIGHT:
                result += elements[0] + " up to " + elements[1];
                break;
            case FLUSH:
                result += elements[0] + " of " + elements[1] + WITH_HIGH_CARD + elements[2];
                break;
            case FULL_HOUSE:
                result += elements[0] + " of three " + elements[1] + "s and two " + elements[2] +"s";
                break;
            case FOUR_OF_A_KIND:
                result += elements[0] + " of " + elements[1] + S_WITH_KICKER + elements[2];
                break;
            case STRAIGHT_FLUSH:
                result += elements[0] + " of " + elements[1] + WITH_HIGH_CARD + elements[2];
                break;
            case ROYAL_FLUSH:
                result += elements[0] + " of " + elements[1] + WITH_HIGH_CARD + elements[2];
                break;
        }
        return result;
    }

    static String answerToMessage(LinkedHashMap<String, String> msg) {
        PropertyConfigurator.configure("D:\\Studia AGH\\Programowanie zaawansowane 1\\Zadanie-1\\socketPoker\\poker-client\\src\\main\\resources\\log4j.properties");
        Scanner scanner = new Scanner(System.in);
        StringBuilder answer = new StringBuilder();
        String inputString = "";
        if(msg.get(STATE).equals("JOIN_OR_CREATE_GAME")) {
            LOGGER.info("Welcome to the POKER SERVER!");
            answer.append("State:").append(msg.get(STATE)).append("-");
            answer.append(PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append("-");
            Pattern gamePattern = Pattern.compile("Game[0-9]");
            Set<String> availableGames = new HashSet<>();
            for (String key : msg.keySet()) {
                Matcher gameMatcher = gamePattern.matcher(key);
                if(gameMatcher.find()) {
                    availableGames.add(key);
                }
            }
            if(availableGames.isEmpty()) {
                LOGGER.info("There are no games right now. Do you want to create a new game or refresh? (new/refresh)");
                inputString = scanner.nextLine();
                while (!inputString.equals("new") && !inputString.equals("refresh")) {
                    LOGGER.info("Provide valid input: \"new\" or \"refresh\"");
                    inputString = scanner.nextLine();
                }
                if(inputString.equals("new")) {
                    answer.append("Decision:new-");
                }
                else {
                    answer.append(DECISION_REFRESH);
                }
            }
            else {
                LOGGER.info("There is(are) " + msg.get("GameNumber") + " game(s) to join:");
                for(Map.Entry<String, String> entry : msg.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Matcher gameMatcher = gamePattern.matcher(key);
                    if(gameMatcher.find()) {
                        LOGGER.info(key + ": Max players: " + value);
                    }
                }
                LOGGER.info("Do you want to join an existing game, create new game or refresh?(join/new/refresh)");
                inputString = scanner.nextLine();
                while (!inputString.equals("join") && !inputString.equals("new") && !inputString.equals("refresh")) {
                    LOGGER.info("Provide valid input: \"join\" or \"new\" or \"refresh\"");
                    inputString = scanner.nextLine();
                }
                if (inputString.equals("join")) {
                    answer.append("Decision:join-");
                    LOGGER.info("Which game do you want to join?");
                    inputString = scanner.nextLine();
                    while (!availableGames.contains(inputString)) {
                        LOGGER.info("Provide a valid game that is available");
                        inputString = scanner.nextLine();
                    }
                    String chosenGame = "" + inputString.charAt(4);
                    answer.append("Joins:").append(chosenGame).append("-");
                } else if (inputString.equals("new")) {
                    answer.append("Decision:new-");
                }
                else {
                    answer.append(DECISION_REFRESH);
                }
            }
        }
        if(msg.get(STATE).equals("NEW_GAME")) {
            answer.append("State:").append(msg.get(STATE)).append("-");
            answer.append(PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append("-");
            // Choose playerNumber
            LOGGER.info("How many players do you want to play? (2-4)");
            inputString = scanner.nextLine();
            while (Integer.parseInt(inputString) > 4 || Integer.parseInt(inputString) < 2) {
                LOGGER.info("Give a valid number from 2 to 4");
                inputString = scanner.nextLine();
            }
            answer.append("Players:").append(inputString).append("-");
            // Choose starting money
            LOGGER.info("How much money do you want for start? (500-1500)");
            inputString = scanner.nextLine();
            while (Integer.parseInt(inputString) > 1500 || Integer.parseInt(inputString) < 500) {
                LOGGER.info("Give a valid number from 500 to 1500");
                inputString = scanner.nextLine();
            }
            answer.append("StartingMoney:").append(inputString).append("-");
            // Choose ante
            LOGGER.info("How much money do you want for ante? (50-150)");
            inputString = scanner.nextLine();
            while (Integer.parseInt(inputString) > 150 || Integer.parseInt(inputString) < 50) {
                LOGGER.info("Give a valid number from 50 to 150");
                inputString = scanner.nextLine();
            }
            answer.append("Ante:").append(inputString).append("-");
        }
        if(msg.get(STATE).equals("WAITING_FOR_PLAYERS")) {
            int playersNeeded = Integer.parseInt(msg.get("MaxPlayers")) - Integer.parseInt(msg.get("NrOfPlayers"));
            LOGGER.info("GameID: " + msg.get(GAME_ID) + " .Waiting for " + playersNeeded + " more player(s), press ENTER to refresh");
            scanner.nextLine();
            answer = new StringBuilder("State:WAITING_FOR_PLAYERS-PlayerID:" + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID));
        }
        if(msg.get(STATE).equals("IN_GAME")) {
            Game.Round gameRound = Game.Round.valueOf(msg.get("GameRound"));
            ArrayList<Card> playerCards = new ArrayList<>();
            if(msg.containsKey(ALL_FOLDED) && !msg.get(ALL_FOLDED).equals("00")) {
                LOGGER.info("Player" + msg.get(ALL_FOLDED) + " wins the pot, as everyone else folded! Starting new set...");
            }
            if(!msg.containsKey(BANKRUPT) && !msg.containsKey(WINNER)) {
                gameInfoEncoder(msg.get("GameInfo"));
                LOGGER.info("Your cards:");
                Pattern cardPattern = Pattern.compile("Card[0-4]");
                StringBuilder handToPrint = new StringBuilder("|");
                for(Map.Entry<String, String> entry : msg.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Matcher cardMatcher = cardPattern.matcher(key);
                    if (cardMatcher.find()) {
                        String[] cardInfo = value.split(",");
                        handToPrint.append(cardInfo[0]).append(" ").append(cardInfo[1]).append("|");
                        playerCards.add(new Card(value));
                    }
                }
                LOGGER.info(handToPrint.toString());
                LOGGER.info("Your hand is:");
                LOGGER.info(handRankEncoder(msg.get("PlayerHandRank")));

            }
            if(!gameRound.equals(Game.Round.COMPARING_CARDS) && !msg.get("Turn").equals(msg.get(PLAYER_ID))) {
                LOGGER.info("NOT your turn! Press ENTER to refresh");
                scanner.nextLine();
                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID)
                        + "-Decision:Refresh");
                return answer.toString();
            }
            switch (gameRound) {
                case FIRST_BETTING:
                    LOGGER.info("Your turn!");
                    answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID));
                    int minBid = 0;
                    int maxBid = Integer.parseInt(msg.get(MAX_BID));
                    int myMoney = Integer.parseInt(msg.get(MY_MONEY));
                    int myBid = Integer.parseInt(msg.get(MY_BID));
                    if(Integer.parseInt(msg.get(MAX_BID)) > Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))) {
                        minBid = Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID));
                    }
                    else {
                        minBid = Integer.parseInt(msg.get(MAX_BID));
                    }
                    if(myBid == maxBid) {
                        answer.append(DECISION_BID);
                        LOGGER.info(TYPE_BID_TO_BID_FROM_0 + (myMoney+myBid));
                        inputString = scanner.nextLine();
                        while(!inputString.equals("Bid")) {
                            LOGGER.info(TYPE_BID_TO_BID_FROM_0 + (myMoney+myBid));
                            inputString = scanner.nextLine();
                        }
                        LOGGER.info(HOW_MUCH_DO_YOU_WANT_TO_BID + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                        inputString = scanner.nextLine();
                        while (inputString.equals("") || (Integer.parseInt(inputString) > (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))) || Integer.parseInt(inputString) < minBid)) {
                            LOGGER.info(INVALID_AMOUNT_PROVIDE_NUMBER_BETWEEN + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                            inputString = scanner.nextLine();
                        }
                        answer.append(BID).append(inputString);
                    }
                    else if(myMoney==0) {
                        answer.append(DECISION_BID_BID).append(myBid);
                    }
                    else if(maxBid > myBid + myMoney) {
                        LOGGER.info(DO_YOU_WANT_TO_PLAY_ALL_IN_YES_NO);
                        inputString = scanner.nextLine();
                        while(!inputString.equals("yes") && !inputString.equals("no")) {
                            LOGGER.info(DO_YOU_WANT_TO_PLAY_ALL_IN_YES_NO);
                            inputString = scanner.nextLine();
                        }
                        if(inputString.equals("yes")) {
                            answer.append(DECISION_BID_BID).append(myBid + myMoney);
                        }
                        else {
                            answer.append(DECISION_FOLD);
                        }
                    }
                    else {
                        LOGGER.info(WHAT_DO_YOU_WANT_DO_FOLD_OR_BID);
                        inputString = scanner.nextLine();
                        while (!inputString.equals("Fold") && !inputString.equals("Bid")) {
                            LOGGER.info(WHAT_DO_YOU_WANT_DO_FOLD_OR_BID);
                            inputString = scanner.nextLine();
                        }
                        switch (inputString) {
                            case "Fold":
                                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID)
                                        + DECISION_FOLD);
                                break;
                            case "Bid":
                                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID)
                                        + DECISION_BID);
                                LOGGER.info(HOW_MUCH_DO_YOU_WANT_TO_BID + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                                inputString = scanner.nextLine();
                                while (inputString.equals("") || (Integer.parseInt(inputString) > (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))) || Integer.parseInt(inputString) < minBid)) {
                                    LOGGER.info(INVALID_AMOUNT_PROVIDE_NUMBER_BETWEEN + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                                    inputString = scanner.nextLine();
                                }
                                answer.append(BID).append(inputString);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case CHANGE_CARDS:
                    answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID));
                    LOGGER.info("How many cards do you want to change? (0-5)");
                    inputString = scanner.nextLine();
                    while(Integer.parseInt(inputString) > 5 || Integer.parseInt(inputString) < 0) {
                        LOGGER.info("Invalid amount. Provide number between (0-5) ");
                        inputString = scanner.nextLine();
                    }
                    answer.append("-Decision:").append(inputString);
                    int nrOfCardsToChange = Integer.parseInt(inputString);
                    for(int i=0; i<nrOfCardsToChange; i++) {
                        boolean validCard = false;
                        LOGGER.info("What card do you want to change? (RANK SUIT)");
                        inputString = scanner.nextLine();
                        Pattern validCardPattern = Pattern.compile("[A-Za-z] [A-Za-z]");
                        Matcher validMatcher = validCardPattern.matcher(inputString);
                        while(!validMatcher.find()) {
                            LOGGER.info("Invalid input. Try again. (RANK SUIT)");
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
                            LOGGER.info("No such card. Provide a card that you have");
                            inputString = scanner.nextLine();
                            validMatcher = validCardPattern.matcher(inputString);
                            while(!validMatcher.find()) {
                                LOGGER.info("Invalid input. Try again. (RANK SUIT)");
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
                        // Add info on cards to change to answer
                        answer.append("-ToChange").append(i).append(":").append(cardInput[0]).append(",").append(cardInput[1]);
                    }
                    break;
                case SECOND_BETTING:
                    LOGGER.info("Your turn!");
                    answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID));
                    maxBid = Integer.parseInt(msg.get(MAX_BID));
                    myMoney = Integer.parseInt(msg.get(MY_MONEY));
                    myBid = Integer.parseInt(msg.get(MY_BID));
                    if(Integer.parseInt(msg.get(MAX_BID)) > Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))) {
                        minBid = Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID));
                    }
                    else {
                        minBid = Integer.parseInt(msg.get(MAX_BID));
                    }
                    if(myBid == maxBid) {
                        answer.append(DECISION_BID);
                        LOGGER.info(TYPE_BID_TO_BID_FROM_0 + (myMoney+myBid));
                        inputString = scanner.nextLine();
                        while(!inputString.equals("Bid")) {
                            LOGGER.info(TYPE_BID_TO_BID_FROM_0 + (myMoney+myBid));
                            inputString = scanner.nextLine();
                        }
                        LOGGER.info(HOW_MUCH_DO_YOU_WANT_TO_BID + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                        inputString = scanner.nextLine();
                        while (inputString.equals("") || (Integer.parseInt(inputString) > (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))) || Integer.parseInt(inputString) < minBid)) {
                            LOGGER.info(INVALID_AMOUNT_PROVIDE_NUMBER_BETWEEN + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                            inputString = scanner.nextLine();
                        }
                        answer.append(BID).append(inputString);
                    }
                    else if(myMoney==0) {
                        answer.append(DECISION_BID_BID).append(myBid);
                    }
                    else if(maxBid > myBid + myMoney) {
                        LOGGER.info(DO_YOU_WANT_TO_PLAY_ALL_IN_YES_NO);
                        inputString = scanner.nextLine();
                        while(!inputString.equals("yes") && !inputString.equals("no")) {
                            LOGGER.info(DO_YOU_WANT_TO_PLAY_ALL_IN_YES_NO);
                            inputString = scanner.nextLine();
                        }
                        if(inputString.equals("yes")) {
                            answer.append(DECISION_BID_BID).append(myBid + myMoney);
                        }
                        else {
                            answer.append(DECISION_FOLD);
                        }
                    }
                    else {
                        LOGGER.info(WHAT_DO_YOU_WANT_DO_FOLD_OR_BID);
                        inputString = scanner.nextLine();
                        while (!inputString.equals("Fold") && !inputString.equals("Bid")) {
                            LOGGER.info(WHAT_DO_YOU_WANT_DO_FOLD_OR_BID);
                            inputString = scanner.nextLine();
                        }
                        switch (inputString) {
                            case "Fold":
                                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID)
                                        + DECISION_FOLD);
                                break;
                            case "Bid":
                                answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID)
                                        + DECISION_BID);
                                LOGGER.info(HOW_MUCH_DO_YOU_WANT_TO_BID + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                                inputString = scanner.nextLine();
                                while (inputString.equals("") || (Integer.parseInt(inputString) > (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))) || Integer.parseInt(inputString) < minBid)) {
                                    LOGGER.info(INVALID_AMOUNT_PROVIDE_NUMBER_BETWEEN + minBid + "-" + (Integer.parseInt(msg.get(MY_MONEY)) + Integer.parseInt(msg.get(MY_BID))));
                                    inputString = scanner.nextLine();
                                }
                                answer.append(BID).append(inputString);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case COMPARING_CARDS:
                    if(msg.containsKey(BANKRUPT) && msg.get(BANKRUPT).equals("Yes")) {
                        LOGGER.info("You are out of the game! Want to play again? (yes/no)");
                            inputString = scanner.nextLine();
                            while(!inputString.equals("yes") && !inputString.equals("no")) {
                                LOGGER.info("Want to play again? (yes/no)");
                                inputString = scanner.nextLine();
                            }
                            if(inputString.equals("yes")) {
                                //go to join/create game
                                answer.append("State:JOIN_OR_CREATE_GAME-");
                                answer.append(PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append("-");
                                answer.append(DECISION_REFRESH);
                                return answer.toString();
                            }
                            else {
                                LOGGER.info("Bye");
                                answer = new StringBuilder("Quit");
                                return answer.toString();
                            }
                    }
                    if(msg.containsKey(WINNER) && msg.get(WINNER).equals("Yes")) {
                        LOGGER.info("You won the game! Want to play again? (yes/no)");
                            inputString = scanner.nextLine();
                            while(!inputString.equals("yes") && !inputString.equals("no")) {
                                LOGGER.info("Want to play again? (yes/no)");
                                inputString = scanner.nextLine();
                            }
                            if(inputString.equals("yes")) {
                                answer.append("State:JOIN_OR_CREATE_GAME-");
                                answer.append(PLAYER_ID_KEY).append(msg.get(PLAYER_ID)).append("-");
                                answer.append(DECISION_REFRESH);
                                return answer.toString();
                            }
                            else {
                                LOGGER.info("Bye");
                                answer = new StringBuilder("Quit");
                                return answer.toString();
                            }
                    }
                    if(!msg.containsKey("PotWinner")) {
                        answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID) + "-Decision:Unseen");
                        LOGGER.info("Show off your cards! Press ENTER to continue");
                        scanner.nextLine();
                    }
                    else {
                        LOGGER.info("Player" + msg.get("PotWinner") + " won the main pot with " + handRankEncoder(msg.get("WinnerHand")) + "!");
                        LOGGER.info("Press ENTER to continue");
                        scanner.nextLine();
                        answer = new StringBuilder(STATE_IN_GAME_PLAYER_ID + msg.get(PLAYER_ID) + GAME_ID_KEY + msg.get(GAME_ID) + "-Decision:Seen");
                    }
                    break;
                default:
                    break;
            }
        }
        return answer.toString();
    }
}
