package com.sblashkov.games.tic_tac_toe;

/**
 * GUI class for tic-tac-toe game.
 *
 * @author Sergey Blashkov
 * @version 1.0 2015-06-09
 */
public class GameGUI {

	public static void main(String[] args) {

		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final GameForm form = new GameForm();
				form.createAndShowGUI();
			}
		});
	}

}
