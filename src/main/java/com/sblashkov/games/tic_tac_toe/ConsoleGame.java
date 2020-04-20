package com.sblashkov.games.tic_tac_toe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sblashkov.games.tic_tac_toe.model.BoardState;
import com.sblashkov.games.tic_tac_toe.model.CompLevel;

/**
 * Console implementation of game interface.
 *
 * @author Sergey Blashkov
 * @version 1.0 2015-06-12
 */
public class ConsoleGame {

	public static void main(String[] args) throws IOException {

        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Hello, User!");
		System.out.println("This is classic tic tac toe game with computer.");
		System.out.println("Please answer following questions to start.\n");

		System.out.print("What is your name? ");
		final String player2Name = br.readLine().trim();
		
		System.out.print("Would you play against computer? ");
		final String vsComp = br.readLine();
		final boolean isVsComp = "true".equalsIgnoreCase(vsComp) || "yes".equalsIgnoreCase(vsComp) || "1".equalsIgnoreCase(vsComp);

		System.out.print("Will you make first turn? ");
		final String youFirst = br.readLine();
		final boolean isYouFirst = "true".equalsIgnoreCase(youFirst) || "yes".equalsIgnoreCase(youFirst) || "1".equalsIgnoreCase(youFirst);

		CompLevel level = null;
		String player1Name = "Computer";
		if (isVsComp) {
			System.out.print("Enter the computer player difficulty (EASY, MEDIUM, HARD) ");
			final String lev = br.readLine().toUpperCase();
			try {
				level = CompLevel.valueOf(lev);
			} catch (IllegalArgumentException e) {
				System.out.println("You entered incorrect difficulty level, the default one will be used.");
			}
		} else {
			System.out.print("What is your opponent name? ");
			player1Name = br.readLine().trim();
		}

		final Game game = new Game(!isYouFirst, level, isVsComp, player1Name, player2Name);
		play(game);
    }

	public static void play(final Game game) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

        	System.out.println(game.getInitialMessage());
        	while (game.getBoardState() == BoardState.IN_PROGRESS) {

    			System.out.print(game.getCurPlayerName() + ", your turn! Please enter \"x, y\": ");
    			String[] userXY = br.readLine().split(",");

    			final int x = Integer.parseInt(userXY[0].trim()) - 1;
    			final int y = Integer.parseInt(userXY[1].trim()) - 1;

    			System.out.println(game.makeUserTurn(x, y));
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (game.getBoardState() == BoardState.DRAW) {
        	System.out.println("It is a draw, nobody wins.");
        } else if (game.getBoardState() == BoardState.PLAYER1_WIN) {
        	System.out.println("Congratulations to " + game.getPlayer1Name() + "! You WIN!!!");
        } else if (game.getBoardState() == BoardState.PLAYER2_WIN) {
        	System.out.println("Congratulations to " + game.getPlayer2Name() + "! You WIN!!!");
        } else {
        	System.out.println("Impossible!! Game's still running");
        }
	}
}
