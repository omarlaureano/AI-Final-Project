package Game;

import java.util.ArrayList;
import java.util.Random;

public class AlphaBetaAI {

	int depth;

	public AlphaBetaAI (int depth){
		this.depth = depth;
	}
	public AlphaBetaAI (){// default depth of turns
		this(4);
	}
	
	public BoardState alphaBetaPlay(BoardState board) {
		ArrayList<BoardState> moveList = board.getMoves();
		//if only one move available, play said move, usually for eats
		if (moveList.size() == 1) {
			System.out.println("Only one play available");
			return moveList.get(0);
		}

		//		int v = 0;
		if (board.isWhiteTurn) {//look for highest score in white turn
			System.out.println("White turn, finding max");
			ArrayList<BoardState> bestMoves = maxValuePlay(board, 0, -400, 400);
			if(bestMoves.size() == 1) {
				return bestMoves.get(0);
			} else { //play a random move from the list of best moves
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size()));
			}
		}else {//look for lowest score in black turn
			System.out.println("Black turn, finding min");
			ArrayList<BoardState> bestMoves = minValuePlay(board, 0, -400, 400);
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
	 * Used with randomizer in alphaBetaPlay
	 * Uses alpha-beta algorithm
	 * @param board
	 * @param turn
	 * @return
	 */
	public ArrayList<BoardState> maxValuePlay(BoardState board, int turn, int alpha, int beta) {
		ArrayList<BoardState> res = new ArrayList<BoardState>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(board);
		}

		else {
			int v = -400;
			for (BoardState successor: board.getMoves()) {
				int successorUtil = Utility.utility(minValuePlay(successor, turn+1, alpha, beta).get(0));
				
				//v <- Max(v,minValue(s,alpha,beta)
				if (successorUtil > v) {//new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(successor);
				}else if (successorUtil == v) {//tie, add successor to list for randomizer
					res.add(successor);
				}
				
				//if v >= beta return v
				if(v >= beta) {
					return res;
				}
				
				//alpha = max (alpha,v)
				alpha = Math.max(alpha, v);
			}
		}

		return res;
	}
	
	/**
	 * Return a list of plays in children of board that contain maximum value.
	 * Used with randomizer in alphaBetaPlay
	 * Uses alpha-beta algorithm
	 * @param board
	 * @param turn
	 * @return
	 */
	public ArrayList<BoardState> minValuePlay(BoardState board, int turn, int alpha, int beta) {
		ArrayList<BoardState> res = new ArrayList<BoardState>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(board);
		}

		else {
			int v = 400;
			for (BoardState successor: board.getMoves()) {
				int successorUtil = Utility.utility(maxValuePlay(successor, turn+1, alpha, beta).get(0));
				
				//v <- Max(v,minValue(s,alpha,beta)
				if (successorUtil < v) {//new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(successor);
				}else if (successorUtil == v) {//tie, add successor to list for randomizer
					res.add(successor);
				}
				
				//if v <= alpha return v
				if(v <= alpha) {
					return res;
				}
				
				//beta = min (beta,v)
				beta = Math.min(beta, v);
			}
		}

		return res;
	}
	
}
