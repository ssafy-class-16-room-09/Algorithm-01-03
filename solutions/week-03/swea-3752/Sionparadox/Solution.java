import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int TC = Integer.parseInt(br.readLine());
      for (int tc=1; tc<=TC; tc++){
        int N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        int[] scores = new int[N];
        int total = 0;
        for (int i=0; i<N; i++){
          scores[i] = Integer.parseInt(st.nextToken());
          total += scores[i];
        }

        Arrays.sort(scores);

        boolean[] dp = new boolean[total+1];
        dp[0] = true;
        int answer = 1;
        int cnt = 0;
        for (int score:scores){
          cnt += score;
          for (int i=cnt; i>=score;i--){
            if (dp[i-score] && !dp[i]){
              dp[i] = true;
              answer++;
            }
          }
        }

        System.out.printf("#%d %d\n",tc, answer);
      }
    }
}
/*
boolean dp? L = total_score
뒤에서부터 갱신
*/