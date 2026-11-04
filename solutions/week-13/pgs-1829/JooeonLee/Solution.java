import java.util.*;

class Solution {
    public int[] solution(int m, int n, int[][] picture) {
        boolean[][] visited = new boolean[m][n];

        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        ArrayDeque<int[]> queue = new ArrayDeque<>();

        int cnt = 0;
        int maxSize = 0;

        for(int r = 0; r < m; r++) {
            for(int c = 0; c < n; c++) {
                if(picture[r][c] == 0 || visited[r][c]) {
                    continue;
                }

                cnt++;

                int color = picture[r][c];
                int area = 1;

                visited[r][c] = true;
                queue.offerLast(new int[]{r, c});

                while(!queue.isEmpty()) {
                    int[] curr = queue.pollFirst();

                    for(int d = 0; d < 4; d++) {
                        int nr = curr[0] + dr[d];
                        int nc = curr[1] + dc[d];

                        if(nr < 0 || nr >= m || nc < 0 || nc >= n)
                            continue;

                        if(visited[nr][nc] || picture[nr][nc] != color)
                            continue;

                        visited[nr][nc] = true;
                        queue.offerLast(new int[]{nr, nc});
                        area++;
                    }
                }

                maxSize = Math.max(maxSize, area);
            }
        }

        return new int[]{cnt, maxSize};
    }
}