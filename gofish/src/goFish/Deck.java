package goFish;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * This is the deck that contains all 52 cards of a standard deck. 
 * It uses a stack so that the cards can only be pulled and pushed from 
 * the top. There is only one instance of this in the game. 
 * 
 * @author Davlat, Jackson, Zach, Matthew
 */
public class Deck  implements Serializable{
	private Stack<Card> stack;
	private final Set<Card> totalCards = new HashSet<>();
	
	
	/**
	 * This constructor is used to create the 52 card deck. 
	 */
	public Deck() {
		stack = new Stack<>();
		final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
		for (int rank : ranks) { 
			for (Suit suit : suits) {
				Card card = new Card(suit, rank);
				totalCards.add(card);
				stack.push(card);
			}
		}
		this.shuffle();
	}
	
	/**
	 * The constructor that is mainly meant for testing. Does not shuffle, because
	 * random orders of card would make testing near impossible. 
	 * 
	 * @param prebuiltStack a Stack&lt;Card&gt; that you already built.
	 */
	public Deck(Stack<Card> prebuiltStack) {
		stack = prebuiltStack;
	}
	
	/**
	 * This adds the card to the deck stack
	 * @param card
	 * 		card to be added
	 */
	public void addToDeck(Card card) {
		stack.push(card);
	}
	
	/**
	 * This takes a card from the top of the stack
	 * @return
	 * 		returns card from top
	 */
	public Card pullCard() {
		return stack.pop();
	}
	
	/**
	 * Shuffles the deck. Used during initialization
	 */
	public void shuffle() {
		Collections.shuffle(stack);
	}

	/**
	 * Returns the size of the deck
	 * @return
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Checks to see if the deck contains the object 
	 * @param o
	 * 		object being checked for 
	 * @return
	 * 		boolean that tells us if the object is in the deck
	 */
	public boolean contains(Object o) {
		return stack.contains(o);
	}
	
	/**
	 * Simple to string for the stack 
	 */
	@Override
	public String toString() {
		return stack.toString();
	}
	

}
