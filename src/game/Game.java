package game;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Game {
	static Gameboard gb;

	static Computer computer;

	public static void main(String[] args) {
		boolean playAgain = true;
		while (playAgain) {
			gb = new Gameboard(7, 6);
			gb.PrintBoard();
			startGame();
			playGame();
			System.out.println("Play Again? y");
			Scanner scan = new Scanner(System.in);
			String option = scan.nextLine();
			if (!option.equals("y")) {
				playAgain = false;

			}

		}
	}

	public static void startGame() {
		boolean gameNotStarted = true;
		Scanner scan = new Scanner(System.in);
		while (gameNotStarted) {
			System.out
					.println("Enter R or Y to choose your color. R goes first");

			String option = scan.nextLine();

			if (option.equals("R")) {
				gb.playerColor = option.charAt(0);
				gb.computerColor = 'Y';
				gameNotStarted = false;
			} else if (option.equals("Y")) {
				gb.playerColor = option.charAt(0);
				gb.computerColor = 'R';
				gameNotStarted = false;
			}
		}
	}

	public static void playGame() {
		boolean playAgain = true;
		while (gb.playing) {
			boolean selecting = true;
			if (gb.playerColor == 'R') {
				userSelecting(selecting);
				if (gb.CheckForWinner(gb.lastSpacePlayed))
					break;
				computerGo();
			} else {
				computerGo();
				if (gb.CheckForWinner(gb.lastSpacePlayed))
					break;
				userSelecting(selecting);
			}
			gb.PrintBoard();
		}
		if (gb.winner == gb.playerColor) {
			System.out.println("You win!");
		} else if(gb.winner == gb.computerColor) {
			System.out.println("You lose!");
		}else{
			System.out.println("You Tie!");
		}
		gb.PrintBoard();
	}

	private static void computerGo() {
		computer = new Computer(gb);
		gb.PlacePiece(computer.calcValue(), gb.computerColor);
	}

	private static void userSelecting(boolean selecting) {
		int options[] = { 0, 1, 2, 3, 4, 5, 6 };
		while (selecting) {
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter 0-6 to select your spot");
			if (scan.hasNextInt()) {
				int option = scan.nextInt();
				if (IntStream.of(options).anyMatch(x -> x == option)) {
					if (gb.PlacePiece(option, gb.playerColor) == -1) {
						System.out.println("Not a valid choice. Choose again");
					} else {
						selecting = false;
					}
				}
			}
		}
	}
}
