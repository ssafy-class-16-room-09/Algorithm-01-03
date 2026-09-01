import java.util.*;
import java.io.*;

class Node implements Comparable<Node> {
  int x;
  int y;
  int cost;
  Node(int x, int y, int cost) {
    this.x = x;
    this.y = y;
    this.cost = cost;
  }

  @Override
  public int compareTo(Node o) {
    return Integer.compare(this.cost, o.cost);
  }
}

public class Solution {

    final static int PADDING = -1;
    final static int[] dx = new int[]{ -1, 1, 0, 0 }; // UDLR
    final static int[] dy = new int[]{ 0, 0, -1, 1 }; // UDLR

    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int testCases = Integer.parseInt(br.readLine());

      for (int t = 1; t <= testCases; t++) {
        int n = Integer.parseInt(br.readLine());

        int[][] map = new int[n + 2][n + 2];
        Arrays.fill(map[0], PADDING);
        for (int i = 1; i <= n; i++) {
          map[i][0] = PADDING;
          String read = br.readLine();
          for (int j = 1; j <= n; j++) {
            map[i][j] = read.charAt(j - 1) - '0';
          }
          map[i][n + 1] = PADDING;
        }
        Arrays.fill(map[n + 1], PADDING);

        int[][] visited = new int[n + 2][n + 2];
        for (int[] v : visited) {
          Arrays.fill(v, Integer.MAX_VALUE); // 최댓값으로 초기화
        }
        visited[1][1] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(1, 1, 0));
        while (!pq.isEmpty()) {
          Node curr = pq.poll();

          if (visited[curr.x][curr.y] < curr.cost) continue; 
          if (curr.x == n && curr.y == n) break; // 목적지에 도달하면 종료

          for (int d = 0; d < 4; d++) {
            int nx = curr.x + dx[d];
            int ny = curr.y + dy[d];

            if (map[nx][ny] != PADDING) {
              int nextCost = curr.cost + map[nx][ny];
              if (visited[nx][ny] > nextCost) {
                visited[nx][ny] = nextCost;
                
                pq.add(new Node(nx, ny, nextCost));
              }
            }
          }
        }

        System.out.println("#" + t + " " + visited[n][n]);
      }
    }
}