import java.util.*;
import java.io.*;

public class Main {

    private static final int[] dr = { -1, 1, 0, 0 };
    private static final int[] dc = { 0, 0, -1, 1 };

    private static int mountainSize;
    private static int minCourseLength;
    private static int[][] mountain;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        mountainSize = Integer.parseInt(st.nextToken());
        minCourseLength = Integer.parseInt(st.nextToken());
        
        int[][][] dp = new int[mountainSize][mountainSize][minCourseLength];

        mountain = new int[mountainSize][mountainSize];
        for (int i = 0; i < mountainSize; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < mountainSize; j++) {
                mountain[i][j] = Integer.parseInt(st.nextToken());
                Arrays.fill(dp[i][j], Integer.MAX_VALUE);
                dp[i][j][0] = 0;
            }
        }

        int minHeightDiff = Integer.MAX_VALUE;
        for (int courseLength = 1; courseLength < minCourseLength; courseLength++) {
            for (int i = 0; i < mountainSize; i++) {
                for (int j = 0; j < mountainSize; j++) {
                    for (int d = 0; d < 4; d++) {
                        int prevR = i + dr[d]; // 현재 위치에 오기 이전에 있었던 위치
                        int prevC = j + dc[d];

                        if (isNotValid(prevR, prevC)) continue;

                        
                        if (mountain[i][j] > mountain[prevR][prevC]) { // 이전보다 현재 위치가 높을 때만 이동 가능
                            int heightDiff = Math.max(dp[prevR][prevC][courseLength - 1], (mountain[i][j] - mountain[prevR][prevC])); // 이전에 저장된 높이차와 현재 높이차 비교
                            dp[i][j][courseLength] = Math.min(dp[i][j][courseLength], heightDiff); // courseLength번 이동해서 현재 위치에 도달할 수 있는 경로 중 가장 높이차가 작은 값을 저장
                        }
                    }
                    if (courseLength == minCourseLength - 1) { // 최소 코스 길이에 도달하면 비교해서 가장 작은 높이차를 저장
                        minHeightDiff = Math.min(minHeightDiff, dp[i][j][courseLength]);
                    }
                }
            }
        }

        bw.write(((minHeightDiff != Integer.MAX_VALUE) ? minHeightDiff : -1) + "\n");
        bw.flush();
        bw.close();
        br.close();    
    }

    private static boolean isNotValid(int r, int c) {
        return !(r >= 0 && r < mountainSize && c >= 0 && c < mountainSize);
    }
}