package Game;

import java.util.ArrayList;
import java.util.Random;

public class MinimaxAI {

	int depth;

	public MinimaxAI (int depth){
		this.depth = depth;
	}
	public MinimaxAI (){// default depth of turns
		this(4);
	}

	public BoardState minimaxPlay(BoardState board) {
		ArrayList<BoardState> moveList = board.getMoves();
		//if only one move available, play said move, usually for eats
		if (moveList.size() == 1) {
			System.out.println("Only one play available");
			return moveList.get(0);
		}

		//		int v = 0;
		if (board.isWhiteTurn) {//look for highest score in white turn
			System.out.println("White turn, finding max");
			ArrayList<BoardState> bestMoves = maxValuePlay(board, 0);
			if(bestMoves.size() == 1) {
				return bestMoves.get(0);
			} else { //play a random move from the list of best moves
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size()));
			}
		}else {//look for lowest score in black turn
			System.out.println("Black turn, finding min");
			ArrayList<BoardState> bestMoves = minValuePlay(board, 0);
			if(bestMoves.size() == 1) {
				return bestMoves.get(0);
			} else { //play a random move from the list of best moves
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size()));
			}
		}
	}

	/**
	 * Return a list of plays in children of board that contain maximum value.
	 * Used with randomizer in minimaxPlay
	 *  Uses minimax algorithm
	 * @param board
	 * @param turn
	 * @return
	 */
	public ArrayList<BoardState> maxValuePlay(BoardState board, int turn) {
		ArrayList<BoardState> res = new ArrayList<BoardState>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(board);
		}

		else {
			int v = -400;
			for (BoardState successor: board.getMoves()) {
				int successorUtil = Utility.utility(minValuePlay(successor, turn+1).get(0));
				if (successorUtil > v) {//new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(successor);
				}else if (successorUtil == v) {//tie, add successor to list for randomizer
					res.add(successor);
				}
			}
		}

		return res;
	}

	/**
	 * Return a list of plays in children of board that contain minimum value.
	 * Used with randomizer in minimaxPlay
	 * Uses minimax algorithm
	 * @param board
	 * @param turn
	 * @return
	 */
	public ArrayList<BoardState> minValuePlay(BoardState board, int turn) {
		ArrayList<BoardState> res = new ArrayList<BoardState>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(board);
		}

		else {
			int v = 400;
			for (BoardState successor: board.getMoves()) {
				int successorUtil = Utility.utility(maxValuePlay(successor, turn+1).get(0));
				if (successorUtil < v) { //new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(successor);
				}else if (successorUtil == v) { //tie, add successor to list for randomizer
					res.add(successor);
				}
			}
		}

		return res;
	}

}
