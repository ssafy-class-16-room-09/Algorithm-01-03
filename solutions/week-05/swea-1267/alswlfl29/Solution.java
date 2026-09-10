import java.util.*;
import java.io.*;

class Solution {
	static int[] fanIn; // 해당 정점으로 들어오는 간선의 개수
	static List<Integer>[] fanOut; // 해당 정점에서 나가는 간선 정보
	static int V; // 정점 개수
	
	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = 10;
		
		StringBuilder sb = new StringBuilder();
		for(int test_case = 1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			V = Integer.parseInt(st.nextToken());
			int E = Integer.parseInt(st.nextToken());  // 간선의 개수
			
			fanIn = new int[V+1];
			fanOut = new ArrayList[V+1];
			for(int v=1; v<=V; v++) {
				fanOut[v] = new ArrayList<>();
			}
			
			st = new StringTokenizer(br.readLine());
			for(int i=0; i<2*E; i+=2) {
				int node1 = Integer.parseInt(st.nextToken());
				int node2 = Integer.parseInt(st.nextToken());
				
				fanIn[node2]++; // 들어오는 간선의 개수 증가
				fanOut[node1].add(node2); // 정점에서 나가는 간선 정보
			}
			String order = topologicalSort();
			sb.append(String.format("#%d %s\n", test_case, order));
		}
		System.out.print(sb);
	}
	
	// 위상정렬
	private static String topologicalSort() {
		Queue<Integer> queue = new ArrayDeque<>(); // 작업 실행이 가능한 대기 큐
		
		// 가장 먼저 실행되어야 하는 정점 대기 큐에 넣기
		for(int node=1; node<=V; node++) {
			if(fanIn[node] == 0) {
				queue.add(node);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		while(!queue.isEmpty()) {
			int node = queue.poll();
			sb.append(node).append(" "); // 실행 순서에 추가
			
			for(int next : fanOut[node]) {
				// 아직 작업을 수행하지 않은 정점의 경우, 선행 작업 1개 제외
				if(fanIn[next] > 0) {
					fanIn[next]--;
					
					if(fanIn[next] == 0) queue.add(next); // 선행 작업이 0인 작업 대기 큐에 넣기
				}
			}
		}
		return sb.toString();
	}
}

/*
 * return > 올바른 작업 순서 출력
 * 
 * 위상 정렬 알고리즘 이용
 * - in/out 배열 카운트
 * - in의 개수가 0인 경우, 작업 수행 가능(Queue에 넣기)
 * - 작업이 수행되면, out 방향에 있는 노드의 in 제거
 * 
 * node 1 2 3 4 5 6 7 8 9
 * in   1 1 1 0 2 2 1 0 1
 * 
 * 1 -> 2
 * 2 -> 3, 7
 * 3 -> 0
 * 4 -> 1
 * 5 -> 6
 * 6 -> 0
 * 7 -> 6
 * 8 -> 5, 9
 * 9 -> 0
 * 
 * step 1)
 * Queue = [4, 8]
 * 4 수행
 * node 1 2 3 4 5 6 7 8 9
 * in   0 1 1 0 2 2 1 0 1
 * Queue에 1 넣기
 * 
 * step 2)
 * Queue = [8, 1]
 * 8 수행
 * node 1 2 3 4 5 6 7 8 9
 * in   0 1 1 0 1 2 1 0 0
 * Queue에 9 넣기
 * 
 * step 3)
 * Queue = [1, 9]
 * 1 수행
 * node 1 2 3 4 5 6 7 8 9
 * in   0 0 1 0 0 2 1 0 0
 * Queue에 2 넣기
 * */