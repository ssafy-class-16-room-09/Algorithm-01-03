import java.util.*;

class Node {
    int x;
    int y;
    int num;
    Node left;
    Node right;
    public Node(int x, int y, int num) {
        this.x = x;
        this.y = y;
        this.num = num;
    }
}

class Solution {
    List<Integer> pre = new ArrayList<>();
    List<Integer> post = new ArrayList<>();
    
    public int[][] solution(int[][] nodeinfo) {
        int L = nodeinfo.length;
        int[][] answer = new int[2][L];
        List<Node> nodes = new ArrayList<>();
        for (int i=0;i<L;i++){
            nodes.add(new Node(nodeinfo[i][0], nodeinfo[i][1], i+1));
        }
        
        nodes.sort((o1, o2) -> {
            if (o1.y == o2.y){
                return Integer.compare(o1.x, o2.x);
            }
            return Integer.compare(o2.y, o1.y);
        });
        
        Node root = makeTree(nodes);
        preOrder(root);
        postOrder(root);
        for (int i=0; i<L; i++){
            answer[0][i] = pre.get(i);
            answer[1][i] = post.get(i);
        }
        return answer;
    }
    
    private Node makeTree(List<Node> nodes){
        if (nodes.isEmpty()) return null;
        
        Node root = nodes.get(0);
        
        List<Node> left = new ArrayList<>();
        List<Node> right = new ArrayList<>();
        
        for (int i=1; i<nodes.size();i++){
            Node curr = nodes.get(i);
            
            if (curr.x < root.x) left.add(curr);
            else right.add(curr);
        }
        
        root.left = makeTree(left);
        root.right = makeTree(right);
        
        return root;
    }
    
    private void preOrder(Node node){
        if (node == null) return;
        pre.add(node.num);
        preOrder(node.left);
        preOrder(node.right);
    }
    
    private void postOrder(Node node){
        if (node == null) return;
        postOrder(node.left);
        postOrder(node.right);
        post.add(node.num);
    }
}


/*
x가 수평 위치
y가 낮을수록 자식
preorder : root left right
postorder : left right root

분할 정복으로 left subTree, right subTree


*/