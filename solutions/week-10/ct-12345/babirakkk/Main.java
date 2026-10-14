import java.util.*;
import java.io.*;

public class Main {

    private static ArrayList<Integer> holes;
    private static int numPatches;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numHoles = Integer.parseInt(st.nextToken()); // 구멍 개수
        numPatches = Integer.parseInt(st.nextToken()); // 사용 가능한 패치의 최대 개수

        holes = new ArrayList<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < numHoles; i++) {
            holes.add(Integer.parseInt(st.nextToken()));
        }

        int left = 1;
        // int right = holes.getLast(); // java 21 부터 가능!
        int right = holes.get(holes.size() - 1) - holes.get(0) + 1;
        int mid;
        while (left < right) {
            mid = left + (right - left) / 2;
            if (canCoverAllHoles(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        System.out.println(left);
        br.close();
    }
    private static boolean canCoverAllHoles(int patchSize) {
        int nextHoleStartId = 0;
        int usedPatches = 0;
        while (usedPatches < numPatches) {
            usedPatches++;
            int nextUncoverHolePosition = holes.get(nextHoleStartId) + patchSize; // 현재 패치로 가려지지 않는 부분의 시작 위치
            if (nextUncoverHolePosition > holes.get(holes.size() - 1)) return true;
            
            while (++nextHoleStartId < holes.size() 
                && holes.get(nextHoleStartId) < nextUncoverHolePosition) {} // 다음으로 가려지지 않는 구멍의 id++
        }
        return false;
    }
}
