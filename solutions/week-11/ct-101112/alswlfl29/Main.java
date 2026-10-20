import java.util.*;

public class Main {

    static int[][] ranks; // 사람별 등수

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ranks = new int[4][n];
        int[][][] scores = new int[3][n][2];
        int[][] total = new int[n][2];
        for (int contest = 0; contest < 3; contest++) {
            for (int i = 0; i < n; i++) {
                int score = sc.nextInt();
                scores[contest][i][0] = i;
                scores[contest][i][1] = score;
                total[i][0] = i;
                total[i][1] += score;
            }
            setRank(contest, scores[contest]);
        }
        setRank(3, total); // 최종 등수 구하기
        
        StringBuilder sb = new StringBuilder();
        for(int c=0; c<4; c++) {
            for(int p=0; p<n; p++) {
                sb.append(ranks[c][p]).append(" ");
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }

    // 대회 별 등수 매기기
    private static void setRank(int contest, int[][] scores) {
        Arrays.sort(scores, (s1, s2) -> s1[1]-s2[1]); // 점수 기준 오름차순 정렬

        for(int p=scores.length-1; p>=0; p--) {
            int i = scores[p][0]; // 인덱스
            int s = scores[p][1]; // 점수

            if(p == scores.length-1) ranks[contest][i] = 1; // 가장 높은 점수를 가진 사람
            else if(scores[p+1][1] == s) {
                ranks[contest][i] = ranks[contest][scores[p+1][0]]; // 이전 사람과 등수가 같은 경우
            } else {
                ranks[contest][i] = scores.length - p;
            }
        }
    }
}