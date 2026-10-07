import java.util.*;
import java.io.*;

public class Main {
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0 , -1, 1};
    static int[][] dp;
    static int[][] grid;
    static int N, K;

    public static void main(String[] args) {
        FastReader fr = new FastReader();

        N = fr.nextInt();
        K = fr.nextInt();
        int maxH = 0;
        grid = new int[N][N];
        dp = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = fr.nextInt();
                maxH = Math.max(maxH, grid[i][j]);
            }
        }

        int answer = paramSearch(maxH);
        System.out.println(answer);
    }

    static int paramSearch(int maxH) {
        int left = 0;
        int right = maxH;

        int answer = -1;
        while(left <= right) {
            int mid = left + (right-left)/2;

            if(isPossible(mid)) {
                answer = mid;
                right = mid-1;
            } else {
                left = mid+1;
            }
        }

        return answer;
    }

    static boolean isPossible(int limit) {
        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                dp[i][j] = -1;
            }
        }

        for(int i=0; i<N; i++) {
            for(int j=0; j<N; j++) {
                if(dfs(i, j, limit) >= K)
                    return true; 
            }
        }
        return false;
    }

    static int dfs(int r, int c, int limit) {
        if(dp[r][c] != -1)
            return dp[r][c];
        
        dp[r][c] = 1;
        for(int i=0; i<4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];

            if(nr<0 || nr>=N || nc<0 || nc>=N)
                continue;

            // 이 조건 빼먹은 건가...
            if(grid[nr][nc] <= grid[r][c])
                continue;
            
            int diff = grid[nr][nc] - grid[r][c];
            if(diff > limit)
                continue;
            
            dp[r][c] = Math.max(dp[r][c], dfs(nr, nc, limit)+1);
        }
        return dp[r][c];
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while(st==null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
