import java.util.*;

class Solution {
    
    public int solution(int[][] routes) {
        Arrays.sort(routes, (r1, r2) -> { return r1[0] - r2[0]; });
//         ArrayList에 담고 정렬 -> 시간 초과
//         ArrayList<int[]> rlist = new ArrayList<>();
//         for (int[] route : routes) {
//             rlist.add(route);
//         }
//         rlist.sort(Comparator.comparingInt((int[] r) -> r[0]));
        
        int cameras = 0;
        int cameraSpot = routes[0][1];
        for (int[] r : routes) {
            if (r[0] > cameraSpot) { // 현재 차량의 진입 지점이 기존 카메라보다 뒤에 있는 경우 새 카메라 설치
                cameraSpot = r[1];
                cameras++;
            } else if (r[1] < cameraSpot) { // 현재 차량이 기존 카메라 설치 지점보다 먼저 나가는 경우 카메라 위치 조정
                cameraSpot = r[1];
            }
        }
        return ++cameras;
    }
}

