package poker.socket.java;

import java.util.ArrayList;

/**
 * A class to represent a single game on the server
 */
public class Game {
    private int id;
    private int ante;
    private int playersNumber;
    private ArrayList<Player> players;

    public Game() {}

    public Game(int gameId, int gameAnte, int maxPlayers, int startingMoney, ArrayList<Player> gamePlayers) {
        this.id = gameId;
        this.ante = gameAnte;
        this.playersNumber = maxPlayers;
        this.players = gamePlayers;
        for(Player player : players) {
            player.setMoney(startingMoney);
            player.setCurrentGameId(id);
        }
    }
}
