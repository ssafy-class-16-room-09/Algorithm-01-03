import java.util.*;

class Solution {
    public int solution(String numbers) {
        int INF = 100_000 * 15;
        int answer = INF;
        int[][] cost = new int[10][10];
        for(int i=0; i<10; i++) {
            int ir, ic;
            if(i==0) {
                ir = 3;
                ic = 1;
            }
            else {
                ir = (i-1) / 3;
                ic = (i-1) % 3;
            }
            
            for(int j=0; j<10; j++) {
                int jr, jc;
                if(j==0) {
                    jr = 3;
                    jc = 1;
                }
                else {
                    jr = (j-1) / 3;
                    jc = (j-1) % 3;
                }
                
                if(i==j) {
                    cost[i][j] = 1;
                    continue;
                }
                
                int rDiff = Math.abs(ir-jr);
                int cDiff = Math.abs(ic-jc);
                
                cost[i][j] = Math.min(rDiff, cDiff) * 3 + Math.abs(rDiff-cDiff) * 2;
            }
        }
        
        int len = numbers.length();
        int[][][] dp = new int[len+1][10][10];
        for(int i=0; i<len+1; i++)
            for(int j=0; j<10; j++)
                for(int k=0; k<10; k++)
                    dp[i][j][k] = INF;
        dp[0][4][6] = 0;
        
        for(int i=0; i<len; i++) {
            int curr = numbers.charAt(i) - '0';
            
            for(int left=0; left<10; left++) {
                for(int right=0; right<10; right++) {
                    if(dp[i][left][right] == INF)
                        continue;
                    
                    if(right != curr) {
                        int lcost = cost[left][curr];
                        dp[i+1][curr][right] = Math.min(dp[i+1][curr][right], dp[i][left][right]+lcost);
                    }
                    
                    if(left != curr) {
                        int rcost = cost[right][curr];
                        dp[i+1][left][curr] = Math.min(dp[i+1][left][curr], dp[i][left][right]+rcost); 
                    }     
                }
            }
        }
        
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                answer = Math.min(answer, dp[len][i][j]);
        return answer;
    }
}