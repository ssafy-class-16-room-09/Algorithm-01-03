import java.util.*;
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] board = new int[n+1]; // board의 각 행 비트로 표현
        for (int i = 1; i <= n; i++) {
            for (int j = m - 1; j >= 0; j--) {
                int num = sc.nextInt();
                board[i] += (num * (int) Math.pow(2, j));
            }
        }

        int[][] dp = new int[n+1][1 << m];
        for(int i=1; i<=n; i++) {
            for(int mask=0; mask < (1 << m); mask++) {
                for(int mask2=0; mask2 < (1 << m); mask2++) {
                    // 서로 인접해 있는 영역이 선택된 경우, 벽 부분이 선택된 경우, 상하로 인접해 있는 경우
                    if(((mask & (mask << 1)) != 0) || ((mask & board[i]) != 0) || ((mask & mask2) != 0)) {
                        dp[i][mask] = Math.max(dp[i][mask], dp[i-1][mask2]);
                        continue;
                    } else {
                        // 빈칸 수 갱신
                        dp[i][mask] = Math.max(dp[i][mask], dp[i-1][mask2] +  Integer.bitCount(mask));
                    }
                }
            }
        }

        int result = 0;
        for(int mask=0; mask<(1<<m); mask++) {
            result = Math.max(result, dp[n][mask]); // 최대 선택할 수 있는 빈칸 수 구하기
        }
        System.out.println(result);
    }
}
