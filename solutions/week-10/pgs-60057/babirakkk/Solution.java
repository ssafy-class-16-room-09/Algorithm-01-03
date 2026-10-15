class Solution {
    
    String s;
    
    public int solution(String s) {
        this.s = s;
        int minLength = s.length();
        
        for (int unitSize = 1; unitSize < s.length(); unitSize++) {
            minLength = Math.min(minLength, getCompressedLength(unitSize));
        }
        
        return minLength;
    }
    
    private int getCompressedLength(int unitSize) {
        int prevStart = 0; // 현재 단위와 비교할 이전 단위의 시작 위치
        int compressedLength = unitSize; // 가장 첫 단위 이후부터 비교
        for (int i = unitSize; i < s.length(); i += unitSize) {
            boolean isCurrDiffFromPrev = true; // 앞에 나온 문자열 단위와 일치 여부를 확인하는 변수
            if (i + unitSize <= s.length()) { // 마지막에 남는 문자열의 길이가 unitSize보다 작다면 확인할 필요 없이 다름
                isCurrDiffFromPrev = false;
                for (int j = 0; j < unitSize; j++) { // 한 글자씩 비교하면서 하나라도 다르다면 다른 것으로 확인
                    if (s.charAt(prevStart + j) != s.charAt(i + j)) {
                        isCurrDiffFromPrev = true;
                        break;
                    }
                }
            }
            
            if (isCurrDiffFromPrev) { // 만약 이전 문자열 단위와 다르다면
                if ((i - prevStart) / unitSize != 1) { // 이전에 나온 중복된 단위가 1개라면 숫자를 붙이지 않음
                	compressedLength += String.valueOf((i - prevStart) / unitSize).length(); // 아니라면 나온 단위의 자릿수만큼 길이에 더해준다!
                }
                compressedLength += Math.min(unitSize, s.length() - i); // 이번 문자열의 길이
                prevStart = i;
            }
            
            if ((i + unitSize >= s.length()) && !isCurrDiffFromPrev) { // 문자열의 끝까지 돌았을 때 앞의 단위와 중복되어 아직 처리되지 못한 경우
            	compressedLength += String.valueOf((i - prevStart) / unitSize + 1).length();
            }
        }
        return compressedLength;
    }
}
