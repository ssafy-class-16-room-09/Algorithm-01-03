import java.util.*;
class Solution {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;
        int range = w * 2 + 1;
        ArrayList<Integer> splitL = new ArrayList<>();
        
        int start=1;
        for(int s : stations) {
            if(s-w > start)
                splitL.add(s-w - start);
            start = s+w+1;
        }
        if(start <= n)
            splitL.add(n-start+1);
        
        for(int l : splitL) {
            answer += l/range;
            if(l%range != 0)
                answer++;
        }

        return answer;
    }
}