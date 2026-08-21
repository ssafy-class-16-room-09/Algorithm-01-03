import java.util.*;

class Solution {
    public int solution(int[][] routes) {
        Arrays.sort(routes, (a, b) -> Integer.compare(a[1], b[1]));
        
        int cnt = 0;
        int recentCctv = Integer.MIN_VALUE;
        for(int[] route : routes) {
            if(recentCctv >= route[0] && recentCctv <= route[1])
                continue;
            else {
                cnt++;
                recentCctv = route[1];
            }
        }
        return cnt;
    }
}