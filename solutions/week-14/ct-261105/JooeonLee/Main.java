import java.util.Scanner;
import java.util.*;

public class Main {
    static int maxTime = 0;
    static int maxCnt = 0;
    static int[][] grid;
    static int[][] init;
    static int n, m;
    static int[] dr ={1, 0, -1, 0};
    static int[] dc = {0, 1, 0, -1};
    static Set<int[]> outWater;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        grid = new int[n][m];
        init = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = sc.nextInt();
                init[i][j] = grid[i][j];
            }
        }
        // Please write your code here.
        outWater = new HashSet<>();
        grid[0][0] = 2;
        outWater.add(new int[]{0,0});
        dfs(0, 0);

        ArrayDeque<State> q = new ArrayDeque<>();
        for(int[] outW : outWater) 
            q.offer(new State(0, outW[0], outW[1]));

        while(!q.isEmpty()) {
            State curr = q.poll();
            if(maxTime < curr.time && init[curr.r][curr.c]==1) {
                maxCnt = 0;
                maxTime = curr.time;
            }
            if(maxTime == curr.time && init[curr.r][curr.c]==1)
                maxCnt++;

            for(int i=0; i<4; i++) {
                int nr = curr.r + dr[i];
                int nc = curr.c + dc[i];

                if(!isValidIdx(nr, nc))
                    continue;
                if(grid[nr][nc] == 2)
                    continue;
                if(grid[nr][nc] == 0) {
                    grid[nr][nc] = 2;
                    q.offerFirst(new State(curr.time, nr, nc));
                }
                grid[nr][nc] = 2;
                q.offer(new State(curr.time+1, nr, nc));
            }
        }

        System.out.println(maxTime + " " + maxCnt);        
    }

    static class State {
        int time;
        int r, c;
        public State(int time, int r, int c) {
            this.time = time;
            this.r = r;
            this.c = c;
        }
    }

    static void dfs(int sr, int sc) {
        for(int i=0; i<4; i++) {
            int nr = sr + dr[i];
            int nc = sc + dc[i];

            if(!isValidIdx(nr, nc))
                continue;
            if(grid[nr][nc]==2)
                continue;
            if(grid[nr][nc]==0) {
                grid[nr][nc] = 2;
                outWater.add(new int[]{nr, nc});
                dfs(nr, nc);
            }
        }
    }

    static boolean isValidIdx(int r, int c) {
        return r>=0 && r<n && c>=0 && c<m;
    }
}