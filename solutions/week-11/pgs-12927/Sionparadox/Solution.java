import java.util.*;

class Solution {
    public long solution(int n, int[] works) {
        PriorityQueue<Integer> pq = new PriorityQueue(Collections.reverseOrder());
        for (int work:works){
            pq.offer(work);
        }
        
        while (n>0 && !pq.isEmpty()){
            int curr = pq.poll();
            curr -= 1;
            if (curr != 0) pq.offer(curr);
            n -= 1;
            
        }
        
        long answer = 0;
        while (!pq.isEmpty()){
            answer += Math.pow(pq.poll(), 2);
        }
        
        return answer;
    }
}