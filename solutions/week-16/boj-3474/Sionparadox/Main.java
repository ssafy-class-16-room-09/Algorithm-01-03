import java.util.*;
import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringBuilder sb = new StringBuilder();
      int T = Integer.parseInt(br.readLine());
      for (int t=0; t<T; t++){
        int N = Integer.parseInt(br.readLine());
        int answer = 0;
        for (int i=5; i<=N; i*=5){
          answer += N/i;
        }
        sb.append(answer).append("\n");
      }
      System.out.println(sb);
    }
}

/*
10억이라 O(1)로 해야함
2는 어차피 많음
5, 5^2, 5^3...이 얼마나 있냐가 중요


*/