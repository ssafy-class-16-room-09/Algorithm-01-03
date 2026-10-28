import java.util.*;
public class Main {
    public static void main(String[] args) {
        int MAX = Integer.MAX_VALUE;

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); // 동전 종류(가중치는 전부 다름)
        int m = sc.nextInt(); // 거스름돈
        char[] cType = new char[n];
        int[] coin = new int[n];
        for (int i = 0; i < n; i++) {
            cType[i] = sc.next().charAt(0);
            coin[i] = sc.nextInt();
        }

        int[] dp = new int[m+1];
        Arrays.fill(dp, MAX);
        dp[0] = 0;

        for(int c=0; c<n; c++) {
            if(cType[c] == 'A') { // 무제한 사용 가능 -> 작은 동전으로 채울 수 있는 부분 채우기
                for(int i=coin[c]; i<=m; i++) {
                    int num = dp[i-coin[c]] == MAX ? MAX : dp[i-coin[c]] + 1;
                    dp[i] = Math.min(dp[i], num);
                }
            } else { // 오직 한 번만 사용 가능 -> 그리디 적용
                for(int i=m; i>=coin[c]; i--) {
                    int num = dp[i-coin[c]] == MAX ? MAX : dp[i-coin[c]] + 1;
                    dp[i] = Math.min(dp[i], num);
                }
            }
        }
        System.out.println(dp[m] == MAX ? -1 : dp[m]);
    }
}