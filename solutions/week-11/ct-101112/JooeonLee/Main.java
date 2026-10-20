import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int n = fr.nextInt();
        int[][] scores = new int[4][n];

        for (int contest = 0; contest < 3; contest++) {
            for (int i = 0; i < n; i++) {
                scores[contest][i] = fr.nextInt();
                scores[3][i] += scores[contest][i];
            }
        }

        for (int[] contestScores : scores) {
            appendRanks(contestScores, sb);
        }

        System.out.print(sb);
    }

    static void appendRanks(int[] scores, StringBuilder sb) {
        Integer[] sorted = Arrays.stream(scores)
                .boxed()
                .toArray(Integer[]::new);

        Arrays.sort(sorted, Comparator.reverseOrder());

        Map<Integer, Integer> rankByScore = new HashMap<>();

        for (int i = 0; i < sorted.length; i++)
            rankByScore.putIfAbsent(sorted[i], i + 1);

        for (int score : scores)
            sb.append(rankByScore.get(score)).append(' ');

        sb.append('\n');
    }

    static class FastReader {
        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}