package com.sblashkov.games.tic_tac_toe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tic tac toe game field.
 *
 * @author Sergey Blashkov
 * @version 1.0 2015-06-01
 */
public class Board {

	private boolean empty = true;
	private String[][] field = new String[3][3];
	private int[][] winningRow = null;
	private BoardState state = BoardState.IN_PROGRESS;

	public Board() { }
	public Board(final String[][] fill) {
		field = fill;
	}

	public BoardState getState() {
		return state;
	}

	public String getSign(final int x, final int y) {
		return field[x][y];
	}

	public boolean setSign(final int x, final int y, String sign) {

		final boolean result = getSign(x, y) == null;

		field[x][y] = sign;
		empty = false;

		return result;
	}

	public boolean isEmpty() {

		return empty;
	}

	public List<Integer[]> getEmptyCells() {

		final List<Integer[]> emptyCells = new ArrayList<Integer[]>();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (field[x][y] == null) {
					emptyCells.add(new Integer[] { x, y });
				}
			}
		}

		return emptyCells;
	}

	public BoardState checkState() {

		if (state == BoardState.IN_PROGRESS) {
			state = checkHorVer();
		}

		if (state == BoardState.IN_PROGRESS) {
			state = checkDiagonal();
		}

		if (state == BoardState.IN_PROGRESS) {
			state = checkBackDiagonal();
		}

		if (state == BoardState.IN_PROGRESS) {
			state = checkForDraw();
		}

		return state;
	}

	private BoardState checkHorVer() {

		BoardState result = BoardState.IN_PROGRESS;

		for (int i = 0; i < 3; i++) {

			BoardState horResult = checkLine(new int[][] { {i, 0}, {i, 1}, {i, 2} });
			if (horResult != BoardState.IN_PROGRESS) {
				result = horResult;
				break;
			}

			BoardState verResult = checkLine(new int[][] { {0, i}, {1, i}, {2, i} });
			if (verResult != BoardState.IN_PROGRESS) {
				result = verResult;
				break;
			}
		}

		return result;
	}

	private BoardState checkDiagonal() {
		return checkLine(new int[][] { {0, 0}, {1, 1}, {2, 2} });
	}

	private BoardState checkBackDiagonal() {
		return checkLine(new int[][] { {2, 0}, {1, 1}, {0, 2} });
	}

	private BoardState checkLine(final int[][] lineCells) {

		final String line = field[lineCells[0][0]][lineCells[0][1]]
						  + field[lineCells[1][0]][lineCells[1][1]]
						  + field[lineCells[2][0]][lineCells[2][1]];
		BoardState result = BoardState.IN_PROGRESS;

		final String cutLine = line.replaceAll("null", "");

		if (cutLine.length() == 3 && cutLine.replaceAll(cutLine.substring(2), "").length() == 0) {
			winningRow = lineCells;
			if (cutLine.substring(2).equals("X")) {
				result = BoardState.PLAYER1_WIN;
			} else {
				result = BoardState.PLAYER2_WIN;
			}
		}

		return result;
	}

	private BoardState checkForDraw() {
		return getEmptyCells().isEmpty() ? BoardState.DRAW : BoardState.IN_PROGRESS;
	}

	public int[][] getWinRow() {
		return winningRow;
	}

	@Override
	public String toString() {

		final StringBuilder sb = new StringBuilder("Game state:\n  1 2 3\n");
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (x > 0) {
					sb.append("│");
				} else {
					sb.append(y + 1).append(" ");
				}
				sb.append(field[x][y] != null ? field[x][y] : " ");
			}
			sb.append("\n");
			if (y < 2) {
				sb.append("  ");
				for (int x = 0; x < 3; x++) {
					if (x < 2) {
						sb.append("─").append("┼");
					} else {
						sb.append("─");
					}
				}	
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
