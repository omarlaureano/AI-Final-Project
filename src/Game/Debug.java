package Game;

import java.util.ArrayList;

public class Debug {


	public static void main(String[] args) {
		int[] d = {-1, 1,-1, 1,-1, 1, -1,1,-1, 1,-1, 1, -1,1,-1, 1, -1,0, -1,1,-1, 1,-1, 1, -1,0, -1,1, -1,0, -1,
				0, -1,0, -1,3, -1,0,-1, 0,-1, 3,-1, 3,-1, 0,-1, 3, -1,3,-1, 3, -1,3,-1, 3,-1, 3,-1, 3,-1, 3,-1, 3};

		System.out.println();
		int i = 0;
		for (int y = 8; y > 0; y--) {
			for (int x = 1; x <= 8; x++) {
				int t = d[i++];
				if (t== 1)
					System.out.print("-W-\t");
				else if (t == 3)
					System.out.print("-B-\t");
				else if (t== 2)
					System.out.print("-WK-\t");
				else if (t== 4)
					System.out.print("-BK-\t");
				else if (t== 0)
					System.out.print(" o \t");
				else System.out.print(" x \t");
			} System.out.println();
		}
		System.out.println();
	} 



}
