import java.util.*;

public class Main {

    static int n;
    static int t;
    static int tempProfit;
    static int[][] grid;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        t = sc.nextInt();
        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = sc.nextInt();
            }
        }

        int[][] maxProfit = new int[n][n];
        maxProfit[0][0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            maxProfit[0][j] = maxProfit[0][j - 1] + grid[0][j];
        }
        for (int i = 1; i < n; i++) {
            maxProfit[i][0] = maxProfit[i - 1][0] + grid[i][0];
            for (int j = 1; j < n; j++) {
                maxProfit[i][j] = Math.max(maxProfit[i - 1][j], maxProfit[i][j - 1]) + grid[i][j];
            }
        }

        int[][] reverseMaxProfit = new int[n][n];
        reverseMaxProfit[n - 1][n - 1] += grid[n - 1][n - 1];
        for (int j = n - 2; j >= 0; j--) {
            reverseMaxProfit[n - 1][j] = reverseMaxProfit[n - 1][j + 1] + grid[n - 1][j];
        }
        for (int i = n - 2; i >= 0; i--) {
            reverseMaxProfit[i][n - 1] = reverseMaxProfit[i + 1][n - 1] + grid[i][n - 1];
            for (int j = n - 2; j >= 0; j--) {
                reverseMaxProfit[i][j] = Math.max(reverseMaxProfit[i + 1][j], reverseMaxProfit[i][j + 1]) + grid[i][j];
            }
        }

        // 역행했을 때 얻을 수 있는 최대 이익
        int result = maxProfit[n - 1][n - 1]; // 역행을 하지 않았을 때 얻을 수 있는 최대 이익으로 초기화
        if (n != 1) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tempProfit = Integer.MIN_VALUE; // 초기화
                    dfs(i, j, 0, 0);
                    if (tempProfit == Integer.MIN_VALUE) continue; // 역행해서 현재 위치로 돌아올 수 없는 경우
                    result = Math.max(result, 
                        maxProfit[i][j] + reverseMaxProfit[i][j] + tempProfit);
                }
            }
        }

        System.out.println(result);
    }
    
    private static void dfs(int r, int c, int d, int profit) { // 역행 후 돌아온 위치부터 역행하기 전까지의 최대 이익
        if (d == t) {
            tempProfit = Math.max(tempProfit, profit);
            return;
        }

        if (r + 1 < n) {
            dfs(r + 1, c, d + 1, profit + grid[r + 1][c]);
        }
        if (c + 1 < n) {
            dfs(r, c + 1, d + 1, profit + grid[r][c + 1]);
        }
    }
}

