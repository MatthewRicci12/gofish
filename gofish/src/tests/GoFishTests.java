package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.GoFishController;
import goFish.Card;
import goFish.Deck;
import goFish.GameType;
import goFish.Player;
import goFish.Suit;
import model.GoFishModel;

public class GoFishTests {
	private static Set<Card> gameOneDeck = new HashSet<>();
	private static GoFishModel gameOneModel; //Nothing but user moves
	private static GoFishModel gameTwoModel; //Nothing but gofishes
	private static GoFishModel gameThreeModel; //Normal game, mixed moves
	private static GoFishController gameOneController;
	private static GoFishController gameTwoController;
	private static GoFishController gameThreeController;
	private static final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private static final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
	
	@BeforeAll
	static void initialize() {
		//INITIALIZE TEST GAME ONE
		Stack<Card> stack = new Stack<>();
		for (int rank : ranks) { 
			for (Suit suit : suits) {
				Card card = new Card(suit, rank);
				gameOneDeck.add(card);
				stack.push(card);
			}
		}
		Stack<Card> stackTwo = (Stack<Card>) stack.clone();
		Stack<Card> stackThree = (Stack<Card>) stack.clone();
		Deck deckOne = new Deck(stack);
		Deck deckTwo = new Deck(stackTwo);
		Deck deckThree = new Deck(stackThree);
		
		gameOneModel = new GoFishModel(2, deckOne, GameType.BASIC);
		gameTwoModel = new GoFishModel(3, deckTwo, GameType.VARIANT_1);
		gameThreeModel = new GoFishModel(4, deckThree, GameType.VARIANT_2);
		
		gameOneController = new GoFishController(gameOneModel);
		gameTwoController = new GoFishController(gameTwoModel);
		gameThreeController = new GoFishController(gameThreeModel);
	
	/*
	 * Tests to make sure that the deck cards are actually removed from the deck,
	 * as well as making sure none of the cards that the players have, are still
	 * in the deck.
	 */
	}
	@Test
	void test_dealing_cards() {
		GoFishModel modelTwoPlayers = new GoFishModel(2, GameType.BASIC);
		GoFishModel modelThreePlayers = new GoFishModel(3, GameType.VARIANT_1);
		GoFishModel modelFourPlayers = new GoFishModel(4, GameType.VARIANT_2);

		int sizeOfDeckAfterTwoPlayers = modelTwoPlayers.getDeck().size();
		int sizeOfDeckAfterThreePlayers = modelThreePlayers.getDeck().size();
		int sizeOfDeckAfterFourPlayers = modelFourPlayers.getDeck().size();

		assertEquals(sizeOfDeckAfterTwoPlayers, 38);
		assertEquals(sizeOfDeckAfterThreePlayers, 31);
		assertEquals(sizeOfDeckAfterFourPlayers, 32);
		
		for (Player player : modelTwoPlayers.getPlayerDecks()) {
			for (Card card : player.getHand()) {
				assertFalse(modelTwoPlayers.getDeck().contains(card));
			}
		}
		
		for (Player player : modelThreePlayers.getPlayerDecks()) {
			for (Card card : player.getHand()) {
				assertFalse(modelThreePlayers.getDeck().contains(card));
			}	
		}
		
		for (Player player : modelFourPlayers.getPlayerDecks()) {
			for (Card card : player.getHand()) {
				assertFalse(modelFourPlayers.getDeck().contains(card));
			}
		}
		
	}
	
