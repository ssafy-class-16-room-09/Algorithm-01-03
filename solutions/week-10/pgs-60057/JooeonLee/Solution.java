class Solution {
    public int solution(String s) {
        int answer = s.length();
        
        for(int i=1; i<=s.length()/2; i++) {
            String prev = s.substring(0, i);
            int cnt = 1;
            int cLength = 0;
            
            for(int j=i; j<s.length(); j+=i) {
                int end  = Math.min(s.length(), j+i);
                String next = s.substring(j, end);
                
                if(next.equals(prev))
                    cnt++;
                else {
                    cLength += prev.length();
                    if(cnt > 1)
                        cLength += String.valueOf(cnt).length();
                    
                    prev = next;
                    cnt = 1;
                }
            }
            cLength += prev.length();
            if(cnt > 1)
                cLength += String.valueOf(cnt).length();
            
            answer = Math.min(answer, cLength);
        }
        return answer;
    }
}