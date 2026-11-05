import java.util.*;
import java.io.*;
import java.math.*;

class Solution
{
  static BigInteger[][] tile;
	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt(); // 테스트케이스 개수

    // 0: 2x1 세로, 1: 2x1 가로 2개 or 2x2 1개
    tile = new BigInteger[251][2]; 
    tile[0][0] = new BigInteger("0");
    tile[0][1] = new BigInteger("0");
    tile[1][0] = new BigInteger("1");
    tile[1][1] = new BigInteger("0");
    tile[2][0] = new BigInteger("1");
    tile[2][1] = new BigInteger("2");
    StringBuilder sb = new StringBuilder();
		for(int test_case = 1; test_case <= T; test_case++)
		{
      int N = sc.nextInt(); // 2*N의 N
      topDown(N);
      sb.append(String.format("#%d %s\n", test_case, (tile[N][0].add(tile[N][1]))));
		}
    System.out.print(sb);
	}

  private static void topDown(int num) {
    if(tile[num][0] != null) return;
    topDown(num-1);
    tile[num][0] = tile[num-1][0].add(tile[num-1][1]);
    tile[num][1] = tile[num-1][0].add(tile[num-1][0]);
  }
}