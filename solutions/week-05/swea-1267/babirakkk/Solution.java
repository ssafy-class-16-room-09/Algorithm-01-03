import java.util.*;
import java.io.*;

public class Solution {

  static final int TEST_CASES = 10;

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st;
    for (int t = 1; t <= TEST_CASES; t++) {
      st = new StringTokenizer(br.readLine());
      final int V = Integer.parseInt(st.nextToken());
      final int E = Integer.parseInt(st.nextToken());

      int[] indegree = new int[V + 1]; // 각 정점의 진입차수
      ArrayList<Integer>[] graph = new ArrayList[V + 1]; // 각 정점에서 이어지는 다음 정점들
      for (int i = 1; i <= V; i++) {
        graph[i] = new ArrayList<>();
      }
      st = new StringTokenizer(br.readLine());
      for (int i = 0; i < E; i++) {
        int pre = Integer.parseInt(st.nextToken());
        int post = Integer.parseInt(st.nextToken());
        graph[pre].add(post);
        indegree[post]++; // post로 들어오는 간선 추가
      }

      Queue<Integer> q = new ArrayDeque<>();
      for (int i = 1; i <= V; i++) {
        if (indegree[i] == 0) {
          q.add(i); // 선행 작업이 없는 정점부터 시작
        }
      }

      StringBuilder order = new StringBuilder();
      while (!q.isEmpty()) {
        int currTask = q.poll();
        order.append(currTask).append(" ");
        for (int post : graph[currTask]) {
          if (--indegree[post] == 0) { // 모든 선행 작업이 처리된 경우
            q.add(post);
          }
        }
      }

      System.out.println("#" + t + " " + order);
    }
  }
}