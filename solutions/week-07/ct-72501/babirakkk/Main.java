import java.io.*;
import java.util.*;

public class Main {

    static final int[] dr = { -1, 1, 0, 0 };
    static final int[] dc = { 0, 0, -1, 1 };
    static int N, Q;
    static int[][] container;
    static Microbe[] microbeList;
    static int EMPTY = 0;

    static class Microbe {
        int putTime;
        int leftMicrobe;
        int r, c; // 왼쪽하단 좌표
        boolean[][] microbeStatus;
        Microbe(int putTime, int r1, int c1, int r2, int c2) {
            this.putTime = putTime;
            this.leftMicrobe = (r2 - r1) * (c2 - c1);
            this.r = r1;
            this.c = c1;
            microbeStatus = new boolean[r2- r1][c2 - c1];
            for (boolean[] row : microbeStatus) {
                Arrays.fill(row, true);
            }
        }

        void byeMicrobe(int mapR, int mapC) { // (mapR, mapC) 위치의 미생물을 먹힌 걸로 처리
            microbeStatus[mapR - r][mapC - c] = false;
            leftMicrobe--;
        }

        boolean isMicrobeSeparated() {
            Queue<int[]> q = new ArrayDeque<>(); // bfs
            boolean[][] visited = new boolean[microbeStatus.length][microbeStatus[0].length];
            FIND_START: for (int i = 0; i < microbeStatus.length; i++) { // (0, 0) 위치의 미생물이 먹혔을 수도 있으므로 bfs의 시작점을 탐색 
                for (int j = 0; j < microbeStatus[0].length; j++) {
                    if (microbeStatus[i][j]) {
                        q.add(new int[]{i, j});
                        visited[i][j] = true;
                        break FIND_START;
                    }
                }
            }

            int countMicrobe = 1;
            while (!q.isEmpty()) {
                int[] curr = q.poll();
                for (int d = 0; d < 4; d++) {
                    int nr = curr[0] + dr[d];
                    int nc = curr[1] + dc[d];

                    if (nr < 0 || nr >= microbeStatus.length || nc < 0 || nc >= microbeStatus[0].length) continue;
                    if (!visited[nr][nc] && microbeStatus[nr][nc]) {
                        visited[nr][nc] = true;
                        countMicrobe++;
                        q.add(new int[]{nr, nc});
                    }
                }
            }

            return countMicrobe != leftMicrobe; // 두 무리로 나뉘지 않았다면 count == left 
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        container = new int[N][N];
        microbeList = new Microbe[Q + 1];

        for (int q = 1; q <= Q; q++) {
            st = new StringTokenizer(br.readLine());
            int c1 = Integer.parseInt(st.nextToken());
            int r1 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());

            putMicrobe(q, r1, c1, r2, c2);

            System.out.println(moveMicrobe(q));
        }   
    }

    private static void putMicrobe(int putTime, int r1, int c1, int r2, int c2) { // x, y 좌표 주의
        Set<Microbe> ateMicrobe = new HashSet<>();
        microbeList[putTime] = new Microbe(putTime, r1, c1, r2, c2);
        
        for (int i = r1; i < r2; i++) {
            for (int j = c1; j < c2; j++) {
                if (container[i][j]  != EMPTY) {
                    ateMicrobe.add(microbeList[container[i][j]]); // 먹힌 미생물 그룹에 추가
                    microbeList[container[i][j]].byeMicrobe(i, j);
                }
                container[i][j] = putTime; // 투입 시간 == 미생물의 번호
            }
        }

        for (Microbe m : ateMicrobe) {
            if (m.isMicrobeSeparated()) {
                m.leftMicrobe = 0;
            }
        }

    }

    private static int moveMicrobe(int maxGroup) {
        PriorityQueue<Microbe> pq = new PriorityQueue<>(
            Comparator.comparingInt((Microbe m) -> m.leftMicrobe).reversed().thenComparingInt(m -> m.putTime));
        
        for (int i = 1; i <= maxGroup; i++) {
            if (microbeList[i].leftMicrobe != 0) {
                pq.add(microbeList[i]);
            }
        }

        int[][] tempContainer = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tempContainer[i][j] = container[i][j];
                container[i][j] = EMPTY;
            }
        }

        while (!pq.isEmpty()) {
            Microbe curr = pq.poll();

            boolean canFill = false;
            for (int j = -N; j < N; j++) {
                if (canFill) break;
                for (int i = -N; i < N; i++) {
                    if (canMicrobeMove(curr, i, j)) {
                        fillMicrobe(curr, i, j);
                        curr.r = i;
                        curr.c = j;
                        canFill = true;
                        break;
                    }
                }
            }

            if (!canFill) {
                curr.leftMicrobe = 0;
            }
        }

        boolean[][] visitedCell = new boolean[N][N];
        boolean[][] added = new boolean[microbeList.length + 1][microbeList.length + 1];
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{0, 0});
        int result = 0;
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            if (visitedCell[curr[0]][curr[1]]) continue;
            visitedCell[curr[0]][curr[1]] = true;

            for (int d = 0; d < 4; d++) {
                int nr = curr[0] + dr[d];
                int nc = curr[1] + dc[d];
                
                if (nr < 0 || nc < 0 || nr >= N || nc >= N) continue;

                int currId = container[curr[0]][curr[1]];
                int nextId = container[nr][nc];
                if ((currId != 0 && nextId != 0) && currId != nextId && !added[currId][nextId]) {
                    added[currId][nextId] = true;
                    added[nextId][currId] = true;
                    // if (maxGroup == 18) {
                    //     System.out.println("id1: " + currId + ", size1: " + microbeList[currId].leftMicrobe 
                    //     + ", id2: " + nextId + ", size2: " + microbeList[nextId].leftMicrobe);
                    // }
                    result += microbeList[currId].leftMicrobe * microbeList[nextId].leftMicrobe;
                }
                q.add(new int[]{nr, nc});
            }
        }
        return result;
    }


    private static boolean canMicrobeMove(Microbe m, int r, int c) {
        for (int i = 0; i < m.microbeStatus.length; i++) {
            for (int j = 0; j < m.microbeStatus[0].length; j++) {
                if (m.microbeStatus[i][j] && 
                    (r + i < 0 || c + j < 0 || r + i >= N || c + j >= N || container[r + i][c + j] != EMPTY)) return false;
            }
        }
        return true;
    }

    
    private static void fillMicrobe(Microbe m, int r, int c) {
        for (int i = 0; i < m.microbeStatus.length; i++) {
            for (int j = 0; j < m.microbeStatus[0].length; j++) {
                if (m.microbeStatus[i][j]) container[r + i][c + j] = m.putTime;
            }
        }
    }
}