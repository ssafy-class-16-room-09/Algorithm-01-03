import java.io.*;
import java.util.*;

public class Main {

    static class Point {
        int x, y, color;
        public Point(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    static ArrayList<Point>[] groupByColor;
    static int N, K;
    static int minSquareSize;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        minSquareSize = Integer.MAX_VALUE;
        
        groupByColor = new ArrayList[K + 1];
        for (int i = 0; i <= K; i++) {
            groupByColor[i] = new ArrayList<>();
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            groupByColor[k].add(new Point(x, y, k));
        }

        dfs(1, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);

        System.out.println(minSquareSize);
    }

    private static void dfs(int color, int lx, int ux, int ly, int uy) {
        for (Point p : groupByColor[color]) { // 색깔별로 모아둔 점들 중 하나를 선택
            int nlx = Math.min(lx, p.x);
            int nux = Math.max(ux, p.x);
            int nly = Math.min(ly, p.y);
            int nuy = Math.max(uy, p.y);

            int currSquareSize = (nux - nlx) * (nuy - nly); // max - min 의 차는 점점 커지므로 현재까지의 사각형 크기가 최소 크기보다 크거나 같다면 더 이상 탐색할 필요가 없음

            if (currSquareSize >= minSquareSize) continue;

            if (color == K) {
                minSquareSize = currSquareSize;
                continue;
            }

            dfs(color + 1, nlx, nux, nly, nuy);
        }
    }
}