package javaGuiPackage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonBox extends JButton{
	//button box has been extended in order to keep position of each box
	ImageIcon emptyBox = new ImageIcon(getClass().getResource("/images/emptyBox.png"));
	ImageIcon blockedBox = new ImageIcon(getClass().getResource("/images/blockedBox.png"));
	ImageIcon player1Box = new ImageIcon(getClass().getResource("/images/player1icon.png"));
	ImageIcon player2Box = new ImageIcon(getClass().getResource("/images/player2icon.png"));
	
	private static final long serialVersionUID = -4071528670464971328L;
	@SuppressWarnings("unused")
	private byte xCoordinate, yCoordinate; // coordinates of the button
	public static final byte EMPTY = 0, PLAYER1 = 1, PLAYER2 = 2, BLOCKED = 3;
	private byte boxStatus;
	GameView gameView;
	
	public ButtonBox(byte xCoordinate, byte yCoordinate, GameView gameView){

		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		boxStatus = EMPTY;
		setIcon(emptyBox);
		this.gameView = gameView;
		
	this.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			// test line starts here
			
			System.out.println(xCoordinate + " " + yCoordinate);

			//System.out.println("click detected on X: " + xCoordinate + " Y: " + yCoordinate);
			gameView.playerMove(xCoordinate, yCoordinate);
			//gameView.printBoardMatrix();
			// test line ends here
			
			}
		});
	}

	public void setBoxStatus(byte value){
		boxStatus = value;
		if ( boxStatus == EMPTY )
			setIcon( emptyBox );
		else if ( boxStatus == PLAYER1 )
			setIcon( player1Box );
		else if ( boxStatus == PLAYER2 )
			setIcon( player2Box );
		else if ( boxStatus == BLOCKED )
			setIcon ( blockedBox );
		else 
			JOptionPane.showMessageDialog(null, "there is a problem in buttonbox");
	}


}