	@Test
	void test_game_1() {
		Deck gameDeck = gameOneModel.getDeck();
		System.out.println(gameDeck);
		Player playerOne = gameOneModel.getPlayerDecks()[0];
		Player playerTwo = gameOneModel.getPlayerDecks()[1];
		Map<Integer, Player> playersToInts = Map.of(0, playerOne, 1, playerTwo);
		int currTurn = 0;
		int numOfPlayers = 2;
		//System.out.println(gameOneModel.getDeck().toString());
		String[] moves = {"QH", "TS", "TS", "9H", "9H", "9D", "8H", "8D", "8S", "7H", 
				"7D", "7S", "6H","6D","6S","5H","5D","5S",
				"4H","4D","4S","3H","3D","3S",
				"2H","2D","2S","AH","AD","AS", "AS"};
		
		//System.out.println(moves.length);
		//the deck has 38 cards, meaning each player makes 19 moves to clear it out.
		int turns = 0;
		while (turns < 30) {
			if (gameOneModel.getCurrentTurn() == 0) gameOneController.handleUserMove(playerTwo, moves[turns]);
			else gameOneController.handleUserMove(playerOne, moves[turns]);
			turns++;
		}
		
		boolean deckIsEmpty = gameDeck.size() == 0;
		boolean isGameOver = gameOneModel.checkGameOver();
		boolean playerHandsEmpty = playerOne.getHand().size() == 0 &&
				playerTwo.getHand().size() == 0;
		boolean playerOneWins = gameOneModel.getWinningPlayer() == 0;
		
		assertTrue(deckIsEmpty);
		assertTrue(isGameOver);
		assertTrue(playerHandsEmpty);
		assertTrue(playerOneWins);
		
		
	}
	
	
	@Test
	void test_game_2() {
		Deck gameDeck = gameTwoModel.getDeck();
		Player playerOne = gameTwoModel.getPlayerDecks()[0];
		Player playerTwo = gameTwoModel.getPlayerDecks()[1];
		Player playerThree = gameTwoModel.getPlayerDecks()[2];
		Map<Integer, Player> playersToInts = Map.of(0, playerOne, 1, playerTwo, 2, playerThree);
		int currTurn = 0;
		int numOfPlayers = 3;
		
		String moves[] = {"QH", "TH", "8H"};
		
		int turns = 0;
		while (turns < 3) {
			int curTurn = gameTwoModel.getCurrentTurn();
			switch (curTurn) {
				case 0:
					gameTwoController.handleUserMove(playerThree, moves[turns]);
					break;
				case 1:
					gameTwoController.handleUserMove(playerOne, moves[turns]);
					break;
				case 2:
					gameTwoController.handleUserMove(playerTwo, moves[turns]);
					break;
			}
			turns++;
		}
	}
	
	@Test
	void test_game_3() {
		final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
		Stack<Card> stack = new Stack<>();
		for (int rank : ranks) { 
			for (Suit suit : suits) {
				Card card = new Card(suit, rank);
				stack.push(card);
			}
		}
		Deck deck = new Deck(stack);
		GoFishModel model = new GoFishModel(4, deck, GameType.VARIANT_2);
		GoFishController controller = new GoFishController(model);
		Player playerOne = model.getPlayerDecks()[0];
		Player playerTwo = model.getPlayerDecks()[1];
		Player playerThree = model.getPlayerDecks()[2];
		Player playerFour = model.getPlayerDecks()[3];
		List<Card> playerOneHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 12),
				new Card(Suit.DIAMONDS, 12),
				new Card(Suit.CLUBS, 12)
				));
		List<Card> playerTwoHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 11),
				new Card(Suit.DIAMONDS, 11),
				new Card(Suit.CLUBS, 11)
				));
		List<Card> playerThreeHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 6),
				new Card(Suit.DIAMONDS, 6),
				new Card(Suit.CLUBS, 6)
				));
		List<Card> playerFourHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 5),
				new Card(Suit.DIAMONDS, 5),
				new Card(Suit.CLUBS, 5)
				));
		playerOne.setHand(playerOneHand);
		playerTwo.setHand(playerTwoHand);
		playerThree.setHand(playerThreeHand);
		playerFour.setHand(playerFourHand);
		
		String[] moves = {"QH", "JH", "6H", "5H"};
		
		int turns = 0;
		while (turns < 4) {
			int curTurn = model.getCurrentTurn();
			switch (curTurn) {
				case 0:
					controller.handleUserMove(playerTwo, moves[turns]);
					break;
				case 1:
					controller.handleUserMove(playerThree, moves[turns]);
					break;
				case 2:
					controller.handleUserMove(playerFour, moves[turns]);
					break;
				case 3:
					controller.handleUserMove(playerOne, moves[turns]);
					break;
			}
			turns++;
		}
	}
	
	@Test
	void test_edge_case_1() {
		Stack<Card> stack = new Stack<>();
		stack.push(new Card(Suit.SPADES, 2));
		Deck deck = new Deck(stack);
		GoFishModel model = new GoFishModel(2, deck, GameType.BASIC, true);
		GoFishController controller = new GoFishController(model);
		Player playerOne = model.getPlayerDecks()[0];
		Player playerTwo = model.getPlayerDecks()[1];
		List<Card> playerOneHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 2),
				new Card(Suit.DIAMONDS, 2),
				new Card(Suit.CLUBS, 2)
				));
		List<Card> playerTwoHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 3),
				new Card(Suit.DIAMONDS, 3),
				new Card(Suit.CLUBS, 3)
				));
		playerOne.setHand(playerOneHand);
		playerTwo.setHand(playerTwoHand);
		
		controller.handleUserMove(playerTwo, "2D");
	}
	
	@Test
	void test_edge_case_2() {
		Stack<Card> stack = new Stack<>();
		stack.push(new Card(Suit.SPADES, 2));
		stack.push(new Card(Suit.HEARTS, 12));
		Deck deck = new Deck(stack);
		GoFishModel model = new GoFishModel(2, deck, GameType.BASIC, true);
		GoFishController controller = new GoFishController(model);
		Player playerOne = model.getPlayerDecks()[0];
		Player playerTwo = model.getPlayerDecks()[1];
		List<Card> playerOneHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 2),
				new Card(Suit.DIAMONDS, 2),
				new Card(Suit.CLUBS, 2)
				));
		List<Card> playerTwoHand = new ArrayList<>(Arrays.asList(new Card(Suit.HEARTS, 3),
				new Card(Suit.DIAMONDS, 3),
				new Card(Suit.CLUBS, 3)
				));
		playerOne.setHand(playerOneHand);
		playerTwo.setHand(playerTwoHand);
		
		controller.handleUserMove(playerTwo, "2D");
	}
	
	
