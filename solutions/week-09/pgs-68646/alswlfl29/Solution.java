import java.util.*;

class Solution {
    public int solution(int[] a) {
        
        if(a.length <= 2) return a.length; // 풍선의 개수가 2개 이하인 경우, 풍선 개수만큼 리턴
        
        int answer = 1; // 가장 작은 번호를 가진 풍선은 무조건 끝까지 생존 가능함
        
        // 가장 작은 번호를 가진 풍선의 인덱스와 값 찾기
        int minIndex = -1;
        int minValue = Integer.MAX_VALUE;
        for(int i=0; i<a.length; i++) {
            if(minValue > a[i]) {
                minValue = a[i];
                minIndex = i;
            }
        }
        
        if(minIndex != 0) {
            ArrayDeque<Integer> order = new ArrayDeque<>();
            // 가장 작은 원소 기준 왼쪽 탐색
            for(int i=minIndex-1; i>=0; i--) {
                while(!order.isEmpty() && order.peekLast() > a[i]) {
                    order.pollLast();
                }
                order.offer(a[i]);
            }
            answer += order.size(); // 단조 증가 배열 길이 더하기
        }
        
        if(minIndex != a.length-1) {
            ArrayDeque<Integer> order = new ArrayDeque<>();
            // 가장 작은 원소 기준 오른쪽 탐색
            for(int i=minIndex+1; i<a.length; i++) {
                while(!order.isEmpty() && order.peekLast() > a[i]) {
                    order.pollLast();
                }
                order.offer(a[i]);
            }
            answer += order.size(); // 단조 증가 배열 길이 더하기
        }
    
        return answer;
    }
}