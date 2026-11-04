import java.io.*;
import java.util.*;

public class Main {
    static TreeMap<Long, Long> foodCounts = new TreeMap<>();
    static long diff;

    static boolean dirty = true;
    static long[] levels;
    static long[] costs;
    static long[] counts;
    static int size;

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int q = fr.nextInt();

        for(int i = 0; i < q; i++) {
            int command = fr.nextInt();

            switch(command) {
                case 100: {
                    int n = fr.nextInt();

                    for(int j = 0; j < n; j++) {
                        long food = fr.nextLong();
                        foodCounts.merge(food, 1L, Long::sum);
                    }

                    dirty = true;
                    break;
                }
                case 200: {
                    long food = fr.nextLong();
                    foodCounts.merge(food - diff, 1L, Long::sum);
                    dirty = true;
                    break;
                }
                case 300: {
                    diff += fr.nextLong();
                    break;
                }
                case 400: {
                    long budget = fr.nextLong();
                    sb.append(getWinter(budget)).append('\n');
                    break;
                }
            }
        }

        System.out.print(sb);
    }

    static void buildCache() {
        int groupCount = foodCounts.size();
        levels = new long[groupCount];
        costs = new long[groupCount];
        counts = new long[groupCount];
        size = 0;

        long prevLevel = foodCounts.firstKey();
        long cost = 0;
        long count = 0;

        for(Map.Entry<Long, Long> entry : foodCounts.entrySet()) {
            long level = entry.getKey();
            long gap = level - prevLevel;

            if(count > 0 && gap > (Long.MAX_VALUE - cost) / count) {
                break;
            }

            cost += gap * count;
            count += entry.getValue();

            levels[size] = level;
            costs[size] = cost;
            counts[size] = count;
            size++;

            prevLevel = level;
        }

        dirty = false;
    }

    static long getWinter(long budget) {
        if(dirty) {
            buildCache();
        }

        int left = 0;
        int right = size - 1;
        int answer = 0;

        while(left <= right) {
            int mid = left + (right - left) / 2;

            if (costs[mid] <= budget) {
                answer = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        long remaining = budget - costs[answer];
        return levels[answer] + remaining / counts[answer] + diff;
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
                    String line = br.readLine();
                    if(line == null) {
                        throw new EOFException();
                    }
                    st = new StringTokenizer(line);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }
    }
}