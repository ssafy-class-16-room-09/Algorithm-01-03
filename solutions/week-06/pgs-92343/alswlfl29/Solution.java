import java.util.*;

class Node {
    int children; // 다음으로 갈 수 있는 노드 위치
    int sheep; // 양의 개수
    int wolf; // 늑대의 개수
    
    Node(int children, int sheep, int wolf) {
        this.children = children;
        this.sheep = sheep;
        this.wolf = wolf;
    }
    
    @Override
    public String toString() {
        return "[childrenNode=" + children + ", sheep=" + sheep + ", wolf=" + wolf + "]";
    }
}

class Solution {
    
    static int N; // 노드 개수
    static List<Integer>[] connect; // 부모-자식 관계
    static int[] info;

    public int solution(int[] info, int[][] edges) {
        int answer = 0;
        
        N = info.length;
        
        this.info = info;
        connect = new ArrayList[N];
        for(int i=0; i<N; i++) connect[i] = new ArrayList<>();
        for(int[] edge : edges) { // 노드 간 연결 정보
            connect[edge[0]].add(edge[1]);
        }
        
        return bfs(0);
    }
    
    private int bfs(int start) {
        ArrayDeque<Node> deque = new ArrayDeque<>();
        
        int max = 0; // 최대로 모을 수 있는 양
        
        deque.offer(new Node(1, 0, 0)); // 0번 노드부터 시작
        
        while(!deque.isEmpty()) {
            Node node = deque.poll(); // 현재 노드 꺼내기
            
            max = Math.max(max, node.sheep);
            for(int i=0; i<N; i++) {
                int sheep = node.sheep;
                int wolf = node.wolf;
                // 현재 해당 노드가 존재하는 경우, 이동 가능한지 확인
                if((node.children & (1<<i)) != 0) {
                    if(info[i] == 0) sheep++;
                    else wolf++; // 양인지 늑대인지 확인
                    
                    if(sheep > wolf) {
                        int state = (node.children ^ (1<<i)); // 해당 노드 비트 끄기
                        for(int j : connect[i]) {
                            state |= (1<<j);
                        }
                        deque.offer(new Node(state, sheep, wolf));
                    }
                }
            }
        }
        return max;
    }
}