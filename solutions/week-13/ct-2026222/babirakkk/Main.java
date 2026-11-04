import java.io.*;
import java.util.*;

public class Main {

    private static TreeMap<Long, Integer> town;
    private static long[][] requiredExtra; // rE[n][0]: 최소 비축량을 만들기 위한 총 추가 보급 수 rE[n][1]: 그때의 최소 비축량 rE[n][2]: 같은 최소 비축량을 가진 집 수
    private static long totalBoost;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int queries = Integer.parseInt(br.readLine());
        while (queries-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            switch (cmd) {
                case 100:
                    init();
                    long numHouses = Integer.parseInt(st.nextToken());
                    while (numHouses-- > 0) {
                        welcome(Long.parseLong(st.nextToken()));
                    }
                    fillRequiredExtra(town.firstKey());
                    break;
                case 200:
                    long have = Long.parseLong(st.nextToken()) - totalBoost;
                    welcome(have);
                    fillRequiredExtra(have);
                    break;
                case 300:
                    long boost = Integer.parseInt(st.nextToken());
                    upgrade(boost);
                    break;
                case 400:
                    bw.write(survive(Long.parseLong(st.nextToken())) + "\n");
                    break;
            }
        }
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init() {
        town = new TreeMap<>();
        requiredExtra = new long[21001][3];
        totalBoost = 0;
    }

    private static void welcome(long have) {
        town.put(have, town.getOrDefault(have, 0) + 1);
    }

    private static void fillRequiredExtra(long recentHave) { // 최근에 입주한 집 이후만 계산
        long extra = 0L;
        long minCount = 0;
        long minHave = 0L;
        int idx = 0;

        Iterator<Long> it = town.keySet().iterator();
        long have = 0L;
        while (it.hasNext()) {
            have = it.next();

            if (have < recentHave) {
                idx++;
                continue;
            }

            if (have == recentHave) { // 이제부터 계산 필요
                extra = (idx == 0) ? 0 : requiredExtra[idx - 1][0];
                minHave = (idx == 0) ? 0 : requiredExtra[idx - 1][1];
                minCount = (idx == 0) ? 0 : requiredExtra[idx - 1][2];
                break;
            }
        }
        
        while (true) {
            extra += (have - minHave) * minCount;
            minHave = have;
            minCount += town.get(minHave);

            requiredExtra[idx][0] = extra;
            requiredExtra[idx][1] = minHave;
            requiredExtra[idx][2] = minCount;

            idx++;

            if (!it.hasNext()) break;

            have = it.next();
        }
    }

    private static void upgrade(long boost) { // 메인에서 처리해도 되지만.. 그냥 만들고 싶었어요
        totalBoost += boost;
    }

    private static long survive(long extra) {
        int extraIdx = binarySearchAndGetKey(extra); // 주어진  보급량보다 작거나 같은 추가 보급량 찾기
        return requiredExtra[extraIdx][1] + (extra - requiredExtra[extraIdx][0]) / requiredExtra[extraIdx][2] + totalBoost;
    }

    private static int binarySearchAndGetKey(long extra) {
        int left = 0;
        int right = town.size() - 1;
        while (left < right) {
            int mid = (left + right + 1) / 2;
            if (requiredExtra[mid][0] <= extra) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
}
