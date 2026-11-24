import java.util.*;

public class Main {

    private static int[] dp;
    private static List<Integer>[] edges;
    private static int[] values;
    private static boolean[] visited;

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

        if(n == 1) {
            System.out.println(values[1]);
            return;
        }
        // System.out.println(Arrays.toString(edges));
        visited = new boolean[n+1];
        dp = new int[n+1];
        dp[0] = -1001;
        visited[1] = true;
        bottomUp(1);
        
        System.out.println(dp[1]);
    }

    private static void bottomUp(int node) {

        if(node != 1 && edges[node].size() == 1) {
            dp[node] = values[node]; // 자식 노드가 없는 노드
            return;
        }

        dp[node] = values[node];

        int left = 0;
        int right = 0;
        for(int next : edges[node]) {
            if(visited[next]) continue;
            visited[next] = true;
            bottomUp(next);
            if(left == 0) left = next;
            else right = next;
        }

        dp[node] = Math.max(dp[node], dp[left] + values[node]);
        dp[node] = Math.max(dp[node], dp[right] + values[node]);
        dp[node] = Math.max(dp[node], dp[left] + dp[right] + values[node]);  
        System.out.println(node + " > " + Arrays.toString(dp));
    }
}