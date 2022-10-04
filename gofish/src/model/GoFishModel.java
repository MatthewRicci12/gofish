package model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import goFish.Card;
import goFish.Deck;
import goFish.GameType;
import goFish.Player;
/**
 * @author Jackson, Matthew, Zach, Davlat
 *			This is the model for the game of Go Fish. It contains all of the game logic 
 *			and the current state of the game. It contains the players, whose turn it is
 *			the number of books, the number of booked ranked (used for ending game), the 
 *			main deck, the number of players, the name of the file to save to, and the 
 *			current ruleset this game is being played on. 
 */
public class GoFishModel extends Observable implements Serializable{
	private Player[] playerDecks;
	private int currTurn;
	private int numOfBooks;
	private int[] bookedRanks = new int[14];
	public static final int MAX_BOOKS = 13;
	private Deck mainDeck;
	private int numOfPlayers;
	private String fileName = "save.bin";

	private GameType gameType;
	
	
	/**
	 * Constructor for the GoFishModel that takes in the number of players and the ruleset. 
	 * 
	 * It initializes the number of players, the main deck that everyone draws from, the decks 
	 * of the players, the number of cards for each player depending on number of player, and 
	 * which rule set is going to be used 
	 * @param NumOfPlayers
	 * 			this is the number of players 
	 * @param ruleset
	 *			this is the ruleset we are set to 
	 */
	public GoFishModel(int NumOfPlayers, GameType ruleset) {
		numOfPlayers = NumOfPlayers;
		mainDeck = new Deck();
		playerDecks = new Player[NumOfPlayers];
		for (int i = 0; i < NumOfPlayers; i++) {
			playerDecks[i] = new Player();
		}
		numOfBooks = 0;
		currTurn = 0;
	
		int numOfCards;
	
		if (NumOfPlayers < 4) numOfCards = 7;
	
		else numOfCards = 5;

		for (Player player : playerDecks) {
	
			for (int i = 0; i < numOfCards; i++) {
	
				Card pulledCard = mainDeck.pullCard();
				player.getHand().add(pulledCard);
				checkForBooks(player, pulledCard.getRank());

			}

		}
		this.gameType = ruleset;

	}
	
	/**
	 * Constructor for the GoFishModel that takes in the number of players, a custom 
	 * main deck, and the ruleset. This differs from the first constructor in that the 
	 * main deck is custom
	 * 
	 * @param NumOfPlayers
	 * 			Number of players 
	 * @param customDeck
	 * 			The custom deck object 
	 * @param ruleset
	 * 			Which ruleset we want to use 
	 */
	public GoFishModel(int NumOfPlayers, Deck customDeck, GameType ruleset) {
		numOfPlayers = NumOfPlayers;
		mainDeck = customDeck;
		playerDecks = new Player[NumOfPlayers];
		for (int i = 0; i < NumOfPlayers; i++) {
			playerDecks[i] = new Player();
		}
		numOfBooks = 0;
		currTurn = 0;
	
		int numOfCards;
	
		if (NumOfPlayers < 4) numOfCards = 7;
	
		else numOfCards = 5;

		for (Player player : playerDecks) {
	
			for (int i = 0; i < numOfCards; i++) {
				
				Card pulledCard = mainDeck.pullCard();
				player.getHand().add(pulledCard);
				checkForBooks(player, pulledCard.getRank());
			}

		}
		this.gameType = ruleset;

	}
	
	public GoFishModel(int NumOfPlayers, Deck customDeck, GameType ruleset, boolean customHands) {
		numOfPlayers = NumOfPlayers;
		mainDeck = customDeck;
		playerDecks = new Player[NumOfPlayers];
		for (int i = 0; i < NumOfPlayers; i++) {
			playerDecks[i] = new Player();
		}
		numOfBooks = 0;
		currTurn = 0;

		this.gameType = ruleset;

	}
	
