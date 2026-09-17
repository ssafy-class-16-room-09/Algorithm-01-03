import java.util.*;
import java.io.*;

class Node {
    int idx;
    Node parent = null;
    ArrayList<Node> children = new ArrayList<>();
    int profit = 0;
    Node (int idx) {
        this.idx = idx;
    }
}

class Solution { // mlm, Multi-Level Marketing
    
    final int TOOTHBRUSH_COST = 100;
    
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        Node[] mlm = new Node[enroll.length]; // 인덱스 기반 접근
        HashMap<String, Node> mlmMap = new HashMap<>(); // 이름 기반 접근
        for (int i = 0; i < enroll.length; i++) {
            Node node = new Node(i);
            mlm[i] = node;
            mlmMap.put(enroll[i], node);
        }
             
        for (int i = 0; i < referral.length; i++) {
            if (referral[i].equals("-")) continue;
            
            Node curr = mlm[i];
            curr.parent = mlmMap.get(referral[i]);
            mlm[curr.parent.idx].children.add(curr);
        }
        
        for (int i = 0; i < seller.length; i++) {
            Node curr = mlmMap.get(seller[i]);
            int leftProfit = TOOTHBRUSH_COST * amount[i];
            while (leftProfit > 0 && curr != null) {
                curr.profit += leftProfit - leftProfit / 10;
                leftProfit /= 10;
                curr = curr.parent;
            }
        }
        
        int[] profits = new int[enroll.length];
        for (int i = 0; i < enroll.length; i++) {
            profits[i] = mlm[i].profit;
        }
        return profits;
    }
}