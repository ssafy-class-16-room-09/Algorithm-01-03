import java.util.*;
import java.io.*;

public class Main {
    static int[] dr = {0, 1, 0, -1};
    static int[] dc = {1, 0, -1, 0};
    
    static int N, M, L;
    static boolean[] visited;
    static int[][] board;
    static HashSet<Integer> shapes;
    static final int MIN = -100_000;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        L = N*M;

        shapes = new HashSet<>();

        board = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        visited = new boolean[1<<L];
        for (int i=0; i<L; i++){
            dfs(1<<i, 1);
        }
        
        
        int answer = MIN;
        int S = shapes.size();
        ArrayList<Integer> list = new ArrayList<>(shapes);
        for (int i=0; i<S; i++){
            for (int j=i+1; j<S; j++){
                answer = Math.max(answer, score(list.get(i), list.get(j)));
            }
        }
        System.out.println(answer);
    }

    private static void dfs(int mask, int k){
        if(k == 5){
            shapes.add(mask);
            return;
        }

        for (int idx=0; idx<L; idx++){
            if ((mask & (1<<idx)) == 0) continue;
            int r = idx/M, c=idx%M;

            for (int d=0; d<4; d++){
                int nr=r+dr[d], nc=c+dc[d];
                if(nr<0 || nr>=N || nc<0 || nc>=M) continue;
                int pos = nr*M+nc;
                if ((mask & (1<<pos)) > 0) continue;
                dfs(mask|(1<<pos), k+1);
            }
        }
    }

    private static int score(int blue, int red){
        int cnt = 0;
        int res = 0;
        for (int i=0; i<L; i++){
            if((blue & (1<<i)) > 0 && (red & (1<<i)) > 0){
                cnt++;
                res += board[i/M][i%M] *2;
            } else if ((blue & (1<<i)) > 0 || (red & (1<<i)) > 0){
                res += board[i/M][i%M];
            }
            if (cnt>2) return MIN;
            
        }

        return cnt == 2 ? res : MIN;
    }
}

/*
N*M <= 16

mask 칸 방문
backTrack( mask)

*/