import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int N = Integer.parseInt(br.readLine());
        ArrayList<Integer>[] edges = new ArrayList[N];

        for(int i=0; i<N; i++) edges[i] = new ArrayList<>();

        for (int i=0; i<N-1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken())-1;
            int v = Integer.parseInt(st.nextToken())-1;
            edges[u].add(v);
            edges[v].add(u);
        }
        int[] values = new int[N];
        int answer = -1000;
        for (int i=0; i<N; i++) {
            values[i] = Integer.parseInt(br.readLine());
            answer = Math.max(answer, values[i]);
        }

        int[] parent = new int[N];
        int[] childCnt = new int[N];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[N];
        parent[0] = -1;
        visited[0] = true;
        queue.offer(0);
        ArrayDeque<Integer> deque = new ArrayDeque<>();

        while (!queue.isEmpty()){
            int curr = queue.poll();
            boolean flag = true;
            for (int nxt:edges[curr]){
                if(visited[nxt]) continue;
                visited[nxt] = true;
                queue.offer(nxt);
                parent[nxt] = curr;
                flag = false;
                childCnt[curr]++;
            }
            if(flag) deque.offer(curr);
        }
        
        int[] dp = values.clone();
        while (!deque.isEmpty()){
            int node = deque.poll();
            int p = parent[node];
            if(p == -1) continue;
            answer = Math.max(answer, dp[node]+dp[p]);
            dp[p] = Math.max(dp[p], dp[node]+values[p]);
            childCnt[p]--;
            if(childCnt[p]>0) continue;
            deque.offer(p);
        }
        System.out.println(answer);
    }
}
/*
dp[i] i번 노드의 자식들이 올라온 최대값

*/
