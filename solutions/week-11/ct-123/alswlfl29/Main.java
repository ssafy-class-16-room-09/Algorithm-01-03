import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        
        long[][] dp = new long[n][n];

        for(int j=2; j<n; j++) {
            for(int i=0; i<n; i++) {
                if(i+j >= n) continue;
                dp[i][i+j] = Integer.MAX_VALUE;
                for(int k=i+1; k<i+j; k++) {
                    long c = dp[i][k] + dp[k][i+j] + (long) arr[i] * arr[k] * arr[i+j];
                    dp[i][i+j] = Math.min(dp[i][i+j], c);
                }
            }
        }
        System.out.println(dp[0][n-1]);
    }
}