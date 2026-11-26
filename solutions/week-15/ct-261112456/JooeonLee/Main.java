import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static int[][] grid;
    static int[][][] visited;
    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, -1, 0, 1};

    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();

        n = fr.nextInt();
        grid = new int[n][n];
        visited = new int[n][n][4];

        for(int r = 0; r < n; r++)
            for(int c = 0; c < n; c++)
                grid[r][c] = fr.nextInt();

        ArrayDeque<int[]> q = new ArrayDeque<>();
        int answer = -1;

        // r, c, time, dir
        q.offerLast(new int[]{1, 1, 0, -1});

        while(!q.isEmpty()) {
            int[] curr = q.pollFirst();
            int r = curr[0];
            int c = curr[1];
            int time = curr[2];
            int dir = curr[3];

            if(r == n - 2 && c == n - 2) {
                answer = time;
                break;
            }

            for(int nextDir = 0; nextDir < 4; nextDir++) {
                // 앞 칸이 비어 있으면 방향을 바꿀 수 없음
                if(dir != -1 && nextDir != dir && grid[r+dr[dir]][c+dc[dir]] == 0)
                    continue;

                int nr = r + dr[nextDir];
                int nc = c + dc[nextDir];

                if(grid[nr][nc] == 1 || visited[nr][nc][nextDir] == 1)
                    continue;

                visited[nr][nc][nextDir] = 1;
                q.offerLast(new int[]{nr, nc, time+1, nextDir});
            }
        }

        System.out.println(answer);
    }

    static class FastReader {
        private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer st;

        String next() throws IOException {
            while(st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
