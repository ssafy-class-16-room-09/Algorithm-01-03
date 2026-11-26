import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String[] NM = br.readLine().split(" "); // 수 N개(<= 10^6), 부분 구간 합 M(<= 10^3)
      int N = Integer.parseInt(NM[0]);
      int M = Integer.parseInt(NM[1]);

      int[] numbers = new int[N+1];
      int n=0;
      StringTokenizer st = new StringTokenizer(br.readLine());
      
      while(st.hasMoreTokens()) {
        numbers[++n] = Integer.parseInt(st.nextToken());
      }
      
      long cnt = 0; // M으로 나눠지는 구간 개수
      long[] culmulativeSum = new long[N+1];
      long[] remainder = new long[M]; // 나머지 별 개수
      for(int i=1; i<=N; i++) {
        culmulativeSum[i] = culmulativeSum[i-1] + numbers[i];
        if(culmulativeSum[i] % M == 0) cnt++;
        remainder[(int) (culmulativeSum[i] % M)]++;
      }

      for(int m=0; m<M; m++) {
        cnt += (remainder[m] * (remainder[m]-1)) / 2;
      }

      System.out.println(cnt);
      br.close();
    }
}