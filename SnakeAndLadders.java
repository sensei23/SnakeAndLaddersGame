package cebs_training_gui_programs;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.JFrame;

public class SnakeAndLadders extends JFrame implements ActionListener{
	private JPanel board, console;
	private JButton restart, dice;
	private JLabel player1, player2, displayNo, displayStatus;
	private static String value = "Value On Dice : ";
	
	private JLabel[] grids;
	
	private static boolean flag = false;
	
	private boolean winFlag;
	
	private int player;
	private int player1Moves[];
	private int player2Moves[];
	
	private static int limits[][] = {{90, 99}, {89, 80}, {70, 79}, {69, 60}, {50, 59}, {49, 40}, {30, 39}, {29, 20}, {10, 19}, {9, 0}};
	
	private static String onlyYellow = "yellow-move.jpg";
	private static String onlyRed = "red-move.jpg";
	private static String both = "both-move.jpg";
	
	private static HashMap<Integer, Integer> ladders;
	private static HashMap<Integer, Integer> snakes;
	static {
		ladders = new HashMap<>();
		ladders.put(92, 80);ladders.put(95, 86);ladders.put(71, 63);ladders.put(62, 41);ladders.put(30, 22);ladders.put(17, 9);ladders.put(27, 15);ladders.put(10, 2);
		ladders.put(83, 26);ladders.put(58, 36);ladders.put(85, 66);ladders.put(89, 77);ladders.put(43, 24);
		
		snakes = new HashMap<>();
		snakes.put(97, 93);snakes.put(75, 99);snakes.put(40, 72);snakes.put(46, 64);snakes.put(25, 77);snakes.put(12, 54);snakes.put(1, 32);
		snakes.put(82, 90);snakes.put(61, 94);snakes.put(44, 90);snakes.put(49, 95);snakes.put(19, 57);snakes.put(14, 41);snakes.put(3, 16);snakes.put(8, 74);
	}
	
	public SnakeAndLadders() {		
		board = new JPanel();
		console = new JPanel();
		player1 = new JLabel("Now Player 1's Turn");
		player2 = new JLabel("Now Player 2's Turn");
		displayNo = new JLabel();
		restart = new JButton("Start");
		displayStatus = new JLabel();
		
		console.add(displayNo);
		
		//Dice Button with Image
		try {
			BufferedImage i = ImageIO.read(getClass().getResource("images/dice.png"));
			dice = new JButton(new ImageIcon(i));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dice.setBorder(BorderFactory.createEmptyBorder());
		dice.setContentAreaFilled(false);
		dice.addActionListener(this);
		dice.setCursor(new Cursor(Cursor.HAND_CURSOR));
		console.add(dice);
		
		
		grids = new JLabel[100];
		for(int i=0; i<100; i++) {
			grids[i] = new JLabel();
			board.add(grids[i]);
		}
		
		player1.setVisible(false);
		player2.setVisible(false);
		
		//Start Button
		restart.addActionListener(this);
		restart.setCursor(new Cursor(Cursor.HAND_CURSOR));
		console.add(restart);
		
		console.add(player1);
		console.add(player2);
		
		console.add(displayStatus);
		
		resetBoard();
		
		board.setLayout(new GridLayout(10, 10));
		
		console.setLayout(new GridLayout(5, 1, 1, 1));
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 50, 1));
		add(board);
		add(console);
		setSize(1000, 700);
		setLocation(100, 1);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void resetBoard() {
		//setting grids to put images		
		winFlag = false;
		
		int x = 65;
							
		for(int i=0; i<100; i++) {
			grids[i].setBorder(null);
			grids[i].setPreferredSize(new Dimension(x, x));
			BufferedImage img = null;
			try {
			    img = ImageIO.read(getClass().getResource("images/"+(arr[i])+".jpg"));
			} catch (IOException e) {
			    e.printStackTrace();
			    break;
			}
			
			Image dimg = img.getScaledInstance(x, x, Image.SCALE_SMOOTH);
			grids[i].setIcon(new ImageIcon(dimg));
		}
		
		dice.setEnabled(false);
		displayNo.setText(" ");
		
		player = 1;
		int temp[] = {90, 90, 99};
		player1Moves = temp;
		int temp2[] = {90, 90, 99};
		player2Moves = temp2;
	}
	
	private void rollDice() {
		int diceResult = new Random().nextInt(6) + 1;
		setRollValue(diceResult);
		playForward(diceResult);
	}
	
	private void setRollValue(int diceResult) {
		displayNo.setText(value+diceResult);
	}

	//straight traverse Left -> Right
	//reverse traverse Right -> Left
	private static int[] newLimit(int oldLimit) {
		int i;
		for(i=0; i<limits.length; i++) {
			if(limits[i][0]/10 == oldLimit/10) {
				break;
			}
		}
		i++;
		
		return limits[i];
	}
	
	private static int[] sameLimit(int newVal) {
		int i;
		for(i=0; i<limits.length; i++) {
			if(limits[i][0]/10 == newVal/10) {
				break;
			}
		}		
		return limits[i];
	}
	
