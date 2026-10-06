import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int[][] dp;
    static int[] wall;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        dp = new int[N][1 << M];

        wall = new int[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int currMask = 0;
            for (int j = 0; j < M; j++) {
                if (Integer.parseInt(st.nextToken()) == 1) {
                    currMask |= (1 << (M - j - 1));
                }
            }
            wall[i] = currMask;
        }
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < (1 << M); j++) {
                if (i != 0) {
                    for (int k = 0; k < (1 << M); k++) {
                        if ((j & k) == 0) {
                            dp[i][j] = Math.max(dp[i][j], dp[i - 1][k]);
                        }
                    }
                }
                if (isEmpty(i, j) && isLeftRightNotSelected(j)) {
                    dp[i][j] += Integer.bitCount(j);;
                }
            }
        }

        int maxSelected = 0;
        for (int j = 0; j < (1 << M); j++) {
            maxSelected = Math.max(maxSelected, dp[N-1][j]);
        }
        System.out.println(maxSelected);
        br.close();
    }

    private static boolean isEmpty(int row, int curr) {
        return ((curr & wall[row]) == 0);
    }

    private static boolean isLeftRightNotSelected(int curr) {
        return ((curr & (curr << 1)) == 0);
    }
}