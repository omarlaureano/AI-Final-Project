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
			ArrayList<BoardPath> bestMoves = maxValuePlay(board, 0);
			System.out.println("PathScore: " + bestMoves.get(0).getPathScore());
			if(bestMoves.size() == 1) {
				return bestMoves.get(0).getBoard();
			} else { //play a random move from the list of best moves
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size())).getBoard();
			}
		}else {//look for lowest score in black turn
			System.out.println("Black turn, finding min");
			ArrayList<BoardPath> bestMoves = minValuePlay(board, 0);
			System.out.println("PathScore: " + bestMoves.get(0).getPathScore());
			if(bestMoves.size() == 1) {
				return bestMoves.get(0).getBoard();
			} else { //play a random move from the list of best moves
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size())).getBoard();
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
	public ArrayList<BoardPath> maxValuePlay(BoardState board, int turn) {
		ArrayList<BoardPath> res = new ArrayList<BoardPath>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(new BoardPath(board, Utility.utility(board)));
		}

		else {
			double v = -400;
			for (BoardState successor: board.getMoves()) {
				double successorUtil = minValuePlay(successor, turn+1).get(0).getPathScore();
				if (successorUtil > v) {//new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(new BoardPath(successor, successorUtil));
				}else if (successorUtil == v) {//tie, add successor to list for randomizer
					res.add(new BoardPath(successor, successorUtil));
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
	public ArrayList<BoardPath> minValuePlay(BoardState board, int turn) {
		ArrayList<BoardPath> res = new ArrayList<BoardPath>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(new BoardPath(board, Utility.utility(board)));
		}

		else {
			double v = 400;
			for (BoardState successor: board.getMoves()) {
				double successorUtil = maxValuePlay(successor, turn+1).get(0).getPathScore();
				if (successorUtil < v) { //new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(new BoardPath(successor, successorUtil));
				}else if (successorUtil == v) { //tie, add successor to list for randomizer
					res.add(new BoardPath(successor, successorUtil));
				}
			}
		}

		return res;
	}

}
