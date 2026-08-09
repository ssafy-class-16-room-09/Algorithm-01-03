import java.util.*;

class Job {
    int num;
    int start;
    int time;
    Job(int num, int start, int time){
        this.num = num;
        this.start = start;
        this.time = time;
    }
}

class Solution {
    public int solution(int[][] jobs) {
        int answer = 0;
        int L = jobs.length;
        Arrays.sort(jobs, (o1, o2) -> Integer.compare(o1[0], o2[0]));
        PriorityQueue<Job> pq = new PriorityQueue<>(
            Comparator.comparingInt((Job o) -> o.time)
            .thenComparingInt(o -> o.start)
            .thenComparingInt(o -> o.num));
        
        int cnt = 0;
        int index = 0;
        int time = 0;
        while (cnt < L) {
            while (index < L && jobs[index][0] <= time){
                int[] job = jobs[index];
                pq.offer(new Job(index, job[0], job[1]));
                index++;
            }
            if (pq.isEmpty()){
                time = jobs[index][0];
            } else {
                Job curr = pq.poll();
                time += curr.time;
                answer += time - curr.start;
                cnt++;
            }
        }
        
        return answer/L;
    }
}
/*
start 기준으로 정렬
루프돌며 시간 지난 작업 pq에 넣기
pq가 없으면 time = start
*/