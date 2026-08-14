import java.util.*;

class Solution {
    public int[] solution(String[] operations) {
        TreeSet<Integer> pq = new TreeSet<>();
        for (String op:operations){
            String[] cmds = op.split(" ");
            char cmd = cmds[0].charAt(0);
            int v = Integer.parseInt(cmds[1]);
            if (cmd == 'I'){
                pq.add(v);
            } else if (v == -1){
                pq.pollFirst();
            } else {
                pq.pollLast();
            }
        }
        if (pq.isEmpty()){
            return new int[] {0, 0};
        }
        return new int[] {pq.last(), pq.first()};
    }
}