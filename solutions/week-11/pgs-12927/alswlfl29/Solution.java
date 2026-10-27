import java.util.*;
class Solution {
    public long solution(int n, int[] works) {        
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int total = 0; // 남은 작업량의 총합
        for(int work : works) {
            total += work;
            pq.offer(work);
        }
        
        if(total <= n) return 0; // 남아있는 작업이 N 시간보다 작거나 같은 경우 야근 X
        
        while(!pq.isEmpty()) {
            if(n == 0) break; // 퇴근 시간 지난 경우
            
            int work = pq.poll(); // 엄청 많이 남은 작업 꺼내기
            pq.offer(work-1); // 해당 작업량 줄이기
            n--;
        }
        
        long minTired = 0; // 친절하군!
        while(!pq.isEmpty()) {
            int work = pq.poll(); 
            minTired += work * work; // 야근 지수 구하기
        }

        return minTired;
    }
}