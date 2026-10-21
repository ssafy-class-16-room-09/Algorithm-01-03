import java.util.*;
import java.io.*;

public class Main {
    
    static class Puzzle {
        int puzzle = 0;
        int nearPuzzles = 0;
        boolean isExist = false;

        void set(int u, int r, int d, int l) {
            this.puzzle = UP * u + RIGHT * r + DOWN * d + LEFT * l;
        }
    }

    private static final int[] dy = { -1, 0, 1, 0 };
    private static final int[] dx = { 0, 1, 0, -1 };
    private static final int UP = 1000;
    private static final int RIGHT = 100;
    private static final int DOWN = 10;
    private static final int LEFT = 1;

    static Map<Integer, Integer> getRotatedPuzzle; // 회전해서 같은 퍼즐은 같은 퍼즐로 침
    static Map<Integer, Integer> numSparePuzzles; // 스페어 퍼즐의 개수
    static Map<Integer, Integer> holes; // 특정 모양의 퍼즐을 배치할 수 있는 구멍의 개수
    static Puzzle[][] board;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int queries = Integer.parseInt(br.readLine());

        while (queries-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            switch (cmd) {
                case 100:
                    int boardSize = Integer.parseInt(st.nextToken());
                    init(boardSize);
                    break;
                case 200:
                    int y = Integer.parseInt(st.nextToken()) - 1;
                    int x = Integer.parseInt(st.nextToken()) - 1;
                    int u = Integer.parseInt(st.nextToken());
                    int r = Integer.parseInt(st.nextToken());
                    int d = Integer.parseInt(st.nextToken());
                    int l = Integer.parseInt(st.nextToken());
                    placePuzzle(y, x, u, r, d, l);
                    break;
                case 300:
                    y = Integer.parseInt(st.nextToken()) - 1;
                    x = Integer.parseInt(st.nextToken()) - 1;
                    removePuzzle(y, x);
                    break;
                case 400:
                    u = Integer.parseInt(st.nextToken());
                    r = Integer.parseInt(st.nextToken());
                    d = Integer.parseInt(st.nextToken());
                    l = Integer.parseInt(st.nextToken());
                    addSparePuzzle(u, r, d, l);
                    break;
                case 500:
                    System.out.println(getScore());
                    break;
            }
        }
    }

    private static void init(int boardSize) {
        board = new Puzzle[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            if (board[0][i] == null) {
                board[0][i] = new Puzzle();
            }
            board[0][i].puzzle += 1 * UP; // 위쪽 테두리에 붙은 퍼즐은 위쪽이 직선이어야 함
            board[0][i].nearPuzzles++;

            if (board[boardSize - 1][i] == null) {
                board[boardSize - 1][i] = new Puzzle();
            }
            board[boardSize - 1][i].puzzle += 1 * DOWN; // 아래쪽 테두리에 붙은 퍼즐은 아래쪽이 직선이어야 함
            board[boardSize - 1][i].nearPuzzles++;

            if (board[i][0] == null) {
                board[i][0] = new Puzzle();
            }
            board[i][0].puzzle += 1 * LEFT; // 왼쪽 테두리에 붙은 퍼즐은 왼쪽이 직선이어야 함
            board[i][0].nearPuzzles++;

            if (board[i][boardSize - 1] == null) {
                board[i][boardSize - 1] = new Puzzle();
            }
            board[i][boardSize - 1].puzzle += 1 * RIGHT; // 오른쪽 테두리에 붙은 퍼즐은 오른쪽이 직선이어야 함
            board[i][boardSize - 1].nearPuzzles++;
        }
        holes = new HashMap<>();
        numSparePuzzles = new HashMap<>();
        getRotatedPuzzle = new HashMap<>();
        findRotatedPuzzle();
    }

    private static void placePuzzle(int y, int x, int u, int r, int d, int l) {
        if (board[y][x] == null) {
            board[y][x] = new Puzzle();
        }

        Puzzle p = board[y][x];
        p.set(u, r, d, l);
        p.isExist = true;
        if (p.nearPuzzles == 4) { // 주변에 모두 퍼즐이 있었음 -> 이전에는 구멍이었다! 하지만 메꿔짐. 따라서 구멍에서 제외해야 함
            int puzzle = getRotatedPuzzle.get(p.puzzle);;
            holes.put(puzzle, holes.get(puzzle) - 1); 
        }

        for (int dir = 0; dir < 4; dir++) {
            int ny = y + dy[dir];
            int nx = x + dx[dir];

            if (ny < 0 || ny >= board.length || nx < 0 || nx >= board.length) continue;
            if (board[ny][nx] == null) {
                board[ny][nx] = new Puzzle();
            }

            // 현재 퍼즐과 붙어있는 쪽의 면을 퍼즐 규칙에 맞게 설정
            switch (dir) {
                case 0: 
                    board[ny][nx].puzzle += (-getD(board[ny][nx].puzzle) + (2 - getU(board[y][x].puzzle))) * DOWN;
                    break;
                case 1:
                    board[ny][nx].puzzle += (-getL(board[ny][nx].puzzle) + (2 - getR(board[y][x].puzzle))) * LEFT; 
                    break;
                case 2:
                    board[ny][nx].puzzle += (-getU(board[ny][nx].puzzle) + (2 - getD(board[y][x].puzzle))) * UP;
                    break;
                case 3:
                    board[ny][nx].puzzle += (-getR(board[ny][nx].puzzle) + (2 - getL(board[y][x].puzzle))) * RIGHT;
                    break;
            }
            
            if (++board[ny][nx].nearPuzzles == 4 && !board[ny][nx].isExist) { // 만약 퍼즐이 존재한다면 그냥 사방에 퍼즐이 있는 구멍 아닌 퍼즐임
                int puzzle = getRotatedPuzzle.get(board[ny][nx].puzzle);
                holes.put(puzzle, holes.getOrDefault(puzzle, 0) + 1); // 상하좌우에 모두 퍼즐 또는 벽으로 이루어졌지만 현재 칸에는 퍼즐이 없는 경우 구멍 
            }
        }
    }

    private static void removePuzzle(int y, int x) {
        for (int dir = 0; dir < 4; dir++) {
            int ny = y + dy[dir];
            int nx = x + dx[dir];

            if (ny < 0 || ny >= board.length || nx < 0 || nx >= board.length || board[ny][nx] == null) continue;
            if (board[ny][nx].nearPuzzles-- == 4 && !board[ny][nx].isExist) { // 현재 삭제하려는 퍼즐의 근처에 구멍이 있다면
                int puzzle = getRotatedPuzzle.get(board[ny][nx].puzzle);
                holes.put(puzzle, holes.get(puzzle) - 1); // 더 이상 구멍이 아님
            }
        }
        
        board[y][x].isExist = false; // 없어요 없어
        if (board[y][x].nearPuzzles == 4) {
            int puzzle = getRotatedPuzzle.get(board[y][x].puzzle);
            holes.put(puzzle, holes.getOrDefault(puzzle, 0) + 1); // 이번에 삭제된 퍼즐이 구멍이 되는 경우 추가 
        }
    }

    private static void addSparePuzzle(int u, int r, int d, int l) {
        int spare = getRotatedPuzzle.get(UP * u + RIGHT * r + DOWN * d + LEFT * l);
        numSparePuzzles.put(spare, numSparePuzzles.get(spare) + 1);
    }

    private static long getScore() {
        long score = 0;
    
        for (int puzzle : holes.keySet()) {
            score += (long) numSparePuzzles.get(puzzle) * holes.get(puzzle); // spare 개수 * hole 개수
        }
        return score;
    }

    private static int getU(int puzzle) {
        return puzzle / UP; // 1234 / 1000 = 1
    }

    private static int getR(int puzzle) {
        return puzzle % UP / RIGHT; // 1234 -> 234 -> 2
    }

    private static int getD(int puzzle) {
        return puzzle % RIGHT / DOWN; // 1234 -> 34 -> 3
    }

    private static int getL(int puzzle) {
        return puzzle % DOWN; // 1234 -> 4
    }

    private static void findRotatedPuzzle() {
        for (int u = 0; u < 3; u++) {
            for (int r = 0; r < 3; r++) {
                for (int d = 0; d < 3; d++) {
                    for (int l = 0; l < 3; l++) {
                        int p1 = UP * u + RIGHT * r + DOWN * d + LEFT * l;
                        int p2 = (p1 % 1000) * 10 + u;
                        int p3 = (p2 % 1000) * 10 + r;
                        int p4 = (p3 % 1000) * 10 + d;
                        int min = Math.min(p1, Math.min(p2, Math.min(p3, p4)));
                        getRotatedPuzzle.put(p1, min);
                        numSparePuzzles.put(min, 0);
                    }
                }
            }
        }
    }
}
