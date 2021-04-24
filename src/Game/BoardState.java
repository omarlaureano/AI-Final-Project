package Game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class BoardState {

	/**
	 * Represents a state in the checkers game. 
	 * A function of potential actions for a given BoardState will return a list of children BoardStates
	 * Represents the board as an array of 32 ints, 
	 * each representing the state of one valid tile on the board
	 * 0 = empty
	 * 1 = white
	 * 2 = white king
	 * 3 = black
	 * 4 = black king
	 */

	ArrayList<Integer> board;
	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int WHITEKING = 2;
	public static final int BLACK = 3;
	public static final int BLACKKING = 4;

	//keep track of whose turn it is
	boolean isWhiteTurn;

	//forces player to make eat play if canEat
	boolean canEat;

	//keeps track of a piece that just ate to check for multi jumps, -1 means no combo in the current board
	int comboPiece;

	public BoardState() {
		board = new ArrayList<Integer>(32);
		for (int i = 0; i < 32; i++) {
			board.add(0);	
		}
		reset();
	}

	//debug boardstate
	public BoardState(int r) {
		board = new ArrayList<Integer>(32);
		for (int i = 0; i < 32; i++) {
			board.add(0);	
		}
		board.set(20, WHITE);
		board.set(27, WHITE);
		board.set(5, WHITE);
		board.set(25, BLACK);
		board.set(9, BLACK);
		board.set(24, BLACK);
		board.set(7, BLACK);

		isWhiteTurn = true;
		canEat = true;
		comboPiece = -1;
	}

	public BoardState(ArrayList<Integer> board, boolean isWhiteTurn, int comboPiece) {
		this.board = board;
		this.comboPiece = comboPiece;
		this.isWhiteTurn = isWhiteTurn;

		//check comboPiece for more eats
		if (comboPiece != -1) {
			if (potentialEats(comboPiece).size() <= 0) {// no more potential eats in combo
				//change turn
				this.isWhiteTurn = !isWhiteTurn;
				this.comboPiece = -1;
				canEat = canEat(); // check if next player can eat
			}else {
				//still same turn
				this.isWhiteTurn = isWhiteTurn;
				this.comboPiece = comboPiece;
				canEat = true;
			}
		} else {
			canEat = canEat();
		}

		//check for new kings when turn is done
		for (int i = 0; i <= 3; i++) {
			if (board.get(i) == BLACK) {
				board.set(i, BLACKKING);
			}
		}
		for (int i = 28; i <= 31; i++) {
			if (board.get(i) == WHITE) {
				board.set(i, WHITEKING);
			}
		}

	}



	//reset board to starting point,
	public void reset() {
		/**
		 * white in 1,3,5,7 of row 0 & 2
		 * 			0,2,4,6 of row 1
		 * black in 1,3,5,7 of row 6
		 * 			0,2,4,6 of row 5 & 7
		 */		

		for (int i = 0; i < 12; i ++) {
			board.set(i, 1);
		}
		for (int i = 12; i < 20; i ++) {
			board.set(i, 0);
		}
		for (int i = 20; i <= 31; i ++) {
			board.set(i, 3);
		}
		isWhiteTurn = true;
		comboPiece = -1;
		canEat = false;
	}

	public void setTileToWhite(int i) {
		board.set(i, WHITE);
	}
	public void setTileToBlack(int i) {
		board.set(i, BLACK);
	}
	public void setTileToWhiteKing(int i) {
		board.set(i, WHITEKING);
	}
	public void setTileToBlackKing(int i) {
		board.set(i, BLACKKING);
	}

	public void setTileToWhite(int x, int y) {
		board.set(getTile(x, y), WHITE);
	}
	public void setTileToBlack(int x, int y) {
		board.set(getTile(x, y), BLACK);
	}
	public void setTileToWhiteKing(int x, int y) {
		board.set(getTile(x, y), WHITEKING);
	}
	public void setTileToBlackKing(int x, int y) {
		board.set(getTile(x, y), BLACKKING);
	}

	private boolean isValidSpace(int i) {
		return i >=0 && i <=31;
	}

	//get piece of given x and y
	public int getTile(int x, int y) {
		/**
		 * check if valid tile
		 * valid tiles are 2,4,6,8
		 *  		 	   9,11,13,15
		 *    		 	   18,20,22,24
		 *      		   25,27,29,31
		 *   		 	   34,36,38,40
		 *   		 	   41,43,45,47
		 *   		 	   50,52,54,56
		 *   		 	   57,59,61,63
		 *   			   8(x-1) + y
		 *   
		 * 	 Invalid cases: out of bounds  
		 *   				both y and x are even
		 *   				both y and x are odd
		 */		
		if (y > 8 || y < 1 || x > 8 || x < 1
				|| (y % 2 == 0 && x % 2 == 0)
				|| (y % 2 == 1 && x % 2 == 1)) {
			return -1;
		}

		int pos = 8*(y-1) + x;
		if(pos % 2 == 1) {
			return pos/2;
		}
		else return pos/2-1;
	}

	/**
	 * Get x and y from given index in board.
	 * Return array of 2 integers
	 * @param i
	 * @return int[] where [0] = x, [1] = y. Null if invalid index
	 */
	public int[] getPos(int i) {

		// check valid index	
		if (i < 0 || i > 31) {
			return null;
		}

		int y = i/4 + 1;
		int x;
		if (y % 2 == 0) {
			x = 2*(i % 4) + 1;	
		} else {
			x = 2*(i % 4) + 2;
		}

		return new int[]{x,y};
	}

	/**
	 * Return piece in given x-y coordinate
	 * @param x
	 * @param y
	 * @return int representing piece type
	 */
	private Integer getPiece(int x, int y) {
		return getPiece(getTile(x, y));
	}

	/**
	 * Return piece in given tile
	 * @param i
	 * @return int representing piece type
	 */
	private Integer getPiece(int i) {
		if (isValidSpace(i))
			return board.get(i);
		return -1;
	}


	/************NORMAL MOVES**********************
	 * 
	 * When piece moves, player turn changes, comboPiece is -1. 
	 * If comboPiece != -1, these are not available, return null.
	 * if canEat boolean is true, automatically return null.
	 * 
	 */

	public BoardState movePieceUpLeft(int x, int y){
		return movePieceUpLeft(getTile(x, y));
	}

	public BoardState movePieceUpLeft(int i){
		//check invalid spots, left/top border
		if (canEat || i < 0 ||  i == 4 || i == 12 || i == 20 || i >= 28 || comboPiece != -1) {
			return null;
		}

		int piece = board.get(i);
		//check whose turn and if space has moveable piece for given player
		if ((isWhiteTurn && (piece == WHITE || piece == WHITEKING)) 
				|| (!isWhiteTurn && piece == BLACKKING)) {
			if ((i/4) % 2 == 0) {//even rows, add 4 to i
				if(board.get(i+4) == EMPTY) {//destination must be empty to move there
					//create new boardstate with moved piece
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i+4, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
			else {//odd rows, add 3 to i
				if(board.get(i+3) == EMPTY) {
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i+3, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
		}
		//invalid play
		return null;
	}

	public BoardState movePieceUpRight(int x, int y){
		return movePieceUpRight(getTile(x, y));
	}

	public BoardState movePieceUpRight(int i){
		//check invalid spots, right/top border
		if (canEat || i < 0 ||  i == 3 || i == 11 || i == 19 || i == 27 || i >= 28 || comboPiece != -1) {
			return null;
		}

		int piece = board.get(i);
		//check whose turn and if space has moveable piece for given player
		if ((isWhiteTurn && (piece == WHITE || piece == WHITEKING)) 
				|| (!isWhiteTurn && piece == BLACKKING)) {
			if ((i/4) % 2 == 0) {//even rows, add 5 to i
				if(board.get(i+5) == EMPTY) {//destination must be empty to move there
					//create new boardstate with moved piece
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i+5, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
			else {//odd rows, add 4 to i
				if(board.get(i+4) == EMPTY) {
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i+4, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
		}
		//invalid play
		return null;
	}
	public BoardState movePieceDownLeft(int x, int y){
		return movePieceDownLeft(getTile(x, y));
	}

	public BoardState movePieceDownLeft(int i){
		//check invalid spots, left/bottom border
		if (canEat || i <= 4 || i == 12 || i == 20 || i == 28 || i > 31 || comboPiece != -1) {
			return null;
		}

		int piece = board.get(i);
		//check whose turn and if space has moveable piece for given player
		if ((isWhiteTurn && piece == WHITEKING) 
				|| (!isWhiteTurn && (piece == BLACK || piece == BLACKKING))) {
			if ((i/4) % 2 == 0) {//even rows, subtract 4 to i
				if(board.get(i-4) == EMPTY) {//destination must be empty to move there
					//create new boardstate with moved piece
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i-4, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
			else {//odd rows, substract 5 to i
				if(board.get(i-5) == EMPTY) {
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i-5, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
		}
		//invalid play
		return null;
	}

	public BoardState movePieceDownRight(int x, int y){
		return movePieceDownRight(getTile(x, y));
	}

	public BoardState movePieceDownRight(int i){
		//check invalid spots, right/bottom border
		if (canEat || i <= 3 || i == 11 || i == 19 || i == 27 || i > 31 || comboPiece != -1) {
			return null;
		}

		int piece = board.get(i);
		//check whose turn and if space has moveable piece for given player
		if ((isWhiteTurn && piece == WHITEKING) 
				|| (!isWhiteTurn && (piece == BLACK || piece == BLACKKING))) {
			if ((i/4) % 2 == 0) {//even rows, subtract 3 to i
				if(board.get(i-3) == EMPTY) {//destination must be empty to move there
					//create new boardstate with moved piece
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i-3, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
			else {//odd rows, subtract 4 to i
				if(board.get(i-4) == EMPTY) {
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.addAll(board);
					res.set(i, EMPTY);
					res.set(i-4, piece);
					return new BoardState(res, !isWhiteTurn, -1);
				}
			}
		}
		//invalid play
		return null;
	}

	/******************EATING MOVES****************************
	 * 
	 * When piece eats, player turn stays same, destination becomes comboPiece.
	 * if canEat boolean is false, automatically return null.
	 * When making new BoardState, check if comboPiece has potentialEats.
	 * If so, force player to use comboPiece for another eat move (i == comboPiece).
	 * 
	 */

	public BoardState eatPieceUpLeft(int i){
		//check invalid spots, left/top double border 
		if (i <= 0 || i == 4 || i == 8 || i == 12 || i == 16 || i == 20 || i >= 24 
				|| (comboPiece != -1 && i != comboPiece)) {//if there is comboPiece and you try to use something else, return null
			return null;
		}

		if (board.get(i+7) != EMPTY) { // destination is i + 7, check first
			return null;
		}

		int piece = board.get(i);

		//check whose turn and if space has moveable piece for given player

		if ((i/4) % 2 == 0) {//even rows, add 4 to i to find middle spot
			int middleman = board.get(i+4);
			if( ((isWhiteTurn && (piece == WHITE || piece == WHITEKING)) 
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white piece with black middleman
					|| ((!isWhiteTurn && piece == BLACKKING) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black king with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i+4, EMPTY);//middleman
				res.set(i+7, piece);//destination
				return new BoardState(res, isWhiteTurn, i+7);
			}
		}
		else {//odd rows, add 3 to i to find middle spot
			int middleman = board.get(i+3);
			if( ((isWhiteTurn && (piece == WHITE || piece == WHITEKING))
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white piece with black middleman
					|| ((!isWhiteTurn && piece == BLACKKING) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black king with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i+3, EMPTY);//middleman
				res.set(i+7, piece);//destination
				return new BoardState(res, isWhiteTurn, i+7);
			}
		}

		//invalid play
		return null;
	}
	public BoardState eatPieceUpLeft(int x, int y){
		return eatPieceUpLeft(getTile(x, y));
	}

	public BoardState eatPieceUpRight(int i){
		//check invalid spots, right/top double border 
		if (i < 0 ||  i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i >= 23 
				|| (comboPiece != -1 && i != comboPiece)) {
			return null;
		}

		if (board.get(i+9) != EMPTY) { // destination is i + 9, check first
			return null;
		}

		int piece = board.get(i);

		//check whose turn and if space has moveable piece for given player

		if ((i/4) % 2 == 0) {//even rows, add 5 to i to find middle spot
			int middleman = board.get(i+5);
			if( ((isWhiteTurn && (piece == WHITE || piece == WHITEKING)) 
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white piece with black middleman
					|| ((!isWhiteTurn && piece == BLACKKING) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black king with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i+5, EMPTY);//middleman
				res.set(i+9, piece);//destination
				return new BoardState(res, isWhiteTurn, i+9);
			}
		}
		else {//odd rows, add 4 to i to find middle spot
			int middleman = board.get(i+4);
			if( ((isWhiteTurn && (piece == WHITE || piece == WHITEKING))
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white piece with black middleman
					|| ((!isWhiteTurn && piece == BLACKKING) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black king with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i+4, EMPTY);//middleman
				res.set(i+9, piece);//destination
				return new BoardState(res, isWhiteTurn, i+9);
			}
		}

		//invalid play
		return null;
	}
	public BoardState eatPieceUpRight(int x, int y){
		return eatPieceUpRight(getTile(x, y));
	}

	public BoardState eatPieceDownLeft(int i){
		//check invalid spots, left/bottom double border 
		if (i <= 8 || i == 12 || i == 16 || i == 20 || i == 24 || i == 28 || i > 31 
				|| (comboPiece != -1 && i != comboPiece)) {
			return null;
		}

		if (board.get(i-9) != EMPTY) { // destination is i - 9, check first
			return null;
		}

		int piece = board.get(i);

		//check whose turn and if space has moveable piece for given player

		if ((i/4) % 2 == 0) {//even rows, subtract 4 to i to find middle spot
			int middleman = board.get(i-4);
			if( ((isWhiteTurn && piece == WHITEKING) 
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white king with black middleman
					|| ((!isWhiteTurn && (piece == BLACK || piece == BLACKKING)) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black piece with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i-4, EMPTY);//middleman
				res.set(i-9, piece);//destination
				return new BoardState(res, isWhiteTurn, i-9);
			}
		}
		else {//odd rows, subtract 5 to i to find middle spot
			int middleman = board.get(i-5);
			if( ((isWhiteTurn && piece == WHITEKING) 
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white king with black middleman
					|| ((!isWhiteTurn && (piece == BLACK || piece == BLACKKING)) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black piece with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i-5, EMPTY);//middleman
				res.set(i-9, piece);//destination
				return new BoardState(res, isWhiteTurn, i-9);
			}
		}

		//invalid play
		return null;
	}
	public BoardState eatPieceDownLeft(int x, int y){
		return eatPieceDownLeft(getTile(x, y));
	}

	public BoardState eatPieceDownRight(int i){
		//check invalid spots, right/bottom double border 
		if (i <= 7 ||  i == 11 || i == 15 || i == 19 || i == 23 || i == 27 || i >= 31 
				|| (comboPiece != -1 && i != comboPiece)) {
			return null;
		}

		if (board.get(i-7) != EMPTY) { // destination is i - 7, check first
			return null;
		}

		int piece = board.get(i);
		//check whose turn and if space has moveable piece for given player

		if ((i/4) % 2 == 0) {//even rows, subtract 3 to i to find middle spot
			int middleman = board.get(i-3);
			if( ((isWhiteTurn && piece == WHITEKING) 
					&& (middleman == BLACK || middleman == BLACKKING)) // check if white king with black middleman
					|| ((!isWhiteTurn && (piece == BLACK || piece == BLACKKING)) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black piece with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i-3, EMPTY);//middleman
				res.set(i-7, piece);//destination
				return new BoardState(res, isWhiteTurn, i-7);
			}
		}
		else {//odd rows, subtract 4 to i to find middle spot
			int middleman = board.get(i-4);
			if( ((isWhiteTurn && piece == WHITEKING) 
					&& (middleman == BLACK || middleman == BLACKKING)) 
					|| ((!isWhiteTurn && (piece == BLACK || piece == BLACKKING)) 
							&& (middleman == WHITE || middleman == WHITEKING))) { // or if black piece with white middleman
				//create new boardstate with moved piece
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.addAll(board);
				res.set(i, EMPTY);
				res.set(i-4, EMPTY);//middleman
				res.set(i-7, piece);//destination
				return new BoardState(res, isWhiteTurn, i-7);
			}
		}

		//invalid play
		return null;
	}
	public BoardState eatPieceDownRight(int x, int y){
		return eatPieceDownRight(getTile(x, y));
	}

	public ArrayList<Integer> whiteList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < board.size(); i++) {
			if (board.get(i) == WHITE || board.get(i) == WHITEKING) {
				list.add(i);
			}
		}
		return list;
	}

	public ArrayList<Integer> blackList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < board.size(); i++) {
			if (board.get(i) == BLACK || board.get(i) == BLACKKING) {
				list.add(i);
			}
		}
		return list;
	}
	
	public ArrayList<Integer> whiteKings() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < board.size(); i++) {
			if (board.get(i) == WHITEKING) {
				list.add(i);
			}
		}
		return list;
	}

	public ArrayList<Integer> blackKings() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < board.size(); i++) {
			if (board.get(i) == BLACKKING) {
				list.add(i);
			}
		}
		return list;
	}
	
	//border pieces are considered safe, smart to have as many borders as possible
	public ArrayList<Integer> safeWhite() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i <= 4; i++) {
			if (board.get(i) == WHITE) {
				list.add(i);
			}
		}
		if (getPiece(11) == WHITE)
			list.add(11);
		if (getPiece(12) == WHITE)
			list.add(12);
		if (getPiece(19) == WHITE)
			list.add(19);
		if (getPiece(20) == WHITE)
			list.add(20);
		if (getPiece(27) == WHITE)
			list.add(27);	
		return list;
	}
	
	public ArrayList<Integer> safeBlack() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (getPiece(4) == BLACK)
			list.add(4);
		if (getPiece(11) == BLACK)
			list.add(11);
		if (getPiece(12) == BLACK)
			list.add(12);
		if (getPiece(19) == BLACK)
			list.add(19);
		if (getPiece(20) == BLACK)
			list.add(20);
		for (int i = 27; i <= 31; i++) {
			if (board.get(i) == BLACK) {
				list.add(i);
			}
		}
		return list;
	}
	
	//kings can trap border kings by standing opposite to them, guarding their exits
		public ArrayList<Integer> whiteGuard() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			if (getPiece(13) == WHITEKING && getPiece(12) == BLACKKING)
				list.add(13);
			if (getPiece(21) == WHITEKING && (getPiece(20) == BLACKKING || getPiece(29) == BLACKKING || getPiece(28) == BLACKKING))
				list.add(21);
			if (getPiece(22) == WHITEKING && getPiece(30) == BLACKKING)
				list.add(22);
			if (getPiece(18) == WHITEKING && getPiece(19) == BLACKKING)
				list.add(18);
			if (getPiece(10) == WHITEKING && (getPiece(11) == BLACKKING || getPiece(2) == BLACKKING || getPiece(3) == BLACKKING))
				list.add(10);
			if (getPiece(9) == WHITEKING && getPiece(1) == BLACKKING)
				list.add(9);
			return list;
		}
		
		public ArrayList<Integer> blackGuard() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			if (getPiece(13) == BLACKKING && getPiece(12) == WHITEKING)
				list.add(13);
			if (getPiece(21) == BLACKKING && (getPiece(20) == WHITEKING || getPiece(29) == WHITEKING || getPiece(28) == WHITEKING))
				list.add(21);
			if (getPiece(22) == BLACKKING && getPiece(30) == WHITEKING)
				list.add(22);
			if (getPiece(18) == BLACKKING && getPiece(19) == WHITEKING)
				list.add(18);
			if (getPiece(10) == BLACKKING && (getPiece(11) == WHITEKING || getPiece(2) == WHITEKING || getPiece(3) == WHITEKING))
				list.add(10);
			if (getPiece(9) == BLACKKING && getPiece(1) == WHITEKING)
				list.add(9);
			return list;
		}

	/**
	 * Potential moves for given tile. To be used with piece lists (blackList, whiteList)
	 * @param i
	 * @return ArrayList of BoardStates as potential moves for given tile.
	 */
	public ArrayList<BoardState> potentialMoves(int i){
		ArrayList<BoardState> moves = new ArrayList<BoardState>();

		BoardState upRight = movePieceUpRight(i);
		BoardState downRight = movePieceDownRight(i);
		BoardState upLeft = movePieceUpLeft(i);
		BoardState downLeft = movePieceDownLeft(i);

		if (upRight != null)
			moves.add(upRight);
		if (downRight != null)
			moves.add(downRight);
		if (upLeft != null)
			moves.add(upLeft);
		if (downLeft != null)
			moves.add(downLeft);

		return moves;
	}
	public ArrayList<BoardState> potentialMoves(int x, int y){
		return potentialMoves(getTile(x, y));
	}

	/**
	 * Potential eating moves for given tile. To be used with piece lists (blackList, whiteList) and comboPiece.
	 * @param i
	 * @return ArrayList of BoardStates as potential moves for given tile.
	 */
	public ArrayList<BoardState> potentialEats(int i){
		ArrayList<BoardState> eats = new ArrayList<BoardState>();

		BoardState upRight = eatPieceUpRight(i);
		BoardState downRight = eatPieceDownRight(i);
		BoardState upLeft = eatPieceUpLeft(i);
		BoardState downLeft = eatPieceDownLeft(i);

		if (upRight != null)
			eats.add(upRight);
		if (downRight != null)
			eats.add(downRight);
		if (upLeft != null)
			eats.add(upLeft);
		if (downLeft != null)
			eats.add(downLeft);

		return eats;
	}
	public ArrayList<BoardState> potentialEats(int x, int y){
		return potentialEats(getTile(x, y));
	}

	public ArrayList<BoardState> getMoves(){
		ArrayList<BoardState> moves = new ArrayList<BoardState>();
		
		if (canEat) {
			if (isWhiteTurn) {
				for (int piece:whiteList()) {
					moves.addAll(potentialEats(piece));
				}
			} else {
				for (int piece:blackList()) {
					moves.addAll(potentialEats(piece));
				}
			}
		} else {
			if (isWhiteTurn) {
				for (int piece:whiteList()) {
					moves.addAll(potentialMoves(piece));
				}
			} else {
				for (int piece:blackList()) {
					moves.addAll(potentialMoves(piece));
				}
			}
		}
		return moves;
	}

	/**
	 * Force player to eat by checking for potential eats for all player's pieces.
	 * If one piece can eat, immediately return true, forcing player to make eat move.
	 */

	public boolean canEat() {
		if (isWhiteTurn) {
			for (int piece: whiteList()) {
				if (potentialEats(piece).size() > 0) // found at least one eat move
					return true;
			}
			return false; // no eat moves found
		}else {//black turn
			for (int piece: blackList()) {
				if (potentialEats(piece).size() > 0)
					return true;
			}
			return false; // no moves found
		}
	}
	public boolean canMove() {
		if (canEat) {
			return false;
		}
		if (isWhiteTurn) {
			for (int piece: whiteList()) {
				if (potentialMoves(piece).size() > 0) // found at least one eat move
					return true;
			}
			return false; // no eat moves found
		}else {//black turn
			for (int piece: blackList()) {
				if (potentialMoves(piece).size() > 0)
					return true;
			}
			return false; // no moves found
		}
	}

	public boolean whiteWin() {
		return blackList().size() <= 0 || (!isWhiteTurn && !canEat && !canMove());
	}
	public boolean blackWin() {
		return whiteList().size() <= 0 || (isWhiteTurn && !canEat && !canMove());
	}
	public boolean gameOver() {
		return whiteWin() || blackWin();
	}
	
	public void printBoard() {
		System.out.println();
		if (this != null) {
			for (int y = 8; y > 0; y--) {
				for (int x = 1; x <= 8; x++) {
					int t = getPiece(x, y);
					if (t== WHITE)
						System.out.print("-W-\t");
					else if (t == BLACK)
						System.out.print("-B-\t");
					else if (t== WHITEKING)
						System.out.print("-WK-\t");
					else if (t== BLACKKING)
						System.out.print("-BK-\t");
					else if (t== EMPTY)
						System.out.print(" o \t");
					else System.out.print(" x \t");
				} System.out.println();
			}
			if (isWhiteTurn){
				System.out.println("White Turn");
			}else{
				System.out.println("Black Turn");
			}
			System.out.println("Combo Piece: " + comboPiece);
			System.out.println("Can Eat: " + canEat);
			System.out.println();
		} 

	}


}