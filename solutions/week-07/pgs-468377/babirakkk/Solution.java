class Solution {
    
    int numStages;
    int minClearCost;
    
    int[][] cost;
    int[][] hint;
    int[] hintCounts;
    
    public int solution(int[][] cost, int[][] hint) {
        numStages = cost.length;
        hintCounts = new int[numStages];
        minClearCost = Integer.MAX_VALUE;
        this.cost = cost;
        this.hint = hint;
        
        dfs(0, 0);
        
        return minClearCost;
    }
    
    private void dfs(int stage, int totalCost) {
        int usableHints = Math.min(hintCounts[stage], numStages - 1); // 힌트는 최대 numStages - 1개 사용 가능
        int currCost = totalCost + cost[stage][usableHints];
        if (currCost >= minClearCost) return;
        
        if (stage == numStages - 1) {
            minClearCost = currCost;
            return;
        }
        
        addHints(hint[stage]);
        dfs(stage + 1, currCost + hint[stage][0]); // 힌트 번들을 구매하는 경우
        removeHints(hint[stage]);
        
        dfs(stage + 1, currCost); // 힌트 번들을 구매하지 않는 경우
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