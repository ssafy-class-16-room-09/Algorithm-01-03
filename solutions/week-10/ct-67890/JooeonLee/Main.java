import java.util.*;
import java.io.*;

public class Main {

    static class Jewel {
        int weight, value;
        boolean active;

        Jewel(int weight, int value) {
            this.weight = weight;
            this.value = value;
            this.active = true;
        }
    }

    // 인덱스를 보석 번호로 사용하여 O(1)에 접근
    // 판매된 보석도 삭제하지 않고 active로 관리하여 번호 유지
    static ArrayList<Jewel> jewels = new ArrayList<>();

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int Q = fr.nextInt();

        jewels.add(null); // 보석 번호를 1부터 사용

        for (int q = 0; q < Q; q++) {
            int cmd = fr.nextInt();

            // 1. 보석 준비
            if (cmd == 1) {
                int N = fr.nextInt();

                for (int i = 0; i < N; i++) {
                    int w = fr.nextInt();
                    int v = fr.nextInt();

                    jewels.add(new Jewel(w, v));
                }
            }

            // 2. 보석 입고
            else if (cmd == 2) {
                int w = fr.nextInt();
                int v = fr.nextInt();

                jewels.add(new Jewel(w, v));
            }

            // 3. 보석 판매
            else if (cmd == 3) {
                int id = fr.nextInt();

                // 존재하지 않거나 이미 판매된 보석이면 -1
                if (id <= 0 || id >= jewels.size()
                        || !jewels.get(id).active) {
                    sb.append(-1).append('\n');
                    continue;
                }

                Jewel jewel = jewels.get(id);
                jewel.active = false;

                sb.append(jewel.value).append('\n');
            }

            // 4. 진열: 0/1 Knapsack DP
            // 무게 합이 W 이하인 보석들의 최대 가치 계산
            else if (cmd == 4) {
                int W = fr.nextInt();

                // dp[w]: 무게 합이 w 이하일 때 얻을 수 있는 최대 가치
                long[] dp = new long[W + 1];

                for (int i = 1; i < jewels.size(); i++) {
                    Jewel jewel = jewels.get(i);

                    // 판매된 보석 또는 제한 무게를 초과하는 보석 제외
                    if (!jewel.active || jewel.weight > W)
                        continue;

                    // 각 보석은 한 번만 선택할 수 있으므로 뒤에서부터 갱신
                    // 앞에서부터 갱신하면 현재 보석이 여러 번 사용될 수 있음
                    for (int w = W; w >= jewel.weight; w--) {
                        dp[w] = Math.max(
                            dp[w],
                            dp[w - jewel.weight] + jewel.value
                        );
                    }
                }

                sb.append(dp[W]).append('\n');
            }

            // 5. 세트 구성: 정렬 + 투 포인터
            // 무게 차이가 K 이하인 서로 다른 두 보석의 쌍 개수 계산
            else if (cmd == 5) {
                int K = fr.nextInt();

                // 현재 남아 있는 보석들의 무게만 추출
                int[] weights = new int[jewels.size() - 1];
                int size = 0;

                for (int i = 1; i < jewels.size(); i++) {
                    Jewel jewel = jewels.get(i);

                    if (jewel.active) {
                        weights[size++] = jewel.weight;
                    }
                }

                // 정렬하면 두 보석의 무게 차이를 구간으로 관리할 수 있음
                Arrays.sort(weights, 0, size);

                long answer = 0; // 쌍의 개수가 int 범위를 초과할 수 있음
                int left = 0;

                for (int right = 0; right < size; right++) {

                    // 무게 차이가 K를 초과하면 left를 오른쪽으로 이동
                    // 조건: weights[right] - weights[left] <= K
                    while (weights[right] - (long) weights[left] > K) {
                        left++;
                    }

                    // 정렬되어 있으므로 [left, right-1]의 모든 보석은
                    // right 보석과 무게 차이가 K 이하
                    // 따라서 새롭게 만들어지는 쌍은 right - left개
                    // right를 순차적으로 증가시켜 중복 없이 계산
                    answer += right - left;
                }

                sb.append(answer).append('\n');
            }
        }

        System.out.print(sb);
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public FastReader(String s) throws FileNotFoundException {
            br = new BufferedReader(new FileReader(s));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
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