import java.util.*;

public class Main {

    static class Move {
        int node;
        int sId; // 노드 번호가 아닌 startPoints의 인덱스
        int dist;
        Move(int node, int sId, int dist) {
            this.node = node;
            this.sId = sId;
            this.dist = dist;
        }
    }

    static ArrayList<Integer>[] graph;
    static Queue<Move> q;
    static int[] visited;
    static int[] dist;
    static int k;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        k = sc.nextInt();
        
        graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            graph[a].add(b);
        }

        // bit masking + bfs
        q = new ArrayDeque<>();
        visited = new int[n + 1];
        dist = new int[n + 1];
        for (int i = 0; i < k; i++) {
            int startPoint = sc.nextInt();
            visited[startPoint] |= (1 << i);
            dist[startPoint] = 0;
            q.offer(new Move(startPoint, i, 0));
        }

        System.out.println(bfs());
    }

    private static int bfs() {
        while (!q.isEmpty()) {
            Move curr = q.poll();

            for (int next : graph[curr.node]) {
                if ((visited[next] & (1 << curr.sId)) == 0) { // 방문하지 않은 노드라면
                    visited[next] |= (1 << curr.sId);
                    dist[next] = Math.max(dist[next], curr.dist + 1);
                    if (visited[next] == (1 << k) - 1) { // 모든 시작 노드에서 방문했다면
                        return dist[next];
                    }
                    q.offer(new Move(next, curr.sId, curr.dist + 1));
                }
            }
        }
        return -1;
    }
}
