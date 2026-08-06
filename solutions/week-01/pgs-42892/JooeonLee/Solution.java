import java.util.*;

class Solution {
    public int[][] solution(int[][] nodeinfo) {
        int[][] answer = new int[2][];
        Node root = makeTree(nodeinfo);
        ArrayList<Integer> pre = new ArrayList<>();
        preOrder(root, pre);
        
        ArrayList<Integer> post = new ArrayList<>();
        postOrder(root, post);
        
        answer[0] = pre.stream()
            .mapToInt(Integer::intValue)
            .toArray();
        answer[1] = post.stream()
            .mapToInt(Integer::intValue)
            .toArray();
        
        return answer;
    }
    
    private class Node {
        int num;
        int x;
        int y;
        Node left;
        Node right;
        
        public Node(int num, int x, int y) {
            this.num = num;
            this.x = x;
            this.y = y;
        }
    }
    
    private Node makeTree(int[][] nodeInfo) {
        ArrayList<Node> nodeArr = new ArrayList<>();
        
        for(int i=0; i<nodeInfo.length; i++) {
            int[] currNodeInfo = nodeInfo[i];
            Node currNode = new Node(i+1, currNodeInfo[0], currNodeInfo[1]);
            nodeArr.add(currNode);
        }
        
        nodeArr.sort((a, b) -> {
            if(a.y != b.y)
                return Integer.compare(b.y, a.y);
            else
                return Integer.compare(a.x, b.x);
        });
        
        Node root = nodeArr.get(0);
        
        for(int i=1; i<nodeArr.size(); i++) {
            Node currNode = nodeArr.get(i);
            setNode(root, currNode);
        }
        
        return root;
    }
    
    private void setNode(Node target, Node addedNode) {
        if(target.x > addedNode.x) {
            if(target.left == null) {
                target.left = addedNode;
                return;
            }
            else {
                setNode(target.left, addedNode);
            }
        }
        else {
            if(target.right == null) {
                target.right = addedNode;
            }
            else {
                setNode(target.right, addedNode);
            }
        }
    }
    
    private void preOrder(Node curr, ArrayList<Integer> result) {
        result.add(curr.num);
        if(curr.left != null)
            preOrder(curr.left, result);
        if(curr.right != null)
            preOrder(curr.right, result);
    }
    
    private void postOrder(Node curr, ArrayList<Integer> result) {
        if(curr.left != null)
            postOrder(curr.left, result);
        if(curr.right != null)
            postOrder(curr.right, result);
        result.add(curr.num);
    }
}