package goFish;

import java.io.Serializable;

/**
 * This is the game type enum that is used to determine which set 
 * of rules we are using. There is the basic version and then two variations
 * on the set of rules for the game. 
 * @author Jackson, Davlat, Matthew, Zach
 *
 */
public enum GameType  implements Serializable{
	BASIC("Basic"), 
	VARIANT_1("Variant_1"), //If they don't have that card, you have to give it to them
	VARIANT_2("Variant_2"); //The player you asked for a card, it is their turn next turn
	
	private final String mode; 
	
	private GameType(String type) {
		this.mode = type;
	}
	
}
