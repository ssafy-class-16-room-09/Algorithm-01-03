import java.util.*;

class Solution {
    
    final int[][] loc = {{3, 1}, 
                   {0, 0}, {0, 1}, {0, 2}, 
                   {1, 0}, {1 ,1}, {1, 2}, 
                   {2, 0}, {2, 1}, {2, 2}};
    
    public int solution(String numbers) {
        int[][] time = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                time[i][j] = getTime(i, j);
            }
        }
        
        int[] prev = new int[100]; // 손가락은 (0, 0) -> (9, 9)까지 있을 수 있음
        int[] curr = new int[100]; // 손가락은 (0, 0) -> (9, 9)까지 있을 수 있음
        Queue<Integer> q = new ArrayDeque<>();
        Set<Integer> next = new HashSet<>();
        q.add(46); // 왼손(4) + 오른손(6)
        for (int i = 0; i < numbers.length(); i++) {
            int currNum = numbers.charAt(i) - '0';
            int numPrevStatus = q.size();
            Arrays.fill(curr, Integer.MAX_VALUE);
            while (!q.isEmpty()) {
                int prevStatus = q.poll();
                
                if (currNum != prevStatus / 10) { // 두 손가락은 같은 곳에 위치할 수 없음
                    int left = (currNum * 10) + (prevStatus / 10);
                    next.add(left);
                    curr[left] = Math.min(curr[left], 
                                          prev[prevStatus] + time[prevStatus % 10][currNum]);
                }
                
                if (prevStatus % 10 != currNum) {
                    int right = prevStatus % 10 * 10 + currNum;
                    next.add(right);
                    curr[right] = Math.min(curr[right], 
                                          prev[prevStatus] + time[currNum][prevStatus / 10]);
                }
            }
            prev = curr.clone();     
            q.addAll(next);
            next.clear();
        }
        
        int minTotalTime = Integer.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            if (prev[i] == 0) continue;
            minTotalTime = Math.min(minTotalTime, prev[i]);
        }
        return minTotalTime;
    }
    
    private int getTime(int start, int end) {
        if (start == end) return 1;
        
        int dr = Math.abs(loc[start][0] - loc[end][0]);
        int dc = Math.abs(loc[start][1] - loc[end][1]);
        
        int time = 0;
        // 대각선으로 이동할 수 있는 만큼 우선 이동한 후 상하좌우 이동
        while (dr > 0 || dc > 0) {
            if (dr > 0 && dc > 0) {
                time += 3;
                dr -= 1;
                dc -= 1;
                continue;
            }
            
            time += 2;
            if (dr > 0) {
                dr -= 1;
            } else {
                dc -= 1;
            }
        }
        return time;
    }
}