# Java Socket Poker
This is an implementation of 5-card poker, with two betting rounds, with a round to change cards between them.
## How to run
In main directory, run ``mvn clean package`` to build the project. 
Then go to ``socketPoker/poker-server`` directory start server with command
````java -jar ./target/poker-server-1.0-jar-with-dependencies.jar <port number>```` 
(choose a valid `<port number>`, e.g. 4444).
You will be shown a welcome message with the name of the host, that you will need to
provide when starting client.
Message will be something like:
```Welcome to server: DESKTOP-C4BFC07```, note that ``DESKTOP-C4BFC07``
is the host name.
Now, for every player in the game, run ```java -jar ./target/poker-client-1.0-jar-with-dependencies.jar <host name> <port number>```.
Following previous examples, your command should be ```java -jar ./target/poker-client-1.0-jar-with-dependencies.jar DESKTOP-C4BFC07 4444```.

If everything went well, you will be getting ready to play!

In case of any errors, you will be given instructions that should guide you the right way.

## Create or join a game
After connecting to the server, you will be able to create new game (``new``), join an existing
game (``join``) or refresh (``refresh``), if you expect someone else to create a game for you.

### New game
If you chose ``new``, you will be asked three additional questions, to 
set the players number, starting money and ante for the game.

### Join game
If you chose ``join``, you will see available games e.g. ``Game0: Max players: 2``.
You will need to provide name of the game you want to join, which for the
above example would be ``Game0``.

## How to play
When there is enough players,the game will start.
Basically, just follow the instructions and do what you are asked for.

Everytime the turn is not yours, press ENTER to refresh and see if the situation 
had changed. 

### Bidding
Bidding takes place in the first and second betting rounds.
There are only to options: ``Bid`` or ``Fold`` (bid represents calling and raising).

Example: someone has placed a bid of 50. You can fold or bid, when bidding you 
must bid **at least** 50 (or go ALL-IN, if you have less money than 50).

Bidding lasts until everyone has bet the same amount or everyone (except one player)
has folded. Then the game proceeds to next round or deals new cards and starts another round
respectively.

### Changing cards
You see your cards like this: 
```
Your cards:
|SIX CLUBS|SEVEN SPADES|EIGHT DIAMONDS|KING SPADES|ACE HEARTS|
```
When it is your turn in the ``CHANGING_CARDS`` round, you are asked for
a number of cards you want to change (0-5).
Then, you need to provide each card separately in format RANK SUIT.
Example:
```
How many cards do you want to change? (0-5)
2
What card do you want to change? (RANK SUIT)
SIX CLUBS
What card do you want to change? (RANK SUIT)
KING SPADES
```
If you make a mistake or type a card you don't have, you will be 
asked again, until you provide as many valid cards as you declared 
to change.

### Comparing cards
When reached this stage, just press ENTER and see who won! 
Then wait for others to proceed pressing ENTER to refresh.
If the game is over for you, you can create or join another game, just like 
at the beginning.

## Communication protocol
The communication is based on key:value pairs, separated by ``-`` and implemented using LinkedHashMap.
Example:
```State:IN_GAME-Round:FIRST_BETTING-PlayerID:0-GameID:0```
Both client and server have dedicated classes (ClientMessageHandler and ServerMessageHandler)
to encode this type of message to communicate with user in a more 'friendly' way and act 
accordingly to user input and current game phase.
