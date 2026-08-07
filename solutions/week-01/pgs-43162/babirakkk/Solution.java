import java.util.*;

class Solution {
    public int solution(int n, int[][] computers) {
        int network = 0;
        boolean[] visit = new boolean[n]; // 방문 여부를 표시하는 배열
        
        for (int i = 0; i < n; i++) {
            if (!visit[i]) { // 방문하지 않은 컴퓨터가 있다면
                Queue<Integer> q = new LinkedList<>();
                q.add(i);
                visit[i] = true;
                
                while (!q.isEmpty()) { // bfs
                    int curr = q.poll();
                    for (int j = 0; j < n; j++) {
                        if (curr != j && computers[curr][j] == 1 && !visit[j]) {
                            visit[j] = true;
                            q.add(j);
                        }
                    }
                }
                network++; 
            } 
        }
        
        return network;
    }
}