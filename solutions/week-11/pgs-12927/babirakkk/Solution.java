import java.util.*;

class Solution {
    public long solution(int n, int[] works) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int w : works) { // 남은 작업들을 모두 pq에 넣어요 
            pq.offer(w);
        }
        pq.offer(0);
        
        
        long stamina = 0L;
        int numHardestWork = 1;
        int hardestWorkTime = pq.poll();
        while (n > 0 && !pq.isEmpty()) {
            int nextWork = pq.peek(); // 다음으로 긴 작업 시간
            if ((hardestWorkTime - nextWork) * numHardestWork <= n) { // 현재까지 가장 긴 작업시간을 가진 작업들을 모두 nextWork로 만들 만큼의 시간이 남았다면
                n -= (hardestWorkTime - nextWork) * numHardestWork;
                numHardestWork++;    
                hardestWorkTime = nextWork;
                pq.poll();
            } else {
                break;
            }                
        }
        
        if (hardestWorkTime != 0) { // 만약 작업을 다 끝내지 못했다면
            // 최대 작업 시간인 작업이 3개 남았는데 5시간 더 일할 수 있다면? 두 개는 2시간, 한 개는 1시간 더 끝낼 수 있음 
            int mod = n % numHardestWork; 
            hardestWorkTime -= (n / numHardestWork);
            for (int i = 0; i < numHardestWork - mod; i++) {
                stamina += (long) hardestWorkTime * hardestWorkTime;
            }

            hardestWorkTime -= 1;
            for (int i = 0; i < mod; i++) {
                stamina += (long) hardestWorkTime * hardestWorkTime;
            }
        }
                
        while (!pq.isEmpty()) { // 남은 작업량에 해당하는 피로도 더해주기
            int work = pq.poll();
            System.out.println(stamina);
            stamina += (long) work * work;
        }
        return stamina;
    }
}

/*
ㅇㅋ 문제는 이해가 됐어 근데그래서 어떻게 구현을 해야 하는 걸까 
-> 무조건 큰 작업량을 줄이는 게 낫겠지
그치
works를 정렬해 -> PriorityQueue..?


*/