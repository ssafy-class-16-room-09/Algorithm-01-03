import java.util.*;
import java.io.*;

public class Main {

    static int n;
    static int[][] cost; // (i, j) 구간을 합칠 때의 최소 비용
    static int[] nums;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        cost = new int[n][n];

        StringTokenizer st = new StringTokenizer(br.readLine());
        nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
            Arrays.fill(cost[i], -1);
        }

        System.out.println(dp(0, n - 1));
    }

    private static int dp(int s, int e) {
        if (cost[s][e] != -1) return cost[s][e];
        if (s + 1 == e) return cost[s][e] = 0; // 원소가 두 개만 남았으면 더 이상 합칠 수 없음

        int minCost = Integer.MAX_VALUE;
        for (int k = s + 1; k < e; k++) {
            int currCost = dp(s, k) + dp(k, e) + nums[s] * nums[k] * nums[e];
            minCost = Math.min(minCost, currCost);
        }
        return cost[s][e] = minCost;
    }
}
