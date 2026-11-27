import java.util.*;
import java.io.*;

public class Main {
    static int[][] dp;
    static int[] arr;
    static int[] pSum;
    static int N;
    public static void main(String[] args) {
      FastReader fr = new FastReader();
      StringBuilder sb = new StringBuilder();

      int T = fr.nextInt();
      for(int i=0; i<T; i++) {
        N = fr.nextInt();
        dp = new int[N][N];
        arr = new int[N];
        pSum = new int[N];
        for(int j=0; j<N; j++)
          arr[j] = fr.nextInt();
        pSum[0] = arr[0];
        for(int j=1; j<N; j++)
          pSum[j] = pSum[j-1] + arr[j];
        
        calcScore(0, N-1);
        sb.append(dp[0][N-1]).append('\n');
      }
      System.out.print(sb.toString());
    }

    static int calcScore(int leftIdx, int rightIdx) {
      int sum = pSum[rightIdx] - pSum[leftIdx] + arr[leftIdx];

      if(leftIdx==rightIdx) {
        dp[leftIdx][rightIdx] = arr[leftIdx];
        return dp[leftIdx][rightIdx];
      }
      if(dp[leftIdx][rightIdx] != 0)
        return dp[leftIdx][rightIdx];
      
      int leftSelect = sum - calcScore(leftIdx+1, rightIdx);
      int rightSelect = sum - calcScore(leftIdx, rightIdx-1);
      dp[leftIdx][rightIdx] = Math.max(leftSelect, rightSelect);
      return dp[leftIdx][rightIdx];
    }

    static class FastReader {
      BufferedReader br;
      StringTokenizer st;
      
      public FastReader() {
        br = new BufferedReader(new InputStreamReader(System.in));
      }

      public String next() {
        while(st == null || !st.hasMoreTokens()) {
          try {
            st = new StringTokenizer(br.readLine());
          } catch(Exception e) {
            e.printStackTrace();
          }
        }
        return st.nextToken();
      }

      public int nextInt() {
        return Integer.parseInt(next());
      }
    }
}