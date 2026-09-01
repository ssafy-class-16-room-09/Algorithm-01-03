import java.util.*;

class Solution {
    public int solution(int[] stones, int k) {
        
        return parametricSearch(stones, k);
    }
    
    public int parametricSearch(int[] stones, int k) {
        int left = 0;
        int right = Arrays.stream(stones)
            .max()
            .getAsInt();
        int answer=0;
        
        while(left <= right) {
            int mid = left + (right - left) / 2;
            
            if(canCross(stones, k, mid)) {
                answer = mid;
                left = mid+1;
            }
            else
                right = mid-1;
        }
        return answer;
    }
    
    public boolean canCross(int[] stones, int step, int cnt) {
        int max = 0;
        int currLength = 0;
        
        for(int i=0; i<stones.length; i++) {
            int currStone = stones[i] - cnt;
            if(currStone < 0) {
                currLength++;
                max = Math.max(max, currLength);
            }
            else {
                currLength = 0;
            }
        }
        
        return max < step;
    }
}