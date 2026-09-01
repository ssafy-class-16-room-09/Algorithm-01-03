import java.util.*;
import java.io.*;
 
public class Solution {
 
  static int N;
  static int[][] board;
  static int[][] dist;
  static int[] dr = {0, 0, 1, -1};
  static int[] dc = {1, -1, 0, 0};
  static final int INF = Integer.MAX_VALUE;
 
  static class State implements Comparable<State> {
    int r;
    int c;
    int cost;
 
    public State(int r, int c, int cost) {
      this.r = r;
      this.c = c;
      this.cost = cost;
    }
 
    @Override
    public int compareTo(State other) {
      return Integer.compare(this.cost, other.cost);
    }
  }
 
  static int dijkstra() {
    dist = new int[N][N];
    PriorityQueue<State> pq = new PriorityQueue<>();
 
    for(int[] row : dist) {
      Arrays.fill(row, INF);
    }
 
    pq.offer(new State(0,0,board[0][0]));
    dist[0][0] = board[0][0];
 
    while(!pq.isEmpty()) {
      State curr = pq.poll();
       
      if(curr.cost != dist[curr.r][curr.c])
        continue;
      if(curr.r == N-1 && curr.c == N-1)
        return curr.cost;
 
      for(int i=0; i<4; i++) {
        int nextR = curr.r + dr[i];
        int nextC = curr.c + dc[i];
 
        if(nextR < 0 || nextR >= N || nextC < 0 || nextC >= N)
          continue;
         
        int nextCost = curr.cost + board[nextR][nextC];
 
        if(nextCost < dist[nextR][nextC]) {
          dist[nextR][nextC] = nextCost;
          pq.offer(new State(nextR, nextC, nextCost));
        }
      }
    }
 
    return -1;
  }
 
  public static void main(String[] args) {
    FastReader fr = new FastReader();
    StringBuilder sb = new StringBuilder();
 
    int T = fr.nextInt();
    for(int testCase=1; testCase<=T; testCase++) {
      N = fr.nextInt();
      board = new int[N][N];
      for(int i=0; i<N; i++) {
        String line = fr.next();
        for(int j=0; j<N; j++) {
          board[i][j] = line.charAt(j) - '0';
        }
      }
 
      sb.append('#').append(testCase).append(' ').append(dijkstra()).append('\n');
    }
 
    System.out.println(sb.toString());
  }
 
  static class FastReader {
    BufferedReader br;
    StringTokenizer st;
 
    public FastReader() {
      br = new BufferedReader(new InputStreamReader(System.in));
    }
 
    String next() {
      while(st == null || !st.hasMoreElements()) {
        try {
          st = new StringTokenizer(br.readLine());
        } catch(IOException e) {
          e.printStackTrace();
        }
      }
 
      return st.nextToken();
    }
 
    int nextInt() {
      return Integer.parseInt(next());
    }
  }
}