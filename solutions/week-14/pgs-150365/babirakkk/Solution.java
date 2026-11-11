class Solution {
    
    final String IMPOSSIBLE = "impossible";
    final char[] dirToChar = { 'd', 'l', 'r', 'u' };
    final int[] dr = { 1, 0, 0, -1 };
    final int[] dc = { 0, -1, 1, 0 };
    char[] route;
    boolean findRoute;
    int n, m, r, c, k;
    
    public String solution(int n, int m, int x, int y, int r, int c, int k) {
        this.n = n;
        this.m = m;
        this.r = r;
        this.c = c;
        this.k = k;
        route = new char[k];
        findRoute = false;
        
        String answer = IMPOSSIBLE;
        int dist = getDist(x, y);
        if (k >= dist && ((k - dist) % 2 == 0)) {
            dfs(k, x, y);
            StringBuilder sb = new StringBuilder();
            for (char dir : route) {
                sb.append(dir);
            }
            answer = sb.toString();
        }
        return answer;
    }
    
    private void dfs(int leftMove, int cr, int cc) {
        if (leftMove == 0) { // 움직임 횟수 다 씀
            if (cr == r && cc == c) { // 탈출 지점에 도달했다면
                findRoute = true;
            }
            return;
        }
        
        if (getDist(cr, cc) > leftMove) return; // 탈출 지점까지 거리보다 남은 횟수가 더 적으면 불가능
        
        for (int d = 0; d < 4; d++) { // 사전순으로 빠른 순서대로 dfs
            int nr = cr + dr[d];
            int nc = cc + dc[d];
            
            if (nr <= 0 || nr > n || nc <= 0 || nc > m) continue;
            if (findRoute) return; // 이미 사전순으로 빠른 정답이 나옴 -> 더 이상 탐색할 필요 없음
            
            route[k - leftMove] = dirToChar[d];
            dfs(leftMove - 1, nr, nc);
        }
    }
    
    private int getDist(int cr, int cc) {
        return Math.abs(cr - r) + Math.abs(cc - c);
    }
}