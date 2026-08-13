import java.util.*;

class Solution {
    public int solution(int[][] jobs) {
        ArrayList<int[]> jobList = new ArrayList<>();
        
        for(int i=0; i<jobs.length; i++) {
            int[] job = new int[] {jobs[i][0], jobs[i][1], i};
            jobList.add(job);
        }
        
        jobList.sort((a, b) -> {
            if(a[0] != b[0])
                return Integer.compare(a[0], b[0]);
            else
                return Integer.compare(a[2], b[2]);
        });
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if(a[1] != b[1])
                return Integer.compare(a[1], b[1]);
            else if(a[0] != b[0])
                return Integer.compare(a[0], b[0]);
            else
                return Integer.compare(a[2], b[2]);
            
        });
        int total = 0;
        int currTime = 0;
        int idx = 0;
        while(!pq.isEmpty() || idx < jobList.size()) {
            while(idx < jobList.size() && jobList.get(idx)[0] <= currTime) {
                int[] nextJob = jobList.get(idx);
                pq.offer(nextJob);
                idx++;
            }
            
            if(pq.isEmpty()) {
                currTime = jobList.get(idx)[0];
                continue;
            }
            
            int[] currJob = pq.poll();
            currTime += currJob[1];
            
            total += currTime - currJob[0];
        }
        
        return total / jobList.size();
    }
}