import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i<n; i++) {
            arr[i] = sc.nextInt();
        }

        long[][] dp = new long[n][n];

        for (int gap = 2; gap < n; gap++) {
            for (int l = 0; l+gap < n; l++) {
                int r = l + gap;
                dp[l][r] = Long.MAX_VALUE;

                for (int k = l+1; k<r; k++) {
                    long cost = dp[l][k] + dp[k][r] + (long) arr[l] * arr[k] * arr[r];

                    dp[l][r] = Math.min(dp[l][r], cost);
                }
            }
        }

        System.out.println(dp[0][n - 1]);
    }
}