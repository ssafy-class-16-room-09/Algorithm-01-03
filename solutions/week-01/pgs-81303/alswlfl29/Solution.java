import java.util.*;

class Node {
    int num;
    int prev; // 연결된 이전 노드
    int next; // 연결된 다음 노드
    
    public Node(int num, int prev, int next) {
        this.num = num;
        this.prev = prev;
        this.next = next;
    }
    
    @Override
    public String toString() {
        return "[num="+num+", prev="+prev+", next="+next+"]";
    }
}

class Solution {
    public String solution(int n, int k, String[] cmd) {
        
        Map<Integer, Node> table = new HashMap<>();
        for(int i=0; i<n; i++) {
            int prev = i > 0 ? i-1 : -1;
            int next = i < n-1 ? i+1 : -1;
            table.put(i, new Node(i, prev, next)); // 연결된 노드 정보 저장(이중 연결 리스트)
        }
        
        int index = k; // 현재 노드
        List<Node> stack = new ArrayList<>();
        for(String command : cmd) {
            String[] c = command.split(" ");
            if(c[0].equals("U")) {
                int X = Integer.parseInt(c[1]);
                // X만큼 앞 노드로 이동
                for(int x=0; x<X; x++) {
                    Node node = table.get(index);
                    index = node.prev;
                }
            } else if(c[0].equals("D")) {
                int X = Integer.parseInt(c[1]);
                // X만큼 뒷 노드로 이동
                for(int x=0; x<X; x++) {
                    Node node = table.get(index);
                    index = node.next;
                }
            } else if(c[0].equals("C")) {
                Node node = table.get(index);
                // 삭제된 노드 기준 연결 리스트 재구성
                if(node.prev != -1) table.get(node.prev).next = node.next;
                if(node.next != -1) table.get(node.next).prev = node.prev;
                if(node.next == -1) index = node.prev; // 마지막 행 삭제한 경우 현재 노드를 이전 노드로 변경
                else index = node.next; // 그 외에는 현재 노드를 다음 노드로 변경
                stack.add(node); // 삭제된 노드 추가
                table.remove(node.num); // 테이블에서 삭제
            } else if(c[0].equals("Z")) {
                Node node = stack.remove(stack.size()-1); // 복구한 노드
                // 복구한 노드 기준 연결 리스트 재구성
                if(node.prev != -1) table.get(node.prev).next = node.num;
                if(node.next != -1) table.get(node.next).prev = node.num;
                // 테이블에 다시 추가
                table.put(node.num, new Node(node.num, node.prev, node.next));
            }
        }
        
        String[] answer = new String[n];
        Arrays.fill(answer, "O");
        for(int i=0; i<stack.size(); i++){
            answer[stack.get(i).num] = "X";
        }
        
        
        return String.join("", answer);
    }
}