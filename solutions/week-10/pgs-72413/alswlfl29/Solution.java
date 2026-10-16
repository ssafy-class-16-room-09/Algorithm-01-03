import java.util.*;
class Solution {
    static final int INF = Integer.MAX_VALUE;
    static int n; // 정점 개수
    static int[][] edges; // 연결 경로
    
    public int solution(int n, int s, int a, int b, int[][] fares) {
        
        this.n = n;
        init(); // 1. 연결 경로 배열 초기화 수행
        for(int[] fare : fares) { // 서로 연결되어 있는 지점 연결
            int c = fare[0];
            int d = fare[1];
            edges[c][d] = Math.min(edges[c][d], fare[2]);
            edges[d][c] = Math.min(edges[d][c], fare[2]);
        }
        
        floydWarshall(); // 2. 각 정점 간의 최단 비용 구하기
        
        // 3. 시작 위치에서 1~n까지 경유지를 거쳐 두 목적지에 도달하기까지의 최단 택시 비용 구하기
        int answer = edges[s][a] + edges[s][b]; // 각자 이동하는 비용
        for(int via=1; via<=n; via++) {
            int cost = edges[s][via] + edges[via][a] + edges[via][b];
            answer = Math.min(answer, cost);
        }
        
        return answer;
    }
    
    private void init() { // 연결 경로 배열 초기화
        edges = new int[n+1][n+1];
        
        for(int i=1; i<=n; i++) {
            for(int j=1; j<=n; j++) {
                if(i == j) edges[i][j] = 0; // 본인 정점 연결 상태는 0으로 설정
                else edges[i][j] = INF;
            }
        }
    }
    
    private void floydWarshall() { // 각 정점 간의 최단 거리 구하기
        for(int k=1; k<=n; k++) {
            for(int i=1; i<=n; i++) {
                for(int j=1; j<=n; j++) {
                    if(i == j) continue;
                    if(edges[i][k] == INF || edges[k][j] == INF) continue;
                    edges[i][j] = Math.min(edges[i][j], edges[i][k] + edges[k][j]);
                }
            }
        }
    }
}
