import java.util.*;

class Solution {
    boolean[] visited;

    public int solution(int n, int[][] computers) {
        List<List<Integer>> graph = new ArrayList<>();
        visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && computers[i][j] == 1) {
                    graph.get(i).add(j);
                }
            }
        }

        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(i, graph);
            }
        }

        return count;
    }

    private void dfs(int node, List<List<Integer>> graph) {
        visited[node] = true;

        for (int next : graph.get(node)) {
            if (!visited[next]) {
                dfs(next, graph);
            }
        }
    }
}

/**
전형적인 연결요소 개수 탐색하는 문제
그래프 표현 방식 연습
1. 인접행렬
2. 인접 리스트
    2.1 List의 배열
    2.2 List의 List <- 내가 선택한 방법

그래프 탐색 dfs로 연결요소 식별
*/