import java.util.*;
import java.io.*;

public class Main {

    // 집 번호 -> 집 정보
    // 철거된 집도 이후 번호 체계를 유지해야 하므로 homeMap에서는 삭제 안함
    static Map<Integer, Home> homeMap = new HashMap<>();

    // 현재 남아 있는 집 번호를 오름차순으로 관리
    // 삭제 시 바로 왼쪽/오른쪽 집을 lower(), higher()로 찾기 위해 사용
    static TreeSet<Integer> activeHomeIds = new TreeSet<>();

    // 현재 남아 있는 인접한 집 사이의 간격을 왼쪽 집 번호 순서대로 관리
    // 400번 명령에서 실제 집의 순서대로 간격을 순회하기 위해 사용
    static TreeSet<Gap> gaps = new TreeSet<>(
        Comparator.comparingInt(g -> g.left.id)
    );

    static int homeCount = 0;

    public static void main(String[] args) {
        FastReader fr = new FastReader();
        StringBuilder sb = new StringBuilder();

        int Q = fr.nextInt();

        for (int q = 0; q < Q; q++) {
            int command = fr.nextInt();

            switch (command) {
                case 100:
                    init(fr);
                    break;

                case 200:
                    addHome(fr.nextInt());
                    break;

                case 300:
                    removeHome(fr.nextInt());
                    break;

                case 400:
                    int antCount = fr.nextInt();
                    sb.append(parametricSearch(antCount)).append('\n');
                    break;
            }
        }

        System.out.print(sb);
    }

    // 초기 집들을 순서대로 연결하면서 인접한 집 사이의 gap을 생성
    static void init(FastReader fr) {
        homeCount = fr.nextInt();

        Home prev = null;

        for (int id = 1; id <= homeCount; id++) {
            int pos = fr.nextInt();

            Home curr = new Home(id, pos);
            homeMap.put(id, curr);
            activeHomeIds.add(id);

            if (prev != null) {
                gaps.add(new Gap(prev, curr));
            }

            prev = curr;
        }
    }

    // 새 집은 기존 집들의 오른쪽에 추가되므로
    // 현재 마지막 집과 새 집 사이의 gap 하나만 추가하면 됨
    static void addHome(int pos) {
        int newId = ++homeCount;
        Home newHome = new Home(newId, pos);

        if (!activeHomeIds.isEmpty()) {
            int lastId = activeHomeIds.last();
            Home lastHome = homeMap.get(lastId);

            gaps.add(new Gap(lastHome, newHome));
        }

        homeMap.put(newId, newHome);
        activeHomeIds.add(newId);
    }

    static void removeHome(int removeId) {
        /*
         * A - B - C에서 B를 삭제한다고 생각하면
         *
         * 기존 gap:
         * A-B, B-C
         *
         * 삭제 후 gap:
         * A-C
         *
         * 따라서 삭제할 집의 양옆을 먼저 찾고,
         * 기존 gap 두 개를 제거한 뒤 새로운 gap 하나를 연결
         */
        Integer leftId = activeHomeIds.lower(removeId);
        Integer rightId = activeHomeIds.higher(removeId);

        Home removed = homeMap.get(removeId);

        if (leftId != null) {
            Home left = homeMap.get(leftId);
            gaps.remove(new Gap(left, removed));
        }

        if (rightId != null) {
            Home right = homeMap.get(rightId);
            gaps.remove(new Gap(removed, right));
        }

        activeHomeIds.remove(removeId);

        if (leftId != null && rightId != null) {
            Home left = homeMap.get(leftId);
            Home right = homeMap.get(rightId);

            gaps.add(new Gap(left, right));
        }
    }

    /*
     * 정답을 "모든 개미가 정찰을 끝내는 최대 시간"이라고 두고 매개변수 탐색
     *
     * time 안에 r마리로 정찰 가능하다면
     * 더 큰 시간도 항상 가능!
     *
     * false false false true true true
     *
     * 형태의 단조성이 생기므로 가능한 최소 time을 이분 탐색
     */
    static int parametricSearch(int r) {
        if (activeHomeIds.isEmpty()) {
            return 0;
        }

        Home first = homeMap.get(activeHomeIds.first());
        Home last = homeMap.get(activeHomeIds.last());

        int left = 0;
        int right = last.pos - first.pos;
        int answer = right;

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

    /*
     * 한 개미가 담당하는 구간의 길이가 time을 넘지 않도록
     * 왼쪽부터 최대한 많은 집을 묶음
     *
     * 현재 개미가 다음 집까지 갈 수 없다면
     * 그 집부터 새로운 개미가 정찰을 시작
     *
     * 필요한 개미 수가 r 이하라면 time 안에 정찰 가능!
     */
    static boolean canVisit(int time, int r) {
        if (activeHomeIds.isEmpty()) {
            return true;
        }

        int antCount = 1;
        int distance = 0;

        for (Gap gap : gaps) {
            if (distance + gap.distance <= time) {
                distance += gap.distance;
            } else {
                antCount++;
                distance = 0;

                if (antCount > r) {
                    return false;
                }
            }
        }

        return true;
    }

    static class Home {
        int id;
        int pos;

        Home(int id, int pos) {
            this.id = id;
            this.pos = pos;
        }
    }

    static class Gap {
        Home left;
        Home right;
        int distance;

        Gap(Home left, Home right) {
            this.left = left;
            this.right = right;
            this.distance = right.pos - left.pos;
        }
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (Exception e) {
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