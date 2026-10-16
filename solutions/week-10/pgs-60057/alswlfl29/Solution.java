import java.util.*;

class Solution {
    public int solution(String s) {
        int answer = s.length();
        
        for(int range=1; range<=s.length(); range++) {
            ArrayDeque<String> string = new ArrayDeque<>(); // 현재까지 들어간 문자형태
            ArrayDeque<Integer> count = new ArrayDeque<>(); // 각 문자 형태의 연속 횟수
            
            for(int i=0; i<s.length(); i+=range) {
                StringBuilder sb = new StringBuilder();
                for(int j=0; j<range; j++) {
                    if(i+j < s.length()) sb.append(s.charAt(i+j)); // 단위마다 문자 자르기
                }
                String compressionStr = sb.toString();
                // 직전의 문자형태와 같은 문자형태인 경우, 연속 횟수 증가시키기
                if(string.size() != 0 && string.getLast().equals(compressionStr)) {
                    count.offer(count.pollLast()+1);
                } else { // 처음이거나 다른 문자형태인 경우, 덱에 넣고 숫자 1로 설정
                    string.offer(compressionStr);
                    count.offer(1);
                }
            }
            
            StringBuilder sb = new StringBuilder();
            while(!string.isEmpty()) {
                String compressionStr = string.poll();
                int cnt = count.poll();
                if(cnt > 1) sb.append(cnt); // 연속 횟수가 1 초과인 경우에만 숫자 붙이기
                sb.append(compressionStr);
            }
            answer = Math.min(answer, sb.toString().length()); // 최소 문자 길이 갱신
        }
        
        return answer;
    }
}
