package algorithm;
import java.util.*;
import java.io.*;

class Pos {
	int r; // 행 위치
	int c; // 열 위치
	int cost; // 복구 작업에 드는 시간
	
	Pos(int r, int c, int cost) {
		this.r = r;
		this.c = c;
		this.cost = cost;
	}
}

class Solution {
	static final int MAX_INT = Integer.MAX_VALUE;
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, -1, 0, 1};
	
	public static void main(String args[]) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int T = Integer.parseInt(br.readLine()); // 테스크 케이스 개수

		StringBuilder sb = new StringBuilder();
		for(int test_case = 1; test_case <= T; test_case++)
		{
			int N = Integer.parseInt(br.readLine()); // 지도의 크기
			
			int[][] map = new int[N][N]; // 지도
			for(int r=0; r<N; r++) {
				String[] row = br.readLine().split("");
				for(int c=0; c<N; c++) {
					map[r][c] = Integer.parseInt(row[c]); // 지도 정보 채우기
				}
			}
			
			PriorityQueue<Pos> pq = new PriorityQueue<>(
					Comparator.comparing((Pos pos) -> pos.cost)); // PQ 생성(cost 기준 오름차순)
			int[][] visited = new int[N][N]; // 각 위치까지 발생한 복구 작업 시간
			for(int r=0; r<N; r++) {
				Arrays.fill(visited[r], MAX_INT); // 각 위치의 복구 작업 시간 INT의 최대 범위로 초기화
			}
			
			pq.offer(new Pos(0, 0, 0)); // 시작 위치(0, 0) PQ에 넣기
			visited[0][0] = 0; // 시작 위치 발생 비용 0으로 설정
			
			while(!pq.isEmpty()) {
				Pos current = pq.poll(); // PQ에서 비용이 가장 낮은 정보 꺼내기
				
				if(current.r == N-1 && current.c == N-1) break;
				
				if(current.cost > visited[current.r][current.c]) continue; // 더 작은 시간으로 갈 수 있는 경우가 있는 경우 다음으로 넘어가기
				
				for(int d=0; d<4; d++) {
					int nr = current.r + dr[d]; // 다음 행 위치
					int nc = current.c + dc[d]; // 다음 열 위치
					if(isOut(nr, nc, N)) continue; // 다음 위치가 영역을 벗어나는지 확인
					
					int repairCost = current.cost + map[nr][nc]; // 복구 작업 시간
					// visited 배열에 저장된 다음 위치의 복구 작업 시간이 현재 계산된 복구 작업 시간보다 큰 경우, 더 작은 것으로 업데이트
					if(visited[nr][nc] > repairCost) {
						pq.offer(new Pos(nr, nc, repairCost));
						visited[nr][nc] = repairCost;
					}
				}
			}
			sb.append(String.format("#%d %d\n", test_case, visited[N-1][N-1]));
		}
		
		System.out.print(sb);
	}
	
	// 현재 위치가 지도 영역을 벗어나는지 확인하는 메서드
	private static boolean isOut(int r, int c, int n) {
		return r < 0 || c < 0 || r >= n || c >= n;
	}
}

/*
 * return: 출발지에서 도착지까지 가는 경로 중에 복구 작업에 드는 시간이 가장 작은 경로의 복구 시간 출력
 * 
 * 다익스트라 알고리즘 이용
 * - 현재까지 복구 작업에 드는 시간이 작은 것 기준으로 정렬
 * - (0, 0)부터 시작해 visited 배열을 사용해 더 적은 복구 시간이 발생하는 경우 PQ에 넣고, 해당 시간 갱신
 * */