import java.util.*;
import java.io.*;

public class Solution {
    static class Work {
        int start;
        int end;
        int income;

        public Work(int start, int end, int income) {
            this.start = start;
            this.end = end;
            this.income = income;
        }
    }
    public static void main(String[] args) {
        // Please write your code here.
        FastReader fr = new FastReader();

        int n = fr.nextInt();
        Work[] works = new Work[n+1];

        for(int i=1; i<=n; i++) {
            int duration = fr.nextInt();
            int income = fr.nextInt();
            int end = i + duration - 1;

            works[i] = new Work(i, end, income);
        }

        // 수정함 처음에 -1이었음
        int maxIncome = 0;

        Queue<int[]> queue = new ArrayDeque<>();
        for(int i=1; i<=n; i++) {
            if(works[i].end > n)
                continue;
            queue.offer(new int[] {works[i].start, works[i].income});
            maxIncome = Math.max(maxIncome, works[i].income);
        }
        while(!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currStart = curr[0];
            int currIncome = curr[1];
            Work currWork = works[currStart];
            int nextPossibleStart = currWork.end + 1;
            if(nextPossibleStart > n)
                continue;

            for(int i=nextPossibleStart; i<=n; i++) {
                int nextIncome = currIncome + works[i].income;
                int nextEnd = works[i].end;
                if(nextEnd > n)
                    continue;
                maxIncome = Math.max(maxIncome, nextIncome);

                queue.offer(new int[]{i, nextIncome});
            }
        }

        System.out.println(maxIncome);
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while(st == null || !st.hasMoreTokens()) {
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
