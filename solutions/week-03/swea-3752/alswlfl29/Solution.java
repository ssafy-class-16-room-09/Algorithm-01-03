import java.util.*;
import java.io.*;
 
class Solution
{
    public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int T = Integer.parseInt(br.readLine()); // 테스트케이스 개수
 
      StringBuilder sb = new StringBuilder();
      for(int test_case=1; test_case<=T; test_case++) {
        int N = Integer.parseInt(br.readLine()); // 문제 수 N
        int[] problems = new int[N]; // 문제 배점
        StringTokenizer st = new StringTokenizer(br.readLine());
        int p = 0;
        while(st.hasMoreTokens()) {
          problems[p++] = Integer.parseInt(st.nextToken());
        }
 
        Arrays.sort(problems); // 오름차순 정렬
         
        Set<Integer> scores = new HashSet<>(); // 가능한 시험점수
        scores.add(0); // 아무 문제도 못 맞춘 경우
        for(int problem : problems) {
          List<Integer> correct = new ArrayList<>(); // 맞게 처리
          for(int score : scores) {
            correct.add(score + problem); // 점수 더하기
          }
 
          for(int c : correct) {
            scores.add(c);
          }
        }
 
        sb.append(String.format("#%d %d\n", test_case, scores.size()));
      }
      System.out.print(sb);
      br.close();
    }
}