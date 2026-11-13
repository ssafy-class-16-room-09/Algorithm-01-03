class Solution {
    // d -> l -> r -> u
    int[] dr = {1, 0, 0, -1};
    int[] dc = {0, -1, 1, 0};
    char[] directions = {'d', 'l', 'r', 'u'};
    
    int n, m; // 미로 크기
    int r, c; // 탈출 지점
    int k; // 이동 횟수
    
    String result;
    boolean[][][] visited;
    public String solution(int n, int m, int x, int y, int r, int c, int k) {
        this.n = n;
        this.m = m;
        this.r = r-1;
        this.c = c-1;
        this.k = k;
        result = null;
        visited = new boolean[n][m][k+1];
        
        int dis = distance(x-1, y-1);
        if(dis > k || (k-dis)%2 != 0) return "impossible";
        
        move(x-1, y-1, 1, new StringBuilder());
        return result == null ? "impossible" : result;
    }
    
    private void move(int cr, int cc, int cnt, StringBuilder route) {
        if(cnt > k) { // k 횟수 이동 후 도착지에 도달한 경우
            if(cr == r && cc == c) result = route.toString();
            return;
        }
        
        for(int d=0; d<4; d++) {
            int nr = cr + dr[d];
            int nc = cc + dc[d];
            if(isOut(nr, nc) || visited[nr][nc][cnt] || distance(nr, nc) > k-cnt) continue; // 격자 바깥으로 나간 경우
            route.append(directions[d]);
            visited[nr][nc][cnt] = true;
            move(nr, nc, cnt+1, route);
            if(result != null) return;
            route.deleteCharAt(route.length()-1);
        }
    }
    
    private boolean isOut(int r, int c) {
        return r < 0 || c < 0 || r >= n || c >= m;
    }
    
    private int distance(int r, int c) {
        return Math.abs(this.r-r)+Math.abs(this.c-c);
    }
}