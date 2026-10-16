import java.util.*;

class Solution {
    ArrayList<int[]>[] graph;
    int N;
    int INF = Integer.MAX_VALUE;
    
    public int solution(int n, int s, int a, int b, int[][] fares) {
        N = n;
        graph = new ArrayList[n+1];
        for(int i=1; i<=n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for(int[] fare : fares) {
            int from = fare[0];
            int to = fare[1];
            int cost = fare[2];
            
            graph[from].add(new int[]{to, cost});
            graph[to].add(new int[]{from, cost});
        }
        
        int[] distS = dijkstra(s);
        int[] distA = dijkstra(a);
        int[] distB = dijkstra(b);
        
        int answer = INF;
        for(int i=1; i<=n; i++) {
            if(distS[i]==INF || distA[i]==INF || distB[i]==INF)
                continue;
            
            answer = Math.min(answer, distS[i]+distA[i]+distB[i]);
        }
        
        return answer;
    }
    
    public int[] dijkstra(int s) {
        int[] dist = new int[N+1];
        Arrays.fill(dist, INF);
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        
        dist[s] = 0;
        pq.offer(new int[]{s, 0});
        
        while(!pq.isEmpty()) {
            int[] curr = pq.poll();
            int currN = curr[0];
            int currCost = curr[1];
            
            if(currCost > dist[currN])
                continue;
            for(int[] next : graph[currN]) {
                int nextN = next[0];
                int nextCost = currCost + next[1];
                
                if(nextCost < dist[nextN]) {
                    dist[nextN] = nextCost;
                    pq.offer(new int[]{nextN, nextCost});
                }
            }
        }
        
        return dist;
    }
}