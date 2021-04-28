package Game;

public class Utility {

	//Regular pieces worth 2 points, Kings worth 4 points, Safe Pieces worth an extra point
		public static int blackUtility(BoardState board) {
			int points = (board.blackList().size()*2) + (board.blackKings().size()*4) 
					+ board.safeBlack().size() + board.blackGuard().size();
			if (!board.isWhiteTurn && board.canEat){
				points += 3;
			}
			if (board.comboPiece != -1){
				points += 3;
			}
			return points;
		}
		
		public static int whiteUtility(BoardState board) {
			int points = (board.whiteList().size()*2) + (board.whiteKings().size()*4) 
					+ board.safeWhite().size() + board.whiteGuard().size();
			if (board.isWhiteTurn && board.canEat){
				points += 3;
			}
			if (board.comboPiece != -1){
				points += 3;
			}
			return points;
		}

		//utility function; high = white advantage, low = black advantage
		public static double utility(BoardState board) {
			if (board.blackWin()) {
				return -200;
			}
			if (board.whiteWin()) {
				return 200;
			}
			double res = (double) whiteUtility(board)/blackUtility(board);
			return res;
		}
}
