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
		int currSteps = 0;
		
		ArrayList<BoardState> moveList = board.getMoves();
		//if only one move available, play said move, usually for eats
		if (moveList.size() == 1) {
			System.out.println("Only one play available");
			return moveList.get(0);
		}

		//		int v = 0;
		if (board.isWhiteTurn) {//look for highest score in white turn
			System.out.println("White turn, finding max");
			ArrayList<BoardPath> bestMoves = maxValuePlay(board, currSteps, -400, 400);
			System.out.println("PathScore: " + bestMoves.get(0).getPathScore());
			
			//All paths lead to a loss. Shorten depth and try again
			while(bestMoves.get(0).getPathScore() == -200 && currSteps != (depth - 1)) {
				System.out.println("White might lose soon!");
				currSteps = (currSteps + depth)/2;
				System.out.println("Attempting with depth of " + (depth - currSteps));
				bestMoves = maxValuePlay(board, (currSteps + depth)/2, -400, 400);
			}
			
			if (bestMoves.get(0).getPathScore() == 200 && bestMoves.size() > 1) {//more than one way to win, find shortest path
				return BoardPath.shortestPath(bestMoves).getBoard();
			}
			
			if(bestMoves.size() == 1) {
				return bestMoves.get(0).getBoard();
			} else { //play a random move from the list of best moves
				Random r = new Random();
				return bestMoves.get(r.nextInt(bestMoves.size())).getBoard();
			}
		}else {//look for lowest score in black turn
			System.out.println("Black turn, finding min");
			ArrayList<BoardPath> bestMoves = minValuePlay(board, 0, -400, 400);
			System.out.println("PathScore: " + bestMoves.get(0).getPathScore());
			
			//All paths lead to a loss. Shorten depth and try again
			while(bestMoves.get(0).getPathScore() == 200 && currSteps != (depth - 1)) {
				System.out.println("Black might lose soon!");
				currSteps = (currSteps + depth)/2;
				System.out.println("Attempting with depth of " + (depth - currSteps));
				bestMoves = minValuePlay(board, (currSteps + depth)/2, -400, 400);
				System.out.println("PathScore: " + bestMoves.get(0).getPathScore());
			}
			
			if (bestMoves.get(0).getPathScore() == -200 && bestMoves.size() > 1) {//more than one way to win, find shortest path
				return BoardPath.shortestPath(bestMoves).getBoard();
			}
			
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
	 * Used with randomizer in alphaBetaPlay
	 * Uses alpha-beta algorithm
	 * @param board
	 * @param turn
	 * @return
	 */
	public ArrayList<BoardPath> maxValuePlay(BoardState board, int turn, double alpha, double beta) {
		ArrayList<BoardPath> res = new ArrayList<BoardPath>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(new BoardPath(board, Utility.utility(board), turn));
		}

		else {
			double v = -400;
			for (BoardState successor: board.getMoves()) {
				double successorUtil = minValuePlay(successor, turn+1, alpha, beta).get(0).getPathScore();
				
				//v <- Max(v,minValue(s,alpha,beta)
				if (successorUtil > v) {//new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(new BoardPath(successor, successorUtil, turn));
				}else if (successorUtil == v) {//tie, add successor to list for randomizer
					res.add(new BoardPath(successor, successorUtil, turn));
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
	public ArrayList<BoardPath> minValuePlay(BoardState board, int turn, double alpha, double beta) {
		ArrayList<BoardPath> res = new ArrayList<BoardPath>();

		if (board.gameOver() || turn >= depth) {
			//System.out.println("End of loop. Utility: " + utility(board));
			res.add(new BoardPath(board, Utility.utility(board), turn));
		}

		else {
			double v = 400;
			for (BoardState successor: board.getMoves()) {
				double successorUtil = maxValuePlay(successor, turn+1, alpha, beta).get(0).getPathScore();
				
				//v <- Max(v,minValue(s,alpha,beta)
				if (successorUtil < v) {//new v to beat, clear list
					v = successorUtil;
					res.clear();
					res.add(new BoardPath(successor, successorUtil, turn));
				}else if (successorUtil == v) {//tie, add successor to list for randomizer
					res.add(new BoardPath(successor, successorUtil, turn));
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
