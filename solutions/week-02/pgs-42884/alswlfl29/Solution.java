import java.util.*;

class Solution {
    public int solution(int[][] routes) {
        int answer = 1;
        
        Arrays.sort(routes, (a, b) -> a[0]-b[0]); // 차량 진입 시점 기준 오름차순 정렬
        
        int minOutTime = routes[0][1]; // 제일 처음 진입하는 차량의 진출 지점
        for(int r=1; r<routes.length; r++) {
            if(minOutTime >= routes[r][0]) {
                minOutTime = Math.min(minOutTime, routes[r][1]);
            } else {
                answer++; // 카메라 추가
                minOutTime = routes[r][1];
            }
        }
        return answer;
    }
}

/*
모든 차량이 단속용 카메라를 한 번 이상 만나기 위한 단속 카메라 최소 개수

차량 번호 >  0   1   2   3
진입 시점 > -20 -18 -14 -5
나간 시점 > -15 -13 -5  -3

1. 진입 시점 기준 오름차순 정렬
2. 같은 카메라 내 나간 시점 최소 <= 다음 차량 진입 시점 => 같은 카메라
3. 그 외 다른 카메라

*/