package linukGamePackage;

import java.util.ArrayList;


public class AI {
	protected byte[][] board ;
	protected static final byte EMPTY = 0, PLAYER1 = 1, PLAYER2 = 2, BLOCKED = 3;
	private byte[] player1Location;
	private byte[] player2Location;
	private static final boolean printNodeCount = false;
	private static final boolean debugMode = false;
	private static final boolean alphaBetaEnable = true;
	private static final boolean debugEvaluationScores = false;
	private static final int maxEvaluationScore = 99;

	protected Node bestNode ;
	ArrayList<Node> DFSTree;
	private static final byte evaluationFunctionRadius = 2;
	private static long nodeCounter = 0;
	

	public AI ( byte[][] board, byte[] player1Location, byte[] player2Location, boolean aIVsAI) {
		this(board, player1Location, player2Location );
		
	}
	public AI(byte[][] board, byte[] player1Location, byte[] player2Location) {
		if ( printNodeCount )
			System.out.println(++nodeCounter);

		this.board = getNewCopyOf2DByteArray(board);
		
		this.setPlayer1Location(player1Location.clone());
		this.setPlayer2Location(player2Location.clone());
		if (checkBoardLocation(board, player1Location) != PLAYER1) {
			
			System.out.println("bug detected: player 1 Location" + player1Location[0] + " " + player1Location[1]);
			printBoardMatrix(board);
			System.exit(1);
		}

		if (checkBoardLocation(board, player2Location) != PLAYER2) {
			System.out.println("bug detected: player 2 Location" + player2Location[0] + " " + player2Location[1]);
			printBoardMatrix(board);
			System.exit(1);
		}

		checkBoardIntegrity();
	}

	public Node getBestMove() {
		return bestNode;
	}

	private double evaluationFunction(byte playerType) {
		double score =  evaluateScore(getPlayer2Location())
				- evaluateScore(getPlayer1Location());
		
		return (playerType == PLAYER2) ? score : -score;
	}

	private double evaluateScore( byte[] location ) {
		double score = 0;
		double levelScore;
		byte minX, maxX, minY, maxY;
		double distanceValue;

		for ( byte i = 1; i <= evaluationFunctionRadius ; i++ ) {
			minX = (byte) (location[0] - i);
			maxX = (byte) (location[0] + i);
			minY = (byte) (location[1] - i);
			maxY = (byte) (location[1] + i);
			levelScore = 0;
			distanceValue = 1.0 / i ;
			if (minY >= 0 && minY < 7)
				for (byte x = minX; x < maxX; x++) {
					if (x < 0)
						continue;
					if (x > 6)
						break;

					if (checkBoardLocation(board, x, minY) == EMPTY)
						levelScore += distanceValue;
				}
			if (maxX >= 0 && maxX < 7)
				for (byte y = minY; y < maxY; y++) {
					if (y < 0)
						continue;
					if (y > 6)
						break;
					if (checkBoardLocation(board, maxX, y) == EMPTY)
						levelScore += distanceValue;
				}
			if (maxY >= 0 && maxY < 7)
				for (byte x = maxX; x > minX; x--) {
					if (x < 0)
						break;
					if (x > 6)
						continue;
					if (checkBoardLocation(board, x, maxY) == EMPTY)
						levelScore += distanceValue;
				}
			if (minX >= 0 && minX < 7)
				for (byte y = maxY; y > minY; y--) {
					if (y < 0)
						break;
					if (y > 6)
						continue;
					if (checkBoardLocation(board, minX, y) == EMPTY)
						levelScore += distanceValue;
				}
			score += levelScore;
			if( score == 0 && i == 1) {
				return -maxEvaluationScore;
			}
		}
		return score;
	}


	private byte checkBoardLocation(byte[][] board, byte[] currentLocation) {
		// this method checks the board and returns the status of the box
		try {
			return board[currentLocation[0]][currentLocation[1]];
		} catch (ArrayIndexOutOfBoundsException e) {
			if (debugMode)
				System.out.println( "location " + currentLocation[0] + " "
						+ currentLocation[1]);
//			System.exit(1);
			return -1;
		}
	}

	private byte checkBoardLocation(byte[][] board, int x, int y) {
		if ( x > 6 || x < 0){
			System.out.println("x out of boundry");
			return -1;
		}
		if ( y > 6 || y < 0){
			System.out.println("y out of boundry");
			return -1;
		}
		return board[x][y];
	}


