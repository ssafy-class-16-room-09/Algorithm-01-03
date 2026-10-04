import java.util.*;

class Solution {
    
    List<int[]>[] edgeInfo; // 인접리스트
    int openK; // 파이프 연 횟수
    int N;
    
    int[] infectionStatus; // 감염 여부
    int maxInfectionCulture = 1; // 감염된 배양체 수
    
    public int solution(int n, int infection, int[][] edges, int k) {
        
        openK = k;
        N = n;
        edgeInfo = new ArrayList[N+1];
        for(int i=1; i<N+1; i++) edgeInfo[i] = new ArrayList<>();
        for(int i=0; i<N-1; i++) {
            int x = edges[i][0];
            int y = edges[i][1];
            int type = edges[i][2];
            edgeInfo[x].add(new int[] {y, type});
            edgeInfo[y].add(new int[] {x, type});
        }
        infectionStatus = new int[N+1];
        infectionStatus[infection] = 1;
        dfs(1);

        return maxInfectionCulture;
    }
    
    private void dfs(int cnt) {
        // 파이프 열 수 있는 횟수가 끝난 경우
        if(cnt > openK) {
            int count = 0;
            for(int i=1; i<=N; i++) {
                if(infectionStatus[i] > 0) count++;
            }
            maxInfectionCulture = Math.max(count, maxInfectionCulture);
            return;
        }
        
        for(int openPipe=1; openPipe<=3; openPipe++) {
            // 열려있는 파이프로 감염시킬 수 있는 배양체 감염시키기
            for(int culture=1; culture<=N; culture++) {
                if(infectionStatus[culture] > 0) {
                    infect(culture, openPipe, cnt+1);
                }
            }
            dfs(cnt+1);
            // 열려있는 파이프로 감염시킬 수 있는 배양체 감염시키기 취소
            for(int culture=1; culture<=N; culture++) {
                if(infectionStatus[culture] > 0) {
                    unInfect(culture, openPipe, cnt);
                }
            }
        }
        
        
    }
    
    private void infect(int node, int pipe, int num) {
        for(int[] connect : edgeInfo[node]) {
            if(connect[1] == pipe && infectionStatus[connect[0]] == 0) {
                infectionStatus[connect[0]] = num;
                infect(connect[0], pipe, num);
            }
        }
    }
    
    private void unInfect(int node, int pipe, int num) {
        for(int[] connect : edgeInfo[node]) {
            if(connect[1] == pipe && infectionStatus[connect[0]] > num) {
                infectionStatus[connect[0]] = 0;
                unInfect(connect[0], pipe, num);
            }
        }
    }
    
}