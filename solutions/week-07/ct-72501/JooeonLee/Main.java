import java.util.*;
import java.io.*;

public class Main {

    // 현재 배양 용기 상태
    // 0: 빈 공간, 그 외: 미생물 ID
    static int[][] map;

    // id == index가 되도록 관리
    static ArrayList<Microbe> microbes = new ArrayList<>();

    // 분리되거나 이동에 실패하여 사라진 미생물
    static boolean[] removed;

    static int N, Q;

    static int[] dr = {0, 0, -1, 1};
    static int[] dc = {-1, 1, 0, 0};

    public static void main(String[] args) {

        FastReader fr = new FastReader();

        N = fr.nextInt();
        Q = fr.nextInt();

        map = new int[N][N];
        removed = new boolean[Q + 1];

        // microbes.get(id)로 바로 접근하기 위해 0번은 null로 채움
        microbes.add(null);

        for (int id = 1; id <= Q; id++) {

            int lbR = fr.nextInt();
            int lbC = fr.nextInt();
            int ruR = fr.nextInt();
            int ruC = fr.nextInt();

            /*
             * 한 번의 실험 과정
             *
             * 1. 새로운 미생물 투입
             * 2. 기존 미생물이 둘 이상으로 분리됐는지 확인 후 제거
             * 3. 살아있는 미생물의 현재 모양을 상대좌표로 갱신
             * 4. 새로운 배양 용기로 미생물 이동
             * 5. 서로 맞닿은 미생물 쌍의 점수 계산
             */
            addMicrobe(id, lbR, lbC, ruR, ruC);
            removeSeparatedMicrobes();
            updateMicrobeShape();
            moveMicrobes();

            System.out.println(calculateScore());
        }
    }

    /*
     * 새로운 미생물을 투입
     *
     * 새 미생물이 기존 미생물 영역과 겹치면
     * 새로운 미생물이 기존 영역을 덮어씀
     */
    static void addMicrobe(int id, int lbR, int lbC, int ruR, int ruC) {

        for (int r = lbR; r < ruR; r++) {
            for (int c = lbC; c < ruC; c++) {
                map[r][c] = id;
            }
        }

        microbes.add(new Microbe(id, lbR, lbC, ruR, ruC));
    }

