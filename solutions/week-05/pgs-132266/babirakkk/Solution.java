import java.util.*;

class Solution {
    public int[] solution(int n, int[][] roads, int[] sources, int destination) {
        List<Integer>[] graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] road : roads) { // 양방향 도로를 인접 리스트에 저장
            graph[road[0]].add(road[1]);
            graph[road[1]].add(road[0]);
        }
        
        int[] dist = new int[n + 1];
        Arrays.fill(dist, -1);
        dist[destination] = 0; // 목적지에서부터 최단거리 탐색 시작
        Queue<Integer> q = new ArrayDeque<>();
        q.add(destination);
        
        while (!q.isEmpty()) {
            int curr = q.poll();
            
            for (int next : graph[curr]) {
                if (dist[next] != -1) continue; // 이미 방문한 지역은 건너뜀
                
                dist[next] = dist[curr] + 1;
                q.add(next);
            }
        }
        
        int[] result = new int[sources.length];
        for (int i = 0; i < sources.length; i++) {
            result[i] = dist[sources[i]];
        }
        return result;
    }
}