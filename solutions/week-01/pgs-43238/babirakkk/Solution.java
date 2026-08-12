import java.util.*;

class Solution {
    public long solution(int n, int[] times) {
        long left = 1;
        long right = (long) Arrays.stream(times).max().getAsInt() * n;
        long mid;
        while (left < right) {
            mid = (left + right) / 2;
            long availablePeople = 0;
            for (int time : times) {
                availablePeople += mid / time;
            }
            
            if (availablePeople < n) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}