import java.util.*;

class Solution {
    public int[] solution(int m, int n, int h, int w, int[][] drops) {
        int[][] grid = new int[m][n];
        int r = m;
        int c = n;
        for(int i=0; i<m; i++)
            Arrays.fill(grid[i], Integer.MAX_VALUE);
        for(int i=0; i<drops.length; i++) {
            int rIdx = drops[i][0];
            int cIdx = drops[i][1];
            grid[rIdx][cIdx] = i+1;
        }
        
        int[][] rowMax = new int[r][c-w+1];
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for(int i=0; i<r; i++) {
            dq.clear();
            for(int j=0; j<c; j++) {
                while(!dq.isEmpty() && dq.peekFirst() <= j-w) {
                    dq.pollFirst();
                }
                
                while(!dq.isEmpty() && grid[i][dq.peekLast()] > grid[i][j]) {
                    dq.pollLast();
                }
                
                dq.offerLast(j);
                
                if(j >= w-1) {
                    rowMax[i][j-w+1] = grid[i][dq.peekFirst()];
                }
            }
        }
        
        int[][] result = new int[r-h+1][c-w+1];
        for(int j=0; j<c-w+1; j++) {
            dq.clear();
            for(int i=0; i<r; i++) {
                while(!dq.isEmpty() && dq.peekFirst() <= i-h) {
                    dq.pollFirst();
                }
                
                while(!dq.isEmpty() && rowMax[dq.peekLast()][j] > rowMax[i][j]) {
                    dq.pollLast();
                }
                
                dq.offerLast(i);
                
                if(i >= h-1) {
                    result[i-h+1][j] = rowMax[dq.peekFirst()][j];
                }
            }
        }
        
        int max = -1;
        for(int i=0; i<r-h+1; i++) {
            for(int j=0; j<c-w+1; j++) {
                if(result[i][j] == Integer.MAX_VALUE)
                    return new int[]{i, j};
                else {
                    max = Math.max(max, result[i][j]);
                }
            }
        }
        for(int i=0; i<r-h+1; i++) {
            for(int j=0; j<c-w+1; j++) {
                if(result[i][j] == max)
                    return new int[]{i, j};
            }
        }
        return new int[]{-1, -1};
    }
}
