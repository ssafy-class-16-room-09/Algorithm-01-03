import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int testCases = Integer.parseInt(br.readLine());

      StringTokenizer st;
      for (int t = 1; t <= testCases; t++) {
        st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[] parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
          parent[i] = i; // 부모는 자기 자신으로 초기화
        }
        
        StringBuilder answer = new StringBuilder();
        while (m-- > 0) {
          st = new StringTokenizer(br.readLine());
          int task = Integer.parseInt(st.nextToken());
          int a = Integer.parseInt(st.nextToken());
          int b = Integer.parseInt(st.nextToken());

          if (task == 0) {
            union(a, b, parent);
          } else if (task == 1) {
            answer.append(find(a, parent) == find(b, parent) ? 1 : 0);
          }
        }

        System.out.println("#" + t + " " + answer);
      }
    }

    private static int find(int a, int[] parent) {
      if (parent[a] == a) return a;

      return parent[a] = find(parent[a], parent); // 경로 압축
    }
    
    private static void union(int a, int b, int[] parent) {
      int rootA = find(a, parent);
      int rootB = find(b, parent);

      if (rootA != rootB) {
        parent[rootB] = rootA; // 서로 다른 집합이면 한쪽 루트를 다른 루트에 연결
      }
    }
}