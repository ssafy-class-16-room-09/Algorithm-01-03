import java.util.*;

class Solution {
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        Map<String, Integer> index = new HashMap<>();
        int[] parents = new int[enroll.length];
        int[] answer = new int[enroll.length];
        
        for(int i=0; i<enroll.length; i++) {
            index.put(enroll[i], i);
        }
        
        for(int i=0; i<enroll.length; i++) {
            parents[i] = index.getOrDefault(referral[i], -1);
        }
        
        for(int i=0; i<seller.length; i++) {
            int currIdx = index.get(seller[i]);
            int currPrice = amount[i] * 100;
            
            while(currIdx != -1 && currPrice > 0) {
                int give = currPrice / 10;
                answer[currIdx] += currPrice - give;
                currIdx = parents[currIdx];
                currPrice = give;
            }
        }
        return answer;
    }
}