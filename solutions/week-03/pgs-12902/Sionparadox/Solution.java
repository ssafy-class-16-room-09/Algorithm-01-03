class Solution {
    final int MOD = 1_000_000_007;
    
    public int solution(int n) {
        if (n % 2 == 1) return 0;

        long[] dp = new long[n+1];
        dp[0] = 1;
        dp[2] = 3;

        for (int i=4; i<=n; i+=2) {
            dp[i] = (4*dp[i-2] - dp[i-4] + MOD)%MOD;
        }

        return (int) dp[n];
    }  
}
/*
dp[0] = 1
dp[2] = 3
dp[4] = 11

6을 만들기 위해
dp[4] * 3 (2짜리)
dp[2] * 2 (4짜리 상하대칭)
dp[0] * 2 (6짜리 상하대칭)

dp[n] = dp[n-2]*3 + dp[n-4]*2 + dp[n-6]*2 + dp[n-8]*2...
dp[n-2] = dp[n-4]*3 + dp[n-6]*2 + dp[n-8]*2 + ...
dp[n-2] - dp[n-4]*3 = dp[n-6]*2 + dp[n-8]*2 + ...
dp[n] = dp[n-2]*3 + dp[n-4]*2 + (dp[n-2] - dp[n-4]*3)
     = dp[n-2]*4 - dp[n-4]
*/