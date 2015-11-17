package javaGuiPackage;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class MainMenuView extends JFrame {
	/**
	 * this is the main menu gui
	 * it only runs for opening new game
	 */
	private static final long serialVersionUID = -1732263586940197838L;
	private boolean playerMovesFirst = true;
	private boolean aIVsAI = false;


	ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/images/background.jpg"));
	JLabel background = new JLabel(backgroundIcon);
	
	JLabel textLabel = new JLabel("Linuk Game");
	JPanel textPanel = new JPanel();
	JPanel radioPanel = new JPanel();
	JPanel controlPanel = new JPanel();
    
    //radio buttons
    
	JRadioButton computerMovesFirstRadioButton = new JRadioButton("Computer moves first");
	JRadioButton playerMovesFirstRadioButton = new JRadioButton ("Player moves first", true);
	JRadioButton aIVsAIRadioButton = new JRadioButton ("AI vs AI test bench");
	ButtonGroup radioButtonGroup = new ButtonGroup();

	//game menu buttons
	
    JButton newGameButton = new JButton("New Game");
    JButton helpButton = new JButton("Help");
    JButton exitButton = new JButton("Exit");
    String helpMessage = new String("This game is played on 7x7 board with computer \n" +
    								"Every player has two movements.\n" + 
    								"First one is to move player\n" + 
    								"Second one is to block the square\n" +
    								"If one of the players can not move, the game is over.");
	public MainMenuView() {		
		textLabel.setForeground(Color.red);
		textPanel.add(textLabel);
		textPanel.setLocation(88,100 );
		background.setSize(350, 350);
		radioPanel.setSize(150, 70);
		radioPanel.setLocation(110,100);
		radioPanel.setLayout(new GridLayout(0,1));
		radioPanel.setOpaque(true);
		radioPanel.setBackground(getBackground());
		controlPanel.setSize(150, 150);
		controlPanel.setLocation(110,200);
	    controlPanel.setLayout(new GridLayout(3,0));
		computerMovesFirstRadioButton.setActionCommand("computerMovesFirst");
		playerMovesFirstRadioButton.setActionCommand("playerMovesFirst");
		aIVsAIRadioButton.setActionCommand("aIVsAI");
		
		computerMovesFirstRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playerMovesFirst = false;
				aIVsAI = false;
			}
		});	
		playerMovesFirstRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				playerMovesFirst = true;
				aIVsAI = false;
			}
		});
		aIVsAIRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aIVsAI = true;
			}
		});
		radioButtonGroup.add(computerMovesFirstRadioButton);
		radioButtonGroup.add(playerMovesFirstRadioButton);
		radioButtonGroup.add(aIVsAIRadioButton);
		
	    newGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( aIVsAI )
					new GameView();
				else
					new GameView(playerMovesFirst);
			}
		});
	    helpButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(background, helpMessage);
			}
		});
	    exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		controlPanel.add(newGameButton);
		controlPanel.add(helpButton);
		controlPanel.add(exitButton);
		radioButtonGroup.add(computerMovesFirstRadioButton);
		radioButtonGroup.add(playerMovesFirstRadioButton);
		radioButtonGroup.add(aIVsAIRadioButton);
		radioPanel.add(computerMovesFirstRadioButton);
		radioPanel.add(playerMovesFirstRadioButton);
		radioPanel.add(aIVsAIRadioButton);
		setContentPane( background );
		add(controlPanel);
		add(radioPanel);
		add(textLabel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(400,500);
	}
	public static void main(String[] args){
		new MainMenuView();
	}

}
