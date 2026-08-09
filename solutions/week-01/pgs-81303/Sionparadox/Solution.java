import java.util.*;

class Solution {
    public String solution(int n, int k, String[] cmd) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] next = new int[n];
        int[] prev = new int[n];
        boolean[] remains = new boolean[n];
        for (int i=0; i<n; i++){
            next[i] = i+1;
            prev[i] = i-1;
            remains[i] = true;
        }
        next[n-1] = -1;
        
        
        for (String s:cmd){
            char c = s.charAt(0);
            if (c == 'C'){
                remains[k] = false;
                stack.push(k);
                
                int prv = prev[k];
                int nxt = next[k];
                if (prv != -1) next[prv] = nxt;
                if (nxt != -1) prev[nxt] = prv;
                
                if (nxt != -1) k = nxt;
                else k = prv;
                
            } else if (c == 'Z'){
                int v = stack.pop();
                remains[v] = true;
                
                int prv = prev[v];
                int nxt = next[v];
                if (prv != -1) next[prv] = v;
                if (nxt != -1) prev[nxt] = v;
                
            } else {
                int v = Integer.parseInt(s.substring(2));
                if (c == 'U') k = move(k, v, prev);
                else k = move(k, v, next);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<n; i++){
            sb.append(remains[i] ? "O" : "X");
        }
        
        return sb.toString();
    }
    
    private int move(int pos, int dist, int[] arr){
        for (int i=0; i<dist;i++){
            pos = arr[pos];
        }
        return pos;
    }
}
/*
DoublyLinkedList
삽입, 삭제 O(N)
이동 O(1)

next, prev 배열 2개로 관리
삽입, 삭제 O(1)
이동 O(N)

이동거리가 최대 100만으로 제한
*/