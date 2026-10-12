import java.util.*;

class Solution {
    ArrayList<int[]>[] graph;
    int N;
    public int solution(int n, int s, int a, int b, int[][] fares) {
        N = n;
        graph = new ArrayList[n];
        for (int i=0; i<n; i++){
            graph[i] = new ArrayList<>();
        }
        
        for (int[] fare:fares){
            int u = fare[0]-1;
            int v = fare[1]-1;
            int w = fare[2];
            graph[u].add(new int[]{v, w});
            graph[v].add(new int[]{u, w});
        }
        int[] distA = dijkstra(a-1);
        int[] distB = dijkstra(b-1);
        int[] distS = dijkstra(s-1);
        
        int answer = Integer.MAX_VALUE;
        for (int i=0; i<N; i++){
            int cost = distA[i] + distB[i] + distS[i];
            answer = Math.min(answer, cost);
        }
        
        return answer;
    }
    
    private int[] dijkstra(int node){
        int[] dist = new int[N];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[node] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[1], o2[1]));
        pq.offer(new int[] {node, 0});
        while (!pq.isEmpty()){
            int[] curr = pq.poll();
            int cur = curr[0];
            int d = curr[1];
            if (d>dist[cur]) continue;
            
            for (int[] next:graph[cur]){
                int nxt = next[0];
                int nd = next[1]+d;
                if (nd < dist[nxt]){
                    dist[nxt] = nd;
                    pq.offer(new int[] {nxt, nd});
                }
            }
        }
        return dist;
    }
    
}

/*
A도착지에서부터 다익스트라
B도착지에서부터 다익스트라 
S출발지에서부터 다익스트라

distA = [25, 48, 26, 35,  2,  0]
distB = [63,  0, 22, 66, 46, 48]
distS = [10, 66, 51,  0, 34, 35]
cost  = [98,114, 99,101, 82, 83]

*/