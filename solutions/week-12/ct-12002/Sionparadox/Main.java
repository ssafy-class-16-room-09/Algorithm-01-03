import java.util.*;
import java.io.*;

public class Main {
    static final int MIN = -100_000_000;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int T = Integer.parseInt(st.nextToken());
        int[][] grid = new int[N][N];
        for (int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j=0; j<N; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int[][] front = new int[N][N];
        for (int r=0; r<N; r++){
            for (int c=0; c<N; c++){
                front[r][c] = MIN;
                if (r == 0 && c == 0) front[r][c] = grid[r][c];
                if (r != 0) front[r][c] = Math.max(front[r][c], front[r-1][c]+grid[r][c]);
                if (c != 0) front[r][c] = Math.max(front[r][c], front[r][c-1]+grid[r][c]);
            }
        }

        int[][] back = new int[N][N];
        for (int[] row:back) Arrays.fill(row, MIN);

        for (int r=N-1; r>=0; r--){
            for (int c=N-1; c>=0; c--){
                if(r==N-1 && c==N-1) back[r][c] = grid[r][c];
                if(r != N-1) back[r][c] = Math.max(back[r][c], back[r+1][c]+grid[r][c]);
                if(c != N-1) back[r][c] = Math.max(back[r][c], back[r][c+1]+grid[r][c]);
            }
        }

        int answer = front[N-1][N-1];

        int[][][] dp = new int[N][N][T+1];
        for (int r=0; r<N; r++){
            for (int c=0; c<N; c++){
                dp[r][c][0] = grid[r][c];
            }
        }


        for (int t=1; t<=T; t++){
            for (int r=0; r<N; r++){
                for (int c=0; c<N; c++){
                    dp[r][c][t] = MIN;
                    if(r==N-1 && c==N-1) continue;
                    if(r!=N-1) dp[r][c][t] = Math.max(dp[r][c][t], dp[r+1][c][t-1]+dp[r][c][0]);
                    if(c!=N-1) dp[r][c][t] = Math.max(dp[r][c][t], dp[r][c+1][t-1]+dp[r][c][0]);
                }
            }
        }

        for (int r=0; r<N; r++){
            for (int c=0; c<N; c++){
                if(dp[r][c][T] == MIN) continue;
                answer = Math.max(answer, front[r][c] + back[r][c] + dp[r][c][T] - grid[r][c]);
            }
        }
        System.out.println(answer);
    }
}

/*
최대 1번 : 안써도 된다는 뜻
N <= 1000
T <= 5

dp[r][c][0]

dp[r][c][1] = dp[r][c][0] + Math.max(dp[r+1][c][0], dp[r][c+1][0])

dp[r][c][2] = dp[r][c][0] + Math.max(dp[r+1][c][1], dp[r][c+1][1])

back으로 그 칸에서 타임머신 돌아왔을 때 최대값 찾기

dp[r][c] : 0,0 부터 r,c까지 얻은 점수
dist[r][c] : r,c 부터 N-1까지 얻은 점수
back[r][c][T] : r,c칸에서 역행해서 얻는 최대 점수

*/

