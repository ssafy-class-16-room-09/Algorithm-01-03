import java.io.*;
import java.util.*;

public class Main {

    static TreeSet<Integer> village; // 개미집의 위치를 관리하는 트리셋
    static Map<Integer, Integer> housePositionById; // 개미집의 id로 위치를 찾기 위한 맵
    static int lastHouseId; // 현재까지 발급된 마지막 개미집 ID

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        village = new TreeSet<>();
        housePositionById = new HashMap<>();
        lastHouseId = 0;

        int queries = Integer.parseInt(br.readLine());
        while (queries-- > 0) {
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case 100:
                    int initHouses = Integer.parseInt(st.nextToken());
                    while (initHouses-- > 0) {
                        buildAntHouse(Integer.parseInt(st.nextToken()));
                    }
                    break;
                case 200:
                    buildAntHouse(Integer.parseInt(st.nextToken()));
                    break;
                case 300:
                    removeAntHouse(Integer.parseInt(st.nextToken()));
                    break;
                case 400:
                    bw.write(scoutAntHouse(Integer.parseInt(st.nextToken())) + "\n");
                    break;
            }
        }

        bw.flush();
        bw.close();
        br.close();
    }


    private static void buildAntHouse(int buildPosition) {
        village.add(buildPosition);
        housePositionById.put(++lastHouseId, buildPosition);
    }


    private static void removeAntHouse(int removeId) {
        int removePosition = housePositionById.get(removeId); // id로 위치를 찾고 삭제
        village.remove(removePosition);
    }


    private static int scoutAntHouse(int scoutAnts) {
        if (village.size() <= scoutAnts) return 0; // 현재 개미집 개수보다 일 개미 수가 더 많을 경우 이동 없이 모두 정찰 가능

        int left = 1;
        int right = (village.last() - village.first()) / scoutAnts + 1;
        int mid;
        while (left < right) {
            mid = left + (right - left) / 2;
            if (isVillageSafe(scoutAnts, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private static boolean isVillageSafe(int scoutAnts, int dist) {
        int usedScoutAnts = 0; // 정찰에 필요한 개미 수
        int nextUnsafeHousePosition = village.first();
        int lastHousePosition = village.last();
        while (usedScoutAnts < scoutAnts) {
            usedScoutAnts++;
            if (nextUnsafeHousePosition + dist >= lastHousePosition) return true; // 모든 집을 정찰할 수 있음
            nextUnsafeHousePosition = village.higher(nextUnsafeHousePosition + dist);
        }
        return false; // 모든 일 개미가 다 dist만큼 이동해도 모든 집을 정찰할 수 없음
    }
}


