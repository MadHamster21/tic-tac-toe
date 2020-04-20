package com.sblashkov.games.tic_tac_toe;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.sblashkov.games.tic_tac_toe.model.Board;
import com.sblashkov.games.tic_tac_toe.model.CompLevel;

/**
 * AI player logic for classic tic-tac-toe on board 3x3.
 *
 * @author Sergey Blashkov
 * @version 1.0 2015-06-09
 */
public class ComputerPlayer {

	private Random rnd = new Random();
	private Board board;
	private CompLevel level = CompLevel.MEDIUM;

	public ComputerPlayer() { }
	public ComputerPlayer(final Board initialBoard, final CompLevel startLevel) {
		board = initialBoard;
		level = startLevel;
	}

	public int[] getTurn() {

		final int[] turn;
		if (level == CompLevel.EASY) {
			turn = playEasy();
		} else if (level == CompLevel.MEDIUM) {
			if (rnd.nextBoolean()) {
				turn = playEasy();
			} else {
				turn = playHard();
			}
		} else if (level == CompLevel.HARD) {
			turn = playHard();
		} else {
			turn = new int[] { 0, 0 };
		}

		return turn;
	}

	private int[] playEasy() {
		return getRandomTurn();
	}

	private int[] playHard() {

		if (board.isEmpty() || board.getSign(1, 1) == null) {
			return new int[] { 1, 1 };
		} else if (board.getSign(0, 0) == null && board.getSign(2, 2) == null
				&& board.getSign(0, 2) == null && board.getSign(2, 0) == null) {
			return new int[] { rnd.nextInt(2) * 2, rnd.nextInt(2) * 2 };
		} else {

			int[] turn = getAttack("X");
			if (turn != null) {
				return turn;
			}

			turn = getAttack("O");
			if (turn != null) {
				return turn;
			}

			turn = getFork("X");
			if (turn != null) {
				return turn;
			}

			turn = getFork("O");
			if (turn != null) {
				return turn;
			}

			turn = getPotentialAttack();
			if (turn != null) {
				return turn;
			}

			turn = getRandomTurn();
			return turn;
		}
	}

	private int[] getRandomTurn() {

		final List<Integer[]> allTurns = board.getEmptyCells();
		Collections.shuffle(allTurns);
		return new int[] { allTurns.get(0)[0], allTurns.get(0)[1] };
	}

	private int[] getPotentialAttack() {

		final List<Integer[]> allTurns = board.getEmptyCells();
		
		for (Integer[] turn : allTurns) {
			final int x = turn[0];
			final int y = turn[1];
			String row = board.getSign(x, 0) + board.getSign(x, 1) + board.getSign(x, 2);
			row = row.replaceAll("null", "");
			String col = board.getSign(0, y) + board.getSign(1, y) + board.getSign(2, y);
			col = col.replaceAll("null", "");

			if (row.equals("X") || col.equals("X")) {
				return new int[] { x, y };
			}
		}

		return null;
	}

	private int[] getFork(final String sign) {

		final List<Integer[]> allTurns = board.getEmptyCells();

		for (Integer[] turn : allTurns) {
			final int x = turn[0];
			final int y = turn[1];
			String fork = board.getSign(x, y);
			for (int i = 0; i < 3; i++) {
				if (i != x) {
					fork += board.getSign(i, y);
				}
				if (i != y) {
					fork += board.getSign(x, i);
				}
			}
			fork = fork.replaceAll("null", "");
			if (fork.equals(sign + sign)) {
				return new int[] { x, y };
			}
		}

		return null;
	}

	private int[] getAttack(final String sign) {

		int[] turn = getHorizontalAttack(sign);
		if (turn != null) {
			return turn;
		}

		turn = getVerticalAttack(sign);
		if (turn != null) {
			return turn;
		}

		turn = getDiagonalAttack(sign);
		if (turn != null) {
			return turn;
		}

		turn = getBackDiagonalAttack(sign);
		if (turn != null) {
			return turn;
		}

		return null;
	}

	private int[] getHorizontalAttack(final String sign) {

		int[] turn = null;

		for (int x = 0; x < 3; x++) {
			String row = "";
			for (int y = 0; y < 3; y++) {
				row += board.getSign(x, y);
			}
			row = row.replaceAll("null", " ");
			if (row.equals(sign + sign + " ")) {
				turn = new int[] { x, 2 };
				break;
			} else if (row.equals(sign + " " + sign)) {
				turn = new int[] { x, 1 };
				break;
			} else if (row.equals(" " + sign + sign)) {
				turn = new int[] { x, 0 };
				break;
			}
		}
		return turn;
	}

	private int[] getVerticalAttack(final String sign) {

		int[] turn = null;

		for (int y = 0; y < 3; y++) {
			String col = "";
			for (int x = 0; x < 3; x++) {
				col += board.getSign(x, y);
			}
			col = col.replaceAll("null", " ");
			if (col.equals(sign + sign + " ")) {
				turn = new int[] { 2, y };
				break;
			} else if (col.equals(sign + " " + sign)) {
				turn = new int[] { 1, y };
				break;
			} else if (col.equals(" " + sign + sign)) {
				turn = new int[] { 0, y };
				break;
			}
		}
		return turn;
	}

	private int[] getDiagonalAttack(final String sign) {

		int[] turn = null;

		String diag = board.getSign(0, 0) + board.getSign(1, 1) + board.getSign(2, 2);
		diag = diag.replaceAll("null", " ");
		if (diag.equals(sign + sign + " ")) {
			turn = new int[] { 2, 2 };
		} else if (diag.equals(sign + " " + sign)) {
			turn = new int[] { 1, 1 };
		} else if (diag.equals(" " + sign + sign)) {
			turn = new int[] { 0, 0 };
		}
		return turn;
	}

	private int[] getBackDiagonalAttack(final String sign) {

		int[] turn = null;

		String diag = board.getSign(0, 2) + board.getSign(1, 1) + board.getSign(2, 0);
		diag = diag.replaceAll("null", " ");
		if (diag.equals(sign + sign + " ")) {
			turn = new int[] { 2, 0 };
		} else if (diag.equals(sign + " " + sign)) {
			turn = new int[] { 1, 1 };
		} else if (diag.equals(" " + sign + sign)) {
			turn = new int[] { 0, 2 };
		}
		return turn;
	}
}
