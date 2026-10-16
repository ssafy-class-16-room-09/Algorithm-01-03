import java.util.*;
import java.io.*;

public class Main {
    static int N;
    static int K;
    static int[] positions;
    public static void main(String[] args) {
        FastReader fr = new FastReader();
        N = fr.nextInt();
        K = fr.nextInt();
        positions = new int[N];
        for (int i = 0; i < N; i++) {
            positions[i] = fr.nextInt();
        }
        // Please write your code here.
        System.out.println(paramSearch());
    }

    static int paramSearch() {
        int left = 1;
        int right = positions[N-1];

        int answer = positions[N-1];
        while(left <= right) {
            int mid = left + (right - left)/2;

            if(possible(mid)) {
                answer = mid;
                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
        }

        return answer;
    }

    static boolean possible(int length) {
        int cnt = 1;
        int curr = positions[0] + length - 1;
        for(int i=1; i<N; i++) {
            if(cnt > K)
                return false;
            if(positions[i] <= curr)
                continue;
            else {
                curr = positions[i] + length - 1;
                cnt++;
            }
        }
        
        if(cnt > K)
            return false;
        else
            return true;
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while(st==null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}