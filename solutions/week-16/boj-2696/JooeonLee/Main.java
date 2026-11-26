import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
      PriorityQueue<Integer> lowPq;
      PriorityQueue<Integer> highPq;
      
      FastReader fr = new FastReader();
      StringBuilder sb = new StringBuilder();
      int T = fr.nextInt();
      for(int i=0; i<T; i++) {
        int M = fr.nextInt();
        sb.append(M - M/2).append('\n');
        // 최대힙
        lowPq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        // 최소힙
        highPq = new PriorityQueue<>();

        // 10개마다 출력 변경
        int cnt = 0;

        for(int j=0; j<M; j++) {
          int curr = fr.nextInt();

          if(lowPq.isEmpty())
            lowPq.offer(curr);
          else if(lowPq.peek() >= curr)
            lowPq.offer(curr);
          else
            highPq.offer(curr);

          if(lowPq.size() > highPq.size()+1)
            highPq.offer(lowPq.poll());
          else if(highPq.size() > lowPq.size())
            lowPq.offer(highPq.poll());

          if(j%2==0) {
            sb.append(lowPq.peek()).append(' ');
            cnt++;
            if(cnt % 10 == 0)
              sb.append('\n');
          }
        }
        sb.append('\n');
      }
      System.out.print(sb.toString());
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