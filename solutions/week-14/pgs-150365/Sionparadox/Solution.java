import java.util.*;

class Solution {
    public String solution(int n, int m, int x, int y, int r, int c, int k) {
        StringBuilder answer = new StringBuilder();
        int dst = Math.abs(x-r)+Math.abs(y-c);
        if(dst>k || (k-dst)%2 != 0) return "impossible";
        
        int[] need = new int[4];
        if((x-r)>0) need[3] = (x-r);
        else need[0] = (r-x);
        if((y-c)>0) need[1] = (y-c);
        else need[2] = (c-y);
        
        int remains = (k-dst)/2;
        
        int down = Math.min(remains, n - (x+need[0]));
        remains -= down;
        int left = Math.min(remains, (y - need[1])-1);
        remains -= left;
        
        answer.append("d".repeat(need[0]+down));
        answer.append("l".repeat(need[1]+left));
        answer.append("rl".repeat(remains));
        answer.append("r".repeat(need[2]+left));
        answer.append("u".repeat(need[3]+down));
        
        return answer.toString();
    }
}

/*
dlru

ud보다 rl이 더 앞
이동하고 남는건 일단 가능한 아래, 왼쪽으로 보내
그 후 남은건 rl 붙이기


*/