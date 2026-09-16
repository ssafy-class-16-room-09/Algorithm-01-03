import java.util.*;
import java.io.*;

public class Solution {

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine()); // 작업 가능한 개수

        StringTokenizer st;
        int[] dp = new int[N*5+2];
        for(int n=1; n<=N*5; n++) {
            dp[n] = Math.max(dp[n], dp[n-1]);
            if(n <= N) {
                st = new StringTokenizer(br.readLine());
                int t = Integer.parseInt(st.nextToken()); // 걸리는 시간
                int p = Integer.parseInt(st.nextToken()); // 수익
                dp[n+t] = Math.max(dp[n+t], dp[n] + p);
            }
            
        }

        System.out.println(dp[N+1]);
    }
}