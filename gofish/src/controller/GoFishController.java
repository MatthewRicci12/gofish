package controller;

import goFish.Player;
import model.GoFishModel;

/**
 * 
 * @author Jackson, Davlat, Mattehw, Zach
 *			This is the controller for GoFish. It acts as the inbetween 
 *			for the model and view. 
 */
public class GoFishController {
	private GoFishModel model;
	
	/**
	 * Constructor for the controller. Simply takes in a model
	 * @param model
	 * 		The model we are using
	 */
	public GoFishController(GoFishModel model) {
		this.model = model;
	}
	

	/**
	 * This method is used to handle a move when a player click on another player. Calls the 
	 * method from the model 
	 * @param player
	 * 		The player/opponent the current player clicked on 
	 * @param cardIdRequested
	 * 		the ID of the card that the current player clicked on to ask the opponent for
	 * @return
	 * 		returns an integer from 1 - 3  that tells us info about the operation
	 * 		0 - no card was acquired
	 * 		1 - 1 or more cards were acquired
	 * 		2 - 1 or more cards were acquired AND booked
	 */
	public int handleUserMove(Player player, String cardIdRequested) {
		return model.getUserMove(player, cardIdRequested);

	}
	
	/**
	 * This is called from the view by the user to save the game.
	 */
	public void saveModel() {
		model.saveModel();
	}
	
	/**
	 * This grabs the model from the controller 
	 * @return
	 * 		returns the current Go Fish Model
	 */
	public GoFishModel getModel() {
		return model;
	}
	
	/**
	 * This method loads a save file of a previous game. 
	 */
	public GoFishModel loadModel() {
		model = this.model.loadModel();
		return model;
	}
	
	/**
	 * This changes the current model from the view. It is used for changing number of players 
	 * and rule sets
	 * @param mod
	 * 		the model we want to set as the model in this controller
	 */
	public void setModel(GoFishModel mod) {
		this.model = mod;
	}
	
}
