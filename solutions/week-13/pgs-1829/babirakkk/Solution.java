import java.util.*;

class Solution {
    
    private final int[] dr = { -1, 1, 0, 0 };
    private final int[] dc = { 0, 0, -1, 1 };
    
    private int[][] picture;
    private boolean[][] visited;
    
    public int[] solution(int m, int n, int[][] picture) {
        this.picture = picture;
        visited = new boolean[m][n];
        int maxArea = 0;
        int numAreas = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] != 0 && !visited[i][j]) {
                    numAreas++;
                    maxArea = Math.max(maxArea, bfs(i, j));
                }
            }
        }
        
        return new int[]{numAreas, maxArea};
    }
    
    private int bfs(int i, int j) {
        int color = picture[i][j];
        int currArea = 1;
        visited[i][j] = true;
                    
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{i, j});
        while (!q.isEmpty()) {
            int[] curr = q.poll();
                        
            for (int d = 0; d < 4; d++) {
                int nr = curr[0] + dr[d];
                int nc = curr[1] + dc[d];
                if (nr < 0 || nr >= picture.length || nc < 0 || nc >= picture[0].length) continue;
                if (visited[nr][nc] || picture[nr][nc] != color) continue;
                            
                visited[nr][nc] = true;
                q.add(new int[]{nr, nc});
                currArea++;
            }
        }
        return currArea;
    }
}