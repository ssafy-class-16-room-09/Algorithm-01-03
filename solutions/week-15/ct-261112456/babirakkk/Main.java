import java.util.*;
import java.io.*;

public class Main {

    static class Move {
        int r, c, dir;
        Move(int r, int c, int dir) {
            this.r = r;
            this.c = c;
            this.dir = dir;
        }
    }

    static int[] dr = { -1, 0, 1, 0 };
    static int[] dc = { 0, -1, 0, 1 };

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        boolean[][] isWall = new boolean[n][n];
        int[][][] visited = new int[n][n][4];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(visited[i][j], Integer.MAX_VALUE);
            }
        }

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                if (Integer.parseInt(st.nextToken()) == 1) {
                    isWall[i][j] = true;
                }
            }
        }

        Queue<Move> q = new ArrayDeque<>();
        for (int d = 2; d < 4; d++) {
            q.add(new Move(1, 1, d));
            visited[1][1][d] = 0;
        }

        while (!q.isEmpty()) {
            Move curr = q.poll();

            int nr = curr.r + dr[curr.dir];
            int nc = curr.c + dc[curr.dir];
            
            if (isWall[nr][nc]) {
                for (int d = 0; d < 4; d++) {
                    if (d == curr.dir) continue;
                    if (visited[curr.r][curr.c][curr.dir] >= visited[curr.r][curr.c][d]) continue;

                    visited[curr.r][curr.c][d] = visited[curr.r][curr.c][curr.dir];
                    q.add(new Move(curr.r, curr.c, d));
                }
            } else {
                if (visited[curr.r][curr.c][curr.dir] + 1 >= visited[nr][nc][curr.dir]) continue;
                visited[nr][nc][curr.dir] = visited[curr.r][curr.c][curr.dir] + 1;
                if (nr != (n - 2) || nc != (n - 2)) {
                    q.add(new Move(nr, nc, curr.dir));
                }
            }
        }

        int minTime = Integer.MAX_VALUE;
        for (int d = 0; d < 4; d++) {
            minTime = Math.min(minTime, visited[n - 2][n - 2][d]);
        }
        minTime = (minTime == Integer.MAX_VALUE) ? -1 : minTime;

        System.out.println(minTime);
    }
}
