import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        int[] arr = new int[N];
        for (int i=0; i<N; i++) arr[i] = Integer.parseInt(st.nextToken());

        int[][] dp = new int[N][N];

        for (int len=2; len<N; len++){
            for (int i=0; i<N-len; i++){
                int j = i+len;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k=i+1; k<j; k++){
                    int tmp = dp[i][k] + dp[k][j] + arr[i]*arr[k]*arr[j];
                    dp[i][j] = Math.min(dp[i][j], tmp);
                }
            }
        }
        System.out.println(dp[0][N-1]);
    }
}

/*

dp[i][j] : i~j까지 남기기 위한 최소비용

dp[i][j] : dp[i][k] + dp[k][j] + arr[i]*arr[k]*arr[j]
100*100*100*500
500,000,000
*/