import java.util.*;

class Job {
    int index, requestTime, duration;
    Job(int index, int requestTime, int duration) {
        this.index = index;
        this.requestTime = requestTime;
        this.duration = duration;
    }
}

class Solution {
    public int solution(int[][] jobs) {
        ArrayList<Job> jList = new ArrayList<>(); // 요청이 들어온 순서대로 정렬된 job list
        for (int i = 0; i < jobs.length; i++) {
            jList.add(new Job(i, jobs[i][0], jobs[i][1]));
        }
        jList.sort(Comparator.comparingInt((Job j) -> j.requestTime));
        
        PriorityQueue<Job> pq = new PriorityQueue<>(Comparator.comparingInt((Job j) -> j.duration)
                                                    .thenComparingInt(j -> j.requestTime)
                                                    .thenComparingInt(j -> j.index));
        int totalDuration = 0;
        int jIndex = 0;
        int currTime = -1;
        int currJobLeftTime = 0;
        Job curr = null;
        while (jIndex < jobs.length || (!pq.isEmpty() || currJobLeftTime > 0)) {
            currTime++;
            if (--currJobLeftTime == 0 && curr != null) { // 현재 작업이 다 처리되었으면
                totalDuration += (currTime - curr.requestTime);
            }
                      
            while (jIndex < jobs.length && jList.get(jIndex).requestTime <= currTime) { // 현재 시간 이전에 요청이 들어온 job을 pq에 모두 추가
                pq.add(jList.get(jIndex++));
            }
                
            if (currJobLeftTime <= 0) { // 현재 처리 중인 작업이 없는 경우
                if (!pq.isEmpty()) {
                    curr = pq.poll(); // 대기 큐에 작업이 있다면 그중 우선순위가 높은 작업 처리 시작
                    currJobLeftTime = curr.duration;
                }
            }
        }
        
        return totalDuration / jobs.length;
    }
}