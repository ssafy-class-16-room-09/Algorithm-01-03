import java.util.*;
import java.io.*;

public class Main {

    static class Coord {
        int r, c;
        Coord(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    private static final int[] dr = { -1, 1, 0, 0 };
    private static final int[] dc = { 0, 0, -1, 1 };

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        boolean[][] isWater = new boolean[n][m];
        boolean[][] visited = new boolean[n][m];

        int remainingGlaciers = 0;
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                int info = Integer.parseInt(st.nextToken());
                isWater[i][j] = (info == 0); // 물이면 true, 빙하이면 false
                if (info == 1) remainingGlaciers++; // 빙하인 경우 빙하 개수++
            }
        }

        Queue<Coord> water = new ArrayDeque<>(); // 사방이 빙하로 둘러싸인 상태가 아닌 물
        ArrayList<Coord> melt = new ArrayList<>(); // 이번에 녹을 빙하
        water.add(new Coord(0, 0));
        visited[0][0] = true;

        int time = 0; // 걸린 시간
        int lastMeltedCount = 0; // 가장 최근 시간에 녹은 빙하의 개수
        while (remainingGlaciers > 0) {
            time++;
            while (!water.isEmpty()) {
                Coord curr = water.poll();

                for (int d = 0; d < 4; d++) {
                    int nr = curr.r + dr[d];
                    int nc = curr.c + dc[d];
                    
                    if (nr < 0 || nr >= n || nc < 0 || nc >= m) continue;
                    if (visited[nr][nc]) continue;

                    if (isWater[nr][nc]) { // 물인 경우
                        water.add(new Coord(nr, nc));
                    } else { // 빙하인 경우
                        melt.add(new Coord(nr, nc));
                    }
                    visited[nr][nc] = true;
                }
            }

            for (Coord c : melt) { // 탐색 종료 후 빙하 녹이기
                isWater[c.r][c.c] = true;
            }
            lastMeltedCount = melt.size();
            remainingGlaciers -= melt.size(); 
            water.addAll(melt); // 이번에 녹인 빙하를 다음 bfs 시작으로 설정 
            melt.clear(); // 녹일 빙하 리스트 초기화
        }

        System.out.println(time + " " + lastMeltedCount);
    }
}