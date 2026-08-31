import java.util.*;

class Solution {
    /**
    0번 idx = 아래
    1번 idx = 위
    2번 idx = 오른쪽
    3번 idx = 왼쪽
    */
    int[] dr = {1, -1, 0, 0};
    int[] dc = {0, 0, 1, -1};
    
    public int solution(int[][] board) {
        int N = board.length;
        // 출발지 표현을 위해 인덱스 4추가해서 5로 설정
        int[][][] cost = new int[N][N][5];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Arrays.fill(cost[i][j], Integer.MAX_VALUE);
            }
        }
        
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{0, 0, 4});
        cost[0][0][4] = 0;
        
        while(!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currR = curr[0];
            int currC = curr[1];
            int currDir = curr[2];
            
            for(int i=0; i<4; i++) {
                int nextR = currR + dr[i];
                int nextC = currC + dc[i];
                
                if(isValidIdx(nextR, nextC, N) && board[nextR][nextC] != 1) {
                    if(isTurn(currDir, i)) {
                        int newCost = cost[currR][currC][currDir] + 600;
                        if(newCost < cost[nextR][nextC][i]) {
                            cost[nextR][nextC][i] = newCost;
                            queue.add(new int[]{nextR, nextC, i});
                        }
                    }
                    else {
                        int newCost = cost[currR][currC][currDir] + 100;
                        if(newCost < cost[nextR][nextC][i]) {
                            cost[nextR][nextC][i] = newCost;
                            queue.add(new int[]{nextR, nextC, i});
                        }
                    }
                }
            }
        }
        
        return Arrays.stream(cost[N-1][N-1])
            .min()
            .orElse(-1);
    }
    
    private boolean isValidIdx(int rIdx, int cIdx, int N) {
        return rIdx>=0 && rIdx<N && cIdx>=0 && cIdx<N;
    }
    
    private boolean isTurn(int prevDir, int currDir) {
        if(prevDir == 4)
            return false;
        else if(prevDir == currDir)
            return false;
        else
            return true;
    }
  }
