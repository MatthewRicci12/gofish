package view;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import controller.GoFishController;
import goFish.Card;
import goFish.GameType;
import goFish.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import model.GoFishModel;

/**
 * @author Jackson, Davlat, Matthew, Zach
 *		This is the GUI view for the game of Go Fish. It handles all of the 
 *		input from the player and displays the outputs that result from those 
 *		inputs. 
 */
public class GoFishGUIView extends Application implements Observer{

	private GoFishController controller;
	private static Map<String, Color> suitsToColor;
	private String selectedCard;
	private Stage mainStage;
	private Scene table;
	private Scene menu;
	private Scene books;
	private Scene turnBuffer;
	private BorderPane turnPane;
	private GridPane tablePane, menuPane, bookPane;
	private FlowPane currentPlayerPane;
	private HBox playersSetting;
	private HBox ruleSetting;
	private int numOfPlayers = 4;
	private final int WINDOW_WIDTH = 1050;
	private final int WINDOW_HEIGHT = 800;
	private int lastPlayer = 0;
	private GameType currRuleset = GameType.BASIC;
	
	/**
	 * Initialize the GUI components
	 * @param stage The stage element of the application
	 */
	@Override
	public void start(Stage stage) {
		mainStage = stage;
		GoFishModel m = new GoFishModel(4, currRuleset);
		m.addObserver(this);
		controller = new GoFishController(m);
		stage.setTitle(selectedCard);
		
		// set up the gridpanes for the books and the table
		bookPane = new GridPane();
		bookPane.getStyleClass().add("bookWindow");
		bookPane.setMinWidth(WINDOW_WIDTH);
		bookPane.setMinHeight(WINDOW_HEIGHT);
		books = new Scene(bookPane,WINDOW_WIDTH,WINDOW_HEIGHT);
		books.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
		tablePane = new GridPane();
		tablePane.getStyleClass().add("tableWindow");
		table = new Scene(tablePane,WINDOW_WIDTH,WINDOW_HEIGHT);
		table.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
		
		// input on key "b" allows for player to switch between the book and table panes
		table.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getText().toLowerCase().equals("b")) {
					stage.setScene(books);
				}
			}
		});
		books.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getText().toLowerCase().equals("b")) {
					stage.setScene(table);
				}
			}
		});
		
		// set up the menu by calling the getMenuPane
		menuPane = getMenuPane();
		menu = new Scene(menuPane,WINDOW_WIDTH,WINDOW_HEIGHT);
		menu.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
		
		// sets up the buffer / turn pane that is displayed between turns. 
		turnPane = new BorderPane();
		turnPane.getStyleClass().add("turnBufferWindow");
		turnPane.setMinWidth(WINDOW_WIDTH);
		turnPane.setMinHeight(WINDOW_HEIGHT);
		turnBuffer = new Scene(turnPane,WINDOW_WIDTH,WINDOW_HEIGHT);
		
		// sets up input on pressing of "enter" to allow for players to proceed from buffer pane
		turnBuffer.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if(ke.getCode().equals(KeyCode.ENTER)) {
					mainStage.setScene(table);
				}
			}
		});
		turnBuffer.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
		stage.setScene(menu);
		stage.show();
		update(controller.getModel(),null);
	}
	
	/**
	 * This sets up the visual representation of the cards by creating labels for them. 
	 * @param card
	 * 		The card that we are instantiating 
	 * @param playerHand
	 * 		boolean that indicates if the card we are making is in the player hand 
	 * @param onTable
	 * 		boolean that indicates if the card we are making is displayed on the table or book table
	 * @return
	 * 		returns a label of the card. 
	 */
	private Label makeCardView(Card card,boolean playerHand, boolean onTable) {
		Label ret;
		
		// if card is null then this is a placeholder card. Else get id from card and place in label
		if(card==null) {
			ret = new Label("");
			ret.getStyleClass().add("placeholder");
		} else if((onTable&&playerHand)||(!onTable)){
			ret = new Label(card.getId());
			ret.getStyleClass().add("card"+card.getId());
		} else if((onTable && !playerHand)){
			ret = new Label("");
			ret.setText("");
			ret.getStyleClass().add("cardBack");
			ret.getStyleClass().add("card"+card.getId());
		} else {
			ret = new Label("");
		}
		ret.getStyleClass().add("card");
		ret.applyCss();
		
		// if this is a playerhand and it's on the table, then set up the input event that 
		// allows for the card to be selected by player and set selectedcard to the card id. 
		if(playerHand && onTable) {
			ret.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent me) {
					selectedCard = ret.getText();
					for(int i=0;i<currentPlayerPane.getChildren().size();i++) {
						currentPlayerPane.getChildren().get(i).getStyleClass().remove("selected");
					}
					ret.getStyleClass().add("selected");
				}
			});
		}
		return ret;
	}
	
	/**
	 * This method sets up everything about visual representation of the "playerzones"
	 *  or 4 panes where players are placed.
	 * 
	 * @param mod
	 * 		the GoFishModel that we are basing this view off of.
	 */
	private void setPlayerZones(GoFishModel mod) {
		// First get number of players and then make the flowpane for the bottom / current player. 
		int numPlayers = mod.getPlayerDecks().length;
		FlowPane tablePlayerPane = (FlowPane)makePlayerZone(true,mod.getPlayerDecks()[mod.getCurrentTurn()],true);
		tablePlayerPane.getStyleClass().add("bottomPane");
		
		// iterate through the cards of the current player, make labels of them, and add them to the flowpane
		// if they have no cards then make it empty
		List<Card> cards = mod.getPlayerDecks()[mod.getCurrentTurn()].getHand();
		for(int j=0;j<cards.size();j++) {
			tablePlayerPane.getChildren().add(makeCardView(cards.get(j),true,true));
		}
		if(cards.size()==0) {
			for(int i=0;i<4;i++) {
				tablePlayerPane.getChildren().add(makeCardView(null,true,true));
			}
		}
		
		ScrollPane playerScroll = new ScrollPane();
		playerScroll.getStyleClass().add("scrollPane");
		playerScroll.setContent(tablePlayerPane);
		tablePane.add(playerScroll,1,2);//bottom
		currentPlayerPane = tablePlayerPane; // set currPlayerPane so we can set up click inputs for cards
		
		// now set up the books for the book view for the bottom pane
		FlowPane bookPlayerPane = (FlowPane)makePlayerZone(true,mod.getPlayerDecks()[mod.getCurrentTurn()],false);
		bookPlayerPane.getStyleClass().add("bottomPane");
		cards = mod.getPlayerDecks()[mod.getCurrentTurn()].getBookedCards();
		for(int j=0;j<cards.size();j++) {
			bookPlayerPane.getChildren().add(makeCardView(cards.get(j),true,false));
		}
		if(cards.size()==0) {
			for(int i=0;i<4;i++) {
				bookPlayerPane.getChildren().add(makeCardView(null,true,false));
			}
		}
		ScrollPane playerBookScroll = new ScrollPane();
		playerBookScroll.getStyleClass().add("scrollPaneBook");
		playerBookScroll.setContent(bookPlayerPane);
		bookPane.add(playerBookScroll, 1, 2);//bottom
		
		// these next two big for loops set up the views for the other players. 
		// this first for loop goes through index of the current player to the index of the last player 
		// and sets up their views 
		int i;
		for(i=mod.getCurrentTurn()+1;i<4;i++) {
			int diff = i-mod.getCurrentTurn(); // difference between index and current player. 
			// check to see if index is greater than number of players. If it is then make their pane blank
			// else then make their pane accordingly 
			ScrollPane opponentScroll = new ScrollPane();
			opponentScroll.getStyleClass().add("scrollPane");
			ScrollPane opponentScrollBook = new ScrollPane();
			opponentScrollBook.getStyleClass().add("scrollPaneBook");
			if (i >= numPlayers) {
				tablePlayerPane = (FlowPane)makePlayerZone(false,mod.getPlayerDecks()[0], false);
				bookPlayerPane = (FlowPane)makePlayerZone(false,mod.getPlayerDecks()[0],false);
			}
			else { 
				tablePlayerPane = (FlowPane)makePlayerZone(false,mod.getPlayerDecks()[i],true);
				bookPlayerPane = (FlowPane)makePlayerZone(false,mod.getPlayerDecks()[i],false);
			}
			// this switch statement uses the difference between the current index and current turn index 
			// to decide which pane to set the player flow pane in. 
			switch(diff) {
			case 1:
				//tablePlayerPane.getStyleClass().add("leftPane");
				opponentScroll.setContent(tablePlayerPane);
				tablePane.add(opponentScroll,0,1);//left
				//bookPlayerPane.getStyleClass().add("leftPane");
				opponentScrollBook.setContent(bookPlayerPane);
				bookPane.add(opponentScrollBook, 0, 1);//left
				break;
			case 2:
				opponentScroll.setContent(tablePlayerPane);
				//tablePlayerPane.getStyleClass().add("topPane");
				tablePane.add(opponentScroll,1,0);//top
				//bookPlayerPane.getStyleClass().add("topPane");
				opponentScrollBook.setContent(bookPlayerPane);
				bookPane.add(opponentScrollBook, 1, 0);//top
				break;
			case 3:
				opponentScroll.setContent(tablePlayerPane);
				//tablePlayerPane.getStyleClass().add("rightPane");
				tablePane.add(opponentScroll,2,1);//right
				//bookPlayerPane.getStyleClass().add("rightPane");
				opponentScrollBook.setContent(bookPlayerPane);
				bookPane.add(opponentScrollBook, 2, 1);//right
				break;
			}
			// this places the labels of the cards into the pane. If the index is greater 
			// than or equal to the numPlayers, then it sets blank cards in the pane
			if(i<numPlayers) {
				cards = mod.getPlayerDecks()[i].getHand();
				for(int j=0;j<cards.size();j++) {
					tablePlayerPane.getChildren().add(makeCardView(cards.get(j),false,true));
				}
				if(cards.size()==0) {
					for(int k=0;k<4;k++) {
						tablePlayerPane.getChildren().add(makeCardView(null,false,true));
					}
				}
				cards = mod.getPlayerDecks()[i].getBookedCards();
				for(int j=0;j<cards.size();j++) {
					bookPlayerPane.getChildren().add(makeCardView(cards.get(j),false,false));
				}
				if(cards.size()==0) {
					for(int k=0;k<4;k++) {
						bookPlayerPane.getChildren().add(makeCardView(null,false,false));
					}
				}
			}
			else {
				for (int k = 0; k<4; k++) {
					tablePlayerPane.getChildren().add(makeCardView(null, false, true));
				}
			}
		}
		// this for loop sets the panes for the players whose indices are less than that of the current
		// player's. 
		for(i=0;i<mod.getCurrentTurn();i++) {
			int diff = i+4-mod.getCurrentTurn();
			tablePlayerPane = (FlowPane)makePlayerZone(false,mod.getPlayerDecks()[i],true);
			bookPlayerPane = (FlowPane)makePlayerZone(false,mod.getPlayerDecks()[i],false);
			ScrollPane opponentScroll = new ScrollPane();
			opponentScroll.getStyleClass().add("scrollPane");
			ScrollPane opponentScrollBook = new ScrollPane();
			opponentScrollBook.getStyleClass().add("scrollPaneBook");
			
			switch(diff) {
			case 1:
				opponentScroll.setContent(tablePlayerPane);
				//tablePlayerPane.getStyleClass().add("leftPane");
				tablePane.add(opponentScroll,0,1);//left
				//bookPlayerPane.getStyleClass().add("leftPane");
				opponentScrollBook.setContent(bookPlayerPane);
				bookPane.add(opponentScrollBook, 0, 1);//left
				break;
			case 2:
				opponentScroll.setContent(tablePlayerPane);
				//tablePlayerPane.getStyleClass().add("topPane");
				tablePane.add(opponentScroll,1,0);//top
				//bookPlayerPane.getStyleClass().add("topPane");
				opponentScrollBook.setContent(bookPlayerPane);
				bookPane.add(opponentScrollBook, 1, 0);//top
				break;
			case 3:
				opponentScroll.setContent(tablePlayerPane);
				//tablePlayerPane.getStyleClass().add("rightPane");
				tablePane.add(opponentScroll,2,1);//right
				//bookPlayerPane.getStyleClass().add("rightPane");
				opponentScrollBook.setContent(bookPlayerPane);
				bookPane.add(opponentScrollBook, 2, 1);//right
				break;
			}
			cards = mod.getPlayerDecks()[i].getHand();
			for(int j=0;j<cards.size();j++) {
				tablePlayerPane.getChildren().add(makeCardView(cards.get(j),false,true));
			}
			if(cards.size()==0) {
				for(int k=0;k<4;k++) {
					tablePlayerPane.getChildren().add(makeCardView(null,false,true));
				}
			}
			cards = mod.getPlayerDecks()[i].getBookedCards();
			for(int j=0;j<cards.size();j++) {
				bookPlayerPane.getChildren().add(makeCardView(cards.get(j),false,false));
			}
			if(cards.size()==0) {
				for(int k=0;k<4;k++) {
					bookPlayerPane.getChildren().add(makeCardView(null,false,false));
				}
			}
		}
	}
	
	/**
	 * Set up a player indicator on the table view to label each player on the board
	 * @param currentTurn The index of the player whose turn it is
	 * @param numPlayers The total number of players in the game
	 */
	private void setPlayerIndicator(int currentTurn, int numPlayers) {
		BorderPane ind = new BorderPane();
		BorderPane bInd = new BorderPane();
		Label playerLabel = new Label("Player "+ (currentTurn + 1));
		Label bookLabel = new Label("Player "+(currentTurn+1));
		BorderPane.setAlignment(bookLabel,Pos.CENTER);
		BorderPane.setAlignment(playerLabel, Pos.CENTER);
		playerLabel.getStyleClass().add("playerLabel");
		bookLabel.getStyleClass().add("bookLabel");
		bInd.setBottom(bookLabel);
		ind.setBottom(playerLabel);
		
		// this works the same as the two big for loops in the setPlayerZones method
		for(int i=currentTurn+1;i<4;i++) {
			if(i<numPlayers) {
				playerLabel = new Label("Player "+ (i + 1));
				bookLabel = new Label("Player "+(i+1));
			} else {
				playerLabel = new Label("");
				bookLabel = new Label("");
			}
			BorderPane.setAlignment(playerLabel, Pos.CENTER);
			playerLabel.getStyleClass().add("playerLabel");
			BorderPane.setAlignment(bookLabel, Pos.CENTER);
			bookLabel.getStyleClass().add("bookLabel");
			int diff = i-currentTurn;
			switch(diff) {
			case 1:
				ind.setLeft(playerLabel);
				bInd.setLeft(bookLabel);
				break;
			case 2:
				ind.setTop(playerLabel);
				bInd.setTop(bookLabel);
				break;
			case 3:
				ind.setRight(playerLabel);
				bInd.setRight(bookLabel);
				break;
			}
		}
		for(int i=0;i<currentTurn;i++) {
			playerLabel = new Label("Player "+(i+1));
			BorderPane.setAlignment(playerLabel, Pos.CENTER);
			playerLabel.getStyleClass().add("playerLabel");
			bookLabel = new Label("Player "+(i+1));
			BorderPane.setAlignment(bookLabel, Pos.CENTER);
			bookLabel.getStyleClass().add("bookLabel");
			int diff = i+4-currentTurn;
			switch(diff) {
			case 1:
				ind.setLeft(playerLabel);
				bInd.setLeft(bookLabel);
				break;
			case 2:
				ind.setTop(playerLabel);
				bInd.setTop(bookLabel);
				break;
			case 3:
				ind.setRight(playerLabel);
				bInd.setRight(bookLabel);
				break;
			}
		}
		bookPane.add(bInd, 1, 1);
		tablePane.add(ind, 1, 1);
		
  }
  /**
	 * Sets up an indicator to show how many cards are remaining in the deck
	 * @param numCards The number of cards remaining in the deck
	 */
	private void setDeckIndicator(int numCards) {
		Label deck = new Label(""+numCards);
		deck.getStyleClass().add("deckIndicator");
		deck.setAlignment(Pos.CENTER);
		URL resource = GoFishGUIView.class.getResource("/images/back.png");
		Image img = new Image(resource.toString());
		ImageView view = new ImageView(img);
		view.setFitHeight(100);
		view.setFitWidth(65);
		deck.setGraphic(view);
		GridPane.setHalignment(deck, HPos.CENTER);
		tablePane.add(deck,2,2);
	}

	/**
	 * Update the table view with appropriate player zones from the model
	 * @param o The model object being observed
	 * @param arg An object used to pass data to observers - Not used
	 */
	@Override
	public void update(Observable o, Object arg) {
		GoFishModel mod = (GoFishModel) o;
		
		// First check to see if the game is over and alert if true. 
		if(mod.checkGameOver()) {
			ButtonType close = new ButtonType("Close");
			Alert al = new Alert(AlertType.INFORMATION, "Player "+(mod.getWinningPlayer()+1)+" has won!\nGame over." , close );
			Optional<ButtonType> result = al.showAndWait();
			if (result.isPresent() && result.get() == close) {
				Platform.exit();
				System.exit(0);
			}
		}
		
		// clear the panes so they can be changed. 
		turnPane.getChildren().clear();
		tablePane.getChildren().clear();
		bookPane.getChildren().clear();
		
		// set up the buffer. The if prevents the buffer from displaying when you first start 
		if(lastPlayer!=mod.getCurrentTurn()) {
			lastPlayer = mod.getCurrentTurn();
			Label bufferLabel = new Label("Player "+(lastPlayer + 1)+"'s turn!" + "\n" +"Press ENTER to continue");
			bufferLabel.getStyleClass().add("bufferLabel");
			BorderPane.setAlignment(bufferLabel, Pos.CENTER);
			turnPane.setCenter(bufferLabel);
			mainStage.setScene(turnBuffer);
		}
		
		// then set all of the player zones, the player indicators, and the deck indicator
		setPlayerZones(mod);
		setPlayerIndicator(mod.getCurrentTurn(),mod.getPlayerDecks().length);
		setDeckIndicator(mod.getDeck().size());
		
		// set up the save button in the top right corner
		Button saveButton = new Button("Save Game");
		GridPane.setHalignment(saveButton, HPos.CENTER);
		saveButton.getStyleClass().add("saveButton");
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent a) {
				mod.saveModel();
			}
		});
		tablePane.add(saveButton, 2, 0);
	}
	
	/**
	 * This sets up the menu pane. This is the title screen that the player can choose their settings, 
	 * and if they want to load their game from their last save. 
	 * @return
	 * 		returns the gridpane for the menu
	 */
	private GridPane getMenuPane() {
		GridPane toReturn = new GridPane();
		toReturn.setVgap(50);
		
		Label title = new Label("Go Fish");
		title.getStyleClass().add("title");
		
		// Set up the buttons for new game and load game and their actions
		Button newGame = new Button("New Game");
		newGame.setOnAction((event)->{
			mainStage.setScene(table);
		});
		toReturn.getStylesheets().add(getClass().getResource("view.css").toExternalForm());
		newGame.getStyleClass().add("startLoadButton");
		toReturn.getStyleClass().add("menuWindow");
		Button loadGame = new Button("Load Game");
		loadGame.setOnAction((event)->{
			GoFishModel mod = controller.loadModel();
			mod.addObserver(this);
			update(mod, null);
			mainStage.setScene(table);
			
		});
		loadGame.getStyleClass().add("startLoadButton");
		
		playersSetting = new HBox();
		playersSetting.setAlignment(Pos.CENTER);
		playersSetting.setSpacing(10);
		Button twoP = new Button("2 Players");
		twoP.getStyleClass().add("menuButton");
		Button threeP = new Button("3 Players");
		threeP.getStyleClass().add("menuButton");
		Button fourP = new Button("4 Players");
		fourP.getStyleClass().add("menuButton");
		twoP.setOnAction((event) -> {
			numOfPlayers = 2;
			GoFishModel mod = new GoFishModel(numOfPlayers, currRuleset);
			for(int i=0;i<playersSetting.getChildren().size();i++) {
				playersSetting.getChildren().get(i).getStyleClass().remove("selected");
			}
			twoP.getStyleClass().add("selected");
			mod.addObserver(this);
			controller.setModel(mod);
			update(mod, null);
		});
		threeP.setOnAction((event) -> {
			numOfPlayers = 3;
			GoFishModel mod = new GoFishModel(numOfPlayers, currRuleset);
			for(int i=0;i<playersSetting.getChildren().size();i++) {
				playersSetting.getChildren().get(i).getStyleClass().remove("selected");
			}
			threeP.getStyleClass().add("selected");
			mod.addObserver(this);
			controller.setModel(mod);
			update(mod, null);
		});
		fourP.setOnAction((event) -> {
			numOfPlayers = 4;
			GoFishModel mod = new GoFishModel(numOfPlayers, currRuleset);
			for(int i=0;i<playersSetting.getChildren().size();i++) {
				playersSetting.getChildren().get(i).getStyleClass().remove("selected");
			}
			fourP.getStyleClass().add("selected");
			mod.addObserver(this);
			controller.setModel(mod);
			update(mod, null);
		});
		playersSetting.getChildren().add(twoP);
		playersSetting.getChildren().add(threeP);
		playersSetting.getChildren().add(fourP);
		
		
		ruleSetting = new HBox();
		ruleSetting.setAlignment(Pos.CENTER);
		Button r1 = new Button("Standard");
		r1.getStyleClass().add("menuButton");
		Button r2 = new Button("Risky");
		r2.getStyleClass().add("menuButton");
		Button r3 = new Button("Popcorn");
		r3.getStyleClass().add("menuButton");
		r1.setOnAction((event) -> {
			for(int i=0;i<ruleSetting.getChildren().size();i++) {
				ruleSetting.getChildren().get(i).getStyleClass().remove("selected");
			}
			r1.getStyleClass().add("selected");
			currRuleset = GameType.BASIC;
			GoFishModel mod = new GoFishModel(numOfPlayers, currRuleset);
			mod.addObserver(this);
			controller.setModel(mod);
			update(mod, null);
		});
		r2.setOnAction((event) -> {
			for(int i=0;i<ruleSetting.getChildren().size();i++) {
				ruleSetting.getChildren().get(i).getStyleClass().remove("selected");
			}
			r2.getStyleClass().add("selected");
			currRuleset = GameType.VARIANT_1;
			GoFishModel mod = new GoFishModel(numOfPlayers, currRuleset);
			mod.addObserver(this);
			controller.setModel(mod);
			update(mod, null);
		});
		r3.setOnAction((event) -> {
			for(int i=0;i<ruleSetting.getChildren().size();i++) {
				ruleSetting.getChildren().get(i).getStyleClass().remove("selected");
			}
			r3.getStyleClass().add("selected");
			currRuleset = GameType.VARIANT_2;
			GoFishModel mod = new GoFishModel(numOfPlayers, currRuleset);
			mod.addObserver(this);
			controller.setModel(mod);
			update(mod, null);
		});		
		toReturn.add(title, 0, 0);
		toReturn.add(newGame, 0, 1);
		toReturn.add(loadGame, 0, 2);
		toReturn.add(playersSetting, 0, 3);
		toReturn.add(ruleSetting, 0, 4);
		
		return toReturn;
	}
	
	/**
	 * Create a Pane object representing a player's hand for the table or book views
	 * @param myTurn If it is currently this player's turn
	 * @param player The player object being represented
	 * @param isTable True to make a pane for the table view, false for the books view
	 * @return A Pane object for the Table or Book views
	 */
	private Pane makePlayerZone(boolean myTurn,Player player,boolean isTable) {
		Pane ret = new FlowPane();
		ret.getStyleClass().add("playerZone");
		if(!myTurn && isTable) {
			ret.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent me) {
					if(selectedCard!=null) {
						controller.handleUserMove(player, selectedCard);
					}
				}
			});
		}
		return ret;
	}
	
	/**
	 * Main method, launches GUI application
	 * @param args Command line arguments - Not used
	 */
	public static void main(String[] args) {
		launch();
	}
}