import java.util.*;

class Node {
    int index, x, y;
    int leftBound = -1; // 좌표 값은 0 이상
    int rightBound = 100001; // 100,000 이하
    Node left = null;
    Node right = null;
    
    Node(int index, int x, int y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }
}

class Solution {
    
    public int[][] solution(int[][] nodeinfo) {
        PriorityQueue<Node> pq = new PriorityQueue<>( // y(레벨)를 기준으로 내림차순, 같다면 x를 기준으로 오름차순 정렬
            Comparator.comparingInt((Node n) -> n.y).reversed().thenComparingInt(n -> n.x));
        
        for (int i = 0; i < nodeinfo.length; i++) {
            pq.add(new Node(i + 1, nodeinfo[i][0], nodeinfo[i][1])); 
        }
        
        Node root = pq.poll();
        Queue<Node> q = new LinkedList<>(); // parent 후보 큐
        q.add(root);
        
        int prevY = root.y;
        Node parent = null;
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            
            if (prevY != curr.y) { // 레벨이 바뀌었다면
                while (parent == null || parent.y != prevY) {
                    parent = q.poll(); // 부모도 다음 레벨로 교체
                }
            }
            
            NEXT_NODE: while (true) {
                if (curr.x > parent.leftBound && curr.x < parent.x) { // 현재 노드의 x값이 부모의 왼쪽 범위와 부모의 x사이라면
                    parent.left = curr;
                    curr.leftBound = parent.leftBound;
                    curr.rightBound = parent.x;
                    break NEXT_NODE;
                } else if (curr.x > parent.x && curr.x < parent.rightBound) { // 현재 노드의 x값이 부모의 x와 부모의 오른쪽 범위 사이라면
                    parent.right = curr;
                    curr.leftBound = parent.x;
                    curr.rightBound = parent.rightBound;
                    parent = q.poll();
                    break NEXT_NODE;
                } else { // 둘 다 아니라면 같은 레벨의 다음 노드로 넘어가서 검사
                    parent = q.poll();
                }
            }
            q.add(curr); // curr의 다음 레벨을 위해 부모 후보 큐에 curr 추가
            prevY = curr.y;
        }
       
        
        int[][] answer = new int[2][nodeinfo.length];
    
        // preorder: 현재 - 왼쪽 - 오른쪽
        ArrayList<Integer> preorder = new ArrayList<>();
        preorder(root, preorder);
        answer[0] = preorder. stream().mapToInt(Integer::intValue).toArray();
        
        // postorder: 왼쪽 - 오른쪽 - 현재
        ArrayList<Integer> postorder = new ArrayList<>();
        postorder(root, postorder);
        answer[1] = postorder. stream().mapToInt(Integer::intValue).toArray();
        
        return answer;
    }
    

    private void preorder(Node node, ArrayList<Integer> visit) {
        if (node == null) {
            return;
        }
        visit.add(node.index);
        preorder(node.left, visit);
        preorder(node.right, visit);
    }
    
    
    private void postorder(Node node, ArrayList<Integer> visit) {
        if (node == null) {
            return;
        }
        postorder(node.left, visit);
        postorder(node.right, visit);
        visit.add(node.index);
    }
}
