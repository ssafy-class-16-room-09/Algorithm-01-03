import java.util.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int T = Integer.parseInt(br.readLine()); // 테스트 케이스

    StringBuilder sb = new StringBuilder();
    for(int t=0; t<T; t++) {
      int N = Integer.parseInt(br.readLine()); // 정수 N
      
      int two = 0;
      int five = 0;

      for(int i=2; i<=N; i*=2) two += (N/i);
      for(int i=5; i<=N; i*=5) five += (N/i);

      sb.append(Math.min(two, five)).append("\n");
    }
    System.out.print(sb);
    br.close();
  }
}