import java.util.*;

public class Main {
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    static int[][] board;
    static boolean[][] visited;
    static int[] selected = new int[5];
    static int N, M;
    static Set<String> found = new HashSet<>();
    static ArrayList<int[]> placements = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        board = new int[N][M];
        visited = new boolean[N][M];
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] = sc.nextInt();
            }
        }
        // Please write your code here.
        for(int r = 0; r < N; r++) {
            for(int c = 0; c < M; c++) {
                visited[r][c] = true;
                selected[0] = r * M + c;

                dfs(1); // 이미 한 칸을 골랐으므로 1

                visited[r][c] = false;
            }
        }

        int size = placements.size();
        int[] points = new int[size];

        for(int i=0; i<size; i++)
            points[i] = calc(placements.get(i));
        
        int answer = Integer.MIN_VALUE;
        
        for(int i=0; i<size; i++) {
            for(int j=i+1; j<size; j++) {
                int[] a = placements.get(i);
                int[] b = placements.get(j);

                if(overlapCnt(a, b) == 2)
                    answer = Math.max(answer, points[i]+points[j]);
            }
        }
        System.out.println(answer);
    }

    static void dfs(int count) {
        if (count == 5) {
            // 일단 선택한 5칸을 출력해 보자.
            int[] shape = selected.clone();
            Arrays.sort(shape);

            String key = Arrays.toString(shape);

            if (found.add(key)) {
                placements.add(shape);
            }

            return;
        }

        // 이미 선택한 모든 칸을 하나씩 확인
        for (int i = 0; i < count; i++) {
            int r = selected[i] / M;
            int c = selected[i] % M;

            // 그 칸의 상하좌우를 다음 후보로 확인
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr < 0 || nr >= N || nc < 0 || nc >= M) {
                    continue;
                }

                if (visited[nr][nc]) {
                    continue;
                }

                // 1. 선택
                visited[nr][nc] = true;
                selected[count] = nr * M + nc;

                // 2. 다음 칸 선택하러 가기
                dfs(count + 1);

                // 3. 선택 취소
                visited[nr][nc] = false;
            }
        }
    }

    static int overlapCnt(int[] a, int[] b) {
        int cnt = 0;
        for(int i : a) {
            for(int j : b) {
                if(i==j)
                    cnt++;
            }
        }

        return cnt;
    }

    static int calc(int[] a) {
        int sum = 0;
        
        for(int key : a) {
            int r = key / M;
            int c = key % M;

            sum += board[r][c];
        }
        return sum;
    }
}