import java.util.*;
import java.io.*;

public class Solution {

    static int N, M, K;

    // 문제에서 주는 방향 그대로 사용
    // 1: 상, 2: 하, 3: 좌, 4: 우
    static int[] dr = {0, -1, 1, 0, 0};
    static int[] dc = {0, 0, 0, -1, 1};

    static int[] reverse = {0, 2, 1, 4, 3};

    static class Microbe {
        int r;
        int c;
        int size;
        int dir;

        // 현재 시간에 이 칸으로 들어온 군집 중 최대 크기
        int maxSize;

        Microbe(int r, int c, int size, int dir) {
            this.r = r;
            this.c = c;
            this.size = size;
            this.dir = dir;
            this.maxSize = size;
        }
    }

    static boolean isEdge(int r, int c) {
        return r == 0 || r == N - 1 ||
        c == 0 || c == N - 1;
    }

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int T = fr.nextInt();

        for (int t = 1; t <= T; t++) {

            N = fr.nextInt();
            M = fr.nextInt();
            K = fr.nextInt();

            ArrayList<Microbe> microbes = new ArrayList<>();

            for (int i = 0; i < K; i++) {
                int r = fr.nextInt();
                int c = fr.nextInt();
                int size = fr.nextInt();
                int dir = fr.nextInt();

                microbes.add(new Microbe(r, c, size, dir));
            }

            Microbe[][] board = new Microbe[N][N];

            for (int time = 0; time < M; time++) {

                ArrayList<Microbe> next = new ArrayList<>();

                for (Microbe curr : microbes) {

                    int nr = curr.r + dr[curr.dir];
                    int nc = curr.c + dc[curr.dir];

                    int size = curr.size;
                    int dir = curr.dir;

                    // 약품 처리
                    if (isEdge(nr, nc)) {
                        size /= 2;

                        if (size == 0) {
                            continue;
                        }

                        dir = reverse[dir];
                    }

                    Microbe prev = board[nr][nc];

                    if (prev == null) {

                        Microbe moved =
                                new Microbe(nr, nc, size, dir);

                        board[nr][nc] = moved;
                        next.add(moved);

                    } else {

                        // 이동해 온 군집 전체 크기는 합산
                        prev.size += size;

                        // 가장 큰 군집의 방향 선택
                        if (size > prev.maxSize) {
                            prev.maxSize = size;
                            prev.dir = dir;
                        }
                    }
                }

                // 다음 시간을 위해 사용한 칸만 초기화
                for (Microbe m : next) {
                    board[m.r][m.c] = null;
                }

                microbes = next;
            }

            int answer = 0;

            for (Microbe m : microbes) {
                answer += m.size;
            }

            sb.append('#')
            .append(t)
            .append(' ')
            .append(answer)
            .append('\n');
        }

        System.out.print(sb);
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        FastReader() {
            br = new BufferedReader(
                    new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}