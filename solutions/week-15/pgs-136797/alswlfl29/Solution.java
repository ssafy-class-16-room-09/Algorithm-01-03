import java.util.*;

class Solution {
    
    final int R = 4;
    final int C = 3;
    final int N = 12;
    final String positions = "123456789*0#";
    public int solution(String numbers) {
        int answer = Integer.MAX_VALUE;
        
        // 0: numbers 인덱스, 1: 왼손 위치, 2: 오른손 위치
        int[][][] dp = new int[numbers.length()+1][N][N];
        for(int i=0; i<=numbers.length(); i++) { // 초기화 
            for(int r=0; r<N; r++) {
                Arrays.fill(dp[i][r], Integer.MAX_VALUE);
            }
        }
        
        dp[0][3][5] = 0;
        
        for(int i=1; i<=numbers.length(); i++) {
            int pos = positions.indexOf(numbers.charAt(i-1));
            
            for(int l=0; l<N; l++) {
                for(int r=0; r<N; r++) {
                    if(l == r) continue; // 한 번호에 두 손가락 모두 존재 X
                    if(dp[i-1][l][r] != Integer.MAX_VALUE) {
                        // 왼손 이동
                        dp[i][pos][r] = Math.min(dp[i][pos][r], dp[i-1][l][r] + getCost(l, pos));
                        // 오른손 이동
                        dp[i][l][pos] = Math.min(dp[i][l][pos], dp[i-1][l][r] + getCost(r, pos));
                    }
                }
            }
        }
        
        for(int l=0; l<N; l++) {
            for(int r=0; r<N; r++) {
                answer = Math.min(answer, dp[numbers.length()][l][r]);
            }
        }
        
        return answer;
    }
    
    private int getCost(int from, int to) {
        int fr = from / C;
        int fc = from % C;
        int tr = to / C;
        int tc = to % C;
        
        if((fr == tr) && (fc == tc)) return 1;
        else if((fr == tr) || (fc == tc)) 
            return (Math.abs(fr-tr) + Math.abs(fc-tc)) * 2;
        else {
            int distanceR = Math.abs(fr-tr);
            int distanceC = Math.abs(fc-tc);
            int minD = Math.min(distanceR, distanceC);
            int maxD = Math.max(distanceR, distanceC);
            return minD * 3 + (maxD - minD) * 2;
        }
    }
}