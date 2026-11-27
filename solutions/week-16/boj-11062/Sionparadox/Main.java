import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st;

      int T = Integer.parseInt(br.readLine());
      for(int t=0; t<T; t++){
        int N = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());

        int[] card = new int[N];
        int[][] dp = new int[N][N];
        int[] prefix = new int[N+1];

        for (int i=0; i<N; i++){
          card[i] = Integer.parseInt(st.nextToken());
          dp[i][i] = card[i];
          prefix[i+1] = prefix[i]+card[i];
        }

        for (int l=1; l<N; l++){
          for (int i=0; i<N-l; i++){
            int j = i+l;
            dp[i][j] = Math.max(card[i] + (prefix[j+1]-prefix[i+1]-dp[i+1][j]),
            card[j] + (prefix[j]-prefix[i]-dp[i][j-1]));

          }
        }
        System.out.println(dp[0][N-1]);
      }
    }
}

/*
1 2 5 2

k 1 m 2
k 5 m 2
no  greedy

1 9 4 6 3 1

i~j 범위로 얻는 최대 점수
dp[i][j]

1 2 5 2
dp[2][2] = 5
dp[0][1] = 2
dp[1][2] = 5
dp[2][3] = 5
dp[0][2] = 6
dp[1][3] = 4
dp[0][3] = 6

0 1 3 8 10
dp[0][3] = dp[0][0] + (9-dp[1][3]), dp[3][3] + (8-dp[0][2])
         = 1+5, 2+2
         = 6


*/