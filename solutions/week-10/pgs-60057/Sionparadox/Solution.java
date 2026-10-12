class Solution {
    public int solution(String s) {
        int L = s.length();
        int answer = L;
        for (int len=1; len<=L/2; len++){
            int k = 0;
            String prev = s.substring(0, len);
            int cnt = 1;
            for (int i=len; i<L; i+=len){
                int right = Math.min(i+len, L);
                String unit = s.substring(i, right);
                if (prev.equals(unit)) {
                    cnt++;
                }
                else {
                    if (cnt != 1) k += String.valueOf(cnt).length();
                    k += len;
                    prev = unit;
                    cnt = 1;
                }
            }
            if (cnt>1) k += String.valueOf(cnt).length();
            k += prev.length();
            
            answer = Math.min(answer, k);
        }
        
        return answer;
    }
}

/*
2~500
완탐? O(N^2)

5번 테케 친절맨
*/