import java.util.*;

class Num {
    int value;
    boolean isDeleted = false;
    Num(int value) {
        this.value = value;
    }
}

class Solution {
    
    PriorityQueue<Num> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.value)); // 오름차순 pq
    PriorityQueue<Num> revpq = new PriorityQueue<>(Comparator.comparingInt((Num n) -> n.value).reversed()); // 내림차순 pq
    
    public int[] solution(String[] operations) {
        for (String op : operations) {
            StringTokenizer st = new StringTokenizer(op);
            String type = st.nextToken();
            int n = Integer.parseInt(st.nextToken());
            
            if (type.equals("I")) {
                insert(n);
            } else if (type.equals("D")) {
                if (n == -1) { // remove minimum
                    deleteMin();
                } else { // remove maximum
                    deleteMax();
                }
            }
        }
                
        int[] answer = new int[2];
        Num max = deleteMax();
        Num min = deleteMin();
        if (max == null) {
            answer[0] = 0;
            answer[1] = 0;
        } else {
            answer[0] = max.value;
            if (min == null) {
                answer[1] = answer[0];
            } else {
                answer[1] = min.value;
            }
        }
        return answer;
    }
    
    public Num deleteMin() {
        Num min = pq.poll();
        while (min != null && min.isDeleted) { // 이미 deleteMax()에 의해 지워진 Num이면 다시 poll
            min = pq.poll();
        }
        
        if (min != null) {
            min.isDeleted = true;
        }
        
        return min;
    }
    
    public Num deleteMax() {
        Num max = revpq.poll();
        while (max != null && max.isDeleted) { // 이미 deleteMin()에 의해 지워진 Num이면 다시 poll
            max = revpq.poll();
        }
        
        if (max != null) {
            max.isDeleted = true;
        }
        
        return max;
    }
    
    public void insert(int n) {
        Num num = new Num(n);
        pq.add(num);
        revpq.add(num);
    }
}