import java.util.*;

class Solution {
    int R, C;
    int[][] board;
    boolean[][] visited;
    int[] dr = new int[] {-1, 1, 0, 0};
    int[] dc = new int[] {0, 0, -1, 1};
    
    public int[] solution(int m, int n, int[][] picture) {
        int cnt=0, size=0;
        R = m;
        C = n;
        board = picture;
        
        visited = new boolean[m][n];
        for(int r=0; r<m; r++){
            for (int c=0; c<n; c++){
                if(picture[r][c] == 0 || visited[r][c]) continue;
                cnt++;
                size = Math.max(size, bfs(r, c));
            }
        }
        
        
        return new int[] {cnt, size};
    }
    
    private int bfs(int r, int c){
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        visited[r][c] = true;
        queue.offer(r*C+c);
        int color = board[r][c];
        int ret = 1;
        
        while (!queue.isEmpty()){
            int curr = queue.poll();
            r = curr/C;
            c = curr%C;
            
            for (int d=0; d<4; d++){
                int nr=r+dr[d], nc=c+dc[d];
                if(nr<0 || nr>=R || nc<0 || nc>=C) continue;
                if(board[nr][nc] != color || visited[nr][nc]) continue;
                visited[nr][nc] = true;
                queue.offer(nr*C+nc);
                ret++;
            }
        }
        
        return ret;
    }
}