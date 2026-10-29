import java.util.*;
import java.io.*;

public class Main {
    static final int MAX = Integer.MAX_VALUE;
    static int N;
    static ArrayList<Integer>[] graph;
    static int[] maxTime;
    static boolean[] cantVisit;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        graph = new ArrayList[N+1];
        for (int i=1; i<=N; i++){
            graph[i] = new ArrayList<>();
        }
        for (int i=0; i<M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph[u].add(v);
        }
        st = new StringTokenizer(br.readLine());
        maxTime = new int[N+1];
        cantVisit = new boolean[N+1];

        for (int i=0; i<K; i++) {
            int k = Integer.parseInt(st.nextToken());
            BFS(k);
        }
        
        int answer = MAX;
        for (int i=1; i<=N; i++){
            if(cantVisit[i]) continue;
            answer = Math.min(answer, maxTime[i]);
        }
        System.out.println(answer == MAX ? -1 : answer);
    }

    private static void BFS(int k){
        int[] dist = new int[N+1];
        Arrays.fill(dist, -1);
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.offer(k);
        dist[k] = 0;

        while (!queue.isEmpty()){
            int curr = queue.poll();

            for (int nxt:graph[curr]){
                if (dist[nxt] != -1) continue;
                dist[nxt] = dist[curr]+1;
                queue.offer(nxt);
                maxTime[nxt] = Math.max(maxTime[nxt], dist[nxt]);
            }
        }
        for (int i=1; i<=N; i++){
            if (dist[i] == -1) cantVisit[i] = true;
        }
    }
}

/*
모든 k에서 BFS

maxTime : i번 노드에 도착하는 최대 시간

모든 k에서 방문할 수 있으면서 거리가 최소인곳

*/
