import java.util.*;
import java.io.*;
 
public class Solution {
    public static void main(String[] args) {
      FastReader fr = new FastReader();
      int T = fr.nextInt();
      StringBuilder sb = new StringBuilder();
 
      int[] cnt = new int[200];
      for(int testCase = 1; testCase<=T; testCase++) {
        Arrays.fill(cnt, 0);
        int N = fr.nextInt();
         
        for(int i=0; i<N; i++) {
          int startIdx = fr.nextInt();
          int endIdx = fr.nextInt();
 
          int start = (Math.min(startIdx, endIdx) - 1) / 2;
          int end = (Math.max(startIdx, endIdx) - 1) / 2;
 
          for(int j=start; j<=end; j++)
            cnt[j]++;
        }
 
        int answer = Arrays.stream(cnt)
          .max()
          .orElse(-1);
        sb.append("#").append(testCase).append(" ").append(answer).append('\n');
      }
 
      System.out.print(sb.toString());
    }
 
    static class FastReader {
      BufferedReader br;
      StringTokenizer st;
 
      public FastReader() {
        br = new BufferedReader(new InputStreamReader(System.in));
      }
 
      String next() {
        while(st == null || !st.hasMoreElements()) {
          try {
            st = new StringTokenizer(br.readLine());
          } catch(IOException e) {
            e.printStackTrace();
          }
        }
        return st.nextToken();
      }
 
      int nextInt() {
        return Integer.parseInt(next());
      }
    }
}