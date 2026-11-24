import java.util.*;
import java.io.*;

public class Main {
    static int[] dr = new int[] {-1, 1, 0, 0};
    static int[] dc = new int[] {0, 0, -1, 1};

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int N = Integer.parseInt(br.readLine());
        int[][] board = new int[N][N];
        for (int r=0; r<N; r++){
            st = new StringTokenizer(br.readLine());
            for (int c=0; c<N; c++){
                board[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        boolean[][][] visited = new boolean[N][N][4];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int d=0; d<4; d++){
            queue.offer(new int[] {1, 1, d, 0});
            visited[1][1][d] = true;
        }
        int answer = -1;

        while(!queue.isEmpty()){
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];
            int dir = curr[2];
            int time = curr[3];
            
            if(r == N-2 && c == N-2){
                answer = time;
                break;
            }
            int nr = r+dr[dir];
            int nc = c+dc[dir];
            if(board[nr][nc] == 0){
                queue.offer(new int[] {nr, nc, dir, time+1});
                visited[nr][nc][dir] = true;
                continue;
            }
            for(int d=0; d<4; d++){
                if(visited[r][c][d]) continue;
                visited[r][c][d] = true;
                queue.addFirst(new int[] {r, c, d, time});
            }
        }
        System.out.println(answer);
    }
}
