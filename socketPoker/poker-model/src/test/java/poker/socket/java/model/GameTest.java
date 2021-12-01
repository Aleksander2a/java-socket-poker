package poker.socket.java.model;

import org.junit.Test;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    Game game = new Game();
    Player player0 = new Player(0);
    Player player1 = new Player(1);
    List<Player> players = new ArrayList<>();


    @Test
    public void takeAnteFromPlayerTest() {
            game.setId(0);
            game.setAnte(5);
            game.setMaxPlayersNumber(2);
            game.setStartingMoney(1000);
            game.addPlayer(player0);
            game.addPlayer(player1);
            player0.setMoney(1000);
            player1.setMoney(1000);
            game.takeAnteFromPlayer();
            boolean result = true;
            for(Player p : game.gamePlayers()) {
                if(p.getMoney()!=995) {
                    result = false;
                }
            }
            Assert.assertTrue(result);
    }

    @Test
    public void dealCardsToActivePlayersTest() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        game.dealCardsToActivePlayers();
        boolean result = true;
        for(Player p : game.activePlayers()) {
            if(p.getCards().size()<5) {
                result = false;
            }
        }
        Assert.assertTrue(result);
    }

    @Test
    public void gameInfoTest() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.dealCardsToActivePlayers();
        game.setDealer(player0);
        game.setPlayerTurn(player1);
        game.setRound(Game.Round.FIRST_BETTING);
        String result = game.gameInfo();
        String expected = "Round=FIRST_BETTING|Pot=0,|Player0|Money=0|InGame=false|Dealer,|Player1|Money=0|InGame=false|Turn,";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void addActivePlayerTest() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        game.addActivePlayer(player1);
        Assert.assertEquals(2, game.activePlayers().size());
    }

    @Test
    public void updatePotTest() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.setPot(0);
        player0.setInPot(10);
        player1.setInPot(10);
        game.updatePot();
        Assert.assertEquals(20, game.getPot());
    }

    @Test
    public void giveMoneyToPlayersTest() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.giveMoneyToPlayers();
        boolean result = true;
        for(Player p : game.gamePlayers()) {
            if(p.getMoney()!=1000) {
                result = false;
            }
        }
        Assert.assertTrue(result);
    }

    @Test
    public void initializeGameTest() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.initializeGame();
        Assert.assertTrue(game.isInitialized());
    }

    @Test
    public void nextPhaseTest() {
        game.setRound(Game.Round.FIRST_BETTING);
        game.nextPhase();
        game.nextPhase();
        game.nextPhase();
        Assert.assertEquals(Game.Round.COMPARING_CARDS, game.getRound());
    }

    @Test
    public void proceedBettingRoundTest1() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        game.setRound(Game.Round.FIRST_BETTING);
        game.initializeGame();
        game.proceedBettingRound();
        Assert.assertEquals(Game.Round.FIRST_BETTING, game.getRound());
    }

    @Test
    public void proceedBettingRoundTest2() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        game.setRound(Game.Round.FIRST_BETTING);
        player0.setActive(true);
        player1.setActive(true);
        game.setMaxBid(10);
        player0.setInPot(10);
        player0.setInPot(10);
        game.initializeGame();
        player0.setAction(Player.Action.FOLD);
        player1.setAction(Player.Action.FOLD);
        player0.setBid(10);
        player1.setBid(10);
        game.proceedBettingRound();
        Assert.assertEquals(Game.Round.SET_OVER, game.getRound());
    }

    @Test
    public void proceedBettingRoundTest3() {
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        game.setRound(Game.Round.FIRST_BETTING);
        player0.setInPot(10);
        player0.setInPot(10);
        game.initializeGame();
        player0.setActive(true);
        player1.setActive(true);
        player0.setAction(Player.Action.BID);
        player1.setAction(Player.Action.BID);
        game.setMaxBid(10);
        player0.setBid(game.getMaxBid());
        player1.setBid(game.getMaxBid());
        System.out.println(game.gameInfo());
        game.proceedBettingRound();
        Assert.assertEquals(Game.Round.CHANGE_CARDS, game.getRound());
    }

    @Test
    public void comparePlayersCardsTest() {
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        player0.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player0.addCard(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        player0.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        player0.addCard(new Card(Card.Rank.FIVE, Card.Suit.DIAMONDS));
        player0.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player0.setHand();

        player1.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        player1.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player1.addCard(new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        player1.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player1.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        player1.setHand();
        game.comparePlayersCards();
        Assert.assertEquals(1,game.getPlayersByHand().get(0).getId());
    }

    @Test
    public void distributePot() {
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        player0.setInPot(10);
        player1.setInPot(10);
        player0.setAction(Player.Action.BID);
        player1.setAction(Player.Action.BID);
        game.setPot(30);
        player0.addCard(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        player0.addCard(new Card(Card.Rank.THREE, Card.Suit.SPADES));
        player0.addCard(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        player0.addCard(new Card(Card.Rank.FIVE, Card.Suit.DIAMONDS));
        player0.addCard(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        player0.setHand();

        player1.addCard(new Card(Card.Rank.ACE, Card.Suit.HEARTS));
        player1.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player1.addCard(new Card(Card.Rank.QUEEN, Card.Suit.CLUBS));
        player1.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        player1.addCard(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        player1.setHand();
        game.comparePlayersCards();
        game.distributePot();
        Assert.assertEquals(20, player1.getMoney());
    }

    @Test
    public void proceedAfterComparingCards() {
        game.addPlayer(player0);
        game.addPlayer(player1);
        game.addActivePlayer(player0);
        game.addActivePlayer(player1);
        player0.setInPot(10);
        player1.setInPot(10);
        player0.setAction(Player.Action.BID);
        player1.setAction(Player.Action.BID);
        player0.setActive(true);
        player1.setActive(true);
        player0.setMoney(100);
        player1.setMoney(100);
        game.setPot(30);
        game.dealCardsToActivePlayers();
        game.setDealer(player0);
        game.setPlayerTurn(player1);
        game.proceedAfterComparingCards();
        Assert.assertEquals(1, game.getDealer().getId());
    }

    @Test
    public void multiTest() {
        boolean result = true;
        game.setId(0);
        game.setAnte(5);
        game.setMaxPlayersNumber(2);
        game.setStartingMoney(1000);
        game.addPlayer(player0);
        game.addPlayer(player1);
        player0.setMoney(1000);
        player1.setMoney(1000);
        int id = game.getId();
        int startingMoney= game.getStartingMoney();
        int ante = game.getAnte();
        int maxPlayers = game.getMaxPlayersNumber();
        game.setDealer(player0);
        game.setPlayerTurn(player1);
        Player p = game.getPlayerTurn();
        int playersNumber = game.getPlayersNumber();
        String areFolded = game.isAllFolded();
        if(id!=0 || ante!=5 || startingMoney!=1000 || maxPlayers!=2 || p.getId()!=1 || playersNumber!=2 || !areFolded.equals("00")) {
            result = false;
        }
        game.setAllFolded("0");
        Deck deck = game.getDeck();
        Player p1 = game.getPlayerAtIndex(1);
        if(!game.isAllFolded().equals("0") || deck==null || p1.getId()!=1) {
            result = false;
        }
        Player potWinner = game.getPotWinner();
        if(potWinner!=null) {
            result = false;
        }
        game.setPotWinner(player0);
        potWinner = game.getPotWinner();
        if(potWinner.getId()!=player0.getId()) {
            result = false;
        }
        if(game.isFinished()) {
            result = false;
        }
        game.setFinished(true);
        if(!game.isFinished()) {
            result = false;
        }
        game.setFinished(false);
        if(game.isSetProceeded()) {
            result = false;
        }
        game.setSetProceeded(true);
        if(!game.isSetProceeded()) {
            result = false;
        }
        game.setSetProceeded(false);
        game.setRoundProceeded(false);
        if(game.isRoundProceeded() || game.getPlayersToProceedRound()>0) {
            result = false;
        }
        game.setPlayersToProceedRound(2);
        game.updatePlayersToProceedRound(1);
        if(game.getPlayersToProceedRound()!=3 || game.getMainPotWon()!=0 || !game.getWinningHand().equals("none")) {
            result = false;
        }

        Assert.assertTrue(result);
    }
}
