import java.util.*;
import java.io.*;

public class Main {
    static int N;
    static int K;
    static int answer = Integer.MAX_VALUE;
    // 색별로 구분한 point 리스트
    static ArrayList<int[]>[] points;
    
    public static void main(String[] args) {
        FastReader fr = new FastReader();

        N = fr.nextInt();
        K = fr.nextInt();

        int[] x = new int[N];
        int[] y = new int[N];
        int[] c = new int[N];
        points = new ArrayList[K+1];
        for(int i=0; i<=K; i++)
            points[i] = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            x[i] = fr.nextInt();
            y[i] = fr.nextInt();
            c[i] = fr.nextInt();

            points[c[i]].add(new int[]{x[i], y[i]});
        }
        // Please write your code here.
        dfs(new State(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
        System.out.println(answer);
    }

    static class State {
        int maxR;
        int minR;
        int maxC;
        int minC;

        int selectCnt;

        public State(int maxR, int minR, int maxC, int minC, int selectCnt) {
            this.maxR = maxR;
            this.minR = minR;
            this.maxC = maxC;
            this.minC = minC;

            this.selectCnt = selectCnt;
        }
    }

    static void dfs(State s) {
        if(s.selectCnt == K) {
            answer = Math.min(answer, (s.maxR-s.minR)*(s.maxC-s.minC));
            return;
        }

        for(int[] point : points[s.selectCnt+1]) {
            int nMaxR = Math.max(s.maxR, point[0]);
            int nMinR = Math.min(s.minR, point[0]);
            int nMaxC = Math.max(s.maxC, point[1]);
            int nMinC = Math.min(s.minC, point[1]);

            int currWidth = (nMaxR-nMinR)*(nMaxC-nMinC);
            
            // 가지치기
            if(currWidth > answer)
                continue;

            dfs(new State(nMaxR, nMinR, nMaxC, nMinC, s.selectCnt+1));
        }
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while(st==null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
          return Integer.parseInt(next());
        }
    }
}
