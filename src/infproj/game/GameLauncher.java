package infproj.game;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class GameLauncher 
{
	private static Game game = new Game();
	
	public static void main(String[] args)
	{
		game.setMinimumSize(new Dimension(game.DIMENSIONS));
		game.setMaximumSize(new Dimension(game.DIMENSIONS));
		game.setPreferredSize(new Dimension(game.DIMENSIONS));
		
		game.frame = new JFrame(game.NAME);
		
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLayout(new BorderLayout());
		
		game.frame.setAlwaysOnTop(true); 
		
		game.frame.add(game,BorderLayout.CENTER);
		game.frame.pack();
		
		game.frame.setResizable(false);
		game.frame.setLocationRelativeTo(null);
		
		game.frame.setVisible(true);
		game.frame.setTitle("InfoProj Version 100319");
		game.start();
	}

}
