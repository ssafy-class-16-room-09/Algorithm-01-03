import java.util.*;
import java.io.*;

/**
 * 회전해서 같은 모양이 되는 퍼즐을 하나의 대표 모양으로 묶음
 *
 * spareCnt[s]: 대표 모양이 s인 스페어 수
 * holeCnt[s]: 필요한 모양의 대표값이 s인 구멍 수
 *
 * 현재 점수는 모든 s에 대한 holeCnt[s] * spareCnt[s]의 합
 * 배치, 제거 시 영향을 받는 최대 5칸만 갱신하여 전체 보드 순회를 피하기
 *
 */
public class Main {
    // 방향 번호: 0=위, 1=오른쪽, 2=아래, 3=왼쪽
    static int[] div = {1000, 100, 10, 1};
    static int[] dy = {-1, 0, 1, 0};
    static int[] dx = {0, 1, 0, -1};

    // 빈칸은 -1, 퍼즐이 있는 칸은 네 변을 인코딩한 값으로 저장
    static int[][] board;
    static int N;

    static int[] spareCnt;
    static int[] holeCnt;

    static long score;

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int Q = fr.nextInt();

        for (int i = 0; i < Q; i++) {
            int cmd = fr.nextInt();

            if (cmd == 100) {
                N = fr.nextInt();
                init();
            } else if (cmd == 200) {
                int y = fr.nextInt();
                int x = fr.nextInt();
                int up = fr.nextInt();
                int right = fr.nextInt();
                int down = fr.nextInt();
                int left = fr.nextInt();

                putPuzzle(y, x, up, right, down, left);
            } else if (cmd == 300) {
                int y = fr.nextInt();
                int x = fr.nextInt();

                removePuzzle(y, x);
            } else if (cmd == 400) {
                int up = fr.nextInt();
                int right = fr.nextInt();
                int down = fr.nextInt();
                int left = fr.nextInt();

                addSpare(up, right, down, left);
            } else if (cmd == 500) {
                // 다른 명령에서 점수를 유지하므로 조회 시 재계산X
                sb.append(score).append('\n');
            }
        }

        System.out.print(sb);
    }

    // 위, 오른쪽, 아래, 왼쪽을 각각 천, 백, 십, 일의 자리에 저장
    static int encode(int up, int right, int down, int left) {
        return up * 1000 + right * 100 + down * 10 + left;
    }

    // 지정한 방향의 자릿수만 추출
    static int getShape(int shape, int dir) {
        return shape / div[dir] % 10;
    }

    // 시계 방향 회전: a b c d -> d a b c
    static int rotate(int shape) {
        return (shape % 10) * 1000 + shape / 10;
    }

    // 네 회전 중 최솟값을 사용하여 회전으로 같은 모양을 같은 인덱스로 묶음
    static int getRepresentShape(int shape) {
        int result = shape;

        for (int i = 0; i < 3; i++) {
            shape = rotate(shape);
            result = Math.min(result, shape);
        }

        return result;
    }

    static boolean inRange(int y, int x) {
        return y >= 1 && y <= N && x >= 1 && x <= N;
    }

    // 구멍이면 필요한 모양의 대표값을, 구멍이 아니면 -1을 반환
    static int getHoleShape(int y, int x) {
        if (board[y][x] != -1) {
            return -1;
        }

        int shape = 0;

        for (int dir = 0; dir < 4; dir++) {
            int ny = y + dy[dir];
            int nx = x + dx[dir];
            int side;

            if (!inRange(ny, nx)) {
                // 테두리에 닿는 변은 반드시 직선
                side = 1;
            } else {
                // 빈 이웃이 하나라도 있으면 구멍 아님
                if (board[ny][nx] == -1) {
                    return -1;
                }

                // 이웃 퍼즐에서 현재 칸과 맞닿는 반대 방향의 변을 확인
                int opposite = (dir + 2) % 4;
                int nSide = getShape(board[ny][nx], opposite);

                // 맞물림 관계: 오목(0) & 볼록(2), 직선(1) & 직선(1)
                // 각각 2 - 0 = 2, 2 - 2 = 0, 1 - 1 = 0
                side = 2 - nSide;
            }

            shape = shape * 10 + side;
        }

        return getRepresentShape(shape);
    }

    // d=-1이면 기존 구멍의 집계를 제거하고, d=1이면 새 집계를 추가
    static void updateHole(int y, int x, int d) {
        if (!inRange(y, x)) {
            return;
        }

        int shape = getHoleShape(y, x);

        if (shape == -1) {
            return;
        }

        holeCnt[shape] += d;

        // 구멍 하나는 해당 모양의 스페어 수만큼 점수에 기여
        score += d * spareCnt[shape];
    }

    // 한 칸의 변경은 해당 칸과 직접 맞닿은 네 칸에만 영향을 미치므로, 그 다섯 칸만 갱신
    static void updateAround(int y, int x, int d) {
        updateHole(y, x, d);

        for (int dir = 0; dir < 4; dir++) {
            updateHole(y + dy[dir], x + dx[dir], d);
        }
    }

    static void init() {
        board = new int[N + 1][N + 1];

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                board[i][j] = -1;
            }
        }

        // 인코딩한 모양의 최댓값은 2222임(모든 변이 볼록)
        spareCnt = new int[2223];
        holeCnt = new int[2223];
        score = 0;
    }

    static void putPuzzle(int y, int x, int up, int right, int down, int left) {
        // 변경 전 집계를 제거한 뒤 변경 후 상태를 다시 집계
        updateAround(y, x, -1);

        int shape = encode(up, right, down, left);
        board[y][x] = shape;

        updateAround(y, x, 1);
    }

    static void removePuzzle(int y, int x) {
        updateAround(y, x, -1);

        board[y][x] = -1;

        updateAround(y, x, 1);
    }

    static void addSpare(int up, int right, int down, int left) {
        int shape = getRepresentShape(encode(up, right, down, left));

        // 새 스페어는 같은 대표 모양의 모든 구멍에 한 번씩 기여
        // 회전 방향별로 추가하지 않으므로 대칭 모양도 중복 집계하지 않음
        score += holeCnt[shape];
        spareCnt[shape]++;
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
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