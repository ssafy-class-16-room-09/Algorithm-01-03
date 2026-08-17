class Solution {
    public int solution(int[][] triangle) {
        int answer = 0;
        
        // DP[r][c] = Math.max(DP[r+1][c], DP[r+1][c+1]) + t[r][c]
        int t = triangle.length;
        int[][] dp = new int[t+1][t+1]; // 삼각형 DP
        
        for(int r=dp.length-2; r>=0; r--) {
            for(int c=0; c<r+1; c++) {
                dp[r][c] = Math.max(dp[r+1][c], dp[r+1][c+1]) + triangle[r][c];
            }
        }
        
        return dp[0][0];
    }
}

/*
7
3 8
8 1 0
2 7 4 4
4 5 2 6 5

아래 칸으로 이동 -> 대각선 방향으로 한칸 오른쪽, 왼쪽만 이동 가능
거쳐간 숫자의 최댓값 구하기
30
23 21
20 13 10
7 12 10 10
4 5  2  6  5

Bottom-UP DP 이용
DP[r][c] = Math.max(DP[r+1][c], DP[r+1][c+1]) + t[r][c]
*/