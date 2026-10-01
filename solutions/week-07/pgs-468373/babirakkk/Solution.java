import java.io.*;
import java.util.*;

class Solution {
    
    private static final int PIPE_TYPE_COUNT = 3;
    ArrayList<Integer>[][] graph;
    boolean[] isInfected;
    boolean[][] isPipeOpened;
    int maxInfectionCount;
    int maxActions;
    Queue<Integer> q = new ArrayDeque<>();
    
    public int solution(int n, int infection, int[][] edges, int k) {
        isInfected = new boolean[n + 1];
        isPipeOpened = new boolean[n + 1][PIPE_TYPE_COUNT]; 
        maxInfectionCount = 1; 
        maxActions = k;
        
        graph = new ArrayList[n + 1][PIPE_TYPE_COUNT];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < PIPE_TYPE_COUNT; j++) {
                graph[i][j] = new ArrayList<>();   
            }
        }
        
        for (int[] e : edges) {
            int from = e[0];
            int to = e[1];
            int pipeType = e[2] - 1;
            graph[from][pipeType].add(to);
            graph[to][pipeType].add(from);
        }
        
        isInfected[infection] = true;
        
        Set<Integer> infected = new HashSet<>();
        infected.add(infection);
        
        dfs(infected, 1, 0);
        
        return maxInfectionCount; 
    }
    
    private void dfs(Set<Integer> infected, int infectionCount, int actionCount) {
        if (actionCount == maxActions) {
            maxInfectionCount = Math.max(infectionCount, maxInfectionCount);
            return;
        }
        
        for (int i = 0; i < PIPE_TYPE_COUNT; i++) {
            Set<Integer> newlyInfected = new HashSet<>();
            Set<Integer> newlyOpened = new HashSet<>();
            q.clear();
            q.addAll(infected); // 현재 감염된 모든 감염체를 큐에 넣음 -> bfs
            
            int newInfectionCount = 0;
            while (!q.isEmpty()) {
                int curr = q.poll();
                if (!isPipeOpened[curr][i]) { // 감염된 현재 배양체에서 i 파이프를 연 적이 없다면
                    isPipeOpened[curr][i] = true;
                    newlyOpened.add(curr);
                    for (int next : graph[curr][i]) { // 현재 감염된 배양체의 i 파이프와 연결된 배양체에 대해
                        if (!isInfected[next]) { // 감염되지 않은 상태라면
                            isInfected[next] = true;
                            newInfectionCount++;
                            newlyInfected.add(next);
                            q.add(next);
                        }
                    }
                }
            }
            Set<Integer> nextInfected = new HashSet<>();
            nextInfected.addAll(infected);
            nextInfected.addAll(newlyInfected);
            
            dfs(nextInfected, infectionCount + newInfectionCount, actionCount + 1);
            
            for (int ni : newlyInfected) { // 되돌리기
                isInfected[ni] = false;
            }
            for (int cv : newlyOpened) {
                isPipeOpened[cv][i] = false;
            }
        }
    }
}