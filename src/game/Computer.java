package game;

import java.util.ArrayList;
import java.util.Random;

public class Computer {
	Gameboard gameBoard;
	int maxDepth = 3;
	int bestChoice;
	int bestValue = Integer.MIN_VALUE;

	public Computer(Gameboard gb) {
		this.gameBoard = new Gameboard(gb);
	}

	public int computerOption() {
		Random rand = new Random();
		int value = rand.nextInt(6) + 1;
		return value;
	}

	public int calcValue() {
		// check to see if computer has gone yet
		int count = 0;
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (gameBoard.spaces.get(row).get(col).getColor() == (gameBoard.playerColor)) {
					count++;
				}
			}
		}
		// middle of the board is the best so choose 3 if possible, otherwise
		// choose 2
		if (count <= 1) {
			if (gameBoard.spaces.get(0).get(3).getColor() == gameBoard.playerColor) {
				return 2;
			} else {
				return 3;
			}
		}

		// If not first turn return the value using minimax algorithm
		return minimax(gameBoard, 0, gameBoard.computerColor, 3);
	}

	private int minimax(Gameboard gameBoard, int depth, char color, int move) {
		Gameboard gb = new Gameboard(gameBoard);
		int value = 0;
		// determine which use the value is for

		if (gb.CheckForWinner(gb.lastSpacePlayed)) {
			bestValue = (1000000000 - depth);
			bestChoice = move;
		}

		// get the bestValue at our maximum recrusion depth
		else if (depth == maxDepth) {
			int moveWeight = (threatLevel(gb, color));
			if (moveWeight != 0) {
				moveWeight = value * (moveWeight - depth);
			}
			if (moveWeight >= bestValue) {
				bestChoice = move;
				bestValue = moveWeight;
			}
		} else {
			// Generate moves for each col and find the best score from each of
			// the generated moves.
			for (int c = 0; c < 7; c++) {
				Gameboard game = new Gameboard(gb);
				int selectedPlace = game.PlacePiece(c, color);
				// Recursive call the generated game grid and compare the
				// current value to move value
				// If move is higher, make it the new current value.

				if (selectedPlace != -1) {
					char tempColor;
					// change the user for the child node after a piece is
					// played
					if (color == 'Y') {
						tempColor = 'R';
					} else {
						tempColor = 'Y';
					}
					// call the function so we can explore to maximum depth
					if (depth < maxDepth) {
						int v = minimax(new Gameboard(game), depth + 1,
								tempColor, c) > minimax(new Gameboard(game),
								depth + 1, color, c) ? minimax(new Gameboard(
								game), depth + 1, tempColor, c) : minimax(
								new Gameboard(game), depth + 1, color, c);

					}
				}
			}
		}
		if (depth == 0) {
			if (threatLevel(gb, gb.lastSpacePlayed.getColor()) == 0) {
				return gb.spaces.get(0).get(3).getColor() == gb.playerColor ? 2
						: 3;
			} else {
				return bestChoice;
			}
		} else {
			return bestValue;
		}

	}

	// Goes through all threatening positions to find the ones with the most
	// value
	// Values are based on how threatening the game state is
	// return sum of the values since the threats are additive

	public int threatLevel(Gameboard gb, char player) {
		// Value each place as an order of magnitude above the next,
		// values double for diag and horizontal if two pieces are touching when
		// looking for only 2
		// values tripled if diag and horizontal if three pieces are touching
		int v = 1;
		int d = 2;
		int h = 3;
		int doubles = 100;
		int triples = 1000;
		int threat = 0;
		// horizontal 2
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 4; col++) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row).get(col + 3).getColor() == '-') {
					threat += doubles * h * 2;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == '-') {
					threat += doubles * h;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == '-'
						&& gb.spaces.get(row).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row).get(col + 2).getColor() == player) {
					threat += doubles * h;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == '-') {
					threat += 2 * doubles * h * 2;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row).get(col + 3).getColor() == player) {
					threat += doubles * h;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row).get(col + 2).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == player) {
					threat += doubles * h;
				}
			}
		}
		// horizontal 3.
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 4; col++) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row).get(col + 3).getColor() == player) {
					threat += triples * h;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row).get(col + 2).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == player) {
					threat += triples * h;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == player) {
					threat += triples * h * 3;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row).get(col + 1).getColor() == player
						&& gb.spaces.get(row).get(col + 2).getColor() == player
						&& gb.spaces.get(row).get(col + 3).getColor() == '-') {
					threat += triples * h * 3;
				}
			}
		}
		// Check 3 in a row with a space on each end
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 3; col++) {
				// horizontal
				if (gb.spaces.get(col).get(row).getColor() == '-'
						&& gb.spaces.get(col + 1).get(row).getColor() == player
						&& gb.spaces.get(col + 2).get(row).getColor() == player
						&& gb.spaces.get(col + 3).get(row).getColor() == player
						&& gb.spaces.get(col + 4).get(row + 4).getColor() == '-') {
					threat += 2 * triples * h * 3;
				}
			}
		}
		// vertical 2.
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 7; col++) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col).getColor() == player
						&& gb.spaces.get(row + 2).get(col).getColor() == '-') {
					threat += doubles * v;
				}
			}
		}
		// Check for vertical 3.
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 6; col++) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col).getColor() == player
						&& gb.spaces.get(row + 2).get(col).getColor() == player
						&& gb.spaces.get(row + 3).get(col).getColor() == '-') {
					threat += triples * v;
				}
			}
		}
		// diagonal bottom left top right.
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 4; col++) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == '-') {
					threat += doubles * d * 2;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player) {
					threat += doubles * d;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player) {
					threat += doubles * d * 2;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player) {
					threat += doubles * d;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == '-') {
					threat += doubles * d;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == '-') {
					threat += 2 * doubles * d * 2;
				}
			}
		}
		// diagonal bottom right top left.
		for (int row = 0; row < 3; row++) {
			for (int col = 6; col > 2; col--) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == '-') {
					threat += doubles * d * 2;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player) {
					threat += doubles * d;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player) {
					threat += doubles * d * 2;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player) {
					threat += doubles * d;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == '-') {
					threat += doubles * d;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == '-') {
					threat += 2 * doubles * d * 2;
				}
			}
		}

		// diagonally spaced 3 bottom left top right.
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 4; col++) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player) {
					threat += triples * h;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player) {
					threat += triples * h;
				} else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player) {
					threat += triples * h * 3;
				} else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == '-') {
					threat += triples * h * 3;
				}
			}
		}
		// diagonally spaced 3 bottom right top left.
		for (int row = 0; row < 3; row++) {
			for (int col = 6; col > 2; col--) {
				if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == '-'
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player) {
					threat += triples * h;
				}
				// (-0--)
				else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == '-'
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player) {
					threat += triples * h;
				}
				// (0---)
				else if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player) {
					threat += triples * h * 3;
				}
				// (---0)
				else if (gb.spaces.get(row).get(col).getColor() == player
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == '-') {
					threat += triples * h * 3;
				}
			}
		}
		// diag bottom right top left
		for (int row = 0; row < 2; row++) {
			for (int col = 6; col > 3; col--) {
				if (gb.spaces.get(row).get(col).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col - 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col - 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col - 3).getColor() == player
						&& gb.spaces.get(row + 4).get(col - 4).getColor() == '-') {
					threat += 2 * triples * d * 3;
				}
			}
		}
		// diag bottom left top right
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 3; col++) {
				if (gb.spaces.get(col).get(row).getColor() == '-'
						&& gb.spaces.get(row + 1).get(col + 1).getColor() == player
						&& gb.spaces.get(row + 2).get(col + 2).getColor() == player
						&& gb.spaces.get(row + 3).get(col + 3).getColor() == player
						&& gb.spaces.get(row + 4).get(col + 4).getColor() == '-') {
					threat += 2 * triples * d * 3;
				}
			}
		}

		return threat;
	}

}
