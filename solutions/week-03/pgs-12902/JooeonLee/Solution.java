import java.util.*;

/**
점화식
f(x) = f(x-2) * 4 - f(x-4);
*/
class Solution {
    long[] dp = new long[5001];
    
    public int solution(int n) {
        dp[0] = 1L;
        dp[2] = 3L;
        return (int)rec(n);
    }
    
    public long rec(int n) {
        if(dp[n] != 0)
            return dp[n];
        else {
            dp[n] = (rec(n-2) * 4 - rec(n-4) + 1000000007) % 1000000007;
            return dp[n];
        }
    }
}