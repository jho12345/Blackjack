package model;

import java.awt.Color;
import java.util.Random;

/* This class must extend Game */
public class ClearCellGame extends Game {
	private Random random;
	private int strategy, score;
	Color toFill;

	// constructor for ClearCellGame class
	public ClearCellGame(int maxRows, int maxCols, Random random, int strategy) {

		super(maxRows, maxCols);
		this.random = random;
		this.strategy = strategy;
		score = 0;

		// sets the grid as empty
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				board[row][col] = BoardCell.EMPTY;
			}
		}
	}

	// returns the score
	public int getScore() {
		return score;
	}

	// determines whether the game is over or not
	public boolean isGameOver() {
		boolean toReturn = false;

		for (int col = 0; col < board[0].length; col++) {
			if (!board[board.length - 1][col].equals(BoardCell.EMPTY)) {
				toReturn = true;
			}
		}
		return toReturn;
	}

	// will insert a row of random cells if the last board row corresponds to
	// empty
	public void nextAnimationStep() {
		if (!isGameOver()) {

			for (int row = board.length - 2; row >= 0; row--) {
				for (int col = 0; col < board[0].length; col++) {
					board[row + 1][col] = board[row][col];
				}
			}

			for (int col = 0; col < board[0].length; col++) {
				board[0][col] = BoardCell.getNonEmptyRandomBoardCell(random);
			}
		}
	}

	// turns selected cell empty and any adjacent to it of the same color
	public void processCell(int rowIndex, int colIndex) {

		if (!isGameOver()) {
			this.setColour(board[rowIndex][colIndex].getColor());
			board[rowIndex][colIndex] = BoardCell.EMPTY;
			score++;

			processHelper(rowIndex + 1, 1, colIndex, 0, toFill);
			processHelper(rowIndex - 1, -1, colIndex, 0, toFill);
			processHelper(rowIndex, 0, colIndex + 1, 1, toFill);
			processHelper(rowIndex, 0, colIndex - 1, -1, toFill);
			processHelper(rowIndex + 1, 1, colIndex + 1, 1, toFill);
			processHelper(rowIndex + 1, 1, colIndex - 1, -1, toFill);
			processHelper(rowIndex - 1, -1, colIndex + 1, 1, toFill);
			processHelper(rowIndex - 1, -1, colIndex - 1, -1, toFill);

			int startRow = 0;
			int endRow = 0;
			int rowsEmpty = 0;

			for (int row = 0; row < this.getMaxRows() - 1; row++) {
				if (this.rowEmptyHelper(row)) {
					startRow = row;
					rowsEmpty++;
				}
			}
			endRow = startRow + rowsEmpty;
			for (int row = startRow; row <= endRow; row++) {
				this.collapseHelper();
			}

		}
	}

	// helper method that helps clear the cells
	public void processHelper(int rowIndex, int rowChange, int colIndex,
			int colChange, Color colour) {

		if (rowIndex < 0 || rowIndex >= board.length || colIndex < 0
				|| colIndex >= board[0].length
				|| !board[rowIndex][colIndex].getColor().equals(colour))
			return;

		board[rowIndex][colIndex] = BoardCell.EMPTY;
		score++;
		processHelper(rowIndex + rowChange, rowChange, colIndex + colChange,
				colChange, colour);

	}

	// helper method that helps collapse the cells
	public void collapseHelper() {
		for (int row = 0; row < this.getMaxRows() - 1; row++) {
			if (this.rowEmptyHelper(row)) {
				for (int col = 0; col < this.getMaxCols(); col++) {
					this.setBoardCell(row, col, board[row + 1][col]);
					this.setBoardCell(row + 1, col, BoardCell.EMPTY);
				}
			}
		}
	}

	// helper method to help determine if a row is empty
	public boolean rowEmptyHelper(int rowNumber) {
		int cellsEmpty = 0;

		for (int col = 0; col < this.getMaxCols(); col++) {
			if (board[rowNumber][col] == BoardCell.EMPTY) {
				cellsEmpty++;
			}
		}
		if (cellsEmpty == this.getMaxCols()) {
			return true;
		} else {
			return false;
		}
	}

	// sets the color of a cell
	public void setColour(Color colour) {
		toFill = colour;
	}
}