	protected double alpha( int depth, double alpha, double beta , byte playerType, byte significantBoardLength ) {
		double score;
		if ( depth == 0 ) {
			return evaluationFunction( playerType );
		}
		 DFSTree = getAvailableMoves( board, playerType, significantBoardLength );
		
		if (DFSTree.isEmpty()) {
			return evaluationFunction( playerType );
		}
		
		for ( Node child : DFSTree ) {

			score = child.beta( depth - 1, alpha, beta , playerType, significantBoardLength );

			if ( score >= beta && alphaBetaEnable ) {
				DFSTree.clear();
				return beta;
			}				
			
			if ( score > alpha ){
				alpha = score;
				bestNode = child;

			}//if of new bestNode
		}// for each child
		DFSTree.clear();
		
		return alpha;
	}//alpha



	protected double beta( int depth, double alpha, double beta , byte playerType, byte significantBoardLength ) {
		double score;
		byte otherPlayer = (playerType == PLAYER1) ? PLAYER2 : PLAYER1;
		if ( depth == 0 ) {		
			return -evaluationFunction( otherPlayer );
		}

		DFSTree = getAvailableMoves( board, otherPlayer, significantBoardLength );
		
		if ( DFSTree.isEmpty() ) {
			return -evaluationFunction( otherPlayer );
		}
		for (Node child : DFSTree) {

			score = child.alpha( depth - 1, alpha, beta ,playerType , significantBoardLength);
			if ( score <= alpha && alphaBetaEnable ) {
				DFSTree.clear();
				return alpha;
			}
			if ( score < beta )
				beta = score;

		}
		DFSTree.clear();
		return beta;
	}


	protected ArrayList<Node> getAvailableMoves(byte[][] board, byte playerType, byte significantBoardLength ) {
		ArrayList<Node> AIList = new ArrayList<Node>();
		byte[] player2SimulatedLocation;
		byte[] player1SimulatedLocation;
		byte[] position;

		if (playerType == PLAYER2) {
			player1SimulatedLocation = getPlayer1Location();
			player2SimulatedLocation = getPlayer2Location().clone();
			position = player2SimulatedLocation;
		} else {
			player2SimulatedLocation = getPlayer2Location();
			player1SimulatedLocation = getPlayer1Location().clone();
			position = player1SimulatedLocation;
		}
		byte[] center = position.clone();
		board[position[0]][position[1]] = EMPTY;

		
		for ( byte x = (byte) (( center[0] -1 < 0 ) ? 0 : center[0] -1); x <= (center[0] + 1) && x < 7 ; x ++ ) 
			for ( byte y = (byte) ((center[1] -1 < 0 ) ? 0 : center[1] -1) ; y <= (center[1] +1 ) && y < 7 ; y ++ ) {
				if ( x == center[0] )
					if ( y == center[1] )
						continue;
				if ( board[x][y] == EMPTY ) {
					board[x][y] = playerType;
					position[0] = x;
					position[1] = y;
					AIList.addAll( getAvailableBlockMoves(
							board, player2SimulatedLocation, player1SimulatedLocation, 
							playerType, position, significantBoardLength ));
					board[x][y] = EMPTY;
				}	
		}
		return AIList;
	}

