import java.util.*;

class Solution {
    public int solution(int[][] info, int n, int m) {
        int answer = n;
        int L = info.length;
        int[] dp = new int[m];
        Arrays.fill(dp, n);
        dp[0] = 0;
        
        for (int[] arr:info){
            int a = arr[0];
            int b = arr[1];
            for(int i=m-1; i>=0; i--){
                if(i<b) {
                    dp[i] += a;
                    continue;
                }
                dp[i] = Math.min(dp[i-b], dp[i]+a);
                
                
            }
            System.out.println(Arrays.toString(dp));
        }
        System.out.println(Arrays.toString(dp));
        for(int x:dp){
            answer = Math.min(answer, x);
        }
        if(answer == n) return -1;
        return answer;
    }
}

/*
A의 최소 구하기

백트래킹?
각 info마다 누가 고를지 선택하면 2^40 = 10^12
안됨
가지치기로 해결 가능할까?

A의 최소를 구해야함.

dp[i] = B의 무게가 i일 때 A의 최소값

[[1, 2], [2, 3], [2, 1]]]

init :  [0, F, F, F]
(1, 2)  [1, F, 0, F]
(2, 3)  [3, F, 2, 1]
(2, 1)  [5, 3, 4, 2]

init :  [0, F, F, F]
(2, 1)  [2, 0, F, F]
(2, 3)  [4, 2, F, 2]
(1, 2)  [5, 3, 4, 2]

init :  [0, F, F, F]
(2, 3)  [2, F, F, 0]
(1, 2)  [3, F, 2, 1]
(2, 1)  [5, 3, 4, 2]

(a, b)
dp[i] 를 뒤부터 갱신
i-b<0이면 본인이 먹어
dp[i-b]가 무한이어도 본인이 먹어
*/