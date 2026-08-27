import java.util.*;

class Car {
    int r;
    int c;
    int cost; // 현재 위치까지 건설하는데 든 비용
    int direction; // 방향
    
    public Car(int r, int c, int cost, int direction) {
        this.r = r;
        this.c = c;
        this.cost = cost;
        this.direction = direction;
    }
}

class Solution {
    static int[][] directions = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}}; // 싱좌하우
    int[][][] visited; // 방문 배열
    final static int ST = 100; // 직선 비용
    final static int CO = 600; // 코너 비용
    
    public int solution(int[][] board) {
        int answer = Integer.MAX_VALUE;
        int R = board.length; // 경주로 세로 길이
        int C = board[0].length; // 경주로 가로 길이
        
        visited = new int[R][C][4];
        for(int r=0; r<R; r++) {
            for(int c=0; c<C; c++) {
                Arrays.fill(visited[r][c], Integer.MAX_VALUE);   
            }
        }

        // 현재까지 발생한 비용 기준 오름차순 정렬
        PriorityQueue<Car> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.cost, b.cost)); 
        pq.offer(new Car(0, 0, 0, 2));
        pq.offer(new Car(0, 0, 0, 3));
        visited[0][0][2] = 0;
        visited[0][0][3] = 0;

        while(!pq.isEmpty()) {
            Car car = pq.poll();

            if(car.r == R-1 && car.c == C-1) {
                answer = Math.min(answer, car.cost);
                break;
            }

            for(int d=0; d<4; d++) {
                int nr = car.r + directions[d][0];
                int nc = car.c + directions[d][1];
                if(nr<0 || nc<0 || nr>=R || nc>=C || board[nr][nc] == 1) continue; // 영역 벗어난 경우
                int newCost = car.cost + ((car.direction + d) % 2 == 0 ? ST : CO);
                if(visited[nr][nc][d] <= newCost) continue; // 더 비용이 큰 경우 패스
                visited[nr][nc][d] = newCost;
                pq.offer(new Car(nr, nc, newCost, d));
            }
        }
        
        return answer;
    }
}

/*
0: 비어있음, 1: 벽 존재
(0,0) -> (N-1, N-1) 이동
- 인접한 두 빈 칸을 상하 또는 좌우로 연결 -> 직선 도로 (100원)
- 두 직선 도로가 서로 직각으로 만나는 지점 -> 코너 (500원)
return: 경주로 건설 시 필요한 최소 비용

우선순위 큐 이용
- 현재까지 든 비용을 기준으로 오름차순 정렬
- 
상: (-1, 0), 하: (1, 0), 좌: (0, -1), 우: (0, 1)
0 1 2 3
현재 방향 + 새로운 방향 -> 홀수: 코너
현재 방향 + 새로운 방향 -> 짝수: 직선

*/