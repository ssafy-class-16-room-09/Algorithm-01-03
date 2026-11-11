import java.util.*;

class Solution {
    public int solution(int[] A, int[] B) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        
        A = Arrays.stream(A)
            .boxed()
            .sorted(Collections.reverseOrder())
            .mapToInt(Integer::intValue)
            .toArray();

        for(int b : B)
            pq.offer(b);
        
        int answer = 0;
        for(int a : A) {
            int top = pq.peek();
            if(top > a) {
                answer++;
                pq.poll();
            }
        }
        return answer;
    }
}

/**
7 5 3 1
8 6 2 2

7 7 7 1
8 6 2 2
*/