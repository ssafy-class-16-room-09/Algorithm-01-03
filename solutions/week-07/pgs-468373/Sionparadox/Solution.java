import java.util.*;

class Solution {
    boolean[] visited;
    ArrayList<int[]>[] edges;
    int N, answer, K;
    
    public int solution(int n, int infection, int[][] edges, int k) {
        answer = 0;
        N = n;
        K = k;
        visited = new boolean[n+1];
        this.edges = new ArrayList[n+1];
        for (int i=1; i<=n; i++){
            this.edges[i] = new ArrayList<>();
        }
        for (int[] edge: edges){
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            this.edges[u].add(new int[] {v, w});
            this.edges[v].add(new int[] {u, w});
        }
        
        visited[infection] = true;
        dfs(1, 1, 0);
        
        return answer;
    }
    
    private void dfs(int k, int cnt, int prevType) {
        if (k > K){
            answer = Math.max(answer, cnt);
            return;
        }
        
        for (int t=1; t<=3; t++){
            if (t == prevType) continue;
            
            HashSet<Integer> infected = new HashSet<>();
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            
            for (int i=1; i<=N; i++){
                if (visited[i]) queue.offer(i);
                
            }
            
            while (!queue.isEmpty()){
                int curr = queue.poll();
                
                for (int[] info : edges[curr]){
                    int nxt = info[0];
                    int ntype = info[1];
                    if (visited[nxt]) continue;
                    if (ntype == t){
                        visited[nxt] = true;
                        queue.offer(nxt);
                        infected.add(nxt);
                    }
                }
            }
            
            dfs(k+1, cnt+infected.size(), t);
            
            // 방문 해제
            for (int prev : infected){
                visited[prev] = false;
            }
        }
    }
    
}

/*
edges[i] = [u, v, type]

다 해보기 : 3 * 2^(k-1) = 3*512 = 1500
*/