import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] dp = new int[N+1];
        int[][] works = new int[N][2];
        StringTokenizer st;
        for (int i=0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            works[i][0] = t;
            works[i][1] = p;
        }

        for (int i=N-1; i>=0; i--){
            int t = works[i][0];
            int p = works[i][1];

            int endDate = i+t;
            dp[i] = dp[i+1];
            if (endDate <= N) dp[i] = Math.max(dp[endDate]+p, dp[i]);
        }
        System.out.println(dp[0]);
    }
}
/*
dp[i] = i일차에서 시작했을 때 얻을 수 있는 최대값
*/
