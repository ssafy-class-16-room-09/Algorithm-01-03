import java.util.*;

/*
1. 일단 번들을 산다.
2. 번들을 이용해서 나중에 할인 받을 수 있는 비용을 확인한다.
3. 번들을 이용해서 할인 받을수 있는 비용이 번들을 사는 가격보다 크면 번들을 사는 것이 이득이다.

힌트의 최대수인 n이 16가지
즉 힌트를 사용할지 말지 확인할 수 있는 경우의 수는 2^16 가지
-> 완탐 가능할듯?, 어떻게? -> 비트마스킹 사용해보자!
*/
class Solution {
    public int solution(int[][] cost, int[][] hint) {
        
        int n = cost.length;
        int[][] given = new int[n - 1][n];
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 1; j < hint[i].length; j++) {
                int stage = hint[i][j] - 1;
                
                given[i][stage]++;
            }
        }
        
        int answer = Integer.MAX_VALUE;
        
        int maxMask = 1 << (n - 1);
        
        for (int mask = 0; mask < maxMask; mask++) {
            int[] hintCnt = new int[n];
            int totalCost = 0;
            
            for (int i = 0; i < n - 1; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalCost += hint[i][0];
                    
                    for (int stage = 0; stage < n; stage++) {
                        hintCnt[stage] += given[i][stage];
                    }
                }
            }
            
            for (int stage = 0; stage < n; stage++) {
                int hintNum = Math.min(hintCnt[stage], n - 1);
                totalCost += cost[stage][hintNum];
            }
            
            answer = Math.min(answer, totalCost);
        }
        
        return answer;
    }
}