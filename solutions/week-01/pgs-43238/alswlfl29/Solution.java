class Solution {
    public long solution(int n, int[] times) {

        long min = 1000000000;
        for(int time : times) {
            min = Math.min(min, time);
        }
        
        long left = 0;
        long right = min * n;
    
        while(left <= right) {
            long mid = (left+right)/2;
            
            long cnt = 0; // 심사 받을 수 있는 사람 수
            for(int time : times) {
                cnt += mid/time; // 각 심사대에서 mid 시간 안에 심사 받을 수 있는 사람 수
            }
            
            if(cnt < n) left = mid+1; // n명 모두 심사 X => 심사 시간 늘리기
            else right = mid-1; // n명 모두 심사 O => 심사 시간 줄이기
        }
        
        return left;
    }
}

/*
모든 사람이 심사를 받는데 걸리는 시간의 최솟값 구하기
n = 입국심사를 기다리는 사람 수
times = 각 심사관이 한 명을 심사하는 데 걸리는 시간

이분탐색 -> target: 심사 받는데 걸리는 시간
target 시간 내에 심사 받을 수 있는 사람의 수 = x
x < n -> 심사 시간 늘리기
x >= n -> 심사 시간 줄이기

** int형 범위 넘어감 => long 타입
*/