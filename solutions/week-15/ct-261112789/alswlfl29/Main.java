import java.util.*;
public class Main {

    static List<Integer>[] edges;
    static boolean[] visited;
    static int[][][] dp;
    static int[] values;
    static int k;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        edges = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) edges[i] = new ArrayList<>();

        visited = new boolean[n + 1];
        
        for (int i = 0; i < n - 1; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            edges[a].add(b);
            edges[b].add(a);
        }

        values = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            values[i] = sc.nextInt();
        }
        k = sc.nextInt();

        // 0번째 요소: 노드 번호
        // 1번째 요소: 0: i번 노드를 색칠하지 않은 상황, 1: i번 노드를 색칠한 상황
        // 2번째 요소: 현재 색칠된 개수의 최대값
        dp = new int[n + 1][2][k+1];

        dfs(1);

        int answer = 0;
        for(int i=0; i<=k; i++) {
            answer = Math.max(answer, Math.max(dp[1][0][i], dp[1][1][i]));
        }
        System.out.println(answer);
    }

    private static void dfs(int node) {

        visited[node] = true;

        for(int i=0; i<2; i++) Arrays.fill(dp[node][i], -1);

        dp[node][0][0] = 0; // 해당 노드 색칠X
        dp[node][1][1] = values[node]; // 해당 노드 색칠하기

        for (int child : edges[node]) {
            if (visited[child]) continue;
            dfs(child);
            for (int i=k; i>=0; i--) { // 색칠된 개수
                for(int j=i; j>=0; j--) {

                    dp[node][0][i] = Math.max(dp[node][0][i], 
                        dp[node][0][j] + Math.max(dp[child][0][i-j], dp[child][1][i-j])); // 부모 색칠 X -> 자식 색칠 or 색칠 X 중 선택
                    dp[node][1][i] = Math.max(dp[node][1][i], dp[node][1][j] + dp[child][0][i-j]); // 부모 색칠 O -> 자식 색칠 X
                }
            }
        }
    }
}

