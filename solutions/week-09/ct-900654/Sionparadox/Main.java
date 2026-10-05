import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[] board = new int[N];
        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());
            int row = 0;

            for (int c = 0; c < M; c++) {
                int k = Integer.parseInt(st.nextToken());
                if (k == 1){
                    row |= (1<<c);
                }
            }
            board[r] = row;
        }
        
        int[][] dp = new int[N][1<<M];

        for (int r=0; r<N; r++){
            for (int mask=0; mask<(1<<M); mask++){
                if ((mask & (mask<<1)) != 0) continue;
                if ((mask & board[r]) != 0) continue;

                int bitCnt = 0;
                for (int i=0; i<M; i++){
                    if ((mask & (1<<i)) != 0) bitCnt++;
                }

                if (r == 0){
                    dp[r][mask] = bitCnt;
                    continue;
                }

                for (int prev=0; prev<(1<<M); prev++){
                    if ((mask&prev) != 0) continue;
                    dp[r][mask] = Math.max(dp[r][mask], dp[r-1][prev] + bitCnt);
                }
            }
        }
        
        int answer = 0;
        for (int mask=0; mask<(1<<M); mask++){
            answer = Math.max(answer, dp[N-1][mask]);
        }
        System.out.println(answer);
    }
}

/*
dp[r][mask]
각 행에서 가능한 mask만 선택
- 좌우 인접x
- 벽과 충돌x
열 겹침은 prev & mask

*/
