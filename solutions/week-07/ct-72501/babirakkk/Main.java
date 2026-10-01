import java.io.*;
import java.util.*;

public class Main {

    static final int[] dr = { -1, 1, 0, 0 };
    static final int[] dc = { 0, 0, -1, 1 };
    static final int EMPTY = 0;
    static int N, Q;
    static int[][] container;
    static Microbe[] microbeList;

    static class Microbe {
        int id;
        int remainingMicrobe;
        int baseR, baseC; // 왼쪽하단 좌표
        boolean[][] microbeStatus;
        Microbe(int putTime, int r1, int c1, int r2, int c2) {
            this.id = putTime;
            this.remainingMicrobe = (r2 - r1) * (c2 - c1); // 초기 미생물 수는 사각형의 크기만큼
            this.baseR = r1;
            this.baseC = c1;
            microbeStatus = new boolean[r2- r1][c2 - c1];
            for (boolean[] row : microbeStatus) {
                Arrays.fill(row, true);
            }
        }

        void byeMicrobe(int mapR, int mapC) { // (mapR, mapC) 위치의 미생물을 먹힌 걸로 처리
            microbeStatus[mapR - baseR][mapC - baseC] = false;
            remainingMicrobe--;
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

            return countMicrobe != remainingMicrobe; // 두 무리로 나뉘지 않았다면 count == left 
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
        Set<Microbe> affectedMicrobes = new HashSet<>(); // 새로 투입된 미생물에게 한 칸이라도 먹힌 미생물 그룹 -> 분리되었는지 검사 필요
        microbeList[putTime] = new Microbe(putTime, r1, c1, r2, c2);
        
        for (int i = r1; i < r2; i++) {
            for (int j = c1; j < c2; j++) {
                if (container[i][j]  != EMPTY) {
                    affectedMicrobes.add(microbeList[container[i][j]]);
                    microbeList[container[i][j]].byeMicrobe(i, j);
                }
                container[i][j] = putTime; // 투입 시간 == 미생물의 번호
            }
        }

        for (Microbe m : affectedMicrobes) {
            if (m.isMicrobeSeparated()) { // 만약 두 무리로 나뉘었다면 해당 미생물 그룹은 소멸 처리
                m.remainingMicrobe = 0;
            }
        }

    }

    private static int moveMicrobe(int maxGroupId) {
        PriorityQueue<Microbe> pq = new PriorityQueue<>(
            Comparator.comparingInt((Microbe m) -> m.remainingMicrobe).reversed().thenComparingInt(m -> m.id));
        
        for (int i = 1; i <= maxGroupId; i++) {
            if (microbeList[i].remainingMicrobe != 0) { // 미생물이 남아있는 그룹을 pq에 추가
                pq.add(microbeList[i]);
            }
        }

        for (int i = 0; i < N; i++) {
            Arrays.fill(container[i], EMPTY);
        }

        while (!pq.isEmpty()) {
            Microbe curr = pq.poll();

            boolean placed = false; // 현재 미생물 그룹의 배치 가능 여부
            for (int j = -N; j < N; j++) {
                if (placed) break; // 만약 배치 가능하다면 더 이상 탐색할 필요 없음
                for (int i = -N; i < N; i++) {
                    if (canPlace(curr, i, j)) {
                        placeMicrobe(curr, i, j);
                        curr.baseR = i;
                        curr.baseC = j;
                        placed = true;
                        break;
                    }
                }
            }

            if (!placed) { // 만약 아무 곳에도 배치할 수 없다면 소멸 처리
                curr.remainingMicrobe = 0;
            }
        }

        boolean[][] visited = new boolean[N][N]; // 해당 셀의 방문 여부
        boolean[][] added = new boolean[microbeList.length][microbeList.length]; // added[i][j] = true -> i와 j가 인접한 경우의 점수는 이미 계산 완료
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{0, 0});
        int result = 0;
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            if (visited[curr[0]][curr[1]]) continue;
            visited[curr[0]][curr[1]] = true;

            for (int d = 0; d < 4; d++) {
                int nr = curr[0] + dr[d];
                int nc = curr[1] + dc[d];
                
                if (nr < 0 || nc < 0 || nr >= N || nc >= N) continue;

                int currId = container[curr[0]][curr[1]];
                int nextId = container[nr][nc];
                if ((currId != 0 && nextId != 0) && currId != nextId && !added[currId][nextId]) { // 두 그룹이 인접한 경우를 아직 계산하지 않은 경우
                    added[currId][nextId] = true;
                    added[nextId][currId] = true;
                    result += microbeList[currId].remainingMicrobe * microbeList[nextId].remainingMicrobe;
                }
                q.add(new int[]{nr, nc});
            }
        }
        return result;
    }


    /**
     * 현재 위치에 미생물 그룹을 배치할 수 있는지 검사하는 함수
     */
    private static boolean canPlace(Microbe m, int r, int c) {
        for (int i = 0; i < m.microbeStatus.length; i++) {
            for (int j = 0; j < m.microbeStatus[0].length; j++) {
                if (m.microbeStatus[i][j] && 
                    (r + i < 0 || c + j < 0 || r + i >= N || c + j >= N || container[r + i][c + j] != EMPTY)) return false;
            }
        }
        return true;
    }

    /**
     * 현재 위치에 미생물 그룹을 배치하는 함수
     */
    private static void placeMicrobe(Microbe m, int r, int c) {
        for (int i = 0; i < m.microbeStatus.length; i++) {
            for (int j = 0; j < m.microbeStatus[0].length; j++) {
                if (m.microbeStatus[i][j]) container[r + i][c + j] = m.id;
            }
        }
    }
}