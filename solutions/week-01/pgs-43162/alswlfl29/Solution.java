import java.util.*;

class Solution {
    boolean[] visited;
    public int solution(int n, int[][] computers) {
        int answer = 0;
        visited = new boolean[n]; // 방문 여부 배열
        
        for(int i=0; i<n; i++) {
            // 새로운 네트워크 상 컴퓨터 연결
            if(!visited[i]) {
                answer += bfs(i, n, computers);
            }
        }

        return answer;
    }
    
    public int bfs(int computer, int n, int[][] computers) {
        Queue<Integer> queue = new LinkedList<>();
        visited[computer] = true;
        queue.offer(computer);
        while(queue.size() > 0) {
            int com = queue.poll();
            for(int i=0; i<n; i++) {
                // 아직 방문하지 않았고, 현재 컴퓨터와 연결되어 있는 경우 -> 같은 네트워크로 표시
                if(!visited[i] && computers[com][i] == 1) {
                    visited[i] = true;
                    queue.offer(i);
                }
            }
        }
        return 1;
    }
}

/*
네트워크 수 구하기 -> 연결되어 있으면 같은 네트워크
n이 최대 200 -> BFS 수행
*/