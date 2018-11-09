package application;
	
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
	// set flagMode to false initially
	boolean flagMode = false;
	
	//set counts
	int minesRemaining = 10;
	int correctFlags = 0;
	
	@Override
	public void start(Stage primaryStage) {
	//Data board creation and Mine setting START
		//set vars for board size and mines
		int mineCount = 10;
		int boardHeight = 10;
		int boardWidth = 10;
				
		// LAYER ONE OF BOARD
		//initialize the 2D array for board
		GameBoardCell[][] minefield = new GameBoardCell[boardWidth][boardHeight];
		
		//for loop to do minefield[0][0] = new GameBoardCell();
		for(int i = 0; i < boardWidth; i++) {
			for(int j = 0; j < boardHeight; j++) {
				minefield[i][j] = new GameBoardCell();
			}
		}
		
		// SET MINES
		// SET COUNT OF CELLS AROUND HERE ALSO
		Random rand  = new Random();
		for(int b = 0; b < mineCount; b++) {
			int randRow = rand.nextInt(boardWidth);
			int randCol = rand.nextInt(boardHeight);
			//System.out.println("Mine created at " + randRow + ", " + randCol);
				if (minefield[randRow][randCol].isMine() == false) {
					// if no mine, set mine here
					minefield[randRow][randCol].setMine(true);
					//must have try catch to catch the out of bounds cells
					//for loop 
					for(int y = (randRow - 1); y < (randRow + 2); y ++) {
						for(int x = (randCol - 1); x < (randCol + 2); x ++) {
							//this does not handle left side of board out of bounds
							try {
								//raise surronding cells minesNear count ++
								minefield[y][x].setBombsNear(minefield[y][x].bombsNear() + 1);
								//System.out.println("Bomb count for cell["+ y + "][" + x + "]: " + minefield[y][x].bombsNear());
							} catch (Exception ex) {
								// JUST MOVE ON IN LOOP
							}
						}
					}
				} else {
					//cell is already bomb, run again, make sure to still get 10 mines set
					b--;
				}
			}
	//Data board creation and Mine setting END
	//GUI button grid and action events START	
		try {
			//set a title
			primaryStage.setTitle("MINESWEEPER");
			
			//set grid pane
			GridPane root = new GridPane();
			
			//array of buttons 10x10
			Button[][] btnField = new Button[10][10];
		
			//mines remaining label
			Label lbl1 = new Label();
			lbl1.setText("mine");
			lbl1.setPrefSize(30, 30);
			lbl1.setAlignment(Pos.BOTTOM_CENTER);
			root.add(lbl1, 0, 11);
			
			Label lbl2 = new Label();
			lbl2.setText("left:");
			lbl2.setPrefSize(30, 30);
			lbl2.setAlignment(Pos.BOTTOM_CENTER);
			root.add(lbl2, 1, 11);
			
			Label count = new Label();
			count.setText(Integer.toString(minesRemaining));
			count.setPrefSize(30, 30);
			count.setAlignment(Pos.BOTTOM_CENTER);
			root.add(count,2,11);
			
			Label gameOverLabel1 = new Label();
			gameOverLabel1.setText("");
			count.setPrefSize(30, 30);
			gameOverLabel1.setAlignment(Pos.BOTTOM_CENTER);
			root.add(gameOverLabel1, 7, 11);
			
			Label gameOverLabel2 = new Label();
			gameOverLabel2.setText("");
			count.setPrefSize(30, 30);
			gameOverLabel2.setAlignment(Pos.BOTTOM_CENTER);
			root.add(gameOverLabel2, 8, 11);
			
			//space bar flagged boolean event filter
			root.addEventFilter(KeyEvent.KEY_PRESSED, e ->{
				if (e.getCode() == KeyCode.SPACE) {
					if (flagMode) {
						flagMode = false;
					}
					else {
						flagMode = true;
					}
					//System.out.println(flagMode);
				}
			});
			
			//mouse click event handler
			//this is where the action happens when a button is clicked:
			EventHandler<MouseEvent> click = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				
				//btn clicked coordinates
				int x = GridPane.getColumnIndex((Node) e.getSource());
				int y = GridPane.getRowIndex((Node) e.getSource());
				
				//if NOT in flag mode:
				if (!flagMode & !minefield[y][x].isFlagged()) {
					//set isClicked() to true
					minefield[y][x].setClicked(true);
					
					//check if mine
					if(minefield[y][x].isMine()) {
						//set B text, disable button
						btnField[y][x].setText("B");
						btnField[y][x].setDisable(true);
						
						//SHOULD end game here if mine is revealed & show whole board
						gameOverLabel1.setText("You");
						gameOverLabel2.setText("Lose");
						
						//reveal and disable all cells
						for(int i = 0; i < boardWidth; i++) {
							for(int j = 0; j < boardHeight; j ++) {
								//reveal all cell values
								if (minefield[i][j].isMine()) {
									btnField[i][j].setText("B");
								}
								else if(minefield[i][j].bombsNear() != 0) {
									btnField[i][j].setText(Integer.toString(minefield[i][j].bombsNear()));
								}
								else {
									btnField[i][j].setText("");
								}
								
								//disable all cells
								btnField[i][j].setDisable(true);
							}
						}
					}
					
					//check if number
					else if(minefield[y][x].bombsNear() != 0) {
						//reveal number of bombs near, disable button
						btnField[y][x].setText(Integer.toString(minefield[y][x].bombsNear()));
						btnField[y][x].setDisable(true);
					}
					
					//empty
					else {
						//***CASCADE CLICK GOES HERE!!!***
						//disable button
						btnField[y][x].setDisable(true);
					}
				}
				
				//if in flagMode:
				else {

					//if (!minefield[y][x].isClicked()) { //NEED TO MAKE SURE THIS FLAG WILL RESIST A NON FLAG MODE CLICK!!!	
						//check if flagged
						if (minefield[y][x].isFlagged()) {
							//undo F text, set flagged back to false
							btnField[y][x].setText("");
							minefield[y][x].setFlagged(false);
							minesRemaining ++;
							count.setText(Integer.toString(minesRemaining));
							if (minefield[y][x].isMine()) {
								correctFlags --;
							}}
						else {
							//set F text, set .isFlagged() to true
							btnField[y][x].setText("F");
							minefield[y][x].setFlagged(true);
							minesRemaining --;
							count.setText(Integer.toString(minesRemaining));
							if (minefield[y][x].isMine()) {
								correctFlags ++;
							}}
						
						//check for win here:
						//condition to check to see if correctFlagCount == 10;
						if (correctFlags == mineCount) {
							gameOverLabel1.setText("You");
							gameOverLabel2.setText("Win!");
							
							//disable all cells
							for(int i = 0; i < boardWidth; i++) {
								for(int j = 0; j < boardHeight; j ++) {
									btnField[i][j].setDisable(true);
								}
							}
						}
						
						//make count for correctFlagCount
						//make count for totalFlags - starts at 10, can be -, if - cant win, change label
						
					//}
				}	
			}};
			
			//BUTTON(2nd) LAYER OF GAMEBOARD
			for(int i = 0; i < boardWidth; i++) {
				for(int j = 0; j < boardHeight; j ++) {
					//create new button
					Button btn = new Button();
					
					//add button to button array
					btnField[i][j] = btn;
					
					//set btn properties
					btn.setPrefSize(30, 30);
					btn.setAlignment(Pos.TOP_CENTER);
		
					
					//set the button equal to an index position [i][j] in the array of buttons
					GridPane.setRowIndex(btn, i);
					GridPane.setColumnIndex(btn, j);
					
					//get btn and show it in grid
					root.getChildren().addAll(btn);
					
					// adding event filter
					btn.addEventFilter(MouseEvent.MOUSE_CLICKED, click);
				}
			}
			
			//show GUI
			Scene scene = new Scene(root, 300, 350);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	//TODO cascading click algorithm

	//TODO We need to find a way to clear board, new method?
	
}
