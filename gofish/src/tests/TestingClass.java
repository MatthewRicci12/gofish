package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import controller.GoFishController;
import goFish.Card;
import goFish.Deck;
import goFish.GameType;
import goFish.Player;
import goFish.Suit;
import model.GoFishModel;

public class TestingClass {

	public static void main(String[] args) {
		Set<Card> gameOneDeck = new HashSet<>();
		GoFishModel gameOneModel; //Nothing but user moves
		GoFishModel gameTwoModel; //Nothing but gofishes
		GoFishModel gameThreeModel; //Normal game, mixed moves
		GoFishController gameOneController;
		GoFishController gameTwoController;
		GoFishController gameThreeController;
		final int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
		final Suit[] suits = {Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit.HEARTS};
		
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
		//// UNIQUE
		stack = new Stack<>();
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
		
//		for (Player player : model.getPlayerDecks()) {
//			for (Card card : player.getHand()) {
//				System.out.print(card + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
	
		
		String[] moves = {"QH", "JH", "6H", "5H"};
		
		int turns = 0;
		while (turns < 4) {
			int curTurn = model.getCurrentTurn();
			for (Card card : playerOne.getHand()) {
				System.out.print(card + " ");
			}
			System.out.println();
			for (Card card : playerTwo.getHand()) {
				System.out.print(card + " ");
			}
			System.out.println();
			for (Card card : playerThree.getHand()) {
				System.out.print(card + " ");
			}
			System.out.println();
			for (Card card : playerFour.getHand()) {
				System.out.print(card + " ");
			}
			System.out.println();
			System.out.println("curTurn: " + curTurn);
			System.out.println("Card asked: " + moves[turns]);
			System.out.println("Index: " + turns);
			System.out.println();
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

}
