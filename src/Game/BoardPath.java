package Game;

public class BoardPath {
/**
 * Used in by AIs to keep track of the path score and child Board State to reach that path score.
 * Terminal BoardStates will return its utility as pathScore while middle board state will return best pathScore of children. 
 */
	
	double pathScore;
	BoardState board;
	public BoardPath(BoardState board, double pathScore) {
		super();
		this.pathScore = pathScore;
		this.board = board;
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
}
