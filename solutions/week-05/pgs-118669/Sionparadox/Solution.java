import java.util.*;

class Solution {
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        
        ArrayList<int[]>[] edges = new ArrayList[n+1];
        for (int i=0; i<=n; i++){
            edges[i] = new ArrayList<>();
        }
        
        for (int[] path:paths){
            int u = path[0], v = path[1], w = path[2];
            edges[u].add(new int[] {v, w});
            edges[v].add(new int[] {u, w});
        }
        
        boolean[] isGate = new boolean[n+1];
        boolean[] isSummit = new boolean[n+1];
        for (int summit : summits){
            isSummit[summit] = true;
        }
        
        int[] dist = new int[n+1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[1], o2[1]));
        
        for (int gate:gates){
            isGate[gate] = true;
            dist[gate] = 0;
            pq.offer(new int[] {gate, 0});
        }
        
        while (!pq.isEmpty()){
            int[] curr = pq.poll();
            int node = curr[0];
            int d = curr[1];
            
            if (dist[node] < d) continue;
            if (isSummit[node]) continue;
            
            for (int[] line: edges[node]){
                int nxt = line[0];
                if (isGate[nxt]) continue;
                
                int nd = Math.max(d, line[1]);
                if (dist[nxt] > nd){
                    dist[nxt] = nd;
                    pq.offer(new int[] {nxt, nd});
                }
                
            }
        }
        
        int answer = 10_000_000;
        int minNumber = n+1;
        for (int summit:summits){
            if (dist[summit] < answer) {
                answer = dist[summit];
                minNumber = summit;
            } else if (dist[summit] == answer) {
                minNumber = Math.min(minNumber, summit);
            }
        }
        
        
        return new int[] {minNumber, answer};
    }

}

/*
multi source dijkstra
*/