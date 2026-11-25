import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st;
      StringBuilder sb = new StringBuilder();
      int T = Integer.parseInt(br.readLine());

      for (int t=0; t<T; t++){
        int N = Integer.parseInt(br.readLine());

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        int[] answer = new int[N/2+1];
        int cnt = 0;

        for (int i=0; i<=N/10; i++){
          st = new StringTokenizer(br.readLine());
          while(st.hasMoreTokens()){
            int k = Integer.parseInt(st.nextToken());
            if(minHeap.size() <= maxHeap.size()){
              minHeap.offer(k);
            } else {
              maxHeap.offer(k);
            }

            if(!maxHeap.isEmpty() && minHeap.peek() < maxHeap.peek()){
              int a = minHeap.poll();
              int b = maxHeap.poll();
              minHeap.offer(b);
              maxHeap.offer(a);
            }

            if(cnt%2 == 0){
              answer[cnt/2] = minHeap.peek();
            }
            cnt++;
          }
        }
        sb.append(N/2+1).append("\n");
        for(int i=0; i<=N/2; i++){
          sb.append(answer[i]).append(" ");
          if(i%10 == 9) sb.append("\n");
        }
        sb.append("\n");
      }
      System.out.println(sb);
    }
}

/*
최소힙, 최대힙 사용?
최소힙 < 중앙값 초과의 수만 저장
최대힙 < 중앙값 이하의 수만 저장

하나씩 번갈아가면서 넣고
비교해서 바꾸기

*/
