import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int T = Integer.parseInt(br.readLine()); // 테스트케이스(<=1,000)

      StringBuilder sb = new StringBuilder();
      for(int t=0; t<T; t++) {
        int M = Integer.parseInt(br.readLine()); // 수열의 크기(<=9,999)

        int row = M / 10 + 1; // 줄 수
        int[] numbers = new int[M];
        int n=0;
        StringTokenizer st;
        for(int r=0; r<row; r++) {
          st = new StringTokenizer(br.readLine());
          while(st.hasMoreTokens()) {
            numbers[n++] = Integer.parseInt(st.nextToken());
          }
        }
        
        int cnt = M / 2 + 1;
        int[] mid = new int[cnt];
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<M; i++) { // 9999 ~~ 10000
          list.add(numbers[i]);
          if(i % 2 == 0) { // 홀수 번째인경우(0부터니까 짝수)
            Collections.sort(list); // 5000 => 5000 * log 5000 = 5000 * 12.xx = 65,000
            mid[i/2] = list.get(i/2);
          }
        }

        sb.append(cnt);
        for(int m=0; m<cnt; m++) {
          if(m%10 == 0) sb.append("\n");
          sb.append(mid[m]).append(" ");
        }
        sb.append("\n");
      }
      System.out.print(sb);
      br.close();
    }
}