//	@Test
//	void test_edge_case_3() {
//		Stack<Card> stack = new Stack<>();
//		stack.push(new Card());
//		stack.push(new Card());
//		Deck deck = new Deck(stack);
//		GoFishModel model = new GoFishModel(2, deck, GameType.BASIC, true);
//		GoFishController controller = new GoFishController(model);
//	}
	
	
	
	@Test
	void test_controller() {
		final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
		Stack<Card> stack = new Stack<>();
		for (int rank : ranks) { 
			for (Suit suit : suits) {
				Card card = new Card(suit, rank);
				stack.push(card);
			}
		}
		Deck deck = new Deck(stack);
		GoFishModel model = new GoFishModel(4, deck, GameType.VARIANT_2);
		GoFishController controller = new GoFishController(model);
		assertEquals(model, controller.getModel());
		controller.setModel(gameOneModel);
		controller.saveModel();
		controller.loadModel();
		
	}
	
	@Test
	void test_throws() {
		final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
		Stack<Card> stack = new Stack<>();
		for (int rank : ranks) { 
			for (Suit suit : suits) {
				Card card = new Card(suit, rank);
				stack.push(card);
			}
		}
		Deck deck = new Deck(stack);
		GoFishModel model = new GoFishModel(4, deck, GameType.VARIANT_2);
		GoFishController controller = new GoFishController(model);
		Player playerOne = model.getPlayerDecks()[0];
		Player playerTwo = model.getPlayerDecks()[1];
		Player playerThree = model.getPlayerDecks()[2];
		Player playerFour = model.getPlayerDecks()[3];
		
		assertThrows(IllegalArgumentException.class, () -> controller.handleUserMove(playerTwo, "A3"));
		
	}
	
	
	@Test
	void test_objects() {
		final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
		Stack<Card> stack = new Stack<>();
		Set<Card> setOfCards = new HashSet<>();
		for (int rank : ranks) { 
			for (Suit suit : suits) {
				Card card = new Card(suit, rank);
				stack.push(card);
				setOfCards.add(card);
			}
		}
		Deck deck = new Deck(stack);
		GoFishModel model = new GoFishModel(4, deck, GameType.VARIANT_2);
		GoFishController controller = new GoFishController(model);
		Player playerOne = model.getPlayerDecks()[0];
		Player playerTwo = model.getPlayerDecks()[1];
		Player playerThree = model.getPlayerDecks()[2];
		Player playerFour = model.getPlayerDecks()[3];
		Card cardToAdd = new Card(Suit.CLUBS, 3);
		deck.addToDeck(cardToAdd);
		assertEquals(cardToAdd.getSuit(), Suit.CLUBS);
		assertEquals(playerOne.getNumOfCardsInDeck(), 1);
		
		
	}



}
