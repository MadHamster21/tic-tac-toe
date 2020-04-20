package com.sblashkov.games.tic_tac_toe;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sblashkov.games.tic_tac_toe.model.Board;
import com.sblashkov.games.tic_tac_toe.model.BoardState;

public class GameTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWinHorisontal() {

		final String[][] testBoard = new String[][] {
				{ "X", "X", "X" },
				{ null,"X", "O" },
				{ null,"O", "O" }
		};
		final Board board = new Board(testBoard);
		Assert.assertTrue("Player1 should win!", board.checkState() == BoardState.PLAYER1_WIN);
	}

	@Test
	public void testWinVertical() {

		final String[][] testBoard = new String[][] {
				{ null, "X", "O" },
				{ "X",  "X", "O" },
				{ null,null, "O" }
		};
		final Board board = new Board(testBoard);
		Assert.assertTrue("Player2 should win!", board.checkState() == BoardState.PLAYER2_WIN);
	}

	@Test
	public void testWinDiagonal() {

		final String[][] testBoard = new String[][] {
				{ "X", null, "O" },
				{ null, "X", "O" },
				{ null,null, "X" }
		};
		final Board board = new Board(testBoard);
		Assert.assertTrue("Player1 should win!", board.checkState() == BoardState.PLAYER1_WIN);
	}

	@Test
	public void testWinBackDiagonal() {

		final String[][] testBoard = new String[][] {
				{ "X", "X", "O" },
				{ null,"O",null },
				{ "O",null, "X" }
		};
		final Board board = new Board(testBoard);
		Assert.assertTrue("Player2 should win!", board.checkState() == BoardState.PLAYER2_WIN);
	}

	@Test
	public void testDraw() {

		final String[][] testBoard = new String[][] {
				{ "X", "X", "O" },
				{ "O", "O", "X" },
				{ "X", "X", "O" }
		};
		final Board board = new Board(testBoard);
		Assert.assertTrue("It should be DRAW!", board.checkState() == BoardState.DRAW);
	}

	@Test
	public void testInProgress() {

		final String[][] testBoard = new String[][] {
				{ "X", "X", "O" },
				{ null,"O", null },
				{ null,null,null }
		};
		final Board board = new Board(testBoard);
		Assert.assertTrue("Game should be IN PROGRESS!", board.checkState() == BoardState.IN_PROGRESS);
	}

}
