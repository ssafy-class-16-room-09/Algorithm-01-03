class Solution {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;
        
        int current = 1; // 현재 위치
        int index = 0; // 초기 설치 되어있는 아파트 인덱스
        while(index < stations.length) {
            if(current >= stations[index]-w) {
                current = stations[index]+w+1;
                index++;
            } else {
                answer++;
                current += 2*w+1;
            }
        }
        while(current <= n) {
            current += 2*w+1;
            answer++;
        }

        return answer;
    }
}