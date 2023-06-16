package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	
	public static void main(String []args) {
		     
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Main Menu");
		
		JPanel panel = new JPanel();
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
		gbc.gridy = 0;
		GamePanel gamePanel = new GamePanel(panel, gbc, window);
		

		panel.add(gamePanel, gbc);
		window.add(panel);
		
		window.pack();

		window.setLocationRelativeTo(null);
		window.setVisible(true);
		gamePanel.setUpGame();
		gamePanel.startGameThread();

		
	}
	
}
