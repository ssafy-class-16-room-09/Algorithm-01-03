import java.util.*;
import java.io.*;

public class Main {
    static int N, answer;
    static int[][] arr;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        N = Integer.parseInt(br.readLine());
        answer = Integer.MAX_VALUE;

        arr = new int[N][N];
        for (int r=0; r<N; r++){
            st = new StringTokenizer(br.readLine());
            for (int c=0; c<N; c++) {
                arr[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        dfs(0, 0, 0);
        System.out.println(answer);
    }

    private static void dfs(int idx, int k, int mask) {
        // 계산 로직
        if (k == N/2) {
            answer = Math.min(answer, calc(mask));
            // System.out.println("idx: "+idx+", k: "+k+", mask: "+mask+", answer: "+answer);
            return;
        }

        // 부족하게 골라온 경우 가지치기
        // 고른 개수 = k, 남은 개수 : N-idx
        if (k + N-idx < N/2) return;
        
        dfs(idx+1, k, mask);
        dfs(idx+1, k+1, mask | (1<<idx));

    }

    private static int calc(int mask) {
        int morning = 0, evening = 0;
        int not = ~mask;

        for (int i=0; i<N-1; i++){
            for (int j=i+1; j<N; j++) {
                if ((mask & (1<<i)) > 0 && (mask & (1<<j)) > 0) morning += arr[i][j] + arr[j][i];
                if ((not & (1<<i)) > 0 && (not & (1<<j)) > 0) evening += arr[i][j] + arr[j][i];
            }
        }

        return Math.abs(morning - evening);
    }


}

/*
모두고려 가능?
가능할듯?

*/