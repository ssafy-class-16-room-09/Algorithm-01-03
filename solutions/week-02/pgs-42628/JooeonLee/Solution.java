import java.util.*;

class Solution {
    public int[] solution(String[] operations) {
        TreeSet<Integer> ts = new TreeSet<>();
        
        for(String element : operations) {
            String[] opt = element.split(" ");
            String operation = opt[0];
            int num = Integer.parseInt(opt[1]);
            
            if(operation.equals("I"))
                ts.add(num);
            else if(num == 1 && !ts.isEmpty())
                ts.pollLast();
            else if(num == -1 && !ts.isEmpty())
                ts.pollFirst();
        }
        
        if(ts.isEmpty())
            return new int[]{0, 0};
        else
            return new int[]{ts.last(), ts.first()};
    }
}