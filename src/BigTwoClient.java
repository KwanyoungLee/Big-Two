import java.util.ArrayList;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class is used for modeling a Big Two card game.
 * It implements the CardGame interface
 * 
 * 
 * @author Kwanyoung lee
 */
public class BigTwoClient implements CardGame, NetworkGame{

	//public constructor
	/**
	 * Creates and returns an instance of the BigTwo class.
	 * 
	 */
	public BigTwoClient() {
		
		this.playerList = new ArrayList<CardGamePlayer>();
		
		CardGamePlayer a = new CardGamePlayer();
		CardGamePlayer b = new CardGamePlayer();
		CardGamePlayer c = new CardGamePlayer();
		CardGamePlayer d = new CardGamePlayer();
		this.playerList.add(a);
		this.playerList.add(b);
		this.playerList.add(c);
		this.playerList.add(d);
		
		this.handsOnTable = new ArrayList<Hand>();
		
		this.table = new BigTwoTable(this);
		table.repaint();
		
		//3)make a connection to the game server by calling the makeConnection() method
		this.makeConnection();
	}
	
	
	//private instance variables
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentIdx = - 1;
	private BigTwoTable table;
	private int turn = 0;
	//newly implemented
	private static final int NUM_OF_PLAYERS = 4;
	private int numOfPlayers = 0;
	private int playerID = -1;
	private String playerName = "Player";
	private String serverIP = "127.0.0.1";
	private int serverPort = 2396;
	private Socket sock;
	private ObjectOutputStream oos;
	private boolean playing = false;
	
	
	//public methods
	/**
	 * Retrieves the number of players playing the game.
	 * 
	 * @return the number of players playing the game.
	 * 
	 */
	public int getNumOfPlayers() {return playerList.size();}
	
	
	/**
	 * Retrieves the deck of cards being used.
	 * 
	 * @return the deck of cards being used.
	 * 
	 */
	public Deck getDeck() {return deck;}
	
	/**
	 * Retrieves the list of players playing the game.
	 * 
	 * @return the list of players playing the game.
	 * 
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {return playerList;}
	
	/**
	 * Retrieves the list of hands played on the table.
	 * 
	 * @return the list of hands played on the table.
	 * 
	 */
	public ArrayList<Hand> getHandsOnTable() {return handsOnTable;}
	
	/**
	 * Retrieves the index of the current player.
	 * 
	 * @return the index of the current player.
	 * 
	 */
	public int getCurrentIdx() {return currentIdx;}
	

