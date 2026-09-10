import java.util.*;

class Node {
    int num; // 지역 번호
    int cost; // 비용
    
    Node(int num, int cost) {
        this.num = num;
        this.cost = cost;
    }
}

class Solution {
    static final int INF = Integer.MAX_VALUE;
    
    List<Integer>[] connect; // 연결 도로 인접리스트
    int[] distance; // 목적지에서 각 출발지까지의 최소 거리
    public int[] solution(int n, int[][] roads, int[] sources, int destination) {
        
        connect = new ArrayList[n+1];
        for(int i=1; i<=n; i++) connect[i] = new ArrayList<>();
        
        distance = new int[n+1];
        Arrays.fill(distance, INF); // 무한대로 거리 초기화
        
        // 1. 인접 리스트 만들기
        for(int r=0; r<roads.length; r++) {
            int node1 = roads[r][0];
            int node2 = roads[r][1];
            
            connect[node1].add(node2);
            connect[node2].add(node1);
        }
        
        // 2. 도착지에서 각 출발지까지의 최단 거리 구하기
        dijkstra(destination);
        
        // 3. 출발지에 있는 부대에서 도착지까지의 최단 거리 answer에 담기
        int[] answer = new int[sources.length];
        for(int s=0; s<sources.length; s++) {
            int source = sources[s];
            answer[s] = distance[source] == INF ? -1 : distance[source];
        }

        return answer;
    }
    
    private void dijkstra(int start) {
        
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparing((Node node) -> node.cost));
        pq.offer(new Node(start, 0));
        distance[start] = 0; // 도착지를 pq에 넣고, 최소 거리 0으로 설정
        
        // 도착지에서 모든 출발지까지의 최단 시간 구하기
        while(!pq.isEmpty()) {
            Node current = pq.poll();
            
            if(distance[current.num] < current.cost) continue;
            
            for(int next : connect[current.num]) {
                int newCost = current.cost + 1;
                if(distance[next] > current.cost + 1) {
                    pq.offer(new Node(next, newCost));
                    distance[next] = newCost;
                }
            }
        }
    }
}