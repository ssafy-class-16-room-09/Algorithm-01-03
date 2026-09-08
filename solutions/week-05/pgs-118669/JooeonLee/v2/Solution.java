import java.util.*;

class Solution {
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        boolean[] isSummit = new boolean[n+1];
        for(int summit : summits)
            isSummit[summit] = true;
        boolean[] isGate = new boolean[n+1];
        for(int gate : gates)
            isGate[gate] = true;
        
        for(int i=0; i<=n; i++)
            graph.add(new ArrayList<>());
        
        for(int[] path : paths) {
            int from = path[0];
            int to = path[1];
            int weight = path[2];
            
            graph.get(to).add(new int[]{from, weight});
            graph.get(from).add(new int[]{to, weight});
        }
        
        int minIntensity = paramSearch(
                graph,
                isGate,
                isSummit
        );

        int minSummit = findSummit(
                minIntensity,
                graph,
                isGate,
                isSummit
        );

        return new int[]{minSummit, minIntensity};
    }
    
    public int paramSearch(ArrayList<ArrayList<int[]>> graph, boolean[] isGate, boolean[] isSummit) {
        int left = 0;
        int right = 10_000_000;
        
        int answer = 0;
        while(left <= right) {
            int mid = left + (right - left)/2;
            
            if(canVisit(mid, graph, isGate, isSummit)) {
                answer = mid;
                right = mid - 1;
            }
            else
                left = mid + 1;
        }
        
        return answer;
    }
    
    public boolean canVisit(
        int target,
        ArrayList<ArrayList<int[]>> graph,
        boolean[] isGate,
        boolean[] isSummit) {
        
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[graph.size()];

        for(int i = 1; i < isGate.length; i++) {
            if(isGate[i]) {
                queue.offer(i);
                visited[i] = true;
            }
        }

        while(!queue.isEmpty()) {
            int curr = queue.poll();

            if(isSummit[curr])
                return true;

            for(int[] next : graph.get(curr)) {
                int nextNode = next[0];
                int weight = next[1];

                if(weight > target)
                    continue;

                if(visited[nextNode])
                    continue;

                if(isGate[nextNode])
                    continue;

                visited[nextNode] = true;
                queue.offer(nextNode);
            }
        }

        return false;
    }
    
    public int findSummit(
        int target,
        ArrayList<ArrayList<int[]>> graph,
        boolean[] isGate,
        boolean[] isSummit) {
        
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[graph.size()];

        for(int i = 1; i < isGate.length; i++) {
            if(isGate[i]) {
                queue.offer(i);
                visited[i] = true;
            }
        }

        int minSummit = Integer.MAX_VALUE;

        while(!queue.isEmpty()) {
            int curr = queue.poll();

            if(isSummit[curr]) {
                minSummit = Math.min(minSummit, curr);

                continue;
            }

            for(int[] next : graph.get(curr)) {
                int nextNode = next[0];
                int weight = next[1];

                if(weight > target)
                    continue;

                if(visited[nextNode])
                    continue;

                if(isGate[nextNode])
                    continue;

                visited[nextNode] = true;
                queue.offer(nextNode);
            }
        }

        return minSummit;
    }
}