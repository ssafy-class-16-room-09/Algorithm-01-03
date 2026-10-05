import java.util.*;
import java.io.*;

public class Main {
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    static final int MAX = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        int[][] grid = new int[N][N];
        for (int i = 0; i < N; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++){
                grid[i][j] = Integer.parseInt(st.nextToken());
            }       
        }
        int[][][] dp = new int[N][N][K+1];
        
        for (int k=1; k<K; k++){
            for (int r=0; r<N; r++){
                for (int c=0; c<N; c++){
                    dp[r][c][k] = MAX;
                    for (int d=0; d<4; d++){
                        int nr = r+dr[d], nc = c+dc[d];
                        if (nr<0 || nr>=N || nc<0 || nc>=N) continue;
                        if (grid[nr][nc] >= grid[r][c]) continue;
                        if (dp[nr][nc][k-1] == MAX) continue;
                        // 현재 칸보다 낮은 칸일 경우만 가능
                        int nDiff = Math.max(grid[r][c] - grid[nr][nc], dp[nr][nc][k-1]);
                        dp[r][c][k] = Math.min(dp[r][c][k], nDiff);
                    }
                }
            }
        }
        int answer = MAX;
        for (int r=0; r<N; r++){
            for (int c=0; c<N; c++){
                answer = Math.min(dp[r][c][K-1], answer);
            }
        }
        System.out.println(answer != MAX ? answer : -1);
    }
}

/*
dp[pos][dist]
dist번 움직여서 pos에 도착할 떄의 최대 높이 차
0<=pos<=10000
0<=dist<=100
dp[pos][0] = 0
dp[pos][k] = Math.min(dp[pos-d][k-1] + 두 칸의 높이 차 (d 4방향))

k가 이동 수가 아니라 칸 수였음.
*/
