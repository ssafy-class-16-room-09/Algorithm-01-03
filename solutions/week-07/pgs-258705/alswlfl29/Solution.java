class Solution {
    static final int MOD = 10007;
    
    public int solution(int n, int[] tops) {
        
        int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = dp[0] + 2 + tops[0];
        
        for(int i=2; i<=n; i++) {
            int square = (2 + tops[i-1]);
            dp[i] = (((dp[i-1] * (1 + square))%MOD - dp[i-2]) + MOD)%MOD;
        }
        
        return dp[n]%MOD;
    }
}