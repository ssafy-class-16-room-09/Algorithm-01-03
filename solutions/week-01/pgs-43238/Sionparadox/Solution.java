class Solution {
    public long solution(int n, int[] times) {
        long answer = 0;
        
        long maxTime = 0;
        for (int time:times){
            if (maxTime < time) maxTime = time;
        }
        long left = 0;
        long right = maxTime*n;
        
        while (left <= right){
            long mid = (left+right) / 2;
            if (check(mid, times, n)){
                right = mid-1;
                answer = mid;
            } else {
                left = mid+1;
            }
        }
        
        
        return answer;
    }
    
    private boolean check(long k, int[] times, int n){
        long sum = 0;
        for (int time : times){
            sum += k/time;
        }
        return sum >= n;
    }
}

/*
걸리는 시간에 대해 이분탐색
check로 확인
check : true >> 시간이 충분 & 시간을 줄여야함
*/