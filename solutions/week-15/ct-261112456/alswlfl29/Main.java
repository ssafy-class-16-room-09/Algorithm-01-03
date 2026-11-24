import java.io.*;
import java.util.*;

class Pos {
    int r, c, d;
    Pos(int r, int c, int d) {
        this.r = r;
        this.c = c;
        this.d = d;
    }
}

public class Main {

    static final int WALL = 1; // 벽
    static final int EMPTY = 0; // 빈 칸

    static int N;
    static int[][] board; // 격자 판
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, -1, 0, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine()); // 격자의 크기
        board = new int[N][N];
        StringTokenizer st;
        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                board[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        int answer = Integer.MAX_VALUE;
        for (int d = 0; d < 4; d++) {
            answer = Math.min(answer, bfs(d));
        }

        System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
    }

    private static int bfs(int direction) {
        int[] visited = new int[N * N];
        Arrays.fill(visited, Integer.MAX_VALUE);

        Deque<Pos> queue = new ArrayDeque<>();
        queue.offer(new Pos(1, 1, direction));
        visited[changeToInt(1, 1)] = 0;

        while (!queue.isEmpty()) {
            Pos pos = queue.poll();

            if (pos.r == N - 2 && pos.c == N - 2) {
                return visited[changeToInt(pos.r, pos.c)];
            }

            // 현재 방향에서 다음 칸이 빈칸인지 확인
            int nr = pos.r + dr[pos.d];
            int nc = pos.c + dc[pos.d];
            if (board[nr][nc] == EMPTY) { // 빈칸이면 그대로 그냥 가기
                queue.offer(new Pos(nr, nc, pos.d));
                visited[changeToInt(nr, nc)] = visited[changeToInt(pos.r, pos.c)] + 1;
            } else { // 벽인 경우 방향 변경
                for (int d = 0; d < 4; d++) {
                    int nextR = pos.r + dr[d];
                    int nextC = pos.c + dc[d];
                    if (nextR < 0 || nextR >= N || nextC < 0 || nextC >= N || board[nextR][nextC] == WALL) continue;
                    if (visited[changeToInt(nextR, nextC)] > visited[changeToInt(pos.r, pos.c)] + 1) {
                        queue.offer(new Pos(nextR, nextC, d));
                        visited[changeToInt(nextR, nextC)] = visited[changeToInt(pos.r, pos.c)] + 1;
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private static int changeToInt(int r, int c) {
        return r * N + c;
    }
}
