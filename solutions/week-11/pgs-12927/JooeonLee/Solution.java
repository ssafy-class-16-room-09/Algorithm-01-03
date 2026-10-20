/**
works 20000 이하 n^2 안됨
뭔가 큰 크기들을 하나씩 줄이면 될 것 같은데 왜 그런지 논지적으로 설명을 못하겠네
일단 구현 해보기

풀이 후 논리 보충
x라는 값이 있고 여기서 하나 줄이면 x-1
이렇게 되었을 때 제곱의 차는 다음과 같음 x^2 - (x^2 - 2x + 1) = 2x-1
따라서 x값이 가장 큰 것을 하나씩 줄이는 것이 그리디하게 맞는 풀이임

뭔가 직관으로 푸는 느낌쓰
*/

import java.util.*;

class Solution {
    public long solution(int n, int[] works) {
        long answer = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        
        for(int work : works)
            pq.offer(work);
        
        int cnt = 0;
        while(cnt < n && !pq.isEmpty()) {
            int curr = pq.poll();
            curr--;
            cnt++;
            
            if(curr == 0)
                continue;
            
            pq.offer(curr);
        }
        
        while(!pq.isEmpty()) {
            int curr = pq.poll();
            answer += (long) curr * curr;
        }
        
        return answer;
    }
}