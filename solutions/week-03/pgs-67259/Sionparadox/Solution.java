import java.util.*;

class Solution {
    public int solution(int[][] board) {
        int N = board.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[3], o2[3]));
        //LURD
        int[] dr = {0, -1, 0, 1};
        int[] dc = {-1, 0, 1, 0};
        int[][][] visited = new int[N][N][4];
        for (int[][] cells:visited){
            for (int[] row:cells){
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }
        visited[0][0][2] = 0;
        visited[0][0][3] = 0;
        pq.offer(new int[] {0, 0, 2, 0});
        pq.offer(new int[] {0, 0, 3, 0});
        
        while (!pq.isEmpty()){
            int[] curr = pq.poll();
            int r = curr[0];
            int c = curr[1];
            int d = curr[2];
            int cost = curr[3];
            if (cost > visited[r][c][d]) continue;
            
            for (int nd=0;nd<4;nd++){
                // 반대방향 회전 불가
                if (Math.abs(d-nd) == 2) continue;
                int nr = r+dr[nd];
                int nc = c+dc[nd];
                if (nr<0 || nr>=N || nc<0 || nc>=N) continue;
                if (board[nr][nc] == 1) continue;
                int ncost = visited[r][c][d] + (d == nd ? 100 : 600);
                if (ncost < visited[nr][nc][nd]){
                    visited[nr][nc][nd] = ncost;
                    pq.offer(new int[] {nr, nc, nd, ncost});
                }
            }
        }
        
        
        return Arrays.stream(visited[N-1][N-1]).min().orElse(-1);
    }
}

/*
직진 : 100
회전 : 500
->  움직임(default) : 100, 
    방향전환(alpha) : +500
dijkstra
pq에 r, c, d, cost 넣음. 
*/