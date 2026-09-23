class Solution {
    static final int MOD = 10_007;
    public int solution(int n, int[] tops) {
        int[] dp = new int[n+1];
        int[] lastDp = new int[n+1];
        dp[0] = 1;
        lastDp[0] = 1;
        
        for (int i=1; i<=n; i++){
            if (tops[i-1] == 0){
                dp[i] = (dp[i-1]*2 + lastDp[i-1])%MOD;
                lastDp[i] = (dp[i-1] + lastDp[i-1])%MOD;
            } else {
                dp[i] = (dp[i-1]*3 + lastDp[i-1])%MOD;
                lastDp[i] = (dp[i-1]*2 + lastDp[i-1])%MOD;
            }
        }
        
        return dp[n];
    }
}

/*
앞 부분이 삼각형인지 사다리꼴인지는 현재 나에게 영향 x

1. 나의 모습이 사다리꼴을 만드는 방법 
dp[n-1] * 2
+ dp(앞 부분의 마지막 한 칸이 삼각형인 경우)

2. 나의 모습이 삼각형을 만드는 방법
dp[n-1] * 3
+ dp(앞 부분의 마지막 한 칸이 삼각형인 경우)



좀 어렵다
잘못 고른거같아요 미안합니다 다들
-> 아닌가?

*/