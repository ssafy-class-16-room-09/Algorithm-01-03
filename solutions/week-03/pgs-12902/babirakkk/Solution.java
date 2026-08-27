import java.util.*;

class Solution {
    
    int[] dp = new int[5001];
    final int MODULO = 1000000007;
    
    public int solution(int n) {
        initArray();
        return countTilings(n);        
    }
    
    private int countTilings(int n) {
        if (dp[n] == -1) {
            dp[n] = (int) ((((long) countTilings(n - 2) * 4 % MODULO) - countTilings(n - 4) + MODULO) % MODULO);
        }
        return dp[n];
    }
    
    private void initArray() {
        Arrays.fill(dp, -1);
        for (int i = 1; i < dp.length; i += 2) {
            dp[i] = 0;
        }
        dp[0] = 1;
        dp[2] = 3;
    }
}