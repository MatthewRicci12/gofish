package goFish;

import java.io.Serializable;

/**
 * This the suit enum. This is used in the card class to specify what 
 * suit a card object has. There are 4 suits corresponding to the 4 
 * standard suits.
 * @author Zach, Matthew, Davlat, Matthew
 *
 */
public enum Suit  implements Serializable{
	CLUBS("Club"), 
	SPADES("Spade"), 
	HEARTS("Heart"), 
	DIAMONDS("Diamond");
	
	private final String suit;
	
	private Suit(String type) {
		this.suit = type;
	}
	
	/**
	 * This gets the suit name
	 * @return suit type
	 */
	public String getSuit() {
		return this.suit;
	}
}