    /*
     * 새로운 미생물이 기존 영역을 덮으면서
     * 기존 미생물이 둘 이상의 영역으로 분리될 수 있음
     *
     * 각 미생물에 대해
     *
     * 현재 map에 존재하는 전체 칸 수
     * ==
     * 한 지점에서 BFS로 도달 가능한 칸 수
     *
     * 인 경우에만 하나의 연결된 영역이라 판단
     *
     * 두 값이 다르면 영역이 분리된 것이므로 미생물을 제거함
     */
    static void removeSeparatedMicrobes() {

        int[] totalCnt = new int[Q + 1];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                totalCnt[map[r][c]]++;
            }
        }

        // 동일한 미생물에 대해 BFS를 여러 번 수행하지 않도록 체크
        boolean[] checked = new boolean[Q + 1];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                int currId = map[r][c];

                if (currId == 0 || checked[currId])
                    continue;

                checked[currId] = true;

                int connectedCnt = bfsCnt(currId, r, c);

                // 전체 칸을 하나의 BFS로 방문하지 못했다면
                // 둘 이상의 영역으로 분리된 상태
                if (totalCnt[currId] != connectedCnt) {
                    removed[currId] = true;
                }
            }
        }

        // 분리된 미생물을 배양 용기에서 제거
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                int currId = map[r][c];

                if (removed[currId]) {
                    map[r][c] = 0;
                }
            }
        }
    }

    // 하나의 미생물이 현재 위치에서 몇 칸으로 연결되어 있는지 BFS로 계산
    static int bfsCnt(int id, int startR, int startC) {

        boolean[][] visited = new boolean[N][N];
        Queue<int[]> queue = new ArrayDeque<>();

        queue.offer(new int[]{startR, startC});
        visited[startR][startC] = true;

        int count = 1;

        while (!queue.isEmpty()) {

            int[] curr = queue.poll();

            int r = curr[0];
            int c = curr[1];

            for (int d = 0; d < 4; d++) {

                int nr = r + dr[d];
                int nc = c + dc[d];

                if (!isValidIdx(nr, nc))
                    continue;

                if (visited[nr][nc])
                    continue;

                if (map[nr][nc] != id)
                    continue;

                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});

                count++;
            }
        }

        return count;
    }

    /*
     * 미생물이 다른 미생물에 의해 일부 덮였을 수 있으므로
     * 현재 map을 기준으로 실제 모양을 다시 구함
     *
     * 이동 과정에서는 미생물의 절대좌표보다 "모양"이 중요하므로
     * 가장 작은 행/열을 (0, 0)으로 맞춘 상대좌표로 저장함
     *
     * 예)
     *
     * 실제 위치             상대좌표
     *
     * (3, 5), (3, 6)  ->   (0, 0), (0, 1)
     */
    static void updateMicrobeShape() {

        ArrayList<int[]>[] positions = new ArrayList[Q + 1];

        for (int id = 1; id <= Q; id++) {
            positions[id] = new ArrayList<>();
        }

        // 현재 map에서 각 미생물이 차지하고 있는 좌표 수집
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                int currId = map[r][c];

                if (currId != 0) {
                    positions[currId].add(new int[]{r, c});
                }
            }
        }

        for (int id = 1; id < microbes.size(); id++) {

            Microbe microbe = microbes.get(id);

            if (removed[id]) {
                microbe.isRemoved = true;
                continue;
            }

            // map에서 완전히 사라진 미생물
            if (positions[id].isEmpty()) {
                microbe.isRemoved = true;
                continue;
            }

            // 상대좌표의 기준점이 될 좌하단 위치 탐색
            int minR = N;
            int minC = N;

            for (int[] position : positions[id]) {
                minR = Math.min(minR, position[0]);
                minC = Math.min(minC, position[1]);
            }

            microbe.relativeCoor.clear();

            // 현재 모양을 기준점으로부터의 상대좌표로 변환
            for (int[] position : positions[id]) {

                microbe.relativeCoor.add(
                    new int[]{
                        position[0] - minR,
                        position[1] - minC
                    }
                );
            }

            microbe.lbR = minR;
            microbe.lbC = minC;
        }
    }

    /*
     * 살아있는 미생물을 새로운 배양 용기로 옮김
     *
     * 이동 우선순위
     * 1. 영역이 큰 미생물
     * 2. 먼저 투입된 미생물(ID가 작은 미생물)
     *
     * 각 미생물은 가능한 위치 중
     * 행 -> 열 순서로 가장 먼저 발견되는 위치에 배치함
     */
    static void moveMicrobes() {

        ArrayList<Microbe> movingMicrobes = new ArrayList<>();

        for (int id = 1; id < microbes.size(); id++) {

            Microbe m = microbes.get(id);

            if (!m.isRemoved) {
                movingMicrobes.add(m);
            }
        }

        movingMicrobes.sort((a, b) -> {

            if (a.size() != b.size()) {
                return Integer.compare(b.size(), a.size());
            }

            return Integer.compare(a.id, b.id);
        });

        int[][] newMap = new int[N][N];

        for (Microbe m : movingMicrobes) {

            boolean placed = false;

            /*
             * 가능한 모든 기준점을 순서대로 확인
             *
             * 상대좌표를 사용하기 때문에
             * 기준점 (r, c)만 정하면 미생물의 전체 위치를 복원할 수 있음
             */
            outer:
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {

                    if (!canPlace(newMap, m, r, c))
                        continue;

                    for (int[] relative : m.relativeCoor) {

                        int nr = r + relative[0];
                        int nc = c + relative[1];

                        newMap[nr][nc] = m.id;
                    }

                    m.lbR = r;
                    m.lbC = c;

                    placed = true;
                    break outer;
                }
            }

            // 어떤 위치에도 배치할 수 없다면 미생물 소멸
            if (!placed) {
                m.isRemoved = true;
                removed[m.id] = true;
            }
        }

        map = newMap;
    }

    // 기준점 (r, c)에 미생물의 현재 모양을 그대로 배치할 수 있는지 확인
    static boolean canPlace(int[][] newMap, Microbe m, int r, int c) {

        for (int[] relative : m.relativeCoor) {

            int nr = r + relative[0];
            int nc = c + relative[1];

            // 배양 용기를 벗어남
            if (!isValidIdx(nr, nc))
                return false;

            // 이미 다른 미생물이 차지한 공간
            if (newMap[nr][nc] != 0)
                return false;
        }

        return true;
    }

    /*
     * 서로 변을 맞대고 있는 두 미생물의
     *
     * 미생물 A의 크기 × 미생물 B의 크기
     *
     * 를 점수에 한 번만 더함
     *
     * 같은 미생물 쌍이 여러 칸에서 접촉할 수 있으므로
     * Set을 이용해 이미 계산한 쌍인지 확인
     */
    static int calculateScore() {

        int result = 0;

        HashSet<Integer> checkedPair = new HashSet<>();

        /*
         * 모든 4방향을 확인하면 같은 경계를 두 번 확인하게 되므로
         * 따라서 두 방향만 확인
         */
        int[] checkR = {0, 1};
        int[] checkC = {1, 0};

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                int currId = map[r][c];

                if (currId == 0)
                    continue;

                for (int d = 0; d < 2; d++) {

                    int nr = r + checkR[d];
                    int nc = c + checkC[d];

                    if (!isValidIdx(nr, nc))
                        continue;

                    int nextId = map[nr][nc];

                    if (nextId == 0 || currId == nextId)
                        continue;

                    /*
                     * (1, 2)와 (2, 1)을 같은 쌍으로 만들기 위해
                     * 작은 ID와 큰 ID 순서로 정규화
                     */
                    int minId = Math.min(currId, nextId);
                    int maxId = Math.max(currId, nextId);

                    // 두 ID를 하나의 정수 key로 표현
                    int key = minId * (Q + 1) + maxId;

                    // 이미 계산한 미생물 쌍이면 제외
                    if (!checkedPair.add(key))
                        continue;

                    result +=
                        microbes.get(minId).size()
                        * microbes.get(maxId).size();
                }
            }
        }

        return result;
    }

    static boolean isValidIdx(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }

    static class Microbe {

        int id;

        // 현재 모양의 기준점
        int lbR;
        int lbC;

        boolean isRemoved;

        /*
         * 미생물의 현재 모양.
         *
         * 절대좌표가 아닌 기준점으로부터의 상대좌표를 저장하여
         * 이동할 때 모양을 그대로 재사용
         */
        ArrayList<int[]> relativeCoor;

        public Microbe(int id, int lbR, int lbC, int ruR, int ruC) {

            this.id = id;
            this.lbR = lbR;
            this.lbC = lbC;
            this.isRemoved = false;

            this.relativeCoor = new ArrayList<>();

            // 처음 투입될 때는 직사각형 전체가 미생물 영역
            for (int r = 0; r < ruR - lbR; r++) {
                for (int c = 0; c < ruC - lbC; c++) {
                    relativeCoor.add(new int[]{r, c});
                }
            }
        }

        public int size() {
            return relativeCoor.size();
        }
    }

    static class FastReader {

        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(
                new InputStreamReader(System.in)
            );
        }

        String next() {

            while (st == null || !st.hasMoreTokens()) {

                try {
                    st = new StringTokenizer(br.readLine());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}