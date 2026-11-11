import java.util.*;

class Solution {
    public int solution(String message, int[][] spoiler_ranges) {
        int answer = 0;
        int L = spoiler_ranges.length;
        String[] words = message.split(" ");
        HashSet<String> visited = new HashSet<>();
        HashSet<String> candidates = new HashSet<>();
        int left = 0;
        int idx = 0;
        
        for (String word:words){
            int right = left+word.length()-1;
            // 현재 단어의 구간 left ~ right
            // 현재 구간이 스포일러와 겹치나?
            // 안겹치는 규칙
            // right < start || left > end
            
            // 인덱스가 커버하는 범위가 내 단어 위치보다 앞에 있으면 땡겨와야함
            while(idx<L && spoiler_ranges[idx][1]<left){
                idx++;
            }
            if(idx<L && left<=spoiler_ranges[idx][1] && right >=spoiler_ranges[idx][0]){
                if(!visited.contains(word)) candidates.add(word);
                
            } else{
                visited.add(word);
                candidates.remove(word);
            }
            
            //다음 단어의 시작 인덱스
            left = right+2;
        }
        
        return candidates.size();
    }
}
/*

my phone number is 01012345678 and may i have your phone number
012345678901234567890123456789012345678901234567890123456789012
000001000000000000000000011110000011111110000000000001111111000
*/