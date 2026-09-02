import java.util.*;
import java.io.*;

class Solution {
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int[] dr = {0, 0, -1, 1};
    int[] dc = {-1, 1, 0, 0};
    
    int TC;
		TC = Integer.parseInt(br.readLine());


		for(int tc = 1; tc <= TC; tc++) {
      int N = Integer.parseInt(br.readLine());

      int[][] board = new int[N][N];
      int[][] dist = new int[N][N];

      for (int r=0; r<N; r++){
        String row = br.readLine();
        for (int c=0; c<N; c++){
          board[r][c] = row.charAt(c) - '0';
          dist[r][c] = Integer.MAX_VALUE;
        }
      }

      PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
      pq.offer(new int[] {0, 0, 0});
      dist[0][0] = 0;
      while (!pq.isEmpty()){
        int[] curr = pq.poll();
        int r = curr[0];
        int c = curr[1];
        int d = curr[2];
        if (r == N-1 && c == N-1) break;
        if (dist[r][c] < d) continue;
        for (int dir=0; dir<4; dir++){
          int nr = r+dr[dir], nc = c+dc[dir];
          if (nr<0 || nr>=N || nc<0 || nc>=N) continue;
          int nd = d+board[nr][nc];
          if (dist[nr][nc] > nd){
            dist[nr][nc] = nd;
            pq.offer(new int[] {nr, nc, nd});
          }
        }

      }
      System.out.printf("#%d %d\n", tc, dist[N-1][N-1]);

		}
	}
}