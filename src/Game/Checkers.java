package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Checkers {

	public static void main(String[] args) throws InterruptedException, NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BoardState board = new BoardState();
		MinimaxAI minimaxBlackPlayer = new MinimaxAI(8);
		MinimaxAI minimaxWhitePlayer = new MinimaxAI(8);
		AlphaBetaAI abBlackPlayer = new AlphaBetaAI(8);
		AlphaBetaAI abWhitePlayer = new AlphaBetaAI(8);

		File fileToBeModified = new File("src/Game/winCount.txt");
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;

		reader = new BufferedReader(new FileReader(fileToBeModified));


		int input = -1;

		int whiteWin = Integer.parseInt(reader.readLine());
		System.out.println("White Wins:" + whiteWin);
		int blackWin = Integer.parseInt(reader.readLine());
		System.out.println("Black Wins:" + blackWin);
		reader.close();

		for (int y = 8; y > 0; y--) {
			for (int x = 1; x <= 8; x++) {
				System.out.print(board.getTile(x, y) + "\t");
			}System.out.println();
		}

		int turn = 1;
		System.out.println("Enter for AI play");
		while (!board.gameOver()) {
			if (board != null) {
				if (board.isWhiteTurn) {
					board = abWhitePlayer.alphaBetaPlay(board);
					//board = minimaxWhitePlayer.minimaxPlay(board);
				}else {
					board = abBlackPlayer.alphaBetaPlay(board);
					//board = minimaxBlackPlayer.minimaxPlay(board);
				}
				board.printBoard();
				System.out.println("Turn #" + turn++);
				System.out.println(board.whiteList().toString());
				System.out.println(board.blackList().toString());
				TimeUnit.SECONDS.sleep((long) 0.5);
			}
		}
		if (board.whiteWin()) {
			whiteWin++;
		}else {
			blackWin++;
		}

		writer = new FileWriter(fileToBeModified);
		writer.write(Integer.toString(whiteWin));
		writer.write(System.getProperty( "line.separator" ));
		writer.write(Integer.toString(blackWin));
		System.out.println("White Wins:" + whiteWin);
		System.out.println("Black Wins:" + blackWin);
		writer.close();
	}

}
