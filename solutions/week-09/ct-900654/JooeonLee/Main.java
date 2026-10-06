import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int[][] map;

    static int[] matched;
    static boolean[] visited;

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) {
        FastReader fr = new FastReader();

        N = fr.nextInt();
        M = fr.nextInt();

        map = new int[N][M];

        int emptyCnt = 0;

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                map[r][c] = fr.nextInt();

                if (map[r][c] == 0) {
                    emptyCnt++;
                }
            }
        }

        matched = new int[N * M];
        Arrays.fill(matched, -1);

        int matchingCnt = 0;

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {

                if (map[r][c] == 1)
                    continue;

                if ((r + c) % 2 != 0)
                    continue;

                visited = new boolean[N * M];

                int curr = getIndex(r, c);

                if (dfs(curr)) {
                    matchingCnt++;
                }
            }
        }

        System.out.println(emptyCnt - matchingCnt);
    }

    static boolean dfs(int curr) {

        int r = curr / M;
        int c = curr % M;

        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d];
            int nc = c + dc[d];

            if (!isRange(nr, nc))
                continue;

            if (map[nr][nc] == 1)
                continue;

            int next = getIndex(nr, nc);

            if (visited[next])
                continue;

            visited[next] = true;

            if (matched[next] == -1 || dfs(matched[next])) {
                matched[next] = curr;
                return true;
            }
        }

        return false;
    }

    static int getIndex(int r, int c) {
        return r * M + c;
    }

    static boolean isRange(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < M;
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}