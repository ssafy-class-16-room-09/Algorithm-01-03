import java.util.*;
class Solution {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;
        int left = 1;
        
        for (int i=0;i<stations.length;i++){
            int st = stations[i];
            
            int right = st-w;
            answer += Math.ceil(Math.max((right-left),0)/(double)(1+w*2));
            left = st+w+1;
        }
        answer += Math.ceil(Math.max((n-left+1),0)/(double)(1+w*2));

        return answer;
    }
}