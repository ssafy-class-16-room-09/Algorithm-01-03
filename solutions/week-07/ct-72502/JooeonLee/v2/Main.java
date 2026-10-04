import java.util.*;
import java.io.*;

public class Main {

    static Map<Integer, Integer> homeMap = new HashMap<>();
    static TreeSet<Integer> activeHomeId = new TreeSet<>();

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int Q = fr.nextInt();
        int N = 0;

        for (int i = 0; i < Q; i++) {
            int opt = fr.nextInt();

            if (opt == 100) {
                N = fr.nextInt();

                for (int j = 1; j <= N; j++) {
                    int pos = fr.nextInt();
                    homeMap.put(j, pos);
                    activeHomeId.add(j);
                }
            }

            if (opt == 200) {
                int pos = fr.nextInt();
                N++;

                homeMap.put(N, pos);
                activeHomeId.add(N);
            }

            if (opt == 300) {
                int removeId = fr.nextInt();
                activeHomeId.remove(removeId);
            }

            if (opt == 400) {
                int r = fr.nextInt();
                sb.append(paramSearch(r)).append('\n');
            }
        }

        System.out.print(sb);
    }

    static int paramSearch(int r) {
        if (activeHomeId.isEmpty())
            return 0;

        int left = 0;
        int right = homeMap.get(activeHomeId.last());
        int answer = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (canVisit(mid, r)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return answer;
    }

    static boolean canVisit(int time, int r) {
        if (activeHomeId.isEmpty())
            return true;

        int antCnt = 0;
        int startPos = -1;

        for (int id : activeHomeId) {
            int pos = homeMap.get(id);

            if (startPos == -1 || pos - startPos > time) {
                antCnt++;
                startPos = pos;

                if (antCnt > r)
                    return false;
            }
        }

        return true;
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (Exception e) {
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
