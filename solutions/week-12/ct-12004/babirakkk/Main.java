import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int numCoins = Integer.parseInt(st.nextToken());
        int changeAmount = Integer.parseInt(st.nextToken());

        int[] dp = new int[changeAmount + 1];
        for (int i = 0; i < numCoins; i++) {
            st = new StringTokenizer(br.readLine());
            boolean isLimited = (st.nextToken().charAt(0) == 'B') ? true : false; // true이면 B타입
            int amount = Integer.parseInt(st.nextToken());

            if (amount > changeAmount) continue; // 동전의 금액이 거스름돈보다 크면 고려하지 않음

            if (isLimited) { // 한정판 동전이라면 한 번만 쓸 수 있음
                for (int j = changeAmount; j > amount; j--) { // 중복 비허용 -> 뒤에서부터
                    if (dp[j - amount] != 0) { // 금액이 (j - coin[i])인 경우를 만들 수 없다면 j도 만들 수 없음
                        if (dp[j] == 0) {
                            dp[j] = dp[j - amount] + 1;
                        } else {
                            dp[j] = Math.min(dp[j], dp[j - amount] + 1);
                        }
                    }
                }
                dp[amount] = 1; // 0원에서 현재 동전을 사용하는 경우
            } else {
                dp[amount] = 1;
                for (int j = amount + 1; j <= changeAmount; j++) { // 중복 허용 -> 앞에서부터 
                    if (dp[j - amount] != 0) {
                        if (dp[j] == 0) {
                            dp[j] = dp[j - amount] + 1;
                        } else {
                            dp[j] = Math.min(dp[j], dp[j - amount] + 1);
                        }
                    }
                }
            }
        }
        System.out.println((dp[changeAmount] == 0) ? -1 : dp[changeAmount]);
    }
}

