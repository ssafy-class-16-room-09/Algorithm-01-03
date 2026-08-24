class Solution {
    final static int MOD = 1_000_000_007;
    public int solution(int n) {
        if(n%2 != 0) return 0;
        
        long[] dp = new long[n+1];
        dp[0] = 1;
        dp[2] = 3; // n=2
        
        long prefixSum = dp[0]; // 이전 값들의 누적 합
        for(int i=4; i<=n; i+=2) {
            dp[i] = (dp[i-2]%MOD*3+ prefixSum*2%MOD)%MOD;
            prefixSum = (prefixSum+dp[i-2])%MOD;
        }

        return (int) dp[n]%MOD;
    }
}

/*
n=2
=> 3가지
n=4
=> 3x3(2/2(경계 O)) + 2(경계 X)
=> dp[2]*3 + dp[0]*2 = 11

n=6
=> 11x3 + 3*2 + 2 = 33+8=41
=> 11x3(4/2(경계 O)) + 3*2(2/4(경계 X)) + 2(경계 X)
=> dp[4]*3 + dp[2]*2 + dp[0]*2

n=8
=> 41x3(6/2(경계 O)) + 11*2(4/4(경계 X)) + 3*2(2/6(경계 X)) + 2(경계 X)
=> dp[6]*3 + dp[4]*2 + dp[2]*2 + dp[0]*2 = 123+22+6 = 145+8=153

=> dp[i] = dp[i-2]*3 + dp[i-4]*2 + dp[i-6]*2 + ... + dp[0]*2
*/