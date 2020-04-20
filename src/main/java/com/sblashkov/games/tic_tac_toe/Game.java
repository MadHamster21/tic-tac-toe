package com.sblashkov.games.tic_tac_toe;

import com.sblashkov.games.tic_tac_toe.model.Board;
import com.sblashkov.games.tic_tac_toe.model.BoardState;
import com.sblashkov.games.tic_tac_toe.model.CompLevel;

/**
 * Game container to run tic tac toe.
 *
 * @author Sergey Blashkov
 * @version 1.0 2015-06-01
 */
public class Game {

	private final ComputerPlayer comp;
	private Board board = new Board();
	private boolean isPlayVsComp = true;
	private boolean player1Turn = true;

	private static final CompLevel DEFAULT_LEVEL = CompLevel.EASY;
	private String player1Name;
	private String player2Name;

	private String initialMessage = "";

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(final String player1NameInit) {
		player1Name = player1NameInit;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(final String player2NameInit) {
		player2Name = player2NameInit;
	}

	public String getInitialMessage() {
		return initialMessage;
	}

	public Game() {
		this(true, DEFAULT_LEVEL, true);
	}

	public Game(final boolean vsComp) {
		this(true, DEFAULT_LEVEL, vsComp);
	}

	public Game(final boolean compStart, final CompLevel compLevelVal, final boolean vsComp) {
		this(compStart, compLevelVal, vsComp, "Player1", "Player2");
	}

	public Game(
		final boolean isPlayer1First,
		final CompLevel compLevel,
		final boolean vsComp,
		final String playerOneName,
		final String playerTwoName
	) {

		player1Name = playerOneName != null ? playerOneName : "Player1";
		player2Name = playerTwoName != null ? playerTwoName : "Player2";

		CompLevel compLevelStart = DEFAULT_LEVEL;
		if (compLevel != null) {
			compLevelStart = compLevel;
		}
		player1Turn = isPlayer1First;

		initialMessage += "Starting game with following parameters\n";
		initialMessage += "\tFirst turn: " + getCurPlayerName() + "\n";
		initialMessage += "\tIs play vs comp: " + vsComp + "\n";
		if (vsComp) {
			initialMessage += "\tComputer level: " + compLevelStart + "\n";
		}

		isPlayVsComp = vsComp;
		if (isPlayVsComp) {
			comp = new ComputerPlayer(board, compLevelStart);
			player1Name = "Computer";

			if (isPlayer1First) {
				initialMessage += "\n" + makeCompTurn();
			}
		} else {
			comp = null;
		}
	}

	public Game(final boolean compStart, final CompLevel compLevel) {
		this(compStart, compLevel, true);
	}

	public BoardState getBoardState() {
		return board.getState();
	}

	public String makeUserTurn(final int x, final int y) {

		String res = "";
		if (board.checkState() == BoardState.IN_PROGRESS && !(isPlayVsComp && player1Turn)) {
			res += makeTurn(x, y);
		}

		if (board.checkState() == BoardState.IN_PROGRESS && isPlayVsComp && player1Turn) {
			res += makeCompTurn();
		}

		board.checkState();

		return res;
	}

	private String makeCompTurn() {

		final int[] compTurn = comp.getTurn();
		return makeTurn(compTurn[0], compTurn[1]);
	}

	private String makeTurn(final int x, final int y) {
	
		if (board.getSign(x, y) != null) {
			return "The cell " + (x + 1) + ", " + (y + 1) + " is already set to " + board.getSign(x, y) + ". Repeat your try";
		}

		board.setSign(x, y, getCurPlayerSign());
	
		final StringBuilder sb = new StringBuilder();
		sb.append(getCurPlayerName()).append(" set ");
		sb.append(getCurPlayerSign()).append(" to ");
		sb.append(x + 1).append(", ").append(y + 1).append(". ");
		sb.append(board.toString());

		player1Turn = !player1Turn;

		return sb.toString();
	}

	public String getNotCurPlayerName() {

		if (!player1Turn) {
			return player1Name;
		} else {
			return player2Name;
		}
	}

	public String getCurPlayerName() {

		if (player1Turn) {
			return player1Name;
		} else {
			return player2Name;
		}
	}

	public String getCurPlayerSign() {

		if (player1Turn) {
			return "X";
		} else {
			return "O";
		}
	}

	public String getSignAt(final int x, final int y) {
		return board.getSign(x, y);
	}

	public int[][] getWinRow() {
		return board.getWinRow();
	}
}
