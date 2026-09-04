import java.util.*;
import java.io.*;

class Microbe implements Comparable<Microbe> {
	int r; // 세로 위치
	int c; // 가로 위치
	int cnt; // 미생물 개수
	int direction; // 이동방향
	
	Microbe(int r, int c, int cnt, int direction) {
		this.r = r;
		this.c = c;
		this.cnt = cnt;
		this.direction = direction;
	}
	
	@Override
	public int compareTo(Microbe m) {
		return Integer.compare(m.cnt, this.cnt);
	}
}

public class Solution {
	// 상: 1, 하: 2, 좌: 3, 우: 4
	static final int[] dr = new int[] {0, -1, 1, 0, 0};
	static final int[] dc = new int[] {0, 0, 0, -1, 1};
	
	static final int[] different = new int[] {0, 2, 1, 4, 3}; // 방향 전환
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine()); // 테스트 케이스 개수
		
		StringBuilder sb = new StringBuilder();
		for(int test_case=1; test_case<=T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken()); // N(셀의 개수)
			int M = Integer.parseInt(st.nextToken()); // M(시간)
			int K = Integer.parseInt(st.nextToken()); // K(미생물 군집 개수)
			
			int result = 0; // 미생물
			
			Queue<Microbe> microbes = new ArrayDeque<>(); // 미생물들
			// 미생물 정보 입력 및 구역 표시
			for(int i=0; i<K; i++) {
				st = new StringTokenizer(br.readLine());
				
				int r = Integer.parseInt(st.nextToken());
				int c = Integer.parseInt(st.nextToken());
				int cnt = Integer.parseInt(st.nextToken());
				int direction = Integer.parseInt(st.nextToken());
				Microbe microbe = new Microbe(r, c, cnt, direction);
				
				result += cnt; // 초기 총 미생물 개수
				microbes.offer(microbe); // 미생물 정보
			}
			 
			while(M-- > 0) {
				int size = microbes.size(); // 현재 미생물 개수
				
				Map<String, PriorityQueue<Microbe>> position = new HashMap<>(); // 좌표별 미생물 정보
				
				while(size-- > 0) {
					Microbe microbe = microbes.poll();
					
					int nr = microbe.r + dr[microbe.direction];
					int nc = microbe.c + dc[microbe.direction];
					
					// 이동한 칸이 약품이 있는 곳인 경우
					if(isMedicine(nr, nc, N)) {
						result -= microbe.cnt;
						microbe.cnt /= 2; // 절반으로 줄이고,
						result += microbe.cnt;
						microbe.direction = different[microbe.direction]; // 방향 반대로 전환
					}
					
					microbe.r = nr;
					microbe.c = nc;

					// 각 좌표에 미생물 넣기
					String pos = nr + " " + nc;
					if(position.containsKey(pos)) {
						PriorityQueue<Microbe> pq = position.get(pos);
						pq.offer(microbe);
					} else {
						PriorityQueue<Microbe> pq = new PriorityQueue<>();
						pq.offer(microbe);
						position.put(pos, pq);
					}
				}
				
				// 미생물의 변경 사항 저장(2마리 이상인 경우 합치고, 방향 큰 쪽으로)
				for(String pos : position.keySet()) {
					PriorityQueue<Microbe> pq = position.get(pos);
					Microbe m1 = pq.poll();
					int total = m1.cnt;
					while(!pq.isEmpty()) total += pq.poll().cnt;
					microbes.offer(new Microbe(m1.r, m1.c, total, m1.direction));
				}
			}
			
			sb.append(String.format("#%d %d\n", test_case, result));
		}
		System.out.print(sb);
	}
	
	private static boolean isMedicine(int r, int c, int n) {
		return (r == 0 || r == n-1 || c == 0 || c == n-1);
	}
}