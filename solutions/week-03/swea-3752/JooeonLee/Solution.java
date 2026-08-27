import java.util.*;
import java.io.*;
 
class Solution {
    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();
 
        int T = fr.nextInt();
 
        for (int testCase = 1; testCase <= T; testCase++) {
            int N = fr.nextInt();
 
            int[] points = new int[N];
            int maxScore = 0;
 
            for (int i = 0; i < N; i++) {
                points[i] = fr.nextInt();
                maxScore += points[i];
            }
 
            boolean[] check = new boolean[maxScore + 1];
            check[0] = true;
 
            int currentMax = 0;
 
            for (int point : points) {
                for (int score = currentMax; score >= 0; score--) {
                    if (check[score]) {
                        check[score + point] = true;
                    }
                }
 
                currentMax += point;
            }
 
            int count = 0;
            for (boolean possible : check) {
                if (possible) {
                    count++;
                }
            }
 
            sb.append('#')
              .append(testCase)
              .append(' ')
              .append(count)
              .append('\n');
        }
 
        System.out.print(sb);
    }
 
    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
 
        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }
 
        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}