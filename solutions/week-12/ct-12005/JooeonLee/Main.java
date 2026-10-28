import java.util.*;

/**
음 그냥 bfs하면 되는 거 아닌감?
*/
public class Main {
    static ArrayList<Integer>[] graph;
    static int[] maxDir;
    static int n;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        n = sc.nextInt();
        int m = sc.nextInt();
        int k = sc.nextInt();
        graph = new ArrayList[n+1];
        maxDir = new int[n+1];
        Arrays.fill(maxDir, -1);

        for(int i=1; i<=n; i++)
            graph[i] = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            graph[from].add(to);
        }
        int[] startPoints = new int[k];
        for (int i = 0; i < k; i++) {
            startPoints[i] = sc.nextInt();
        }
        // Please write your code here.
        for(int i=0; i<k; i++) {
            int currStart = startPoints[i];
            int[] currDist = bfs(currStart);

            for(int j=1; j<=n; j++) {
                maxDir[j] = Math.max(maxDir[j], currDist[j]);
            }
        }
        int min = Integer.MAX_VALUE;
        for(int i=1; i<=n; i++)
            min = Math.min(min, maxDir[i]);
        if(min == Integer.MAX_VALUE)
            min = -1;
        System.out.println(min);
    }

    static int[] bfs(int start) {
        int[] dir = new int[n+1];
        Arrays.fill(dir, Integer.MAX_VALUE);

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{start, 0});
        while(!q.isEmpty()) {
            int[] curr = q.poll();
            int node = curr[0];
            int dist = curr[1];

            if(dir[node] <= dist)
                continue;
            else {
                dir[node] = dist;

                for(int next : graph[node]) {
                    if(dir[next] < dist+1)
                        continue;
                    q.offer(new int[]{next, dist+1});
                }
            } 
        }
        return dir;
    }
}