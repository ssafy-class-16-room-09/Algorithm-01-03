import java.util.*;
class Solution {
    private int[][] info;
    private int len;
    private int n;
    private int m;
    
    private int answer = 121;
    
    public int solution(int[][] info, int n, int m) {
        
        this.info = info;
        len = info.length;
        Arrays.sort(info, (a, b) -> b[0] - a[0]); // A가 훔쳐야 할 흔적 내림차순 정렬
        this.n = n;
        this.m = m;
        
        find(0, 0, 0);
        
        return answer == 121 ? -1 : answer;
    }
    
    private void find(int index, int sumA, int sumB) {
        
        if(index == len) { // 모든 물건 훔친 경우 최소값 갱신
            answer = Math.min(answer, sumA);
            return;
        }
        
        
        for(int i=index; i<len; i++) {
            int newSumA = sumA + info[i][0]; // 현재 물건을 A가 훔쳤을 때 흔적 개수
            int newSumB = sumB + info[i][1]; // 현재 물건을 B가 훔쳤을 때 흔적 개수
            if(newSumB < m) { // B가 훔칠 수 있는 경우 B가 훔치기
                find(i+1, sumA, newSumB);
            }
            if (newSumA < n && answer > newSumA) { // A가 훔칠 수 있고, 지금까지의 최소값보다 더 작은 경우만 A가 훔치기
                find(i+1, newSumA, sumB);
            }
            return;
        }
    }
}