import java.util.*;
/**
현재 위치 
dp[rIdx][cIdx] = max(dp[rIdx-1][cIdx], dp[rIdx-1][cIdx-1]) + graph[rIdx][cIdx]
*/
class Solution {
    public int solution(int[][] triangle) {
        int r = triangle.length;
        int c = triangle[r-1].length;
        int[][] dp = new int[r][c];
        dp[0][0] = triangle[0][0];
        
        for(int i=1; i<r; i++) {
            for(int j=0; j<i+1; j++) {
                if(j==0)
                    dp[i][j] = dp[i-1][j] + triangle[i][j];
                else if(j==i)
                    dp[i][j] = dp[i-1][j-1] + triangle[i][j];
                else
                    dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-1]) + triangle[i][j];
            }
        }
        
        return Arrays.stream(dp[r-1])
            .max()
            .orElse(-1);
    }
}