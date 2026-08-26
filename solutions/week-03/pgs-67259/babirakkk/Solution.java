import java.util.*;

class Loc {
    int x;
    int y;
    int accCost; // 현재까지 누적 금액
    int prevDir; // 현재 칸에 오기 위해 이전에 이동했던 방향
    Loc(int x, int y, int accCost, int prevDir) {
        this.x = x;
        this.y = y;
        this.accCost = accCost;
        this.prevDir = prevDir;
    }
}

class Solution {
    
    static int UP = 0;
    static int DOWN = 1;
    static int LEFT = 2;
    static int RIGHT = 3;
    static int[] dx = new int[]{ -1, 1, 0, 0 };
    static int[] dy = new int[]{ 0, 0, -1, 1 };
    
    public int solution(int[][] board) {
        boolean[][] map = new boolean[board.length + 2][board.length + 2];
        Arrays.fill(map[0], false);
        for (int i = 1; i < board.length + 1; i++) {
            map[i][0] = false;
            for (int j = 1; j < board.length + 1; j++) {
                map[i][j] = (board[i - 1][j - 1] == 0) ? true : false;
            }
            map[i][board.length + 1] = false;
        } 
        Arrays.fill(map[board.length + 1], false);
        
        int[][][] visited = new int[board.length + 2][board.length + 2][4]; // [x][y][d]
        for (int[][] vis : visited) {
            for (int[] v : vis) {
                Arrays.fill(v, Integer.MAX_VALUE);
            }
        }
        visited[1][1][UP] = 0; // 시작점은 모든 방향에서 0으로 초기화
        visited[1][1][DOWN] = 0;        
        visited[1][1][LEFT] = 0;
        visited[1][1][RIGHT] = 0;
        
        Queue<Loc> q = new LinkedList<>();
        q.add(new Loc(1, 1, 0, -1)); // 시작점, prevDir는 -1로 초기화 (아무 방향도 아님)
        while (!q.isEmpty()) {
            Loc curr = q.poll();
            for (int d = 0; d < 4; d++) {
                int nextX = curr.x + dx[d];
                int nextY = curr.y + dy[d];
                
                int cost = curr.accCost + 100; // 직선 도로 생성 비용 100원
                if (isCorner(curr.prevDir, d)) {
                    cost += 500; // 다음 방향으로 이동했을 때 코너가 생기면 500원 추가
                }
                if (map[nextX][nextY] && visited[nextX][nextY][d] > cost) { // 다음 칸이 이동 가능한 칸이고, 현재까지의 비용보다 적은 비용으로 도착할 수 있다면
                    visited[nextX][nextY][d] = cost;
                    if (!(nextX == board.length && nextY == board.length)) { // 도착 지점이 아니라면 큐에 추가
                        q.add(new Loc(nextX, nextY, cost, d));
                    }
                }
            }
        }
        return Arrays.stream(visited[board.length][board.length]).min().getAsInt(); // 도착 지점까지의 최소 비용 반환
    }
    
    private boolean isCorner(int prevDir, int currDir) { // 이전 방향과 현재 방향이 직각으로 꺾이는지 확인
        if ((prevDir == DOWN || prevDir == UP) && (currDir == LEFT || currDir == RIGHT)) {
            return true;
        }
        if ((prevDir == LEFT || prevDir == RIGHT) && (currDir == UP || currDir == DOWN)) {
            return true;
        }
        return false;
    }
}