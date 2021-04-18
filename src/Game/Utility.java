package Game;

public class Utility {

	//Regular pieces worth 1 point, Kings worth 2 points, Safe Pieces worth 1/2 point
		public static int blackUtility(BoardState board) {
			return (board.blackList().size()*2) + (board.blackKings().size()*2) + board.safeBlack().size(); 
		}
		public static int whiteUtility(BoardState board) {
			return (board.whiteList().size()*2) + (board.whiteKings().size()*2) + board.safeWhite().size();
		}

		//utility function; high = white advantage, low = black advantage
		public static int utility(BoardState board) {
			if (board.blackWin()) {
				return -200;
			}
			if (board.whiteWin()) {
				return 200;
			}

			return whiteUtility(board) - blackUtility(board);
		}
}
