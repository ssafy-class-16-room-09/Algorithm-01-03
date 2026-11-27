import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      int N = sc.nextInt();
      int M = sc.nextInt();
      long[] pSum = new long[N];
      long[] mod = new long[M];
      pSum[0] = sc.nextLong();
      mod[(int)(pSum[0]%M)]++;
      for(int i=1; i<N; i++) {
        pSum[i] = pSum[i-1] + sc.nextLong();
        mod[(int)(pSum[i]%M)]++;
      }
      long answer = 0;
      for(int i=0; i<M; i++) {
        if(mod[i] == 0)
          continue;
        else
          answer += (mod[i] * (mod[i]-1))/2;
      }
      answer += mod[0];
      System.out.println(answer);
    }
}