import java.util.*;

/**
dp[i][0] 마무리가 사다리꼴일때
dp[i][1] 마무리가 평행사변형일때

점화식
dp[i][0]
if(tops[i]==1)
dp[i-1][0] * 3 + dp[i-1][1]

if(tops[i]==0)
dp[i-1][0] * 2 + dp[i-1][1]

dp[i][1]
if(tops[i]==1)
dp[i-1][0] * 2 + dp[i-1][1]

if(tops[i]==0)
dp[i-1][0] + dp[i-1][1]
*/
class Solution {
    static final int MOD = 10007;
    public int solution(int n, int[] tops) {
        int[][] dp = new int[n+1][2];
        
        // 0일때 설정
        dp[0][0] = 0;
        dp[0][1] = 0;
        
        // 1일때 설정
        if(tops[0] == 1) {
            dp[1][0] = 4;
            dp[1][1] = 3;
        }
        else {
            dp[1][0] = 3;
            dp[1][1] = 2;
        }
        
        for(int i=2; i<=n; i++) {
            if(tops[i-1] == 1) {
                dp[i][0] = (dp[i-1][0] * 3 + dp[i-1][1]) % MOD;
                dp[i][1] = (dp[i-1][0] * 2 + dp[i-1][1]) % MOD;
            }
            else {
                dp[i][0] = (dp[i-1][0] * 2 + dp[i-1][1]) % MOD;
                dp[i][1] = (dp[i-1][0] + dp[i-1][1]) % MOD;
            }
        }
        return dp[n][0];
    }
}