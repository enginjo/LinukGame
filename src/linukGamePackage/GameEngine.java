package linukGamePackage;



import javaGuiPackage.GameView;

import javax.swing.JOptionPane;

public class GameEngine {
	// this is the engine that runs for the game. 
	// providing another gui will not change this part
	public static int DEPTH = 6;
	private static final boolean enableIterativeDeepening = true;
	private static final int iterativeDeepeningTime = 1000;
	private static byte significantBoardLength = 2;
	private static final boolean debugMode = false;
	byte[][] boardMatrix = new byte[7][7];

	boolean player1Turn = true;
	boolean player1Moved;
	
	GameView gameView;

	private static final byte EMPTY = 0, PLAYER1 = 1, PLAYER2 = 2, BLOCKED = 3;
	
	private byte[] player2Location = { 0, 3 };
	private byte[] player1Location = { 6, 3 };
	
	
	public GameEngine( GameView gameView ){
		player1Moved = false;
		boardMatrix[0][3] = PLAYER2;
		boardMatrix[6][3] = PLAYER1;

		this.gameView = gameView;

	}
	public Node AIMove(byte playerType, int depth ) {
		return AIMove(playerType, depth, significantBoardLength);
	}
	public Node AIMove(byte playerType , int depth, byte significantBoardLength ) {
		Node head;
		if( checkValidMoves ( playerType ) ) {
			head = new Node( boardMatrix, player1Location, player2Location );
			long startTime = System.currentTimeMillis();
			
			head.alpha( depth, -9999, 9999, playerType, significantBoardLength);
			System.out.println( "move calculation time for player" + playerType  + " " + ( System.currentTimeMillis() - startTime ) + " miliseconds" );

			return head;
		} else {
			System.out.println("----player" + playerType + "  lost----");
			return null;
		}
	}

	public void AIMove( byte playerType ) {
		Node head = null;
		if ( enableIterativeDeepening ) {
			long startTime = System.currentTimeMillis();
			for (int i = 1; System.currentTimeMillis() - startTime < iterativeDeepeningTime ; i++ ){
				DEPTH = i;
				System.out.println("iterative depth level " + i);
				head = AIMove ( playerType, i);
			}
		} else {
			head = AIMove( playerType, DEPTH );
		}
		
		gameView.computerMove( playerType, head );
		player1Turn = true;
		player1Moved = false;

		if ( !checkValidMoves( playerType ) ) {
			System.out.println("player" + playerType + " won!");
		}
			
	}

	public boolean playerMove (byte playerType, byte x, byte y){
		byte computer;
		byte player;
		byte[] player1Location;
		if ( playerType == PLAYER1 ) {
			computer = PLAYER2;
			player = PLAYER1;
			player1Location = this.player1Location;
		} else {
			computer = PLAYER1;
			player = PLAYER2;
			player1Location = this.player2Location;
		}
		if ( !checkValidMoves(playerType) ) {
			JOptionPane.showMessageDialog(gameView, "you lost!");
		}
			
		if(player1Turn){
			if(player1Moved)
				if(boardMatrix[x][y] == EMPTY){
					gameView.updateBox(x, y, BLOCKED);
					boardMatrix[x][y] = BLOCKED;
					player1Turn = false;
					if ( !checkValidMoves(computer) ){
						JOptionPane.showMessageDialog(gameView, "you won!");
						return false;
					}
						
					AIMove( computer );
					return true;
				} else { 
					printBoardMatrix();
					JOptionPane.showMessageDialog(null, "That box is not empty");
					return false;
				}
			else{
				if(boardMatrix[x][y] == EMPTY){
					if(Math.abs(player1Location[0] - x) <= 1){
						if(Math.abs(player1Location[1]- y) <= 1){
							boardMatrix[player1Location[0]][player1Location[1]] = EMPTY;
							boardMatrix[x][y] = player;
							gameView.updateBox(x, y, player);
							gameView.updateBox(player1Location[0],player1Location[1],EMPTY);
							player1Location[0] = x;
							player1Location[1] = y;
							player1Moved = true;
//							printBoardMatrix();
							return true;
						}
					}
					JOptionPane.showMessageDialog(null, "You can only move 1 box at a time");
					return false;
				}
				JOptionPane.showMessageDialog(null, "That box is not empty");
				return false;					
			}
		}else {
			JOptionPane.showMessageDialog(null,"Please wait your turn");
			return false;
		}
	}
	public void playerMove(byte x, byte y) {
		playerMove(PLAYER1, x, y);
	}

	public void printBoardMatrix(){
		printBoardMatrix(boardMatrix);
	}
	public void printBoardMatrix(byte[][] boardMatrix){
		if ( debugMode ) 
			for ( int i = 0; i < 7 ; i ++ ){
				for ( int j = 0; j < 7 ; j ++ )
					System.out.print(boardMatrix[i][j] + " ");
				System.out.println();
			}
	}
	public boolean checkValidMoves(byte playerType){
		byte[] player;
		if (playerType == PLAYER2)
			player = player2Location;
		else
			player = player1Location;
		for (int x = player[0] - 1; x <= player[0] + 1; x++ ){
			if (x < 0)
				continue;
			else if( x > 6 )
				break;
			else
				for (int y = player[1] - 1 ; y <= player[1] + 1; y++ ){
					if ( y < 0 )
						continue;
					else if ( y > 6 )
						break;
					else if ( boardMatrix [x][y] == EMPTY ){
						return true;
					}
					else if ( debugMode)
						System.out.println("location " + x + " " + y + "'s value is " + boardMatrix[x][y]);
				}
		}
		System.out.println("no valid move");
//		if (playerType == PLAYER2)
//			JOptionPane.showMessageDialog(null, "Player 2 Won");
//		else
//			JOptionPane.showMessageDialog(null, "Player 1 Won");
		//gameView.dispose();
		return false;
	}
	public void moveComputerToLocation(byte playerType, byte x, byte y){
		byte[] location;
		if ( playerType == PLAYER1 )
			location = player1Location;
		else  if ( playerType == PLAYER2 )
			location = player2Location;
		else {
			System.out.println("error while moving player" + (playerType) );
			return;
		}
			
		printBoardMatrix();
		boardMatrix[location[0]][location[1]] = EMPTY;
		printBoardMatrix();
		boardMatrix[x][y] = playerType;
		printBoardMatrix();
		location[0] = x;
		location[1] = y;
		
	}
	public void moveComputerToLocation(byte playerType, byte[] toLocation){
		moveComputerToLocation(playerType, toLocation[0], toLocation[1]);
	}
	public void movePlayerToLocation(byte x, byte y){
		boardMatrix[player1Location[0]][player1Location[1]] = EMPTY;
		boardMatrix[x][y] = PLAYER1;
		player1Location[0] = x;
		player1Location[1] = y;
	}
	public void movePlayerToLocation(byte[] location){
		movePlayerToLocation(location[0], location[1]);
	}
	public void clearLocation(byte[] location){
		boardMatrix[location[0]][location[1]] = EMPTY;
	}
	public void updateBoardMatrix(byte x, byte y, byte updateValue) {
		// TODO Auto-generated method stub
		boardMatrix[x][y] = updateValue;
	}
}
