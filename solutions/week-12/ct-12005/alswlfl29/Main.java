import java.util.*;

class Node implements Comparable<Node> {
    int node;
    int cost;

    Node(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }

    @Override
    public int compareTo(Node n) {
        return Integer.compare(this.cost, n.cost); // 비용 기준 오름차순 정렬
    }
}

public class Main {
    static final int INF = 100_001;

    static int n; // 정점 수
    static int m; // 간선 후
    static int k; // 사람 수
    static List<Integer>[] edges; // 간선 리스트

    static int[] shortCostToMove; // 각 사람 별 정점까지의 최단거리
    static int[] startPoints; // 사람 별 시작 위치
    static int[] visited;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        k = sc.nextInt();
        edges = new ArrayList[n];
        for(int i=0; i<n; i++) {
            edges[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int x = sc.nextInt() - 1;
            int y = sc.nextInt() - 1;
            edges[x].add(y);
        }

        shortCostToMove = new int[n];

        startPoints = new int[k];
        for (int i = 0; i < k; i++) {
            startPoints[i] = sc.nextInt() - 1;
            dijkstra(i);
        }
        
        int result = INF;
        for(int i=0; i<n; i++) {
            if(shortCostToMove[i] == 0) continue;

            result = Math.min(result, shortCostToMove[i]);
        }

        if(result == INF) System.out.println(-1);
        else System.out.println(result);
    }

    private static void dijkstra(int person) {

        int[] visited = new int[n];
        Arrays.fill(visited, INF);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(startPoints[person], 0));
        visited[startPoints[person]] = 0;

        while(!pq.isEmpty()) {
            Node current = pq.poll();
            
            for(int next : edges[current.node]) {
                int newCost = current.cost + 1;
                if(visited[next] <= newCost) continue;

                pq.offer(new Node(next, newCost));
                visited[next] = newCost;
            }
        }

        for(int i=0; i<n; i++) {
            shortCostToMove[i] = Math.max(shortCostToMove[i], visited[i]);
        }
    }
}