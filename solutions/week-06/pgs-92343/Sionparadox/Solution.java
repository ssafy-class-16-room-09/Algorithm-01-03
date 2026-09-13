import java.util.*;

class Solution {
    ArrayList<Integer>[] tree;
    int[] info;
    boolean[] visited;
    int answer, L;
    
    public int solution(int[] info, int[][] edges) {
        answer = 0;
        this.info = info;
        L = info.length;
        visited = new boolean[1<<L];
        tree = new ArrayList[L]; 
        
        for (int i=0;i<L;i++){
            tree[i] = new ArrayList<>();
        }
        for (int[] edge:edges){
            int u = edge[0];
            int v = edge[1];
            tree[u].add(v);
        }
        
        int nexts = 0;
        for (int nxt: tree[0]){
            nexts |= (1<<nxt);
        }
        dfs(1, 0, 1, nexts);
        
        return answer;
    }
    
    private void dfs(int sheep, int wolf, int mask, int nexts){
        if (visited[mask]) return;
        visited[mask] = true;
        
        if (sheep <= wolf) return;
        answer = Math.max(answer, sheep);
        
        for (int i=0; i<L; i++){
            if ((nexts & 1<<i) == 0) continue;
            int new_nexts = nexts & ~(1<<i);
            for (int nxt: tree[i]){
                new_nexts |= (1<<nxt);
            }
            int new_mask = mask | (1<<i);
            
            if(info[i] == 0){
                dfs(sheep+1, wolf, new_mask, new_nexts);
            } else {
                dfs(sheep, wolf+1, new_mask, new_nexts);
            }
        }
    }
}

/*
N <= 17
dfs? w/ 비트마스킹
다음 갈 곳을 마스킹
방문한 후에는 다음 갈 곳에서 지우기
계속 자식들 다음 갈 곳에 추가하기
2^17 = 128_000
*/