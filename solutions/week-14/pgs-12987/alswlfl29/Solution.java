import java.util.*;

class Solution {
    public int solution(int[] A, int[] B) {
        int answer = -1;
        
        int len = A.length; // 각 팀의 출전 선수 수
        // 두 팀 출전 순서 오름차순 정렬
        Arrays.sort(A);
        Arrays.sort(B);
        
        // 투 포인터 이용
        // A팀이 더 큰 경우 start 이동(크기 유지) 및 갱신
        // A팀이 더 작은 경우 A팀 인덱스 이동
        int pointA = 0; // 비교 대상 A 팀원의 위치
        int start = 0;
        int end = start;
        while(end < len) {
            if(A[pointA] < B[end]) pointA++;
            else {
                answer = Math.max(answer, end-start);
                start++;
            }
            end++;
        }
        answer = Math.max(answer, end-start);
        
        return answer;
    }
}