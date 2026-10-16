import java.util.*;
import java.io.*;

public class Main {

    static class Gem {
        boolean isSold = false;
        int id;
        int weight;
        int value;
        Gem(int id, int weight, int value) {
            this.id = id;
            this.weight = weight;
            this.value = value;
        }
    }

    static ArrayList<Gem> gemList;
    static TreeSet<Gem> gemOrderByWeight; // 무게 순으로 정렬된 트리셋

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        gemList = new ArrayList<>();
        gemList.add(new Gem(-1, -1, -1)); // 인덱스 1부터 사용하기 위한 더미 객체
        gemOrderByWeight = new TreeSet<>(Comparator.comparingInt((Gem g) -> g.weight).thenComparingInt(g -> g.id));

        int queries = Integer.parseInt(br.readLine());
        while (queries-- > 0) {
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case 1:
                    int gems = Integer.parseInt(st.nextToken());
                    while (gems-- > 0) {
                        int w = Integer.parseInt(st.nextToken());
                        int v = Integer.parseInt(st.nextToken());
                        addGem(w, v);
                    }
                    break;
                case 2:
                    int w = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());
                    addGem(w, v);
                    break;
                case 3:
                    int sellId = Integer.parseInt(st.nextToken());
                    sellGem(sellId);
                    break;
                case 4:
                    int maxWeight = Integer.parseInt(st.nextToken());
                    displayGem(maxWeight);
                    break;
                case 5:
                    int weightDiff = Integer.parseInt(st.nextToken());
                    makeGemPairs(weightDiff);
                    break;
            }
        }
    }

    private static void addGem(int w, int v) {
        Gem gem = new Gem(gemList.size(), w, v);
        gemList.add(gem);
        gemOrderByWeight.add(gem);
    }

    private static void sellGem(int sellId) {
        int value = -1;
        if (sellId < gemList.size() && !gemList.get(sellId).isSold) { // 유효한 id이고 아직 팔리지 않은 보석이라면 
            value = gemList.get(sellId).value;
            gemList.get(sellId).isSold = true; // 판매 처리
        }
        System.out.println(value);
    }

    private static void displayGem(int maxWeight) {
        int[] showcase = new int[maxWeight + 1];
        for (int i = 1; i < gemList.size(); i++) {
            Gem g = gemList.get(i);
            if (g.isSold) continue; // 이미 판매된 보석이라면 건너뜀
            if (g.weight > maxWeight) continue; // 보석의 무게가 최대 무게보다 무겁다면 건너뜀
            for (int w = maxWeight; w >= g.weight; w--) {
                showcase[w] = Math.max(showcase[w], showcase[w - g.weight] + g.value);
            }
        }
        System.out.println(showcase[maxWeight]);
    }

    private static void makeGemPairs(int weightDiff) {
        int numPairs = 0;
        ArrayDeque<Gem> dq = new ArrayDeque<>();
        for (Gem g : gemOrderByWeight) {
            if (g.isSold) continue;
            while (!dq.isEmpty() && g.weight - dq.getFirst().weight > weightDiff) {
                dq.removeFirst(); // 현재 보석과 무게 차가 weightDiff보다 크다면 윈도우에서 제거
            }
            numPairs += dq.size(); // 윈도우 안의 모든 보석과 현재 보석은 세트를 만들 수 있음
            dq.add(g);
        }
        System.out.println(numPairs);
    }
}
