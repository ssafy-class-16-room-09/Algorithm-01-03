import java.util.*;

class Solution {
    
    int totalStage; // 총 스테이지 개수
    int k; // 번들 내 힌트권 개수
    int[][] stageCost; // 각 스테이지별 비용
    int[][] stageHint; // 각 스테이지별 힌트
    int[] hintCnt; // 각 스테이지별 힌트 개수
    
    int minCost; // 모든 스테이지 수행하는 데 드는 최소 비용
    
    public int solution(int[][] cost, int[][] hint) {
        
        totalStage = cost.length;
        k = hint[0].length;
        stageCost = cost;
        stageHint = hint;
        hintCnt = new int[totalStage+1];
        minCost = Integer.MAX_VALUE;
        
        dfs(1, 0);
        return minCost;
    }
    
    private void dfs(int stage, int cost) {
        
        if(cost > minCost) return; // 최소 비용보다 크거나 같은 경우 버리기
        
        // 마지막 스테이지
        if(stage > totalStage) {
            minCost = Math.min(minCost, cost);
            return;
        }
        
        // 힌트가 있으면 써버리기
        int cnt = hintCnt[stage] >= totalStage-1 ? totalStage-1 : hintCnt[stage]; // 힌트는 최대 N-1개까지 사용 가능
        // 힌트 구매 X
        dfs(stage+1, cost+stageCost[stage-1][cnt]);

        if(stage == totalStage) return;

        // 힌트 구매
        for(int h=1; h<k; h++) {
            hintCnt[stageHint[stage-1][h]]++;
        }
        dfs(stage+1, cost+stageCost[stage-1][cnt]+stageHint[stage-1][0]);
        // 힌트 구매 취소
        for(int h=1; h<k; h++) {
            hintCnt[stageHint[stage-1][h]]--;
        }
    }
}
