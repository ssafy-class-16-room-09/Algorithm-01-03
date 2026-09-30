import java.util.*;
import java.io.*;

// 미생물
class Microbe implements Comparable<Microbe> {
    int number; // 미생물 번호
    int r1, c1; // 시작 위치
    int r2, c2; // 종료 위치
    int count; // 미생물 수

    Microbe(int number, int r1, int c1, int r2, int c2) {
        this.number = number;
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
        this.count = 0;
    }

    @Override
    public int compareTo(Microbe microbe) {
        if (this.count != microbe.count) return Integer.compare(microbe.count, this.count);
        return Integer.compare(this.number, microbe.number);
    }

    @Override
    public String toString() {
        return "[r1>" + r1 + ", c1 >" + c1 + ", r2>" + r2 + ", c2>" + c2 + ", number> " + number + ", count> " + count + "]";
    }

}

public class Main {

    static int microbeTotal;
    static Microbe[] microbes; // 미생물 정보
    static int[][] originBoard; // 배양 용기 원본
    static int[][] copyBoard; // 옮기는 배양 용기
    static int N; // 배양 용기 크기

    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, -1, 0, 1};
    static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken()); // 배양 용기 크기(최대 15)
        int Q = Integer.parseInt(st.nextToken()); // 실험 횟수(최대 50)

        // 초기화 진행
        init();

        StringBuilder sb = new StringBuilder();
        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            addMicrobe(++microbeTotal, r1, c1, r2, c2); // 미생물 추가
            move(); // 배양 용기 이동
            sb.append(testResult()).append("\n");
        }

        System.out.print(sb);
        br.close();
    }

    // 0. 초기화 함수
    private static void init() {
        originBoard = new int[N][N];
        microbeTotal = 0; // 미생물 수
        microbes = new Microbe[51]; // Q가 최대 50이니까 50까지 미생물 추가 가능
    }

    // 1. 미생물 투입
    private static void addMicrobe(int microbeTotal, int r1, int c1, int r2, int c2) {

        // 새로 투입된 미생물 영역 표시하기
        Microbe microbe = new Microbe(microbeTotal, r1, c1, r2, c2);
        setPosition(microbe, r1, c1, r2, c2);
        microbes[microbeTotal] = microbe;
    }

    // 직사각형 영역에 미생물 무리 표시
    private static void setPosition(Microbe microbe, int r1, int c1, int r2, int c2) {
        for (int x = r1; x < r2; x++) {
            for (int y = c1; y < c2; y++) {
                if (originBoard[x][y] != 0) {
                    microbes[originBoard[x][y]].count--; // 기존에 있던 미생물 무리의 개수 감소
                }

                microbe.count++; // 미생물 개수 증가
                originBoard[x][y] = microbe.number;
            }
        }
    }

    private static int checkDFS(int r, int c, int number, int count) {
        microbes[number].r1 = Math.min(r, microbes[number].r1);
        microbes[number].c1 = Math.min(c, microbes[number].c1);
        microbes[number].r2 = Math.max(r + 1, microbes[number].r2);
        microbes[number].c2 = Math.max(c + 1, microbes[number].c2);

        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d];
            int nc = c + dc[d];

            if (isOut(nr, nc) || originBoard[nr][nc] != number) continue;
            if (!visited[nr][nc]) {
                visited[nr][nc] = true;
                count = checkDFS(nr, nc, number, count + 1);
            }
        }
        return count;
    }

    // 2. 배양 용기 이동
    private static void move() {
        copyBoard = new int[N][N]; // 이동 용기 초기화

        visited = new boolean[N][N];

        // 미생물 영역이 쪼개졌는지 확인
        int count = 0; // 미생물 개수
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int number = originBoard[r][c];
                if (number == 0) continue; // 미생물이 없는 영역인 경우

                if (visited[r][c]) continue;

                // 위치 변경
                microbes[number].r1 = r;
                microbes[number].c1 = c;
                microbes[number].r2 = r + 1;
                microbes[number].c2 = c + 1;

                if (microbes[number].count == 0) continue;

                visited[r][c] = true;
                count = checkDFS(r, c, number, 1);

                // 미생물이 쪼개져있는 경우 미생물 무리 없애기
                if (microbes[number].count != count) {
                    microbes[number].count = 0;
                }
            }
        }

        PriorityQueue<Microbe> pqMicrobes = new PriorityQueue<>(); // 미생물 개수 기준 내림차순
        for (int m = 1; m <= microbeTotal; m++) {
            if (microbes[m].count > 0) {
                pqMicrobes.offer(microbes[m]); // 미생물이 있는 무리들만 PQ에 넣기
            }
        }

        // 미생물 무리 옮기기
        while (!pqMicrobes.isEmpty()) {
            Microbe microbe = pqMicrobes.poll();

            // 둘 수 없는 미생물은 없애기
            if (!moveMicrobe(microbe)) microbes[microbe.number].count = 0;
        }

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                originBoard[r][c] = copyBoard[r][c];
            }
        }
    }

    private static boolean moveMicrobe(Microbe microbe) {
        int sizeR = Math.abs(microbe.r2 - microbe.r1);
        int sizeC = Math.abs(microbe.c2 - microbe.c1);

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (r + sizeR > N || c + sizeC > N) continue;
                // 미생물을 둘 수 있는지 판별
                boolean isAvailable = true;
                for (int nr = 0; nr < sizeR; nr++) {
                    if (isAvailable) {
                        for (int nc = 0; nc < sizeC; nc++) {
                            if (originBoard[microbe.r1 + nr][microbe.c1 + nc] == microbe.number
                                && copyBoard[r + nr][c + nc] != 0) {
                                    isAvailable = false;
                                    break;
                                }
                        }
                    }
                }

                if (!isAvailable) continue; // 둘 수 없음

                // 둘 수 있는 경우
                for (int nr = 0; nr < sizeR; nr++) {
                    for (int nc = 0; nc < sizeC; nc++) {
                        if (originBoard[microbe.r1 + nr][microbe.c1 + nc] == microbe.number) {
                            copyBoard[r + nr][c + nc] = microbe.number;
                        }
                    }
                }
                microbe.r1 = r;
                microbe.c1 = c;
                microbe.r2 = r + sizeR;
                microbe.c2 = c + sizeC;

                return true;
            }
        }

        return false;
    }

    // 영역 벗어나는지 확인
    private static boolean isOut(int r, int c) {
        return r < 0 || r >= N || c < 0 || c >= N;
    }

    // 3. 실험 결과 기록
    private static int testResult() {
        int reward = 0; // 실험 성과

        Set<Integer> checkMicrobe = new HashSet<>(); // 실험 확인한 미생물 번호

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (originBoard[r][c] == 0) continue;

                Microbe microbe = microbes[originBoard[r][c]];

                if (checkMicrobe.contains(microbe.number)) continue; // 이미 확인한 미생물인 경우

                Set<Integer> adjacency = new HashSet<>(); // 인접한 미생물 무리

                for (int or = microbe.r1; or < microbe.r2; or++) {
                    for (int oc = microbe.c1; oc < microbe.c2; oc++) {
                        if (originBoard[or][oc] != microbe.number) continue;
                        for (int d = 0; d < 4; d++) {
                            int nr = or + dr[d];
                            int nc = oc + dc[d];
                            if (isOut(nr, nc) || originBoard[nr][nc] == microbe.number || originBoard[nr][nc] == 0) continue;
                            if (!checkMicrobe.contains(originBoard[nr][nc])) {
                                adjacency.add(originBoard[nr][nc]);
                            }
                        }
                    }
                }

                for (int m : adjacency) {
                    reward += (microbe.count * microbes[m].count);
                }

                checkMicrobe.add(microbe.number);
                if (checkMicrobe.size() == microbeTotal) return reward; // 모든 미생물 무리 확인한 경우
            }
        }
        return reward;
    }
}
