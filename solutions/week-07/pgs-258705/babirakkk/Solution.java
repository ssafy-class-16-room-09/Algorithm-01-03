class Solution {
    
    private static final int MOD = 10007;
    
    public int solution(int n, int[] tops) {
        int[] totalDp = new int[n + 1];
        
        totalDp[0] = 1;
        totalDp[1] = (tops[0] == 1 ? 3 : 2) + 1;
        for (int i = 2; i <= n; i++) { // dp의 i번째 구간은 tops[i - 1]에 대응
            int uprightWays = totalDp[i - 1] * ((tops[i - 1] == 1) ? 3 : 2) % MOD;
            int invertedWays = (totalDp[i - 1] - totalDp[i - 2] + MOD) % MOD;
            totalDp[i] = (uprightWays + invertedWays) % MOD;
        }
        
        return totalDp[n];
    }
}