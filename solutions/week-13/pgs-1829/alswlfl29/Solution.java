class Solution {
    boolean[][] visited; // 방문 배열
    int areaCnt; // 한 영역의 칸 개수
    
    int[] dr = {-1, 0, 1, 0};
    int[] dc = {0, -1, 0, 1};
    
    int m, n;
    int[][] picture;
    public int[] solution(int m, int n, int[][] picture) {
        int numberOfArea = 0;
        int maxSizeOfOneArea = 0;
        this.m = m;
        this.n = n;
        this.picture = picture;
        
        visited = new boolean[m][n];
        
        for(int r=0; r<m; r++) {
            for(int c=0; c<n; c++) {
                if(visited[r][c] || picture[r][c] == 0) continue;
                areaCnt = 0;
                dfs(r, c, picture[r][c]);
                numberOfArea++; // 그림 영역 추가
                maxSizeOfOneArea = Math.max(maxSizeOfOneArea, areaCnt);
            }
        }

        int[] answer = new int[2];
        answer[0] = numberOfArea;
        answer[1] = maxSizeOfOneArea;
        return answer;
    }
    
    private void dfs(int r, int c, int num) {
        for(int d=0; d<4; d++) {
            int nr = r + dr[d];
            int nc = c + dc[d];
            if(nr < 0 || nc < 0 || nr >= m || nc >= n || picture[nr][nc] != num || visited[nr][nc]) continue;
            visited[nr][nc] = true;
            areaCnt++;
            dfs(nr, nc, num);
        }
    }
}