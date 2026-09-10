import java.util.*;

class Solution {
    public int[] solution(int n, int[][] roads, int[] sources, int destination) {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        int[] dist = new int[n+1];
        Arrays.fill(dist, -1);
        for(int i=0; i<=n; i++)
            graph.add(new ArrayList<>());
        
        for(int[] road : roads) {
            int to = road[0];
            int from = road[1];
            
            graph.get(to).add(from);
            graph.get(from).add(to);
        }
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(destination);
        dist[destination] = 0;
        while(!queue.isEmpty()) {
            int curr = queue.poll();
            int currDist = dist[curr];
            
            for(int next : graph.get(curr)) {
                if(dist[next] != -1)
                    continue;
                queue.offer(next);
                dist[next] = currDist+1;
            }
        }
        int[] answer = new int[sources.length];
        for(int i=0; i<sources.length; i++) {
            answer[i] = dist[sources[i]];
        }
        return answer;
    }
}