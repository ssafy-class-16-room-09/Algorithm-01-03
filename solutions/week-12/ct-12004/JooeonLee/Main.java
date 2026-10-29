
import java.util.*;
import java.io.*;

public class Main {
    static final int INF = 1_000_000_000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        int[] dp = new int[M+1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());

            String type = st.nextToken();
            int coin = Integer.parseInt(st.nextToken());

            if(type.equals("A")) {
                for(int j=coin; j<=M; j++) {
                    if(dp[j - coin] != INF) {
                        dp[j] = Math.min(dp[j], dp[j - coin] + 1);
                    }
                }
            }
            else {
                for(int j=M; j>=coin; j--) {
                    if(dp[j - coin] != INF) {
                        dp[j] = Math.min(dp[j], dp[j - coin] + 1);
                    }
                }
            }
        }
        System.out.println(dp[M] == INF ? -1 : dp[M]);
    }
}