	private ArrayList<Node> getAvailableBlockMoves(
			byte[][] modifiedBoard,	byte[] player2Location, 
			byte[] player1Location, byte playerType, byte[] nextMove, 
			byte significantBoardLength) {
		
		// this method provides available block moves for a certain player.
		// if moves are calculated for computer then blocks will be chosen
		// around player
		ArrayList<Node> availableBlockMoves = new ArrayList<Node>();
		byte[] center;
		
		center = (playerType == PLAYER2 ) ? player1Location : player2Location;
		
		byte minX, maxX, minY, maxY;
		byte[] nextBlock = new byte[2];
		for (byte i = 1; i <= significantBoardLength; i++) {
			minX = (byte) (center[0] - i);
			maxX = (byte) (center[0] + i);
			minY = (byte) (center[1] - i);
			maxY = (byte) (center[1] + i);
			
			if (minY >= 0 && minY < 7)
				for (byte x = (minX < 0) ? 0 : minX ; x < maxX && x < 7; x++) {

					if ( checkBoardLocation(modifiedBoard, x, minY) == EMPTY) {

						modifiedBoard[x][minY] = BLOCKED;

						nextBlock[0] = x;
						nextBlock[1] = minY;
						availableBlockMoves.add( new Node(
								modifiedBoard, player1Location, player2Location, nextMove, nextBlock));

						modifiedBoard[x][minY] = EMPTY;
					}//available move
				} // y coordinate constant on min checking empty boxes on x coordinate from left
			
			if (maxX < 7 && maxX >= 0)
				for (byte y = ( minY < 0 ) ? 0 : minY ; y < maxY && y < 7; y++) {

					if (checkBoardLocation(modifiedBoard, maxX, y) == EMPTY) {
						modifiedBoard[maxX][y] = BLOCKED;

						nextBlock[0] = maxX;
						nextBlock[1] = y;			
						availableBlockMoves.add(new Node(
								modifiedBoard, player1Location, player2Location, nextMove, nextBlock));

						modifiedBoard[maxX][y] = EMPTY;
					}//available move
				} // x coordinate constant on max checking empty boxes on y coordinate from bottom
			
			if (maxY >= 0 && maxY < 7)
				for (byte x = (maxX > 6) ? 6 : maxX ; x > minX && x >= 0; x--) {

					if (checkBoardLocation(modifiedBoard, x, maxY) == EMPTY) {

						modifiedBoard[x][maxY] = BLOCKED;
						nextBlock[0] = x;
						nextBlock[1] = maxY;
						availableBlockMoves.add( new Node(
								modifiedBoard, player1Location, player2Location, nextMove, nextBlock));

						modifiedBoard[x][maxY] = EMPTY;

					}//available move
				}// y coordinate constant on max checking empty boxes on x coordinate from right
			if (minX >= 0 && minX < 7)
				for (byte y = (maxY > 6) ? 6 : maxY ; y > minY && y >= 0; y--) {

					if (checkBoardLocation(modifiedBoard, minX, y) == EMPTY) {

						modifiedBoard[minX][y] = BLOCKED;
						nextBlock[0] = minX;
						nextBlock[1] = y;
						availableBlockMoves.add(new Node(
								modifiedBoard, player1Location, player2Location, nextMove, nextBlock));

						modifiedBoard[minX][y] = EMPTY;

					}//available move
				}// x coordinate constant on min checking empty boxes on y coordinate from top
		}// outer for loop

		return availableBlockMoves;
	}

	private byte[][] getNewCopyOf2DByteArray(byte[][] arrayToCopy) {
		byte[][] copyOfArray ;

		copyOfArray = arrayToCopy.clone();
		for (int i = 0; i < arrayToCopy.length; i++)
			copyOfArray[i] = arrayToCopy[i].clone();

		return copyOfArray;
	}

	public void printBoardMatrix(byte[][] boardMatrix) {
		for (int i = 0; i < 7; i++) {
			System.out.printf("%d: ", i);
			for (int j = 0; j < 7; j++)
				System.out.print(boardMatrix[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}

	protected boolean checkBoardIntegrity(byte[][] board) {
		boolean player2LocationFound = false;
		boolean player1LocationFound = false;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (board[i][j] == PLAYER1) {
					if (player1LocationFound) {
						System.out.println("boardMatrix is broken");
						printBoardMatrix(board);
						System.exit(1);
						return false;
					} else
						player1LocationFound = true;
				} else if (board[i][j] == PLAYER2) {
					if (player2LocationFound) {
						System.out.println("boardMatrix is broken");
						printBoardMatrix(board);
						System.exit(1);
						return false;
					} else
						player2LocationFound = true;
				}
			}
		}
		if (!(player1LocationFound && player2LocationFound)) {
			System.out.println("board is broken");
			printBoardMatrix(board);
			System.exit(1);
			return false;
		} else
			return true;
	}

	protected boolean checkBoardIntegrity() {
		return checkBoardIntegrity(board);
	}	
	public boolean checkValidMoves(byte[] location){
		byte[] player = location;

		for ( int x = player[0] - 1; x <= player[0] + 1; x++ ){
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
					else if ( board [x][y] == EMPTY ){
						return true;
					}
					else 
						if ( debugEvaluationScores )
							System.out.println("location " + x + " " + y + "'s value is " + board[x][y]);
				}
		}
		return false;
	}
	public byte[] getPlayer1Location() {
		return player1Location;
	}
	public void setPlayer1Location(byte[] player1Location) {
		this.player1Location = player1Location;
	}
	public byte[] getPlayer2Location() {
		return player2Location;
	}
	public void setPlayer2Location(byte[] player2Location) {
		this.player2Location = player2Location;
	}
}
