import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int N = Integer.parseInt(br.readLine());
        int[][][] scores = new int[3][N][2];
        int[][] total = new int[N][2];
        for (int contest = 0; contest < 3; contest++) {
            st = new StringTokenizer(br.readLine());
            for (int i=0; i<N; i++) {
                scores[contest][i][0] = Integer.parseInt(st.nextToken());
                scores[contest][i][1] = i;
                total[i][0] += scores[contest][i][0];
            }
        }
        for (int i=0; i<N; i++){
            total[i][1] = i;
        }

        int[][] ranks = new int[4][N];

        
        for (int contest=0; contest<3; contest++){
            Arrays.sort(scores[contest], (o1, o2) -> Integer.compare(o2[0], o1[0]));
            
            int rnk = 1;
            for (int i=0; i<N; i++){
                if (i==0 || scores[contest][i][0] != scores[contest][i-1][0]) rnk = i+1;

                int k = scores[contest][i][1];
                ranks[contest][k] = rnk;
            }
        }

        Arrays.sort(total, (o1, o2) -> Integer.compare(o2[0], o1[0]));
        int rnk = 0;
        for (int i=0; i<N; i++){
            if (i==0 || total[i][0] != total[i-1][0]) rnk = i+1;
            int k = total[i][1];
            ranks[3][k] = rnk;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<4; i++){
            for (int j=0; j<N; j++){
                sb.append(ranks[i][j]).append(" ");
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }
}

/*
40 80 70
50 10 20
100 70 30

190 160 120

*/