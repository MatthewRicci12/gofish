package goFish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the player class. It holds the hand of player and the 
 * booked cards of the player.
 * 
 * @author Jackson, Davlat, Matthew, Zach
 *
 */

public class Player  implements Serializable{
	private List<Card> hand;
	private List<Card> bookedCards;
	
	
	public Player() {
		hand = new ArrayList<>();
		bookedCards = new ArrayList<>();
	}
	
	/**
	 * This returns the size of the hand
	 * @return
	 * 		returns size of the hand
	 */
	public int getNumOfCardsInDeck() {
		return hand.size();
	}
	
	/**
	 * This returns the hand of the player 
	 * @return
	 * 		returns the hand of the player
	 */
	public List<Card> getHand(){
		return hand;
	}
	
	/**
	 * This returns booked cards 
	 * @return
	 * 		returns the booked cards
	 */
	public List<Card> getBookedCards() {
		return bookedCards;
	}
	
	public void setHand(List<Card> hand) {
		this.hand = hand;
	}
	
}

