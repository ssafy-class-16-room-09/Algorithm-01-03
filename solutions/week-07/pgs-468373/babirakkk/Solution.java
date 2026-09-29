import java.io.*;
import java.util.*;

class Solution {
    
    ArrayList<Integer>[][] graph;
    boolean[] isInfected;
    boolean[][] visited;
    int maxInfectionCount;
    int lastAction;
    Queue<Integer> q = new ArrayDeque<>();
    
    public int solution(int n, int infection, int[][] edges, int k) {
        isInfected = new boolean[n + 1];
        visited = new boolean[n + 1][3]; 
        maxInfectionCount = 1; 
        lastAction = k;
        
        graph = new ArrayList[n + 1][3];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < 3; j++) {
                graph[i][j] = new ArrayList<>();   
            }
        }
        
        for (int[] e : edges) {
            graph[e[0]][e[2] - 1].add(e[1]);
            graph[e[1]][e[2] - 1].add(e[0]);
        }
        
        isInfected[infection] = true;
        
        Set<Integer> infected = new HashSet<>();
        infected.add(infection);
        
        dfs(infected, 1, 0);
        
        return maxInfectionCount; 
    }
    
    private void dfs(Set<Integer> infected, int infectionCount, int actionCount) {
        if (actionCount == lastAction) {
            maxInfectionCount = Math.max(infectionCount, maxInfectionCount);
            return;
        }
        
        for (int i = 0; i < 3; i++) {
            Set<Integer> nextInfected = new HashSet<>();
            Set<Integer> changeVisited = new HashSet<>();
            q.clear();
            q.addAll(infected); // 현재 감염된 모든 감염체를 큐에 넣음 -> bfs
            
            int currInfectionCount = 0;
            while (!q.isEmpty()) {
                int curr = q.poll();
                if (!visited[curr][i]) { // 감염된 현재 배양체에서 i 파이프를 연 적이 없다면
                    visited[curr][i] = true; // 상태 변경
                    changeVisited.add(curr);
                    for (int next : graph[curr][i]) { // 현재 감염된 배양체의 i 파이프와 연결된 배양체에 대해
                        if (!isInfected[next]) { // 감염되지 않은 상태라면
                            isInfected[next] = true;
                            currInfectionCount++;
                            nextInfected.add(next);
                            q.add(next);
                        }
                    }
                }
            }
            Set<Integer> newSet = new HashSet<>();
            newSet.addAll(infected);
            newSet.addAll(nextInfected);
            
            dfs(newSet, infectionCount + currInfectionCount, actionCount + 1);
            
            for (int ni : nextInfected) { // 되돌리기
                isInfected[ni] = false;
            }
            for (int cv : changeVisited) {
                visited[cv][i] = false;
            }
        }
    }
}