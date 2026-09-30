import java.util.*;

class Stage {
    int stage; // 현재 스테이지
    int cost; // 현재까지 든 비용
    int[] hintCnt; // 각 스테이지별 존재하는 힌트권 개수
    
    public Stage(int stage, int cost, int s) {
        this.stage = stage;
        this.cost = cost;
        this.hintCnt = new int[s+1];
    }
}

class SolutionBFS {
    
    int totalStage; // 총 스테이지 개수
    int k; // 힌트 번들 내 개수
    int[][] stageCost; // 각 스테이지별 비용
    int[][] stageHint; // 각 스테이지별 힌트
    
    ArrayDeque<Stage> ways;
    
    public int solution(int[][] cost, int[][] hint) {
        
        totalStage = cost.length;
        k = hint[0].length;
        stageCost = cost;
        stageHint = hint;
        ways = new ArrayDeque<>();
        
        ways.offer(new Stage(1, 0, totalStage));
        for(int s=0; s<totalStage; s++) {
            stageBFS();
        }
        
        
        int min = Integer.MAX_VALUE;
        int total = ways.size();
        for(int i=0; i<total; i++) {
            Stage stage = ways.poll();
            min = Math.min(min, stage.cost);
        }
        return min;
    }
    
    private void stageBFS() {
        int size = ways.size();
        for(int i=0; i<size; i++) {
            Stage way = ways.poll();
            // 현재 스테이지에서 사용 가능한 힌트들 사용여부
            int use = way.hintCnt[way.stage] >= totalStage-1 ? totalStage-1 : way.hintCnt[way.stage];
            int newCost = way.cost + stageCost[way.stage-1][use];

            // 힌트권 안사
            Stage nextStageX = new Stage(way.stage+1, newCost, totalStage);
            for(int h=way.stage+1; h<=totalStage; h++) {
                nextStageX.hintCnt[h] = way.hintCnt[h];
            }
            ways.offer(nextStageX);
            
            if(way.stage >= totalStage) continue;

            // 힌트권 사
            Stage nextStageO = new Stage(way.stage+1, newCost, totalStage);

            for(int h=way.stage+1; h<=totalStage; h++) {
                nextStageO.hintCnt[h] = way.hintCnt[h];
            }

            nextStageO.cost += stageHint[way.stage-1][0];
            for(int h=1; h<k; h++) {
                int num = stageHint[way.stage-1][h];
                if(num > way.stage) {
                    nextStageO.hintCnt[num]++;
                };
            }
            ways.offer(nextStageO);
        }
    }
}