import java.util.*;

class Row {
    int index;
    Row prev;
    Row next = null;
    boolean isDeleted = false;
    Row(int index, Row prev) {
        this.index = index;
        this.prev = prev;
    }
}

class Solution {
    
    Row[] table;
    Stack<Row> deleted = new Stack<>();
    int maxIndex;
    
    public String solution(int n, int k, String[] cmd) {
        table = new Row[n];
        fillTable(n);
        
        int curr = k;
        for (String c : cmd) {
            StringTokenizer st = new StringTokenizer(c);
            String task = st.nextToken();
            int x = 0;
            if (st.hasMoreTokens()) {
                x = Integer.parseInt(st.nextToken());
            }
            
            if (task.equals("U")) {
                curr = up(curr, x);
            } else if (task.equals("D")) {
                curr = down(curr, x);
            } else if (task.equals("C")) {
                curr = delete(curr);
            } else if (task.equals("Z")) {
                restore();
            }
        }
        
        StringBuilder ans = new StringBuilder("");
        for (int i = 0; i < n; i++) {
            if (table[i].isDeleted) {
                ans.append("X");
            } else {
                ans.append("O");
            }
        }
        return ans.toString();        
    }
    
    private int up(int curr, int x) {
        Row next = table[curr];
        for (int i = 0; i < x; i++) {
            next = next.prev;
        }
        return next.index;
    }
    
    private int down(int curr, int x) {
        Row next = table[curr];
        for (int i = 0; i < x; i++) {
            next = next.next;
        }
        return next.index;
    }
    
    private int delete(int currIndex) {
        Row curr = table[currIndex];
        if (curr.prev != null) {
            curr.prev.next = curr.next; // row[i-1].prev = row[i+1]
        }
        if (curr.next != null) {
            curr.next.prev = curr.prev; // row[i+1].prev = row[i-1]
        }
        
        deleted.push(curr);
        curr.isDeleted = true;
        
        return (curr.next == null) ? curr.prev.index : curr.next.index;
    }
    
    private void restore() {
        Row target = deleted.pop();
        
        if (target.prev != null) {
            target.prev.next = target;
        }
        
        if (target.next != null) {
            target.next.prev = target;
        }
        
        target.isDeleted = false;
    }
    
    private void fillTable(int n) {
        table[0] = new Row(0, null);
        for (int i = 1; i < n; i++) {
            table[i] = new Row(i, table[i - 1]);
            table[i - 1].next = table[i];
        }
    }
}