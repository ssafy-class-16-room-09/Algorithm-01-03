import java.util.*;
import java.io.*;

class Streetlight {
    int idx;
    int loc;
    Streetlight left = null;
    Streetlight right = null;
    public Streetlight(int idx, int loc) {
        this.idx = idx;
        this.loc = loc;
    }

    public String toString() {
        int leftIdx = -2;
        if (left != null) leftIdx = left.idx;
        int rightIdx = -2;
        if (right != null) rightIdx = right.idx;
        return String.format("idx: %d, loc: %d, leftIdx: %d, rightIdx: %d", idx, loc, leftIdx, rightIdx);
    }
}

class Road {
    int leftIdx;
    int rightIdx;
    int dist;
    public Road(int leftIdx, int rightIdx, int dist) {
        this.leftIdx = leftIdx;
        this.rightIdx = rightIdx;
        this.dist = dist;
    }
}

public class Main {

    static ArrayList<Streetlight> streetlights = new ArrayList<>();
    static boolean[] isRemoved = new boolean[200001];
    static PriorityQueue<Road> pq = new PriorityQueue<>(Comparator.comparingInt((Road r) -> r.dist).reversed().thenComparing((Road r1, Road r2) -> {
        if (r1.leftIdx == -1) return -1;
        if (r2.leftIdx == -1) return 1;
        return Integer.compare(streetlights.get(r1.leftIdx).loc, streetlights.get(r2.leftIdx).loc);
    }));
    static int N;
    static Streetlight START = new Streetlight(-1, 1);
    static Streetlight END;
    static int lastStreetlightIdx;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int numCmd = Integer.parseInt(br.readLine());
        StringTokenizer st;
        while (numCmd-- > 0) {
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case 100:
                    init(st);
                    break;
                case 200:
                    addStreetlight();
                    break;
                case 300:
                    removeStreetlight(Integer.parseInt(st.nextToken()));
                    break;
                case 400:
                    bw.write(getMinPowerConsumption() + "\n");
                    break;
            }
        }
        bw.flush();
        bw.close();
        br.close();
    }

    private static void init(StringTokenizer st) {
        N = Integer.parseInt(st.nextToken());
        END = new Streetlight(-1, N);

        lastStreetlightIdx = Integer.parseInt(st.nextToken());
        streetlights.add(new Streetlight(-1, -1)); // 시작 index를 1로 설정
        for (int i = 1; i <= lastStreetlightIdx; i++) {
            streetlights.add(new Streetlight(i, Integer.parseInt(st.nextToken())));
        }
        for (int i = 1; i <= lastStreetlightIdx; i++) {
            if (i != 1) {
                streetlights.get(i).left = streetlights.get(i - 1);
            }
            if (i != lastStreetlightIdx) {
                streetlights.get(i).right = streetlights.get(i + 1);
                pq.add(new Road(i, i + 1, (streetlights.get(i + 1).loc - streetlights.get(i).loc)));
            }
        }
        streetlights.get(1).left = START;
        START.right = streetlights.get(1);
        streetlights.get(lastStreetlightIdx).right = END;
        END.left = streetlights.get(lastStreetlightIdx);

        pq.add(new Road(-1, 1, (streetlights.get(1).loc - START.loc) * 2)); // 양 끝 가로등
        pq.add(new Road(lastStreetlightIdx, -1, (END.loc - streetlights.get(lastStreetlightIdx).loc) * 2));
    }

    private static void addStreetlight() {
        Road road;
        ArrayList<Road> temp = new ArrayList<>();
        while (true) {
            road = pq.poll();
            if (road.leftIdx == -1 || road.rightIdx == -1) {
                temp.add(road);
                continue;
            }
            if (isRemoved[road.leftIdx] || isRemoved[road.rightIdx]) {
                continue;
            }
            if (streetlights.get(road.rightIdx).left.idx != road.leftIdx || streetlights.get(road.leftIdx).right.idx != road.rightIdx) {
                continue;
            }
            pq.add(road);
            break;
        }
        pq.addAll(temp);

        streetlights.add(new Streetlight(++lastStreetlightIdx, (streetlights.get(road.rightIdx).loc + streetlights.get(road.leftIdx).loc + 1) / 2));
        streetlights.get(road.leftIdx).right = streetlights.get(lastStreetlightIdx);
        streetlights.get(road.rightIdx).left = streetlights.get(lastStreetlightIdx);
        streetlights.get(lastStreetlightIdx).left = streetlights.get(road.leftIdx);
        streetlights.get(lastStreetlightIdx).right = streetlights.get(road.rightIdx);
        pq.add(new Road(road.leftIdx, lastStreetlightIdx, (streetlights.get(lastStreetlightIdx).loc - streetlights.get(road.leftIdx).loc)));
        pq.add(new Road(lastStreetlightIdx, road.rightIdx, (streetlights.get(road.rightIdx).loc - streetlights.get(lastStreetlightIdx).loc)));
    }

    private static void removeStreetlight(int removeIdx) {
        isRemoved[removeIdx] = true;
        Streetlight remove = streetlights.get(removeIdx);
        remove.left.right = remove.right;
        remove.right.left = remove.left;

        if (remove.left.idx == -1 || remove.right.idx == -1) {
            pq.add(new Road(remove.left.idx, remove.right.idx, (remove.right.loc - remove.left.loc) * 2));
        } else {
            pq.add(new Road(remove.left.idx, remove.right.idx, (remove.right.loc - remove.left.loc)));
        }
    }

    private static int getMinPowerConsumption() {
        Road road;
        while (true) {
            road = pq.poll();
            if ((road.leftIdx != -1 && isRemoved[road.leftIdx]) || (road.rightIdx != -1 && isRemoved[road.rightIdx])) continue;
            if ((road.leftIdx != -1 && streetlights.get(road.leftIdx).right.idx != road.rightIdx) || (road.rightIdx != -1 && streetlights.get(road.rightIdx).left.idx != road.leftIdx)) continue;
            
            pq.add(road);
            
            return road.dist;
        }
    }
}