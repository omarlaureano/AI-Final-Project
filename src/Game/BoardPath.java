package Game;

import java.util.ArrayList;

public class BoardPath {
/**
 * Used in by AIs to keep track of the path score and child Board State to reach that path score.
 * Terminal BoardStates will return its utility as pathScore while middle board state will return best pathScore of children. 
 */
	
	double pathScore;
	int pathLength;
	BoardState board;
	public BoardPath(BoardState board, double pathScore, int pathLength) {
		super();
		this.pathScore = pathScore;
		this.board = board;
		this.pathLength = pathLength;
	}
	
	
	public int getPathLength() {
		return pathLength;
	}


	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}


	public double getPathScore() {
		return pathScore;
	}
	public void setPathScore(double pathScore) {
		this.pathScore = pathScore;
	}
	public BoardState getBoard() {
		return board;
	}
	public void setBoard(BoardState board) {
		this.board = board;
	}
	
	static BoardPath shortestPath (ArrayList<BoardPath> paths) {
		BoardPath shortestPath = null;
		int shortestLength = 100;
		for (BoardPath path : paths) {
			if (path.getPathLength() < shortestLength) {
				shortestLength = path.getPathLength();
				shortestPath = path;
			}
		}
		
		return shortestPath;
	}
}
