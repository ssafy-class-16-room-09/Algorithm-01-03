import java.util.*;

class Solution {
    int[][] cost;
    int[][] hint;
    int L, answer;
    int[] hintCnt;
    
    public int solution(int[][] cost, int[][] hint) {
        answer = Integer.MAX_VALUE;
        this.cost = cost;
        this.hint = hint;
        L = cost.length;
        hintCnt = new int[L];

        dfs(0, 0);
        
        return answer;
    }
    
    private void dfs(int k, int value) {
        if (k == L) {
            answer = Math.min(answer, value);
            return;
        }
        
        int usedHintCnt = Math.min(L-1, hintCnt[k]);
        int nValue = value+cost[k][usedHintCnt];
        // not buy hint
        dfs(k+1, nValue);
        
        // buy hint
        if (k == L-1) return;
        int[] prev = Arrays.copyOf(hintCnt, L);
        int price = hint[k][0];
        for (int i=1; i<hint[k].length; i++){
            hintCnt[hint[k][i]-1]++;
        }
        dfs(k+1, nValue+price);
        hintCnt = prev;
        
    }
}


/*
n <= 16
1 ~ n 순서대로 해결
힌트권 산다 / 안산다
2^16 = 65536
백트래킹

*/