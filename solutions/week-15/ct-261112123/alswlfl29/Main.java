import java.util.*;

public class Main {

    private static int[] dp;
    private static List<Integer>[] edges;
    private static int[] values;
    private static boolean[] visited;
    private static int maxRoute;
    private static int MIN = -1001;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); // 정점 수

        edges = new ArrayList[n+1]; // 간선 정보
        for(int i=1; i<=n; i++) edges[i] = new ArrayList<>();

        for (int i = 0; i < n - 1; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            edges[a].add(b);
            edges[b].add(a);
        }
        values = new int[n+1]; // 각 노드가 가지는 값
        for (int i = 0; i < n; i++) {
            values[i+1] = sc.nextInt();
        }
        // Please write your code here.
        maxRoute = MIN;
        if(n == 1) {
            System.out.println(values[1]);
            return;
        }
        // System.out.println(Arrays.toString(edges));
        visited = new boolean[n+1];
        dp = new int[n+1];
        Arrays.fill(dp, MIN);
        visited[1] = true;
        bottomUp(1);
        
        System.out.println(maxRoute);
    }

    private static void bottomUp(int node) {

        if(node != 1 && edges[node].size() == 1) {
            dp[node] = values[node]; // 자식 노드가 없는 노드
            maxRoute = Math.max(maxRoute, dp[node]);
            return;
        }
        
        dp[node] = 0;
        int left = MIN;
        int right = MIN;
        for(int next : edges[node]) {
            if(visited[next]) continue;
            visited[next] = true;
            bottomUp(next);
            if(left == MIN) left = dp[next];
            else right = dp[next];
        }
        dp[node] = Math.max(Math.max(dp[node], left), right) + values[node];
        int newRoute = Math.max(0, left) + Math.max(0, right) + values[node];
        maxRoute = Math.max(maxRoute, newRoute);
    }
}

/*
dp[부모] = max(dp[왼쪽 자식], dp[오른쪽 자식], dp[왼쪽 자식+오른쪽 자식])
dp[부모] = max(dp[부모], dp[부모]+본인)

*/
