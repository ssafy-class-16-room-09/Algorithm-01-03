import java.util.*;
class Solution {
    public int solution(int[][] routes) {
        int answer = 0;
        Arrays.sort(routes, (o1, o2) -> Integer.compare(o1[1], o2[1]));
        int pos = -30001;
        for (int[] route:routes){
            int s = route[0];
            int e = route[1];
            if (pos < s){
                pos = e;
                answer++;
            } 
        }
        
        return answer;
    }
}
/*
끝나는 지점을 기준으로 정렬
카메라 위치가 내 시작지점보다 작으면 내 끝나는 지점으로 카메라 위치 설정
*/