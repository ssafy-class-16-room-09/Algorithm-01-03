import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int T = Integer.parseInt(br.readLine()); // 테스트 케이스 수

      StringBuilder sb = new StringBuilder();
      for(int test_case=1; test_case<=T; test_case++) {
        int N = Integer.parseInt(br.readLine()); // 돌아가야 할 학생들의 수

        int[][] students = new int[N][2]; // 학생들의 방 정보
        StringTokenizer st;
        for(int n=0; n<N; n++) {
          st = new StringTokenizer(br.readLine());

          int currentRoom = Integer.parseInt(st.nextToken()); // 현재 방 번호
          int returnRoom = Integer.parseInt(st.nextToken()); // 돌아갈 방 번호

          // 방 번호가 작은 것을 0번 인덱스에 넣기
          // 이때, 각 방 번호의 복도 번호 표시
          if(currentRoom < returnRoom) {
            students[n][0] = (currentRoom - 1) / 2;
            students[n][1] = (returnRoom - 1) / 2;
          } else {
            students[n][0] = (returnRoom - 1) / 2;
            students[n][1] = (currentRoom - 1) / 2;
          }
        }

        Arrays.sort(students, (a, b) -> Integer.compare(a[0], b[0])); // 작은 번호 기준 오름차순 정렬

        int sameCnt = 1; // 현재까지 겹치는 구간 수
        int maxSame = 1; // 겹치는 구간의 최대값
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> a - b); // 큰 번호 기준 오름차순 정렬
        for(int s=0; s<N; s++) {
          while(!pq.isEmpty()) {
            int prev = pq.poll(); // 큰 번호들 중 가장 작은 수
            if(students[s][0] <= prev) { // 이동 동선 겹침
              sameCnt++;
              maxSame = Math.max(sameCnt, maxSame);
              pq.offer(prev);
              break;
            } else {
              sameCnt--;
            }
          }
          pq.offer(students[s][1]);
        }

        sb.append(String.format("#%d %d\n", test_case, maxSame));
      }

      System.out.print(sb);
      br.close();
    }
}

/*
return -> 모든 학생들이 이동 가능한 최소 시간

- 겹치는 구간 중 최대 리턴(겹치는 구간의 최대가 0개인 경우 1 리턴)
1. [작은 숫자, 큰 숫자] 작은 숫자 기준 오름차순 정렬
2. 이전 큰 숫자들과 현재 작은 숫자를 비교
- 이전 큰 숫자 < 현재 작은 숫자 -> 겹치지 않음
- 이전 큰 숫자 > 현재 작은 숫자 -> 겹침
3. 비교할 때마다 겹치는 구간 수 카운트하며, 최대값 갱신

** 놓친 부분: 방 구조가 아래와 같이 되어있음! 즉 1번 방과 2번 방은 같은 복도에 위치함
1 3 5
2 4 6
같은 복도에 위치한 방은 같은 번호로 취급하기 위해 (방 번호-1)/2를 복도 번호로 표시
*/