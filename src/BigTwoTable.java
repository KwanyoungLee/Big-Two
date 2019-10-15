import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * The BigTwoTable class implements the CardGameTable interface.
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * 
 * @author Kwanyoung lee
 */

public class BigTwoTable implements CardGameTable{
	//public constructor
	/**
	 * Creates and returns an instance of the BigTwoTable class.
	 * 
	 * 
	 * @param game
	 * 			a the card game associates with this table
	 */
	public BigTwoTable(CardGame game) {
		this.game = (BigTwoClient) game;
		numOfPlayers = game.getNumOfPlayers();
		playerList = game.getPlayerList();
		handsOnTable = game.getHandsOnTable();
		selected = new boolean[MAX_CARD_NUM];
		
		//load card images
		cardImages = new Image[4][13];
		char[] suits = {'d', 'c', 'h', 's'};
		char[] ranks = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q','k'};
		for (int i=0; i<4; i++) {
			for (int j=0; j<13; j++) {
				cardImages[i][j] = new ImageIcon("image/" + ranks[j] + suits[i] + ".gif").getImage();
			}
		}
		cardBackImage = new ImageIcon("image/b.gif").getImage();
		
		//load player images
		avatars = new Image[4];
		for (int i=0; i<4; i++) {
			avatars[i] = new ImageIcon("image/p" + i + ".png").getImage();
		}
		
		//initialize the width and the height of images
		cardWidth = cardImages[0][0].getWidth(null);
		cardHeight = cardImages[0][0].getHeight(null);
		deltaCardWidth = (int) Math.round(cardWidth/5.0);
		deltaCardHeight = (int) Math.round(cardHeight/5.0);
		iconHeight = cardHeight;
		iconWidth = (int) Math.round(iconHeight*0.8);
		
		//create window
		frame = new JFrame("Big Two");
		
		bigTwoPanel = new BigTwoPanel();
		
		playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(80,30));
		playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton("Pass");
		passButton.setPreferredSize(new Dimension(80,30));
		passButton.addActionListener(new PassButtonListener());
		JPanel buttonP = new JPanel();
		buttonP.add(playButton);
		buttonP.add(passButton);
		
		//left panel
		JPanel leftP = new JPanel(new BorderLayout());
		leftP.add(bigTwoPanel, BorderLayout.CENTER);
		leftP.add(buttonP, BorderLayout.SOUTH);
		frame.add(leftP, BorderLayout.CENTER);
		
		//msgArea
		msgArea = new JTextArea(23,40);
		msgArea.setEditable(false);
		msgArea.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//chatArea
		incoming = new JTextArea(23, 40);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		outgoing = new JTextField(30);
		outgoing.addActionListener(new EnterListener());
		JLabel messageLabel = new JLabel("Message: ");
		
		//right panel
		JPanel rightP = new JPanel(new BorderLayout());
		Border border = BorderFactory.createEmptyBorder(2,3,2,3);
		rightP.setBorder(border);
		rightP.add(scroller, BorderLayout.PAGE_START);
		rightP.add(qScroller, BorderLayout.AFTER_LINE_ENDS);
		JPanel sendP = new JPanel();
		sendP.add(messageLabel);
		sendP.add(outgoing);
		rightP.add(sendP, BorderLayout.SOUTH);
		frame.add(rightP, BorderLayout.EAST);
		
		
		//menu
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu menu1 = new JMenu("Game");
		menuBar.add(menu1);
		JMenuItem menuitem1;
		menuitem1 = new JMenuItem("Connect");
		menuitem1.addActionListener(new ConnectMenuItemListener());
		menu1.add(menuitem1);
		JMenuItem menuitem2;
		menuitem2 = new JMenuItem("Quit");
		menuitem2.addActionListener(new QuitMenuItemListener());
		menu1.add(menuitem2);
		
		JMenu menu2 = new JMenu("Message");
		menuBar.add(menu2);
		//JMenuItem menuitem3;
		//menuitem3 = new JMenuItem("What is Big Two");
		//menuitem3.addActionListener(new WhatMenuItemListener());
		//JMenuItem menuitem4;
		//menuitem4 = new JMenuItem("How to play");
		//menuitem4.addActionListener(new HowMenuItemListener());
		//menu2.add(menuitem3);
		//menu2.add(menuitem4);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	//private instance variables
	private final static int MAX_CARD_NUM = 13;
	private final static int BORDER = 10;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int numOfPlayers;
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea incoming;
	private JTextField outgoing;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	private int cardWidth;
	private int cardHeight;
	private int iconWidth;
	private int iconHeight;
	private int deltaCardWidth;
	private int deltaCardHeight;
	
