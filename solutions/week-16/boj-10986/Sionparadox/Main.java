import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int M = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(br.readLine());
      int[] arr = new int[N];
      for (int i=0; i<N; i++) arr[i] = Integer.parseInt(st.nextToken());

      int cnt = arr[0]%M;
      int[] remains = new int[M];
      remains[cnt]++;

      for (int i=1; i<N; i++){
        cnt = (cnt+arr[i])%M;
        remains[cnt]++;  
      }
      
      long answer = remains[0];
      for (int i=0; i<M; i++){
        answer += (long)remains[i]*(remains[i]-1)/2;
      }
      
      System.out.println(answer);
    }
}