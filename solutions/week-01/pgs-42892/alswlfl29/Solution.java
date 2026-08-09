import java.util.*;

class Node implements Comparable<Node> {
    int number; // 노드 번호
    int x; // x좌표
    int y; // y좌표
    Node leftChild; // 왼쪽 자식 정보
    Node rightChild; // 오른쪽 자식 정보
    
    public Node(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int compareTo(Node n) {
        // 1) y좌표 기준 내림차순 정렬, 2) x좌표 기준 오름차순 정렬
        if(this.y == n.y) return Integer.compare(this.x, n.x); 
        return Integer.compare(n.y, this.y);
    }
    
    @Override
    public String toString() {
        int leftChildNum = leftChild != null ? leftChild.number : -1;
        int rightChildNum = rightChild != null ? rightChild.number : -1;
        return "number: " + number + ", x: " + x + ", y: " + y + 
            ", Left: " + leftChildNum + ", Right: " + rightChildNum + "\n";
    }
}

class Solution {
    static List<Node> tree;
    public int[][] solution(int[][] nodeinfo) {
        
        int n = nodeinfo.length; // 정점 개수
        
        tree = new ArrayList<>();
        for(int index=0; index<n; index++) {
            tree.add(new Node(index+1, nodeinfo[index][0], nodeinfo[index][1]));
        }
        Collections.sort(tree); // 정렬
        
        Node root = tree.get(0);
        // 트리 만들기
        for(Node t : tree) {
            findPosition(root, t);
        }
        
        // 전위 순회 탐색
        int[] preorderArr = preorder(root, new ArrayList<>()).stream().mapToInt(Integer::intValue).toArray();
        // System.out.println(Arrays.toString(preorderArr));
        // 후위 순회 탐색
        int[] postorderArr = postorder(root, new ArrayList<>()).stream().mapToInt(Integer::intValue).toArray();
        // System.out.println(Arrays.toString(postorderArr));
        
        //System.out.println(tree.toString());
        
        int[][] answer = new int[2][n];
        for(int i=0; i<n; i++) {
            answer[0][i] = preorderArr[i];
            answer[1][i] = postorderArr[n-1-i];
        }
        
        return answer;
    }
    
    private void findPosition(Node parent, Node item) {
        // 부모 노드 x > 현재 노드 x => 왼쪽 탐색
        if(parent.x > item.x) {
            // 부모 노드에 왼쪽 자식이 없는 경우, 왼쪽 자식으로 할당
            if(parent.leftChild == null) {
                parent.leftChild = item;
                return;
            } else {
                findPosition(parent.leftChild, item); // 이미 왼쪽 자식이 존재 => depth 1단계 추가
            }
        } else if(parent.x < item.x) {
            if(parent.rightChild == null) {
                parent.rightChild = item;
                return;
            } else {
                findPosition(parent.rightChild, item);
            }
        }
    }
    
    // 전위 순회
    private List<Integer> preorder(Node node, List order) {
        order.add(node.number);
        if(node.leftChild != null) preorder(node.leftChild, order);
        if(node.rightChild != null) preorder(node.rightChild, order);
        
        return order;
    }
    
    // 후위 순회
    private List<Integer> postorder(Node node, List order) {
        order.add(node.number);
        if(node.rightChild != null) postorder(node.rightChild, order);
        if(node.leftChild != null) postorder(node.leftChild, order);
        
        return order;
    }
    
}