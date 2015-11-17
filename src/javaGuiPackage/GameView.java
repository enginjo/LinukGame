package javaGuiPackage;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import linukGamePackage.GameEngine;
import linukGamePackage.Node;



public class GameView extends JFrame {
	/**
	 * this class is the gui of the Linuk game for JAVA 
	 * it runs GameEngine and shows the components in a GUI 
	 */

	protected static final byte EMPTY = 0, PLAYER1 = 1, PLAYER2 = 2, BLOCKED = 3;
	private static final long serialVersionUID = 3670649723572341739L;
	
	final static int boardSize = 7;
	private boolean debugMode = false;

	ImageIcon backgroundIMageIcon = new ImageIcon(getClass().getResource("/images/background.jpg"));
	JLabel background = new JLabel(backgroundIMageIcon);
	JLabel textLabel = new JLabel("Linuk Game");
	JPanel textPanel = new JPanel();
	JPanel controlPanel = new JPanel();
	private GameEngine gameEngine;
	ArrayList<ArrayList<ButtonBox>> buttonArray;
	public GameView(boolean playerMovesFirst){
		
		gameEngine = new GameEngine( this );
		
		textLabel.setForeground(Color.red);
		textPanel.add(textLabel);
		background.setSize(550, 550);
		controlPanel.setSize(310, 310);
	    controlPanel.setLayout(new GridLayout(boardSize,boardSize));
	    controlPanel.setLocation(100,20);
		setContentPane( background );
		add(controlPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(550,550);
		buttonArray = new ArrayList<ArrayList<ButtonBox>>();
		
		for (byte i = 0; i < boardSize; i ++){
			buttonArray.add(new ArrayList<ButtonBox>());
			for (byte j = 0; j < boardSize ; j ++){
				ButtonBox buttonToAdd = new ButtonBox(i,j,this);
				buttonArray.get(i).add(buttonToAdd);
				controlPanel.add(buttonToAdd);
			}
		}
		buttonArray.get(0).get(3).setBoxStatus(ButtonBox.PLAYER2);
		buttonArray.get(6).get(3).setBoxStatus(ButtonBox.PLAYER1);
		if ( !playerMovesFirst )
			gameEngine.AIMove( PLAYER2 );

	}
	public GameView( ){ 
		// if no parameter is used then AI Vs AI test is set
		textLabel.setForeground(Color.red);
		textPanel.add(textLabel);
		background.setSize(550, 550);
		controlPanel.setSize(310, 310);
	    controlPanel.setLayout(new GridLayout(boardSize,boardSize));
	    controlPanel.setLocation(100,20);
		setContentPane( background );
		add(controlPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(550,550);
		buttonArray = new ArrayList<ArrayList<ButtonBox>>();
		
		for (byte i = 0; i < boardSize; i ++){
			buttonArray.add(new ArrayList<ButtonBox>());
			for (byte j = 0; j < boardSize ; j ++){
				ButtonBox buttonToAdd = new ButtonBox(i,j,this);
				buttonArray.get(i).add(buttonToAdd);
				controlPanel.add(buttonToAdd);
			}
		}
		buttonArray.get(0).get(3).setBoxStatus(ButtonBox.PLAYER2);
		buttonArray.get(6).get(3).setBoxStatus(ButtonBox.PLAYER1);
		gameEngine = new GameEngine( this );
		
//		gameEngine.aIVsAIMove();
		AIvsAIMove();
	}

	private void AIvsAIMove() {
		// TODO Auto-generated method stub
		boolean player1sTurn = true;
		int depth1 = 5, depth2 = 5, depth;
		byte significantBoardLength1 = 3,significantBoardLength2 = 2, significantBoardLength;
		Node node;
		byte playerType;
		gameEngine = new GameEngine(this);
		System.out.println("significant board length1: " + significantBoardLength1 + " significant board length2: " + significantBoardLength2 );
		System.out.println("depth1: " + depth1 + " depth2: " + depth2 + "\n");
		for(;;) {
			if ( player1sTurn ) {
				playerType = PLAYER1;
				depth = depth1;
				significantBoardLength = significantBoardLength1;
				if ( !gameEngine.checkValidMoves(PLAYER2) ) {
					JOptionPane.showMessageDialog(this, "player1 won");
					System.out.println("player1 won");
					break;
				}
			} else {
				playerType = PLAYER2;
				depth = depth2;
				significantBoardLength = significantBoardLength2;
				if ( !gameEngine.checkValidMoves(PLAYER1) ) {
					JOptionPane.showMessageDialog(this, "player2 won");
					System.out.println("player2 won");
					break;
				}
			}
			node = gameEngine.AIMove(playerType, depth, significantBoardLength);
			computerMove(playerType, node);
			node = null;
//			System.gc();
			player1sTurn = !player1sTurn;
			
		}// for
	}
	public void playerMove(byte x, byte y){
		gameEngine.playerMove(x,y);
	}
	
	public void computerMove(byte playerType, Node node){

		Node bestMove = node.getBestMove();

		
		byte[] nextMove = bestMove.getNextMove();
		byte[] nextBlock = bestMove.getNextBlock();
		byte[][] player = new byte[2][];
		player[0] = node.getPlayer1Location();
		player[1] = node.getPlayer2Location();
		
		if ( debugMode ) {
			System.out.println("player" + playerType + " was on box " + player[playerType-1][0] + " " + player[playerType-1][1]);
			System.out.println("player" + playerType + " moved to " + bestMove.getNextMove()[0] + " " + bestMove.getNextMove()[1]);
			System.out.println("player" + playerType + "blocked " + bestMove.getNextBlock()[0] + " " + bestMove.getNextBlock()[1]);
			System.out.println("boardMatrix before move");
			printBoardMatrix();
		}
		
		updateBox( nextMove[0], nextMove[1], playerType );
		updateBox( player[playerType-1], EMPTY );

		gameEngine.moveComputerToLocation( playerType, nextMove[0], nextMove[1] );
		
		updateBox( nextBlock, BLOCKED );
		if ( debugMode ){
			System.out.println();
			System.out.println("boardMatrix after move");
		}
		if ( !gameEngine.checkValidMoves(PLAYER1) ) {
			System.out.println("PLAYER2 won!");
			JOptionPane.showMessageDialog(this, "PLAYER2 won!");
		}
			
	}
	
	public void updateBox(byte x, byte y, byte updateValue){

		buttonArray.get(x).get(y).setBoxStatus(updateValue);
		gameEngine.updateBoardMatrix(x, y, updateValue);
	}

	public void updateBox(byte[] location, byte updateValue){
		updateBox( location[0], location[1], updateValue);
	}
	public void printBoardMatrix(){
		gameEngine.printBoardMatrix();
	}

}