	 /**
	 * This is called from the controller when a move is made on another player
	 * @param player
	 * 		the player the move is being made on
	 * 
	 * @return
	 * 			0 - no card was acquired
	 * 			1 - 1 or more cards were acquired
	 * 			2 - 1 or more cards were acquired AND booked
	 */
	public int getUserMove(Player player, String cardIdRequested) {
		// first find the card that was clicked on by the current player. rankRequested 
		// is the rank we are looking for in the player/opponent object
		int rankRequested = -1;
		for (goFish.Card c : playerDecks[currTurn].getHand()) {
			if (c.getId().equals(cardIdRequested)) {
				rankRequested = c.getRank();
				break;
			}
		}
		if (rankRequested == -1) {
			throw new IllegalArgumentException
			("Requested Card not found in the Player's deck");
		}
		
		// go through player's / opponent's hand and add any cards from their deck that match
		// the requested rank. Then remove those cards from the opponent's hand, if any match
		int retVal = 0;
		
		List<goFish.Card> acquired = new LinkedList<goFish.Card>();
		for (goFish.Card c: player.getHand()) {
			if (c.getRank() == rankRequested) {
				acquired.add(c);
				retVal = 1;
			}
		}
		for(goFish.Card c: acquired)
			player.getHand().remove(c);
		
		// if no cards of matching rank were taken from the opponent
		if (retVal == 0) {
			// if we are on ruleset 2, then give the card requested to the opponent  
				// Give them the card you asked for then get and remove requested 
				// card from curr player then add to opponent's hand
				// get and remove requested card from curr player then add to opponent's hand
				if (gameType == GameType.VARIANT_1) {
					Card cardToGive = null;
					for(Card c: playerDecks[currTurn].getHand()) {
						if (c.getId().equals(cardIdRequested)) {
							cardToGive = c;
							playerDecks[currTurn].getHand().remove(cardToGive);
							break;
						}
					}
					player.getHand().add(cardToGive);
				}

			
			// Now begin process of ending turn by pulling card from main deck
			Card fishCard = null;
			if(mainDeck.size()>0) { 
				// if deck is not empty, pull card then check for book 
				fishCard = mainDeck.pullCard();
				playerDecks[currTurn].getHand().add(fishCard);
				checkForBooks(playerDecks[currTurn],fishCard.getRank());
			}
			if(fishCard!=null && fishCard.getRank()==rankRequested) { 
				// if deck was not empty and pulled card is same as requested card 
				// then check if the players hand is empty aka the pulled card resulted in a book. 
				// Then check if the main deck is empty, if it isn't then pull card, else update turn 
				// if players' hand is not empty then just go ahead 
				saveModel();
				while(playerDecks[currTurn].getHand().size()==0) {
					if(mainDeck.size()>0) {
						Card pulledCard = mainDeck.pullCard();
						playerDecks[currTurn].getHand().add(pulledCard);
						checkForBooks(playerDecks[currTurn],pulledCard.getRank());
					} else {
						if (gameType == GameType.VARIANT_2) updateCurrTurn(player);
						else updateCurrTurn();
						break;
					}
				}
				setChanged();
				notifyObservers();
				return retVal;
			} else {
				// if fishCard was null/deck was empty or fish card was not the same as requested rank, 
				// then save the model and update the player turn according to ruleset and end turn
				saveModel();
				if (gameType == GameType.VARIANT_2) updateCurrTurn(player);
				else updateCurrTurn();
				return retVal;
			}
		}
		
		// if current Player did get cards from opponent, check for books. Then if player hand is empty 
		// then check if deck is empty. If not then pull and card and don't called updateTurn. This 
		// lets the current player go again. If the deck is empty, the player is out of the game and 
		// the game continues
		playerDecks[currTurn].getHand().addAll(acquired);
		if(checkForBooks(playerDecks[currTurn],rankRequested)) {
			retVal = 2;
		}
		this.saveModel();
		while(playerDecks[currTurn].getHand().size()==0) {
			if(mainDeck.size()>0) {
				Card pulledCard = mainDeck.pullCard();
				playerDecks[currTurn].getHand().add(pulledCard);
				checkForBooks(playerDecks[currTurn],pulledCard.getRank());
			} else {
				if (gameType == GameType.VARIANT_2) updateCurrTurn(player);
				else updateCurrTurn();
				break;
			}
		}
		setChanged();
		notifyObservers();
		return retVal;
		
	}
	
