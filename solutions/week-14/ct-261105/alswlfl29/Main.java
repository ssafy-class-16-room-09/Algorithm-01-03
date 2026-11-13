import java.util.*;
public class Main {

    private static int[][] grid;
    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, -1, 0, 1};
    private static int n;
    private static int m;

    private static Deque<Integer> waters; // 빙하 위치
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        grid = new int[n][m];
        waters = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                int num = sc.nextInt();
                if(num == 1) waters.offer(i*m+j); // 빙하 위치 넣기
                grid[i][j] = num;
            }
        
        checkWater(0, 0); // 바깥 영역 물로 표시

        int time = 0; // 녹는데 걸리는 시간
        int count = 0; // 직전에 녹아 사라지는 빙하 칸의 수
        while(!waters.isEmpty()) {
            int size = waters.size(); // 현재 녹지 않은 빙하 개수
            count = size;
            if(size == 0) break; // 빙하가 모두 녹은 경우
            ++time; // 시간 증가
            Deque<Integer> fuze = new ArrayDeque<>(); // 녹은 빙하 위치
            for(int g=0; g<size; g++) {
                int pos = waters.poll();
                int r = pos/m;
                int c = pos%m;
                boolean isFuze = false; // 주변에 물이 존재하는 빙하인가?
                for(int d=0; d<4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];
                    if(grid[nr][nc] == -1) { // 주변에 물이 존재해서 이번에 녹는 빙하
                        isFuze = true;
                        break;
                    }
                }
                if(isFuze) fuze.offer(pos); // 녹는 빙하 덱에 넣기
                else waters.offer(pos); // 안녹는 경우 다시 빙하 위치 덱에 넣기
            }

            while(!fuze.isEmpty()) { // 녹는 빙하 친구들 -1로 만들기
                int pos = fuze.poll();
                int r = pos/m;
                int c = pos%m;
                grid[r][c] = -1;
                for(int d=0; d<4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];
                    if(grid[nr][nc] == 0) { // 녹아서 원래 안쪽에 있던 0인 물 위치 바깥이랑 만나게 되니까 이것도 -1로 표시하기
                        checkWater(nr, nc);
                    }
                }
            }
        }

        System.out.println(time + " " + count);
    }

    private static void checkWater(int r, int c) { // 물로 표시
        Deque<Integer> waters = new ArrayDeque<>();
        waters.offer(r*m+c);
        while(!waters.isEmpty()) {
            int pos = waters.poll();
            int cr = pos/m;
            int cc = pos%m;
            grid[cr][cc] = -1;
            for(int d=0; d<4; d++) {
                int nr = cr + dr[d];
                int nc = cc + dc[d];
                if(nr < 0 || nc < 0 || nr >= n || nc >= m || grid[nr][nc] == -1) continue;
                if(grid[nr][nc] == 0) {
                    grid[nr][nc] = -1;
                    waters.offer(nr*m+nc);
                }
            }
        }
    }
}