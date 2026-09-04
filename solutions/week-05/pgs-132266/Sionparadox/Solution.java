import java.util.*;

class Solution {
    public int[] solution(int n, int[][] roads, int[] sources, int destination) {
        int[] dist = new int[n+1];
        Arrays.fill(dist, -1);
        ArrayList<Integer>[] edges = new ArrayList[n+1];
        
        for (int i=1; i<=n; i++){
            edges[i] = new ArrayList<>();
        }
        for (int[] road:roads){
            int u = road[0];
            int v = road[1];
            edges[u].add(v);
            edges[v].add(u);
        }
        
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{destination, 0});
        dist[destination] = 0;
        
        while (!queue.isEmpty()){
            int[] curr = queue.poll();
            int node = curr[0];
            int d = curr[1];
            for (int nxt: edges[node]){
                if (dist[nxt] != -1) continue;
                dist[nxt] = d+1;
                queue.offer(new int[] {nxt, d+1});
                
            }
        }

        int[] answer = new int[sources.length];
        for (int i=0; i<sources.length; i++){
            answer[i] = dist[sources[i]];
        }
        
        return answer;
    }
}