import java.util.*;

class Solution {
    int MAX = 1_000_000;
    public int solution(String numbers) {
        int L = numbers.length();
        int[][] dp = new int[L+1][100];
        for (int i=0; i<=L; i++) Arrays.fill(dp[i], MAX);
        dp[0][35] = 0;
        
        for (int i=0; i<L; i++){
            int nxt = toIndex(numbers.charAt(i));
            
            for(int pos=0; pos<100; pos++){
                if(dp[i][pos] == MAX) continue;
                int left = pos/10;
                int right = pos%10;
                if(left == right) continue;
                
                //왼손 이동
                if(nxt != right){
                    int np = nxt*10+right;
                    dp[i+1][np] = Math.min(dp[i+1][np], dp[i][pos]+getWeight(left, nxt));
                }
                
                //오른손 이동
                if(nxt != left){
                    int np = nxt + left*10;
                    dp[i+1][np] = Math.min(dp[i+1][np], dp[i][pos]+getWeight(right, nxt));
                }
            }
        }
        
        int answer = MAX;
        for(int p=0; p<100; p++){
            answer = Math.min(answer, dp[L][p]);
        }
        
        return answer;
    }
    
    private int toIndex(char c){
        if(c == '0') return 9;
        return c-'1';
    }
    
    private int getWeight(int a, int b){
        if(a == b) return 1;
        if(a == 9) a = 10;
        if(b == 9) b = 10;
        int x1 = a/3, y1 = a%3;
        int x2 = b/3, y2 = b%3;
        int d1 = Math.abs(x1-x2), d2 = Math.abs(y1-y2);
        return Math.abs(d1-d2)*2+Math.min(d1,d2)*3;
    }
}

/*
123
456
789
*0#

dp[0][left*10+right]

*/