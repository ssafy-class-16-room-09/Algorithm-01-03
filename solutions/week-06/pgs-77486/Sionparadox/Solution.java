import java.util.*;

class Node {
    int id, parent, profit;
    ArrayList<Integer> children;
    public Node(int id){
        this.id = id;
        children = new ArrayList<>();
    }
    
}

class Solution {
    int L;
    Node[] tree;
    HashMap<String, Integer> mapper;
    
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        L = enroll.length;
        tree = new Node[L+1];
        mapper = new HashMap<>();
        
        tree[0] = new Node(0);
        mapper.put("-", 0);
        tree[0].parent = -1;
        
        for (int i=0; i<L; i++){
            tree[i+1] = new Node(i+1);
            mapper.put(enroll[i], i+1);
            int p = mapper.get(referral[i]);
            tree[i+1].parent = p;
            tree[p].children.add(i+1);
        }
        
        for (int i=0; i<seller.length; i++){
            int idx = mapper.get(seller[i]);
            int price = amount[i]*100;
            dfs(idx, price);
            
        }
        
        int[] answer = new int[L];
        for (int i=0; i<L; i++){
            answer[i] = tree[i+1].profit;
        }

        return answer;
    }
    
    private void dfs(int idx, int value){
        if (idx == -1) return;
        int loss = value/10;
        
        Node node = tree[idx];
        node.profit += value-loss;
        if (loss == 0) return;
        dfs(node.parent, loss);
        
    }
    
}