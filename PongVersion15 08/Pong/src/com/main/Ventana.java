package com.main;

import javax.swing.JFrame;


public class Ventana {

	
	public Ventana(String title, Juego game) {
		JFrame frame = new JFrame(title);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game); 
		frame.pack();
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true);
	}

}
