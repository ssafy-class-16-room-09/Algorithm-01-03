import java.util.*;

class Job {
    int requestTime; // 작업 요청 시각
    int workTime; // 작업 소요시간
    int number; // 작업 번호
    
    public Job(int number, int requestTime, int workTime) {
        this.number = number;
        this.requestTime = requestTime;
        this.workTime = workTime;
    }
    
    @Override
    public String toString() {
        return "[num="+number+", requestTime="+requestTime+", workTime="+workTime+"]";
    }
}

class Solution {
    public int solution(int[][] jobs) {
        int answer = 0;
        
        PriorityQueue<Job> diskPq = new PriorityQueue<>(new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                return Integer.compare(job1.requestTime, job2.requestTime);
            }
        });
        for(int j=0; j<jobs.length; j++) {
            diskPq.offer(new Job(j, jobs[j][0], jobs[j][1]));
        }
        
        PriorityQueue<Job> waitingPq = new PriorityQueue<>(new Comparator<Job>() {
            @Override
            public int compare(Job job1, Job job2) {
                if(job1.workTime != job2.workTime) return Integer.compare(job1.workTime, job2.workTime);
                if(job1.requestTime != job2.requestTime) return Integer.compare(job1.requestTime, job2.requestTime);
                return Integer.compare(job1.number, job2.number);
            }
        });
        
        int time = 0; // 현재 시각
        int total = 0; // 각 작업에 대한 반환 시간의 총합
        while(!diskPq.isEmpty() || !waitingPq.isEmpty()) {
            // 현재 시각 기준 작업 가능한 작업 대기 큐에 넣기
            while(!diskPq.isEmpty()) {
                Job job = diskPq.peek();
                if(job.requestTime <= time) {
                    waitingPq.offer(diskPq.poll());
                }
                else break;
            }
            
            // 대기 큐에 있는 작업 진행
            if(!waitingPq.isEmpty()) {
                Job job = waitingPq.poll();
                time += job.workTime; // 작업 시간 더하기
                total += (time-job.requestTime); // 반환 시간 더하기
            } else {
                time++; // 대기 큐에 작업 존재하지 않는 경우 시간 1초 늘리기
            }
        }

        return total/jobs.length;
    }
}