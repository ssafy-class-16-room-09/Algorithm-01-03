class Solution {
    
    int numStages;
    int[] hintCounts;
    int minClearCost;
    
    public int solution(int[][] cost, int[][] hint) {
        numStages = cost.length;
        hintCounts = new int[numStages];
        minClearCost = Integer.MAX_VALUE;
        
        dfs(0, 0, cost, hint);
        
        return minClearCost;
    }
    
    private void dfs(int stage, int totalCost, int[][] cost, int[][] hint) {
        int usableHints = Math.min(hintCounts[stage], numStages - 1); // 힌트는 최대 numStages - 1개 사용 가능
        totalCost += cost[stage][usableHints];
        if (totalCost >= minClearCost) return;
        
        if (stage == numStages - 1) {
            minClearCost = totalCost;
            return;
        }
        
        addHints(hint[stage]);
        dfs(stage + 1, totalCost + hint[stage][0], cost, hint); // 힌트 번들을 구매하는 경우
        removeHints(hint[stage]);
        
        dfs(stage + 1, totalCost, cost, hint); // 힌트 번들을 구매하지 않는 경우
    }
    
    private void addHints(int[] hintBundle) {
        for (int i = 1; i < hintBundle.length; i++) { // hintBundle[0]은 구매 가격
            hintCounts[hintBundle[i] - 1]++;
        }
    }
    
    private void removeHints(int[] hintBundle) {
        for (int i = 1; i < hintBundle.length; i++) {
            hintCounts[hintBundle[i] - 1]--;
        }
    }
}