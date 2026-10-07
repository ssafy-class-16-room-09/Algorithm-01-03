import java.util.*;

class Solution {
    public int[] solution(String[] gems) {
        HashSet<String> set = new HashSet<>(Arrays.asList(gems));
        int total = set.size();
        int L = gems.length;
        
        HashMap<String, Integer> map = new HashMap<>();
        int left = 0, right = 0;
        int answerLeft = 0, answerRight = gems.length-1;
        
        while (right < L) {
            map.put(gems[right], map.getOrDefault(gems[right], 0)+1);
            right++;
            
            while (map.size() == total){
                if (right - left < answerRight - answerLeft+1){
                    answerLeft = left;
                    answerRight = right-1;
                }
                String lGem = gems[left];
                map.put(lGem, map.get(lGem) - 1);
                if(map.get(lGem) == 0) map.remove(lGem);
                left++;
            }
        }
        
        return new int[] {answerLeft+1, answerRight+1};
    }

}

/*
이분탐색?
10만까지 해당 길이로 만들 수 있는지 확인
-> 슬라이딩 윈도우


*/