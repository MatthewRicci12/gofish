package goFish;

import java.io.Serializable;

/**
 * This is the card object that is used in play during the course 
 * of the game. It takes in attributes of "rank" (card number) and 
 * suit. It also has an ID that is a combination of the first letter 
 * of the suit and the rank. 
 * 
 * @author Jackson, Davlat, Matthew, Zach
 *
 */
public class Card implements Serializable{
	private int rank; 
	private String id; 
	private Suit suit;
	private static char[] names = {'0', 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};

	/**
	 * Constructor for the card object. Takes in a suit and rank
	 * @param suit
	 * 		this is the suit of the card
	 * @param rank
	 * 		this is the rank of the card
	 */
	public Card(Suit suit, int rank) {
		this.rank = rank;
		this.suit = suit;
		this.id = ""+names[rank] + suit.getSuit().charAt(0);
	}
	
	/**
	 * Getter for the rank
	 * @return
	 * 		returns the rank of 
	 */
	public int getRank() {
		return this.rank;
	}
	
	/**
	 * This returns the ID, a combination of the Rank + first letter of the Suit 
	 * 
	 * @return
	 * 		returns string 
	 */
	public String getId() {
		// this can be changed
		return id;
	}
	
	/**
	 * Getter for the suit of the card 
	 * @return
	 * 		returns the suit of the card
	 */
	public Suit getSuit() {
		return this.suit;
	}
	
	/**
	 * Prints out the id of the card 
	 */
	@Override
	public String toString() {
		return id;
	}
	
	
	
}





