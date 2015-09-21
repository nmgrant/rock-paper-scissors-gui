import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;


public class Client extends JPanel implements ActionListener {
	private boolean moveChosen, difficultyChosen;
	private char choice, prediction;
	private boolean noWin, playerWin, compWin;
	private boolean done;
	private int difficulty;
	private int score, compScore, tie;
	private JButton rock, paper, scissors; 
	private JButton beginner, veteran;
	private JButton exit;
	
	public Client() {
		setBackground( Color.WHITE );
		rock = new JButton( new ImageIcon("R.jpg") );
		paper = new JButton( new ImageIcon("P.jpg") );
		scissors = new JButton( new ImageIcon("S.jpg") );
		beginner = new JButton("Beginner");
		veteran = new JButton("Veteran");
		exit = new JButton("Exit");
		rock.addActionListener(this);
		paper.addActionListener(this);
		scissors.addActionListener(this);
		beginner.addActionListener(this);
		veteran.addActionListener(this);
		exit.addActionListener(this);
		add(beginner);
		add(veteran);
		add(rock);
		add(paper);
		add(scissors);
		add(exit);
		JFrame frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize( 800, 800 );
		frame.setVisible( true );
		choice = ' ';
		prediction = ' ';
		score = compScore = tie = 0;
		moveChosen = difficultyChosen = false;
		done = false;
		difficulty = 1;
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		Socket sock;
		BufferedReader in = null;
		PrintStream out = null;
		try{
			sock = new Socket("localhost",1235);
			System.out.println("Connected");
			in = new BufferedReader(new InputStreamReader(
			sock.getInputStream()));
			out = new PrintStream(sock.getOutputStream());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		while (!client.difficultyChosen) {
			try {
				Thread.sleep( 50 );
			} catch (InterruptedException e) {
				System.out.println("Program interrupted.");
			}
		}
		out.print(client.difficulty);
		out.flush();
		while (!client.done) {
			client.repaint();
			while (!client.moveChosen) {
				try {
					Thread.sleep( 50 );
				} catch (InterruptedException e) {
					System.out.println("Program interrupted.");
				}
			}
			out.println(client.choice);
			out.flush();
			String move = "";
			try {
				move = in.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			client.prediction = move.charAt(0);
			client.noWin = client.playerWin = client.compWin = false;
			if ( client.choice == client.prediction ) {
				client.tie++;
				client.noWin = true;
			}
			else if ( ( client.choice == 'R' && client.prediction == 'P' ) ||
					 ( client.choice == 'P' && client.prediction == 'S' ) ||
					 ( client.choice == 'S' && client.prediction == 'R' )) {
				client.compScore++;
				client.compWin = true;
			}
			else {
				client.score++;
				client.playerWin = true;
			}
			client.moveChosen = false;
		}
	}
	
	public char getChoice() {
		return choice;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!difficultyChosen) {
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 26));
			g.drawString("Choose a difficulty", 300, 250);
		}
		else {
			g.setColor( Color.BLACK );
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 18));
			g.drawString("You chose:", 340, 300);
			if ( choice != ' ' ) {
				try {
					BufferedImage img = ImageIO.read(new File( choice + ".jpg"));
					g.drawImage( img, 330, 310, this);
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		
			g.drawString("The computer chose:", 300, 500);
			if ( prediction != ' ' ) {
				try {
					BufferedImage img = ImageIO.read(new File( prediction + ".jpg"));
					g.drawImage( img, 340, 510, this);
				} catch (IOException e) {
					System.out.println(e);
				}
			}
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 26));
			if ( noWin ) {
				g.drawString("You tied!", 340, 250);
				noWin = false;
			}
			if ( playerWin ) {
				g.drawString("You won!", 340, 250);
				playerWin = false;
			}
			if ( compWin ){
				g.drawString("Computer wins!", 340, 250);
				compWin = false;
			}
			g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 18));
			g.drawString("Your score: " + score, 50, 650);
			g.drawString("Computer score: " + compScore, 50, 700);
			g.drawString("Tied: " + tie, 50, 750);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == beginner || a.getSource() == veteran) {
			difficultyChosen = true;
			if (a.getSource() == beginner) {
				difficulty = 1;
			}
			else {
				difficulty = 2;
			}
			remove(beginner);
			remove(veteran);
		}
		else if (a.getSource() == exit) {
			System.exit(0);
		}
		else {
			moveChosen = true;
			if (a.getSource() == rock) {
				choice = 'R';
			}
			else if (a.getSource() == paper) {
				choice = 'P';
			}
			else if (a.getSource() == scissors) {
				choice = 'S';
			}
		}
	}
}