	/**
	 * Start the game with a (shuffled) deck of cards supplied as the argument. 
	 * 
	 * 
	 * @param deck
	 * 			the deck of cards being used.
	 * 
	 */
	public void start(Deck deck) {
		for (int i=0; i<4; i++) {
			playerList.get(i).getCardsInHand().removeAllCards();
		}
		
		for (int i=0; i<this.handsOnTable.size(); i++) {
			this.handsOnTable.remove(i);
		}
		
		for (int i=0; i<13; i++) {
			BigTwoCard temp = (BigTwoCard) deck.getCard(i);
			playerList.get(0).addCard(temp);
			playerList.get(0).sortCardsInHand();
		}
		for (int i=13; i<26; i++) {
			BigTwoCard temp = (BigTwoCard) deck.getCard(i);
			playerList.get(1).addCard(temp);
			playerList.get(1).sortCardsInHand();
		}
		for (int i=26; i<39; i++) {
			BigTwoCard temp = (BigTwoCard) deck.getCard(i);
			playerList.get(2).addCard(temp);
			playerList.get(2).sortCardsInHand();
		}
		for (int i=39; i<52; i++) {
			BigTwoCard temp = (BigTwoCard) deck.getCard(i);
			playerList.get(3).addCard(temp);
			playerList.get(3).sortCardsInHand();
		}
		
		Card dia3 = new Card(0,2);
		
		for (int i=0; i<4; i++)
			if (playerList.get(i).getCardsInHand().contains(dia3)) {
				currentIdx = i;
			}
		
		table.reset();
		table.setActivePlayer(currentIdx);
		table.printMsg(playerList.get(currentIdx).getName() + "'s turn: \n");
		table.repaint();
	}
		
	
	/**
	 * Make a move by a player with the specified playerID using the cards specified by the list of indices.
	 * This method should be called from the BigTwoTable when active player presses either the “Play” or “Pass” button.
	 * 
	 * 
	 * @param playerID
	 * 			the id of the specified player
	 * 
	 * @param cardIdx
	 * 			the cards specified by the list of indices
	 * 
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		checkMove(playerID, cardIdx);
		GameMessage message = new GameMessage(6, -1, cardIdx);
		sendMessage(message);
	}
	
	
	/**
	 * Check a move made by a player.
	 * This method should be called from the makeMove() method from the CardGame interface.
	 * 
	 * 
	 * @param playerID
	 * 			the id of the specified player
	 * 
	 * @param cardIdx
	 * 			the cards specified by the list of indices
	 * 
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		
		CardGamePlayer currentplayer = getPlayerList().get(playerID);
		
		if (cardIdx != null && turn<3) {
			
				//2. play() (CardGamePlayer)
			CardList cards = currentplayer.play(cardIdx);
			
				//3. composeHand() (BigTwo)
			Hand hand = composeHand(currentplayer, cards);
			
				//4. beats() (hand)
			if (!handsOnTable.isEmpty()) {
				if (hand != null){
					if(hand.size() == handsOnTable.get(handsOnTable.size()-1).size()) {
						if (hand.beats(handsOnTable.get(handsOnTable.size() - 1))){
							table.printMsg("{"+hand.getType()+"} " + hand.toString() + "\n");
							
					
							handsOnTable.add(hand);
							
							for (int k=0; k<hand.size(); k++)
								playerList.get(currentIdx).getCardsInHand().removeCard(hand.getCard(k));
						
							currentIdx++;
							if (currentIdx==4)
								currentIdx = 0;
								
							table.printMsg("Player " + currentIdx + "'s turn:\n");
							turn++;
						}
							else {
								table.printMsg("{"+hand.getType()+"} " + hand.toString() + " <== Not a legal move!!!\n");
							}
					}
					else {
						table.printMsg("{"+hand.getType()+"} " + hand.toString() + " <== Not a legal move!!!\n");
					}
				}
				else
					table.printMsg(hand.toString() + " <== Not a legal move!!!\n");
			}else {
				
				table.printMsg("{"+hand.getType()+"} " + hand.toString() + "\n");
			
				handsOnTable.add(hand);
			
				for (int k=0; k<hand.size(); k++)
					playerList.get(currentIdx).getCardsInHand().removeCard(hand.getCard(k));
			
				currentIdx++;
				if (currentIdx==4)
					currentIdx = 0;
			
				table.printMsg("Player " + currentIdx + "'s turn:\n");
				turn++;
			}
		
		}else if(cardIdx == null && turn == 3) {
			table.printMsg("Not a legal move!!!\n");
		
		}else if(cardIdx != null && turn == 3) {

			CardList cards = currentplayer.play(cardIdx);
			Hand hand = composeHand(currentplayer, cards);

			table.printMsg("{"+hand.getType()+"} " + hand.toString() + "\n");
				
			handsOnTable.add(hand);
				
			for (int k=0; k<hand.size(); k++)
				playerList.get(currentIdx).getCardsInHand().removeCard(hand.getCard(k));
			
			currentIdx++;
			if (currentIdx==4)
				currentIdx = 0;
			
			table.printMsg("Player " + currentIdx + "'s turn:\n");
			turn = 1;
				
		}else if(cardIdx==null && handsOnTable.isEmpty() && turn==0 ){
			table.printMsg("Not a legal move!!!");
				
		}else if(cardIdx == null && turn <3){
			table.printMsg("{Pass}\n");
			currentIdx++;
			if (currentIdx==4)
				currentIdx = 0;
				
			table.printMsg("Player " + currentIdx + "'s turn:\n");
			turn++;
		}
		
		if (endOfGame())
		{
			String temp;
			temp = "Game ends \n";
			for (int i=0; i<4; i++) {
				if (playerList.get(i).getNumOfCards()==0)
					temp +="Player " + i + " wins the game.\n";
				else
					temp += "Player " + i + " has " + playerList.get(i).getNumOfCards() + " cards in hand.\n";
			}	
			table.endMessage(temp);
			table.disable();
			
			// send message of READY,-1,null to the server
			//
			//
			//
		}
		
		table.setActivePlayer(currentIdx);
		table.repaint();
	}
	
	
	/**
	 * Checks if the game ends
	 * 
	 */
	public boolean endOfGame() {
		for(int i=0; i<this.getNumOfPlayers(); i++)
			if(playerList.get(i).getNumOfCards() == 0)
				return true;
		return false;
	}	
	
	
	//public static methods
	/**
	 * Starts a Big Two card game.
	 * Creates a Big Two card game, creates and shuffles a deck of cards, and starts the game with the deck of cards.
	 * 
	 * @param args
	 *            not being used in this application.
	 */
	public static void main(String[] args) {
		
		BigTwoClient game = new BigTwoClient();
		//BigTwoDeck deck = new BigTwoDeck();
		//deck.shuffle();
		//game.start(deck);
	}
	
	/**
	 * Returns a valid hand from the specified list of cards of the player. 
	 * Returns null is no valid hand can be composed from the specified list of cards.
	 * 
	 * @param player
	 * 			the specified player playing the game
	 * 
	 * @param cards
	 * 			the specified list of cards of the player
	 * 
	 * @return a valid hand or null.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		//method for returning a valid hand from the specified list of cards of the player. 
		//Returns null is no valid hand can be composed from the specified list of cards.
		Single tempSingle = new Single(player, cards);
		if (tempSingle.isValid())
			return tempSingle;
		
		Pair tempPair = new Pair(player, cards);
		if (tempPair.isValid())
			return tempPair;
		
		Triple tempTriple = new Triple(player, cards);
		if (tempTriple.isValid())
			return tempTriple;
		
		Quad tempQuad = new Quad(player, cards);
		if (tempQuad.isValid())
			return tempQuad;

		Straight tempStraight = new Straight(player, cards);
		if (tempStraight.isValid())
			return tempStraight;
		
		Flush tempFlush = new Flush(player, cards);
		if (tempFlush.isValid())
			return tempFlush;
		
		FullHouse tempFullHouse = new FullHouse(player, cards);
		if (tempFullHouse.isValid())
			return tempFullHouse;
		
		StraightFlush tempStraightFlush = new StraightFlush(player, cards);
		if (tempStraightFlush.isValid())
			return tempStraightFlush;
		
		
		return null;
	}

	/**
	 * Get the playerID (i.e., index) of the local player.
	 * 
	 * @return the playerID of the local player
	 * 
	 */
	public int getPlayerID() {return this.playerID;}
	
