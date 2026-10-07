import java.util.*;

class Solution {
    public int solution(int[] a) {
        int answer = 0;
        int L = a.length;
        
        int[] toRight = new int[L];
        toRight[0] = a[0];
        int[] toLeft = new int[L];
        toLeft[L-1] = a[L-1];
        
        int minVal = a[0];
        int minIdx = 0;
        
        for (int i=1; i<L; i++){
            toRight[i] = Math.min(toRight[i-1], a[i]);
            if (a[i] < minVal){
                minVal = a[i];
                minIdx = i;
            }
            
        }
        
        for (int i=L-2; i>=0; i--){
            toLeft[i] = Math.min(toLeft[i+1], a[i]);
        }
        
        HashSet<Integer> set = new HashSet<>();
        for (int i=0; i<L; i++){
            if (!set.contains(toRight[i])) set.add(toRight[i]);
            if (!set.contains(toLeft[i])) set.add(toLeft[i]);
        }
        
        return set.size();
    }
}


/*
작은 수가 이김

[-16,27,65,-2,58,-92,-71,-68,-61,-33]
[-16, -16, -16, -16, -16, -92, -92, -92, -92, -92]
[-92, -92, -92, -92, -92, -92, -71, -68, -61, -33]

최소값은 다 이김.
최소값 바로 옆은 걔 옆으로 다 걔보다 작아야함.
양방향의로 최소값 찾기
가능한 값은 구간 내에서 1등, 나머지 다 잡아먹고 최소랑 싸우면 됨

[6, 7, 8, 4, 3, 5, 1]
[6, 6, 6, 4, 3, 3, 1]
[1, 1, 1, 1, 1, 1, 1]


*/