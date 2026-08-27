import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
      Scanner sc = new Scanner(System.in);

      int testCases = sc.nextInt();
      for (int t = 1; t <= testCases; t++) {
        int n = sc.nextInt();
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
          scores[i] = sc.nextInt();
        }

        boolean[] availableScores = new boolean[100 * n + 1]; // 가능한 최대 점수를 임의로 설정 (100점 * 문제 수)
        availableScores[0] = true;
        int currMaxScore = 0;
        for (int score : scores) {
          for (int i = currMaxScore; i >= 0; i--) { // currMaxScore부터 탐색
            if (availableScores[i]) { // 만약 n점이 가능하다면 n점과 n+score점 모두 가능한 점수가 됨
              availableScores[i + score] = true;
            }
          }
          currMaxScore += score;
        }

        int count = 0;
        for (int i = 0; i <= currMaxScore; i++) {
          if (availableScores[i]) {
            count++;
          }
        }

        System.out.println("#" + t + " " + count); // 적은 테스트케이스에 대해서는 BufferedWriter보다 System.out이 더 빠르다. 씽크빅!
      }
    }
}
