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

		int game = 0;
		File fileToBeModified = new File("src/Game/winCount.txt");
		BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));

		int whiteWin = Integer.parseInt(reader.readLine());
		System.out.println("White Wins:" + whiteWin);
		int blackWin = Integer.parseInt(reader.readLine());
		System.out.println("Black Wins:" + blackWin);
		int stalemates = Integer.parseInt(reader.readLine());
		System.out.println("Stalemates:" + stalemates);

		int currWhiteWin = 0;
		int currBlackWin = 0;
		int currStalemate = 0;

		long start= System.currentTimeMillis();

		BoardState board = new BoardState();
		MinimaxAI minimaxBlackPlayer = new MinimaxAI(5);
		MinimaxAI minimaxWhitePlayer = new MinimaxAI(5);
		AlphaBetaAI abBlackPlayer = new AlphaBetaAI(5);
		AlphaBetaAI abWhitePlayer = new AlphaBetaAI(5);
		while (game <=30) {	
			for (int y = 8; y > 0; y--) {
				for (int x = 1; x <= 8; x++) {
					System.out.print(board.getTile(x, y) + "\t");
				}System.out.println();
			}
			
			int turn = 1;
			while (!board.gameOver()) {
				if (board != null) {
					if (board.isWhiteTurn) {
						//board = abWhitePlayer.alphaBetaPlay(board);
						board = minimaxWhitePlayer.minimaxPlay(board);
					}else {
						//board = abBlackPlayer.alphaBetaPlay(board);
						board = minimaxBlackPlayer.minimaxPlay(board);
					}
					System.out.println("Turn #" + turn++);
					board.printBoard();
					
					System.out.println();
					TimeUnit.MILLISECONDS.sleep(250);
				}
			}
			if (board.whiteWin()) {
				System.out.println("White Wins");
				whiteWin++;
				currWhiteWin++;
			}else if(board.blackWin()){
				System.out.println("Black Wins");
				blackWin++;
				currBlackWin++;
			}else {
				System.out.println("Stalemate");
				stalemates++;
				currStalemate++;
			}
			
//			board.reset();
//			game++;
			break;
		}

		long elapsed = System.currentTimeMillis() - start;
		System.out.println("Time Elapsed: " + elapsed);

		FileWriter writer = new FileWriter(fileToBeModified);
		writer.write(Integer.toString(whiteWin));
		writer.write(System.getProperty( "line.separator" ));
		writer.write(Integer.toString(blackWin));
		writer.write(System.getProperty( "line.separator" ));
		writer.write(Integer.toString(stalemates));
		System.out.println("White Wins:" + currWhiteWin);
		System.out.println("Black Wins:" + currBlackWin);
		System.out.println("Stalemates:" + currStalemate);
		reader.close();
		writer.close();
	}	
}