	/**
	 * Set the playerID (i.e., index) of the local player
	 * 
	 * @param playerID
	 * 				the playerID of the local player
	 * 
	 */
	public void setPlayerID(int playerID) {this.playerID = playerID;}

	/**
	 * Get the name of the local player.
	 * 
	 * @return the name of the local player.
	 * 
	 */
	public String getPlayerName() {return this.playerName;}
	
	/**
	 * Set the name of the local player.
	 * 
	 * @param playerName
	 * 				the name of the local player.
	 * 
	 */
	public void setPlayerName(String playerName) {this.playerName = playerName;}
	
	/**
	 * Get the IP address of the game server.
	 * 
	 * @return the IP address of the game server.
	 * 
	 */
	public String getServerIP() {return this.serverIP;}
	
	/**
	 * Set the IP address of the game server.
	 * 
	 * @param serverIP
	 * 				the IP address of the game server
	 * 
	 */
	public void setServerIP(String serverIP) {this.serverIP = serverIP;}
	
	/**
	 * Get the TCP port of the game server.
	 * 
	 * @return the TCP port of the game server.
	 * 
	 */
	public int getServerPort() {return this.serverPort;}
	
	/**
	 * Set the TCP port of the game server.
	 * 
	 * @param serverPort
	 * 				the TCP port of the game server.
	 * 
	 */
	public void setServerPort(int serverPort) {this.serverPort = serverPort;}
	
	/**
	 * Make a socket connection with the game server.
	 * 
	 * 
	 */
	public void makeConnection() {
		try {
			sock = new Socket("127.0.0.1", 2396);
			oos = new ObjectOutputStream(sock.getOutputStream());
			
			Thread readerThread = new Thread(new ServerHandler());
			readerThread.start();
			
			String name = JOptionPane.showInputDialog("Enter your name!");
			
			GameMessage message = new GameMessage(1, -1, name);
	        oos.writeObject(message);
	        //oos.flush();
	        
	        GameMessage message2 = new GameMessage(4, -1, null);
	        oos.writeObject(message2);
	        //oos.flush();
	        
	        //readerThread.start();
	        
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * Parse the messages received from the game server.
	 * 
	 * @param  message
	 * 				messages received from the game server          
	 */
	public void parseMessage(GameMessage message) {
		int type = message.getType();
		int playerID = message.getPlayerID();
		
		if (type == 0) { //PLAYER_LIST
			String[] data = new String[NUM_OF_PLAYERS]; 
			data = (String[]) message.getData();
			this.setPlayerID(playerID);
			
			if(data!=null) {
				for (int i=0; i<numOfPlayers; i++)
					this.playerList.get(i).setName(data[i]);
			}
		}else if (type == 1) { //JOIN
			String data = (String) message.getData();
			this.playerList.get(playerID).setName(data);
			numOfPlayers ++;
			
		}else if (type == 2) { //FULL
			//display message on text area of the Big Two Table)
			table.printMsg("The server is full and cannot join the game. \n");
			
		}else if (type == 3) { //QUIT
			//String data = (String) message.getData();
			this.playerList.get(playerID).setName("");
			
			
		}else if (type == 4) { //READY
			table.printMsg("Player " + playerID + " is ready.\n");
			
		}else if (type == 5) { //START
			BigTwoDeck data = (BigTwoDeck) message.getData();
			start(data);
			
		}else if (type == 6) { //MOVE
			int[] data = new int[13];
			data = (int[]) message.getData();
			checkMove(playerID, data);
			
		}else if (type == 7) { //MSG
			String data = (String) message.getData();
			table.printChat(data);
		}
		
		
	}

	/**
	 * Send the specified message to the game server.
	 * 
	 * @param  message
	 * 				messages to be sent to the game server          
	 */
	public void sendMessage(GameMessage message) {
		try {
			sock = new Socket("127.0.0.1", 2396);
			oos = new ObjectOutputStream(sock.getOutputStream());
			
	        oos.writeObject(message);
	        oos.flush();     
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * This is an inner class that implements the Runnable interface.
	 * 
	 *      
	 */
	//inner classes
	public class ServerHandler implements Runnable {
		private ObjectInputStream oistream; // ObjectInputStream of the client
		
		public void run() {
			CardGameMessage message;
			try {
				// reads incoming messages from the server
				while ((message = (CardGameMessage) oistream.readObject()) != null) {
					parseMessage(message);
				} // close while
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	} // IncomingReader
	
}