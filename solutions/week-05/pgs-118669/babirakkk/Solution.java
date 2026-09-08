import java.util.*;

class Node {
    int idx;
    int intensity;
    Node(int idx, int intensity) {
        this.idx = idx;
        this.intensity = intensity;
    }
}

class Path {
    int next;
    int weight;
    Path(int next, int weight) {
        this.next = next;
        this.weight = weight;
    }
}

class Solution {
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        int[] intensity = new int[n + 1];
        Arrays.fill(intensity, Integer.MAX_VALUE);
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt((Node node) -> node.intensity));
        
        for (int gate : gates) {
            intensity[gate] = 0;
            pq.add(new Node(gate, 0)); // 모든 출입구에서 동시에 탐색 시작
        }
        boolean[] isSummit = new boolean[n + 1];
        for (int summit : summits) {
            isSummit[summit] = true;
        }
        ArrayList<Path>[] pathList = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            pathList[i] = new ArrayList<>();
        }
        for (int[] path : paths) {
            pathList[path[0]].add(new Path(path[1], path[2]));
            pathList[path[1]].add(new Path(path[0], path[2]));
        }
        
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (curr.intensity > intensity[curr.idx]) continue; // 이미 더 작은 intensity로 방문한 경우
            
            for (Path path : pathList[curr.idx]) {
                int nextIntensity = Math.max(curr.intensity , path.weight); // 현재 경로의 최대 가중치가 intensity
                if (intensity[path.next] > nextIntensity) {
                    intensity[path.next] = nextIntensity;
                    
                    if (isSummit[path.next]) continue; // 산봉우리에 도착하면 이후 경로는 탐색하지 않음
                    
                    pq.add(new Node(path.next, nextIntensity));
                }
            }
        }
        
        int minSummit = summits[0];
        for (int summit : summits) { // intensity가 가장 작은 산봉우리 선택, 같으면 번호가 작은 산봉우리 선택
            if (intensity[summit] < intensity[minSummit] 
                || (intensity[summit] == intensity[minSummit] && summit < minSummit)) {
                minSummit = summit;
            }
        }
        return new int[]{minSummit, intensity[minSummit]};
    }
}