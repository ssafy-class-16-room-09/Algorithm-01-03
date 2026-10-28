import java.util.*;
import java.io.*;

public class Main {
    static final int NINF = -1_000_000_000;

    static int n;
    static int t;
    static int[][] grid;

    public static void main(String[] args) {
        FastReader fr = new FastReader();

        n = fr.nextInt();
        t = fr.nextInt();

        grid = new int[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                grid[i][j] = fr.nextInt();
            }
        }

        int[][][] dp = new int[n][n][2];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++)
                Arrays.fill(dp[i][j], NINF);
        }

        dp[0][0][0] = grid[0][0];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {

                // 1. 위에서 현재 칸으로 이동
                if(i > 0) {
                    for(int used = 0; used < 2; used++) {
                        if(dp[i - 1][j][used] == NINF)
                            continue;

                        dp[i][j][used] = Math.max(dp[i][j][used], dp[i - 1][j][used] + grid[i][j]);
                    }
                }

                // 2. 왼쪽에서 현재 칸으로 이동
                if(j > 0) {
                    for(int used = 0; used < 2; used++) {
                        if(dp[i][j - 1][used] == NINF)
                            continue;

                        dp[i][j][used] = Math.max(dp[i][j][used], dp[i][j - 1][used] + grid[i][j]);
                    }
                }

                // 3. 현재 칸에서 T번 이동 후 역행해서 복귀
                if(dp[i][j][0] != NINF) {
                    int gain = reverse(i, j, t);

                    if(gain != NINF)
                        dp[i][j][1] = Math.max(dp[i][j][1], dp[i][j][0] + gain + grid[i][j]);
                }
            }
        }

        System.out.println(
            Math.max(dp[n - 1][n - 1][0], dp[n - 1][n - 1][1])
        );
    }

    static int reverse(int r, int c, int remain) {
        if(r == n - 1 && c == n - 1)
            return NINF;

        if(remain == 0)
            return 0;

        int best = NINF;

        // 아래로 이동
        if(r + 1 < n) {
            int next = reverse(r + 1, c, remain - 1);

            if(next != NINF)
                best = Math.max(best, grid[r + 1][c] + next);
        }

        // 오른쪽으로 이동
        if(c + 1 < n) {
            int next = reverse(r, c + 1, remain - 1);

            if(next != NINF)
                best = Math.max(best, grid[r][c + 1] + next);
        }

        return best;
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}