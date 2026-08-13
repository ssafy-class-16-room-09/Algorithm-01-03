import java.util.*;

class Solution {
    public long solution(int n, int[] times) {
        
        return bs(times, n);
    }
    
    private boolean isPossible(long time, int[] times, int n) {
        long curr = 0;
        for(int t : times)
            curr += time / t;
        
        if(curr >= n)
            return true;
        else
            return false;
    }
    
    private long bs(int[] times, int n) {
        long right = 1000000000000000000L;
        long left = 0;
        
        while(left < right) {
            long mid = left + (right - left) / 2;
            
            if(isPossible(mid, times, n))
                right = mid;
            else
                left = mid+1;
        }
        return left;
    }
}

/**
n <= 10^9
n의 수가 너무 크니 무조건 n log n으로 해결해야함
그렇다면 매개변수 탐색 생각
그럼 매개변수를 무엇으로 둘 것인가? -> 시간(t)로 두자
시간을 매개변수로 두고 이분탐색 전개하면서 해당 시간에 모든 사람을 처리할 수 있는지를 확인하고 이분탐색 전개하면 될 것 같다

t <= 10^9
n*t <= 10^18

최대값이 10^18 이니 int 가 아닌 long으로 연산하자
-> 오버플로 문제 안 겪으려면 어떻게 해야하지??
*/