import java.io.*;
import java.util.*;

public class Solution {

    static int N;
    static TreeSet<Integer> lights;
    static ArrayList<Integer> position;
    static TreeSet<Gap> gaps;

    static class Gap {
        int left;
        int right;

        Gap(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int Q = fr.nextInt();

        for (int q = 0; q < Q; q++) {
            int command = fr.nextInt();

            if (command == 100) {
                N = fr.nextInt();
                int M = fr.nextInt();

                init();
                int prev = -1;

                for (int i = 0; i < M; i++) {
                    int x = fr.nextInt();

                    lights.add(x);
                    position.add(x);

                    if(prev != -1)
                        gaps.add(new Gap(prev, x));
                    
                    prev = x;
                }
            } else if (command == 200) {

                addLight();

            } else if (command == 300) {

                int id = fr.nextInt();
                removeLight(id);

            } else if (command == 400) {

                sb.append(getMinPower()).append('\n');
            }
        }

        System.out.print(sb);
    }

    static void init() {
        lights = new TreeSet<>();

        position = new ArrayList<>();

        position.add(-1);

        gaps = new TreeSet<>((a, b) -> {
            int distA = a.right - a.left;
            int distB = b.right - b.left;

            if (distA != distB) 
                return Integer.compare(distB, distA);

            if(a.left != b.left)
                return Integer.compare(a.left, b.left);

            return Integer.compare(a.right, b.right);
        });
    }

    static void addLight() {

        Gap gap = gaps.first();

        int left = gap.left;
        int right = gap.right;
        int mid = left + (right - left + 1) / 2;

        gaps.remove(gap);

        lights.add(mid);
        position.add(mid);

        gaps.add(new Gap(left, mid));
        gaps.add(new Gap(mid, right));
    }

    static void removeLight(int id) {

        int x = position.get(id);

        Integer left = lights.lower(x);
        Integer right = lights.higher(x);

        if(left != null)
            gaps.remove(new Gap(left, x));

        if(right != null)
            gaps.remove(new Gap(x, right));

        lights.remove(x);

        if(left != null && right != null) 
            gaps.add(new Gap(left, right));
    }

    static int getMinPower() {

        int first = lights.first();
        int last = lights.last();

        int leftPower = 2 * (first - 1);
        int rightPower = 2 * (N - last);

        int middlePower = 0;

        if(!gaps.isEmpty()) {
            Gap maxGap = gaps.first();
            middlePower = maxGap.right - maxGap.left;
        }

        return Math.max(
                Math.max(leftPower, rightPower),
                middlePower
        );
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        int nextInt() throws Exception {
            while (st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }

            return Integer.parseInt(st.nextToken());
        }
    }
}