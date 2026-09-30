import java.util.*;
import java.io.*;

class Solution {
    
    final int MODULO = 10007;
    
    public int solution(int n, int[] tops) {
        int[] uprightDp = new int[n + 1];
        int[] invertedDp = new int[n + 1];
        int[] totalDp = new int[n + 1];
        
        totalDp[0] = uprightDp[0] = invertedDp[1] = 1;
        uprightDp[1] = (tops[0] == 1) ? 3 : 2;
        totalDp[1] = (uprightDp[1] + invertedDp[1]) % MODULO;
        for (int i = 2; i <= n; i++) {
            uprightDp[i] = totalDp[i - 1] * ((tops[i-1] == 1) ? 3 : 2) % MODULO;
            invertedDp[i] = (totalDp[i - 1] - totalDp[i - 2] + MODULO) % MODULO;
            totalDp[i] = (uprightDp[i] + invertedDp[i]) % MODULO;
        }
        
        return totalDp[n];
    }
}