	//CardGameTable interface methods
	/**
	 * Set the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 * 			The index of the active player
	 *            
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= playerList.size())
			this.activePlayer = -1;
		else
			this.activePlayer = activePlayer;
	}
	
	
	/**
	 * Retrieves an array of indices of the cards selected.
	 * 
	 * @return  an array of indices of the cards selected
	 *            
	 */
	public int[] getSelected() {
		int count=0;
		for (int i=0; i< selected.length; i++) {
			if (selected[i]) {
				count ++;
			}
		}
		
		int[] cardIdx = new int[count];
		count = 0;
		for (int i=0; i< selected.length; i++) {
			if (selected[i]) {
				cardIdx[count] = i;
				count ++;
			}
		}
		return cardIdx;
	}

	
	/**
	 * Reset the list of selected cards
	 *            
	 */
	public void resetSelected() {
		selected = new boolean[MAX_CARD_NUM];
	}
	
	
	/**
	 * Repaint the GUI
	 *            
	 */
	public void repaint() {
		frame.repaint();
	}

	
	/**
	 * Print the specified string to the message area of the GUI
	 * 
	 * @param msg
	 * 			the message to be printed
	 *            
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
	}
	
	
	/**
	 * Clear the message area of the GUI
	 *            
	 */
	public void clearMsgArea() {
		msgArea.setText(null);;
	}
	
	
	/**
	 * Reset the GUI by resetting the list of selected cards, clearing the message area and enabling user interactions.
	 *            
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	
	/**
	 * Enable user interactions with the GUI by enabling the "Play" button, "Pass" button and BigTwoPanel for selection of cards through mouse clicks.
	 *            
	 */
	public void enable() {
		bigTwoPanel.setEnabled(true);
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	
	
	/**
	 * Disables user interactions with the GUI by disabling the "Play" button, "Pass" button and BigTwoPanel for selection of cards through mouse clicks.
	 *            
	 */
	public void disable() {
		bigTwoPanel.setEnabled(false);
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	
	public void printChat(String msg) {
		incoming.append(msg + "\n");
		incoming.setCaretPosition(incoming.getDocument().getLength());
	}
	
	public void endMessage(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
	
	//inner classes
	private class BigTwoPanel extends JPanel implements MouseListener{
		
		public BigTwoPanel() {
			int widthP = BORDER*3 + iconWidth + cardWidth + deltaCardWidth * 24;
			int heightP = (iconHeight  + iconHeight + deltaCardHeight + BORDER) * (numOfPlayers + 1);
			setPreferredSize(new Dimension(widthP, heightP));
			setBackground(new Color(11, 132, 70));
			addMouseListener(this);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int x=0;
			int y=0;
			
			for (int i=0; i<playerList.size(); i++) {
				CardGamePlayer player = playerList.get(i);
				x = BORDER;
				y = y + BORDER + deltaCardHeight;
				
				if (i==activePlayer) {
					g.setColor(Color.BLUE);
				}

				g.drawString(player.getName(), x, y - BORDER);
				g.drawImage(avatars[i%5], x, y, iconWidth, iconHeight, null);
				
				x = x + iconWidth + BORDER;
				
				for(int j=0; j<player.getCardsInHand().size(); j++) {
					Image cardImage;
					int y_ = y;
					if(i==activePlayer || game.endOfGame()) {
						Card card = player.getCardsInHand().getCard(j);
						cardImage = cardImages[card.getSuit()][card.getRank()];
					}else {
						cardImage = cardBackImage;
					}
					
					if(i==activePlayer && selected[j]) {
						g.drawImage(cardImage, x, y_-deltaCardHeight, cardWidth, cardHeight, null);
					}
					else{
						g.drawImage(cardImage, x, y_, cardWidth, cardHeight, null);
					}
					x = x + deltaCardWidth;
				}
				y = y + cardHeight + BORDER;
				g.setColor(Color.BLACK);
				g.drawLine(0, y, 1000, y);
			}
			
			x = BORDER;
			y = y + BORDER + deltaCardHeight;
			if (!handsOnTable.isEmpty()) {
				g.drawString("Played by " + handsOnTable.get(handsOnTable.size()-1).getPlayer().getName(), x, y - BORDER);
				for (int i=0; i<handsOnTable.get(handsOnTable.size()-1).size(); i++) {
					Card card = handsOnTable.get(handsOnTable.size()-1).getCard(i);
					Image cardImage = cardImages[card.getSuit()][card.getRank()];
					g.drawImage(cardImage, x, y, cardWidth, cardHeight, null);
					x = x + deltaCardWidth;
				}
			}
			
		}
		
		public void mouseClicked(MouseEvent e) {
			int i = activePlayer;
			if (i >= 0 && i < numOfPlayers) {
				int numOfCards = playerList.get(i).getNumOfCards();
				int mx = e.getX();
				int my = e.getY();
				int x = iconWidth + BORDER*2 + deltaCardWidth*(numOfCards - 1);
				int y = (cardHeight + deltaCardHeight + BORDER * 2) * i + BORDER + deltaCardHeight;
				
				for (int j=numOfCards - 1; j>=0; j--) {
					if (mx>=x && mx<(x+cardWidth)) {
						if(my>=y && my<(y+cardHeight) && !selected[j]) {
							selected[j] = true;
							frame.repaint();
							break;
						}
						else if (my>=(y-deltaCardHeight) && my<(y+cardHeight-deltaCardHeight) && selected[j]) {
							selected[j] = false;
							frame.repaint();
							break;
						}
					}
					x = x - deltaCardWidth;
				}
			}
		}
		
		public void mouseEntered(MouseEvent e) {
		}
		
		public void mouseExited(MouseEvent e) {
		}
		
		public void mousePressed(MouseEvent e) {
		}
		
		public void mouseReleased(MouseEvent e) {
		}
		
	}
	
	private class PlayButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int[] cardIdx = getSelected();
			resetSelected();
			if (cardIdx != null)
				game.makeMove(activePlayer, cardIdx);
		}
	} 
	
	private class PassButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			resetSelected();
			int[] cardIdx = null;
			game.makeMove(activePlayer, cardIdx);
		}
	}
	
	private class ConnectMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//reset();
			//BigTwoDeck deck = new BigTwoDeck();
			//deck.shuffle();
			//game.start(deck);
			game.makeConnection();
		}
	}
	
	private class QuitMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			System.exit(0);
		}
	}
	
	/*private class WhatMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(frame, "Big two is a card game of Chinese origin, which is similar to the games of president, crazy eights, cheat, winner, and other shedding games.  \n");
		}
	}

	private class HowMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(frame, "Please refer to [https://www.pagat.com/climbing/bigtwo.html] for a detailed description of the Big Two card game. \n");
		}
	}*/
	
	
	public class EnterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
		}
	} 
	
	/*public class IncomingReader implements Runnable {

	}*/
}