	private void valueUpdation(int playerMoves[], int diceResult) {
	    System.out.println(player + " moves "+ diceResult +" :  " + Arrays.toString(playerMoves));
		if((playerMoves[1]/10)%2 == 0) {
			if(playerMoves[2] > (playerMoves[0] - diceResult)) {
				int a = playerMoves[2] - (playerMoves[0] - diceResult);
				int temp[] = newLimit(playerMoves[1]);
				playerMoves[1] = temp[0];
				playerMoves[2] = temp[1];
				playerMoves[0] = playerMoves[1] + a - 1;
				System.out.println("1st");
			}
			else {
				System.out.println("2nd");
				playerMoves[0] -= diceResult;
			}
		}
		else {
			if(playerMoves[2] < (diceResult + playerMoves[0])) {
				playerMoves[0] = playerMoves[1] - (playerMoves[0] + diceResult - playerMoves[2]);
				int temp[] = newLimit(playerMoves[1]);
				playerMoves[1] = temp[0];
				playerMoves[2] = temp[1];
				System.out.println("3rd");
			}
			else {
				playerMoves[0] += diceResult;
				System.out.println("4th");
			}
		}
		
	    System.out.println(player + " moves "+ diceResult +" :  " + Arrays.toString(playerMoves));
	}
	
	private void moveForward(int then) {
		if(player == 1) {
			if(player2Moves[0] == player1Moves[0]) {
				changeImageTo(then, arr[then]+".jpg");
				changeImageTo(player1Moves[0], both);
			}
			else if(player2Moves[0] == then) {
				changeImageTo(then, onlyYellow);
				changeImageTo(player1Moves[0], onlyRed);
			}
			else {
				changeImageTo(then, arr[then]+".jpg");
				changeImageTo(player1Moves[0], onlyRed);
			}
		}
		else {
			if(player2Moves[0] == player1Moves[0]) {
				changeImageTo(then, arr[then]+".jpg");
				changeImageTo(player2Moves[0], both);
			}
			else if(player1Moves[0] == then) {
				changeImageTo(then, onlyRed);
				changeImageTo(player2Moves[0], onlyYellow);
			}
			else {
				changeImageTo(then, arr[then]+".jpg");
				changeImageTo(player2Moves[0], onlyYellow);
			}
		}
	}
	
	private void playForward(int diceResult) {
	    int then;
	    if(!checkIfWin(diceResult)) {
			if(player == 1) {
				then = player1Moves[0];
				valueUpdation(player1Moves, diceResult);
				moveForward(then);
			}
			else {
				then = player2Moves[0];
				valueUpdation(player2Moves, diceResult);
				moveForward(then);
			}
			
			if(isStepSnakeOrLadder()) {
				sleep();
				if(player == 1)
					then = updatingValueForSnakeOrLadder(player1Moves);
				else
					then = updatingValueForSnakeOrLadder(player2Moves);
				
				moveForward(then);
			}
		}
	    if(winFlag) {
	    	somebodyWon();
	    	return;
	    }
		player ^= 1;
		showWhichPlayer();
	}
	
	private void somebodyWon() {
		displayStatus.setText("Won By Player " + (player==1?1:2));
		dice.setEnabled(false);
	}
	
	private boolean checkIfWin(int dice) {
		if(player == 1) {
			if(player1Moves[0]-6 >0)
				return false;
			if(player1Moves[0] - dice - 6 > 0) 
				return false;
			if(player1Moves[0] - dice != 0) {
				return true;
			}
			else {
				winFlag = true;
			}
			
		}
		else {
			if(player2Moves[0] - 6 > 0)
				return false;
			if(player2Moves[0] - dice - 6 > 0) 
				return false;
			if(player2Moves[0] - dice != 0) {
				return true;
			}
			else {
				winFlag = true;
			}	
		}
		return false;
	}

	private boolean isStepSnakeOrLadder() {
		if(player == 1) 
			return snakes.containsKey(player1Moves[0]) || ladders.containsKey(player1Moves[0]);
				
		return snakes.containsKey(player2Moves[0]) || ladders.containsKey(player2Moves[0]);
	}

	private int updatingValueForSnakeOrLadder(int[] playerMoves) {
		int then = playerMoves[0];
		int newVal;
		if(ladders.containsKey(then)) {
			newVal = ladders.get(then);						
		}
		else {
			newVal = snakes.get(then);
		}
		playerMoves[0] = newVal;
		int temp[] = sameLimit(newVal);
		playerMoves[1] = temp[0];
		playerMoves[2] = temp[1];		
		return then;
	}

	private static void sleep() {
		try {
			TimeUnit.SECONDS.sleep(1);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	private void changeImageTo(int now, String imgName) {
		int x=65;
		BufferedImage img = null;
		try {
		    img = ImageIO.read(getClass().getResource("images/"+imgName));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Image dimg = img.getScaledInstance(x, x, Image.SCALE_SMOOTH);
		grids[now].setIcon(new ImageIcon(dimg));
		showWhichPlayer();
	}

	public static void main(String...s) {
		new SnakeAndLadders();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == dice) {
			rollDice();
		}
		else if(ae.getSource() == restart) {
			startGame();
		}
	}

	private void startGame() {
		if(!flag) {
			restart.setText("Reset");
			int x=65;
			BufferedImage img = null;
			try {
			    img = ImageIO.read(getClass().getResource("images/both-move.jpg"));
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			Image dimg = img.getScaledInstance(x, x, Image.SCALE_SMOOTH);
			grids[90].setIcon(new ImageIcon(dimg));
			showWhichPlayer();
			dice.setEnabled(true);
		}
		else {
			resetBoard();
			restart.setText("Start");
			player1.setVisible(false);
			player2.setVisible(false);
		}
		
		flag = !flag;
	}

	private void showWhichPlayer() {
		if(player == 1) {
			player2.setVisible(false);
			player1.setVisible(true);
		}
		else {
			player1.setVisible(false);
			player2.setVisible(true);
		}
	}
	
	private static final int arr[] = {100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 80, 79, 78, 77, 76, 75, 74, 73, 72, 71, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 60, 59, 58, 57, 56, 55, 54, 53, 52, 51, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

}
