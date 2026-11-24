import java.util.*;
import java.io.*;

public class Main {
    static int N, K;
    static ArrayList<Integer>[] edges;
    static int[] values;
    static int[][][] dp;
    static boolean[] visited;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        N = Integer.parseInt(br.readLine());
        edges = new ArrayList[N];

        for (int i=0; i<N; i++) edges[i] = new ArrayList<>();

        for (int i = 0; i < N-1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken())-1;
            int v = Integer.parseInt(st.nextToken())-1;
            edges[u].add(v);
            edges[v].add(u);
        }


        values = new int[N];
        for (int i = 0; i < N; i++) values[i] = Integer.parseInt(br.readLine());

        K = Integer.parseInt(br.readLine());
        dp = new int[N][2][K+1];
        visited = new boolean[N];
        
        dfs(0);
        int answer = 0;
        for (int i=0; i<=K; i++){
            answer = Math.max(answer, Math.max(dp[0][0][i], dp[0][1][i]));
        }
        System.out.println(answer);
    }

    private static void dfs(int node){
        visited[node] = true;

        for(int i=0; i<=K; i++){
            dp[node][0][i] = -1;
            dp[node][1][i] = -1;
        }

        dp[node][0][0] = 0;
        dp[node][1][1] = values[node];

        for(int nxt:edges[node]){
            if(visited[nxt]) continue;
            dfs(nxt);
            
            for (int cnt=K; cnt>=0; cnt--){
                for (int k=0; k<=cnt; k++){
                    // node 색칠
                    if(dp[nxt][0][k] != -1 && dp[node][1][cnt-k] != -1){
                        dp[node][1][cnt] = Math.max(dp[node][1][cnt], dp[node][1][cnt-k]+dp[nxt][0][k]);
                    }

                    // node 안색칠
                    int c = Math.max(dp[nxt][0][k], dp[nxt][1][k]);
                    if(c != -1 && dp[node][0][cnt-k] != -1){
                        dp[node][0][cnt] = Math.max(dp[node][0][cnt], dp[node][0][cnt-k]+c);
                    }
                }
            }
        }

    }
}

/*
dp[i][flag][cnt] : cnt개 노드를 색칠했고 i번째 노드를 색칠하거나 말았을 떄 최대값
dp[i][1] = dp[c1][0]+dp[c2][0]+...+v[i]
dp[i][0] = max(dp[c1])+max(dp[c2])+...

cnt를 역순으로 갱신

나 선택 : dp[node][1][cnt]
자식들의 k 합이 cnt-1이 되어야함


나 선택 안함 : dp[node][0][cnt]
자식들의 k합이 cnt가 되어야함
*/
