import java.util.*;

class Solution {
    public int solution(int[] info, int[][] edges) {
        buildTree(info, edges);
        int answer = 0;
        
        ArrayDeque<Info> queue = new ArrayDeque<>();
        queue.add(new Info(0, 1, 0, new HashSet<>()));
        
        while(!queue.isEmpty()) {
            Info curr = queue.poll();
            answer = Math.max(answer, curr.sheep);
            
            curr.visited.addAll(tree[curr.node]);
            
            for(int next : curr.visited) {
                HashSet<Integer> nextVisited = new HashSet<>(curr.visited);
                nextVisited.remove(next);
                
                if(info[next] == 1){
                    if(curr.sheep != curr.wolf + 1)
                        queue.add(new Info(next, curr.sheep, curr.wolf+1, nextVisited));
                }
                else {
                    queue.add(new Info(next, curr.sheep+1, curr.wolf, nextVisited));
                }
            }
        }
        
        return answer;

    }
    
    private static class Info {
        int node, sheep, wolf;
        HashSet<Integer> visited;
        
        public Info(int node, int sheep, int wolf, HashSet<Integer> visited) {
            this.node = node;
            this.sheep = sheep;
            this.wolf = wolf;
            this.visited = visited;
        }
    }
    
    private static ArrayList<Integer>[] tree;
    
    private static void buildTree(int[] info, int[][] edges) {
        tree = new ArrayList[info.length];
        for(int i=0; i<tree.length; i++)
            tree[i] = new ArrayList<>();
        
        for(int[] edge : edges)
            tree[edge[0]].add(edge[1]);
    }
}