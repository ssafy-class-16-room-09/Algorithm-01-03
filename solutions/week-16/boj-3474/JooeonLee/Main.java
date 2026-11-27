import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
      FastReader fr = new FastReader();
      StringBuilder sb = new StringBuilder();

      int T = fr.nextInt();
      for(int i=0; i<T; i++) {
        int N = fr.nextInt();
        int twoCnt = 0;
        int twos = 2;
        while(twos<=N) {
          twoCnt += N/twos;
          twos *= 2;
        }

        int fiveCnt = 0;
        int fives = 5;
        while(fives<=N) {
          fiveCnt += N/fives;
          fives *= 5;
        }

        sb.append(Math.min(twoCnt, fiveCnt)).append('\n');
      }
      System.out.println(sb.toString());
    }

    static class FastReader {
      BufferedReader br;
      StringTokenizer st;

      public FastReader() {
        br = new BufferedReader(new InputStreamReader(System.in));
      }

      public String next() {
        while(st==null || !st.hasMoreTokens()) {
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