	/**
	 * This updates the currTurn to whoever is next and also pulls cards from the main 
	 * deck to place into the current players hand at the end of their turn. 
	 */
	private void updateCurrTurn() {
		// First checks to see if game is over. If so it returns
		if(checkGameOver()) {
			setChanged();
			notifyObservers();
			return;
		}
		
		// Calculates the current turn. 
		currTurn = (currTurn + 1) % numOfPlayers;
		
		// Check to see if current player's hand is empty, add card if so
		if(playerDecks[currTurn].getHand().size()==0 && mainDeck.size()>0) {
			playerDecks[currTurn].getHand().add(mainDeck.pullCard());
		}
		
		// check to see if main deck is empty. If so then it searches for whoever still has cards
		if(mainDeck.size()==0) {
			while(playerDecks[currTurn].getHand().size()==0) {
				currTurn = (currTurn + 1) % numOfPlayers;
			}
		}
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This override of updateCurrentTurn makes the player who is passed in go next. 
	 * It is used in Ruleset 3, popcorn goFish
	 * @param player
	 * 			This is the opponent who is going to go next. 
	 */
	private void updateCurrTurn(Player player) {
		if(checkGameOver()) {
			setChanged();
			notifyObservers();
			return;
		}
		int indexOfThatPlayer = 0;
		while (indexOfThatPlayer < playerDecks.length) {
			if (playerDecks[indexOfThatPlayer] == player) {
				break;
			}
			indexOfThatPlayer++;
		}
		
		currTurn = indexOfThatPlayer;
		setChanged();
		notifyObservers();
	}

	/**
	 * 
	 * After a move has been made, there is the possibility of a book. So this
	 * is where we check for it. If at any point a rank has 4 of a kind, we simply
	 * remove those cards from the player hand and add them to all our booked ranks.
	 * 
	 * @param player The player to check if they have books
	 * @param rankToCheck The rank to check for books
	 * @return True if a book was found
	 */
	private boolean checkForBooks(Player player, int rankToCheck) {
		boolean retVal = false;
		List<Card> cardsOfRank = new LinkedList<Card>();
		for (Card card : player.getHand()) {
			int curCardRank = card.getRank();
			if (curCardRank==rankToCheck) {
				cardsOfRank.add(card);
			}
		}
		if(cardsOfRank.size()==4) {
			player.getBookedCards().add(cardsOfRank.get(0));
			player.getHand().removeAll(cardsOfRank);
			numOfBooks++;
			bookedRanks[rankToCheck] = rankToCheck;
			retVal = true;
		}
		checkGameOver();
		setChanged();
		notifyObservers();
		return retVal;
	}


	/**
	 * This returns the player decks 
	 * @return
	 * 		playerDecks
	 */
	public Player[] getPlayerDecks() {
		return playerDecks;
	}
	
	/**
	 * This method tells if the game is over 
	 * @return
	 * 		returns the status of the game 
	 */
	public boolean checkGameOver() {
		return numOfBooks == MAX_BOOKS;
		//TODO: Notify observer
	}
	
	/**
	 * Turns the main deck. 
	 * @return 
	 * 		returns the main deck 
	 */
	public Deck getDeck() {
		return mainDeck;
	}
	
	/**
	 * Returns the index of the current turn
	 * @return
	 * 		returns index of current turn 
	 */
	public int getCurrentTurn() {
		return currTurn;
	}

	/**
	 * This is called from the view by the user to save the game. The player chooses 
	 * where to save the file and the name of the file. 
	 */
	public void saveModel() {
		// Send the model out to the file
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(this);
			out.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method loads the last saved save file.
	 */
	public GoFishModel loadModel() {
		GoFishModel model = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			model = (GoFishModel) in.readObject();
			in.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * This is used to tell which player is the winner at the end of the game 
	 * @return
	 * 		index of the winning player. 
	 */
	public int getWinningPlayer() {
		int maxBookCount = 0;
		int maxBookIndex = -1;
		for(int i=0;i<playerDecks.length;i++) {
			if(playerDecks[i].getBookedCards().size()>maxBookCount) {
				maxBookCount = playerDecks[i].getBookedCards().size();
				maxBookIndex = i;
			}
		}
		return maxBookIndex;
	}
}

