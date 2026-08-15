import java.util.*;

class Solution {
	int[][] dp;

	public int solution(int[][] triangle) {
		dp = new int[triangle.length][triangle.length];
		for (int[] arr : dp) {
			Arrays.fill(arr, -1);
		}
		return dp(0, 0, triangle);
	}

	private int dp(int i, int j, int[][] tri) {
		if (i == tri.length) { // i가 삼각형의 길이를 벗어나면 0 반환
			return 0;
		}
		
		if (dp[i][j] == -1) {
			dp[i][j] = tri[i][j] + Math.max(dp(i+1, j, tri), dp(i+1, j+1, tri));
		}
		return dp[i][j];
	}
}