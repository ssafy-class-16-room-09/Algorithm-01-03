import java.util.*;

class Solution {
    
    public int solution(int[][] info, int n, int m) {
        int totalBTrace = 0;
        for (int i = 1; i <= info.length; i++) {
            totalBTrace += info[i - 1][1];
        }
        
        int[] dp = new int[n]; // traceB[i]: a가 흔적을 i만큼 남겼을 때 b가 남기는 흔적의 최댓값
        Arrays.fill(dp, totalBTrace);
        
        return perfectCrime(info, dp, n, m);
    }
    
    private int perfectCrime(int[][] info, int[] dp, int n, int m) {     
        for (int i = 1; i <= info.length; i++) {
            for (int j = n - 1; j >= info[i - 1][0]; j--) {
                dp[j] = Math.min(dp[j], dp[j - info[i - 1][0]] - info[i - 1][1]);
            }
        }
        
        for (int i = 0; i < n; i++) {
            if (dp[i] < m) return i;
        }
        return -1;
    }
}

/*
b는 가능한 m-1개까지 채우면서 최대한 많은 물건을 훔쳐야 함.
*/