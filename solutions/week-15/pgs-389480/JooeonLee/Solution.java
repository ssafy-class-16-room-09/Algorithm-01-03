import java.util.*;

class Solution {
    public int solution(int[][] info, int n, int m) {
        int len = info.length;
        
        // 가장 단순한 dp
        boolean[][][] dp = new boolean[len+1][n][m];
        dp[0][0][0] = true;
        
        for(int i=0; i<len; i++) {
            int aT = info[i][0];
            int bT = info[i][1];
            
            for(int a=0; a<n; a++) {
                for(int b=0; b<m; b++) {
                    if(!dp[i][a][b])
                        continue;
                    
                    if(a+aT<n)
                        dp[i+1][a+aT][b] = true;
                    if(b+bT<m)
                        dp[i+1][a][b+bT] = true;
                }
            }
        }
        
        int answer = -1;
        outer: for(int i=0; i<n; i++) {
            for(boolean flag : dp[len][i]) {
                if(flag) {
                    answer = i;
                    break outer;
                }
            }
        }
        
        return answer;
    }
}