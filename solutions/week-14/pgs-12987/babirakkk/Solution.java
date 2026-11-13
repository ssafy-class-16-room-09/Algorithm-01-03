import java.util.*;

class Solution {
    public int solution(int[] A, int[] B) {
        ArrayList<Integer> a = new ArrayList<>();
        TreeMap<Integer, Integer> tm = new TreeMap<>();
        for (int i = 0; i < A.length; i++) {
            a.add(A[i]);
            tm.put(B[i], tm.getOrDefault(B[i], 0) + 1);
        } 
        Collections.sort(a);
        
        int winB = 0;
        for (int i = 0; i < A.length; i++) {
            Integer minValToWin = tm.higherKey(a.get(i));
            if (minValToWin == null) {
                int first = tm.firstKey();
                if (tm.get(first) == 1) {
                    tm.remove(first);
                } else {
                    tm.put(first, tm.get(first) - 1);
                }
            } else {
                if (tm.get(minValToWin) == 1) {
                    tm.remove(minValToWin);
                } else {
                    tm.put(minValToWin, tm.get(minValToWin) - 1);
                }
                winB++;
            }
        }
        return winB;
    }
}