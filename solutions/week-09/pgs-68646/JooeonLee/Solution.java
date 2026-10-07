import java.util.*;

/**
인접한 것들 중에서 번호가 더 작은 풍선은 1번만 터트릴 수 있음
인접한 것들 중에서 번호가 더 큰 풍선을 터트림
원본 배열 [9 , -1, -5]
왼쪽부터 작은 것 기록 [9, -1, -5]
왼쪽부터 작은 것 기록 하면서 더 작은 것 터트린 횟수 저장
[[9, 0], [-1, 0], [-5, 0]]
오른쪽부터 작은 것 기록 [-5, -5, -5]
오른쪽부터 작은 것 기록 하면서 더 작은 것 터트린 횟수 저장
[[-9, 1], [-1, 1], [-5, 0]]

a의 길이 10^6 -> n^2 절대 안됨
*/
class Solution {
    public int solution(int[] a) {
        int[] leftMin = new int[a.length];
        int[] rightMin = new int[a.length];
        
        leftMin[0] = a[0];
        for(int i=1; i<a.length; i++) {
            leftMin[i] = Math.min(leftMin[i-1], a[i]);
        }
        
        rightMin[a.length-1] = a[a.length-1];
        for(int i= a.length-2; i>=0; i--) {
            rightMin[i] = Math.min(rightMin[i+1], a[i]);
        }
        
        int cnt=0;
        for(int i=0; i<a.length; i++) {
            if(a[i]==leftMin[i] || a[i]==rightMin[i]) {
                cnt++;
            }
        }
        return cnt;
    }
}