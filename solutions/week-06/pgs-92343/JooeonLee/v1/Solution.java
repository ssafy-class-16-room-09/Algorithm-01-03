import java.util.*;

class Solution {
    static int answer = 0;
    static ArrayList<Node> tree = new ArrayList<>();
    public int solution(int[] info, int[][] edges) {
        for(int i=0; i<info.length; i++) {
            tree.add(new Node(i, info[i]));
        }
        
        for(int[] edge : edges) {
            Node parent = tree.get(edge[0]);
            Node child = tree.get(edge[1]);
            
            parent.setChild(child);
            child.setParent(parent);
        }
        
        Node root = tree.get(0);
        ArrayList<Node> candidates = new ArrayList<>();
        if(root.leftChild != null)
            candidates.add(root.leftChild);
        if(root.rightChild != null)
            candidates.add(root.rightChild);
        dfs(1, 0, candidates);
        return answer;
    }
    
    static void dfs(int sCnt, int wCnt, ArrayList<Node> candidates) {
        answer = Math.max(answer, sCnt);
        for(int i=0; i<candidates.size(); i++) {
            Node curr = candidates.get(i);
            
            int nextScnt = sCnt;
            int nextWcnt = wCnt;
            
            if(curr.type == 0)
                nextScnt++;
            else
                nextWcnt++;
            
            if(nextWcnt >= nextScnt)
                continue;
            
            ArrayList<Node> nextCandidates = new ArrayList<>(candidates);
            nextCandidates.remove(i);
            
            if(curr.leftChild!=null)
                nextCandidates.add(curr.leftChild);
            if(curr.rightChild!=null)
                nextCandidates.add(curr.rightChild);
            
            dfs(nextScnt, nextWcnt, nextCandidates);
        }
    }
    
    static class Node {
        int idx;
        int type;
        int totalSheep;
        int totalWolf;
        
        Node parent;
        Node leftChild;
        Node rightChild;
        
        public Node(int idx, int type) {
            this.type = type;
            this.totalSheep = 0;
            this.totalWolf = 0;
        }
        
        public void setChild(Node child) {
            if(leftChild==null && rightChild==null)
                leftChild = child;
            else if(leftChild!=null) {
                Node currChild = leftChild;
                if(currChild.idx > child.idx) {
                    leftChild = child;
                    rightChild = currChild;
                }
                else
                    rightChild = child;
            }
        }
        
        public void setParent(Node parent) {
            parent = parent;
        }
    }
}