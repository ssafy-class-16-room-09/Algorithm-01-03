import java.util.*;

class Solution {
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        ArrayList<ArrayList<Edge>> graph = new ArrayList<>();
        int[] intensity = new int[n+1];
        Arrays.fill(intensity, Integer.MAX_VALUE);
        boolean[] isSummit = new boolean[n+1];
        for(int summit : summits)
            isSummit[summit] = true;
        boolean[] isGate = new boolean[n+1];
        for(int gate : gates)
            isGate[gate] = true;
        
        for(int i=0; i<=n; i++)
            graph.add(new ArrayList<>());
        
        for(int[] path : paths) {
            int from = path[0];
            int to = path[1];
            int weight = path[2];
            
            graph.get(from).add(new Edge(to, weight));
            graph.get(to).add(new Edge(from, weight));
        }
        
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> {
            return Integer.compare(a.weight, b.weight);
        });
        
        for(int gate : gates) {
            intensity[gate] = 0;
            pq.add(new Edge(gate, 0));
        }
        
        while(!pq.isEmpty()) {
            Edge curr = pq.poll();
            int now = curr.to;
            int currIntensity = curr.weight;
            
            if(intensity[now] < currIntensity)
                continue;
            if(isSummit[now])
                continue;
            
            for(Edge next : graph.get(now)) {
                if(isGate[next.to])
                    continue;
                int nextIntensity  = Math.max(currIntensity, next.weight);
                
                if(nextIntensity < intensity[next.to]) {
                    intensity[next.to] = nextIntensity;
                    pq.add(new Edge(next.to, nextIntensity));
                }
            }
        }
        
        int minSummit = -1;
        int minIntensity = Integer.MAX_VALUE;
        Arrays.sort(summits);
        for(int summit : summits) {
            if(minIntensity > intensity[summit]) {
                minIntensity = intensity[summit];
                minSummit = summit;
            }
        }
        
        return new int[]{minSummit, minIntensity};
    }
    
    class Edge {
        int to;
        int weight;
        
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}
