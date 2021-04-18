package Game;

import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Checkers {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		BoardState board = new BoardState();
		AI blackPlayer = new AI(6);
		AI whitePlayer = new AI(6);
		Scanner sc = new Scanner(System.in);
		int input = -1;
		

		for (int y = 8; y > 0; y--) {
			for (int x = 1; x <= 8; x++) {
				System.out.print(board.getTile(x, y) + "\t");
			}System.out.println();
		}


		System.out.println("Enter for AI play");
		while (!board.gameOver()) {
			if (board != null) {
				if (board.isWhiteTurn) {
					board = whitePlayer.minimaxPlay(board);
				}else {
					board = blackPlayer.minimaxPlay(board);
				}
				board.printBoard();
				System.out.println(board.whiteList().toString());
				System.out.println(board.blackList().toString());
				TimeUnit.SECONDS.sleep(1);
			}
		}


	}

}
