import java.util.*;
class Solution {
    public int solution(int[] A, int[] B) {
        int answer = 0;
        Arrays.sort(A);
        Arrays.sort(B);
        int idx = A.length-1;
        for (int i=B.length-1; i>=0; i--){
            int num = B[i];
            while (idx>=0 && num<=A[idx]){
                idx--;
            }
            if (idx < 0) break;
            if (A[idx] < num){
                answer++;
                idx--;
            }
        }
        
        return answer;
    }
}

/*
7 5 3 1
8 6 2 2


*/