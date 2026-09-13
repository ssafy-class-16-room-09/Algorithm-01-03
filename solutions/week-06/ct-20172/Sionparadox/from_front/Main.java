import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] dp = new int[N+1];
        int[][] works = new int[N+1][2];
        StringTokenizer st;
        for (int i=1; i<=N; i++){
            dp[i] = Math.max(dp[i], dp[i-1]);
            st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());

            int endDate = i+t-1;
            if (endDate > N) continue;
            dp[endDate] = Math.max(dp[endDate], dp[i-1]+p);
        }
        System.out.println(dp[N]);
    }
}
/*
dp[i] = i일차까지 얻을 수 있는 최대값


*/
