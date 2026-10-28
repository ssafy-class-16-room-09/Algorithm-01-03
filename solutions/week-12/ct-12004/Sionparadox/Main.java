import java.util.*;
import java.io.*;

class Coin {
    boolean type;
    int value;
    public Coin(char type, int value){
        this.type = type == 'A';
        this.value = value;
    }
}

public class Main {
    static final int MAX = 100_000;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        Coin[] coins = new Coin[N];
        for (int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            char c = st.nextToken().charAt(0);
            int p = Integer.parseInt(st.nextToken());
            coins[i] = new Coin(c, p);
        }
        int[] dp = new int[M+1];
        Arrays.fill(dp, MAX);
        dp[0] = 0;

        for (Coin coin:coins){
            int p = coin.value;
            if (coin.type){
                for (int i=p; i<=M; i++){
                    dp[i] = Math.min(dp[i], dp[i-p]+1);
                }
            } else {
                for (int i=M; i>=p; i--){
                    dp[i] = Math.min(dp[i], dp[i-p]+1);
                }
            }
        }
        System.out.println(dp[M] == MAX ? -1 : dp[M]);
    }
}