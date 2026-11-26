import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FastReader fr = new FastReader();
        int n = fr.nextInt();
        int[][] edges = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            edges[i][0] = fr.nextInt();
            edges[i][1] = fr.nextInt();
        }
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = fr.nextInt();
        }
        // Please write your code here.
        ArrayList<Integer>[] graph = new ArrayList[n+1];
        for(int i=1; i<=n; i++)
            graph[i] = new ArrayList<>();
        
        for(int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }

        int[] parent = new int[n+1];
        int[] order = new int[n];
        order[0] = 1;
        int idx = 0;

        Queue<Integer> q = new ArrayDeque<>();
        q.offer(1);
        while(!q.isEmpty()) {
            int u = q.poll();
            order[idx] = u;
            idx++;

            for(int v : graph[u]) {
                if(v==parent[u])
                    continue;
                
                parent[v] = u;
                q.offer(v);
            }
        }

        int[] dp = new int[n+1];
        int answer = Integer.MIN_VALUE;

        for(int i=n-1; i>=0; i--) {
            int u = order[i];
            int first = 0;
            int second = 0;

            for(int v : graph[u]) {
                if(v==parent[u])
                    continue;
                
                int curr = dp[v];
                if(curr > first) {
                    second = first;
                    first = curr;
                }
                else if(curr > second)
                    second = curr;
            }

            dp[u] = values[u-1] + first;
            answer = Math.max(answer, dp[u] + second);
        }

        System.out.println(answer);
    }

    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int nextInt() throws IOException {
            while(st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return Integer.parseInt(st.nextToken());
        }
    }
}