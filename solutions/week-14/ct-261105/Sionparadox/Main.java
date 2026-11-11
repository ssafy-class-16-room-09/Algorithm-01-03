import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
    static int[][] grid;
    static boolean[][] visited;

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    static ArrayList<Integer> list;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        grid = new int[N][M];
        visited = new boolean[N][M];
        list = new ArrayList<>();

        for (int i = 0; i < N; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++)
                grid[i][j] = Integer.parseInt(st.nextToken());
        }
        bfs(0, 0);
        int prev = 0;
        int time = 0;
        while(true){
            time++;
            int cnt = melt();
            if(cnt == 0) break;
            prev = cnt;
        }
        
        System.out.println(time-1+" "+prev);
    }

    private static int melt(){
        ArrayDeque<Integer> queue = new ArrayDeque<>(list);
        list = new ArrayList<>();
        int ret = 0;
        while(!queue.isEmpty()){
            int curr = queue.poll();
            int r = curr/M;
            int c = curr%M;
            for (int d=0; d<4; d++){
                int nr = r+dr[d], nc = c+dc[d];
                if(nr<0 || nr>=N || nc<0 || nc>=M) continue;
                if(visited[nr][nc]) continue;
                visited[nr][nc] = true;
                if(grid[nr][nc] == 0) {
                    queue.offer(nr*M+nc);
                }
                else {
                    list.add(nr*M+nc);
                    grid[nr][nc] = 0;
                    ret++;
                }
            }
        }
        
        return ret;
    }

    private static void bfs(int r, int c){
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        visited[r][c] = true;
        queue.offer(r*M+c);
        list.add(r*M+c);

        while(!queue.isEmpty()){
            int curr = queue.poll();
            r = curr/M;
            c = curr%M;
            for (int d=0; d<4; d++){
                int nr = r+dr[d], nc = c+dc[d];
                if(nr<0 || nr>=N || nc<0 || nc>=M) continue;
                if(visited[nr][nc]) continue;
                if(grid[nr][nc] == 1) continue;
                visited[nr][nc] = true;
                list.add(nr*M+nc);
                queue.offer(nr*M+nc);
            }
        }
    }
}

/*
0인칸 다 리스트에 넣기
해당 리스트 다 큐에 넣고 리스트 비우기
리스트 주변 1칸씩만 보며 0이면 큐에 넣고 1이면 리스트에 넣기

*/
