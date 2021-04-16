package Game;
public class Checkers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BoardState b = new BoardState(1);
		for (int y = 8; y > 0; y--) {
			for (int x = 1; x <= 8; x++) {
				System.out.print(b.getTile(x, y) + "\t");
			}System.out.println();
		}

		if (b != null) {
			b.printBoard();
			System.out.println(b.whiteList().toString());
			System.out.println(b.blackList().toString());
			b = b.eatPieceUpRight(20);
		}
		if (b != null) {
			b.printBoard();
			System.out.println(b.whiteList().toString());
			System.out.println(b.blackList().toString());
			b = b.eatPieceDownLeft(9);
		}
		if (b != null) {
			b.printBoard();
			System.out.println(b.whiteList().toString());
			System.out.println(b.blackList().toString());
			b = b.movePieceUpLeft(27);
		}
		if (b != null) {
			b.printBoard();
			System.out.println(b.whiteList().toString());
			System.out.println(b.blackList().toString());
			b = b.movePieceDownRight(7);
		}
		if (b != null) {
			b.printBoard();
			System.out.println(b.whiteList().toString());
			System.out.println(b.blackList().toString());
			b = b.eatPieceDownRight(29);
		}
		if (b != null) {
			b.printBoard();
			System.out.println(b.whiteList().toString());
			System.out.println(b.blackList().toString());
		}
	}

}
