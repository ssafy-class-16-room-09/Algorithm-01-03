import java.util.*;
import java.io.*;

class Pos {
	int r;
	int c;
	int memory;
	int d;
	
	public Pos(int r, int c, int memory, int d) {
		this.r = r;
		this.c = c;
		this.memory = memory;
		this.d = d;
	}
}

class Solution {
    // 위, 왼, 아, 오
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, -1, 0, 1};
	static boolean[][][][] visited; // 무한 루프 확인을 위한 방문 배열
	
	public static void main(String args[]) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine()); // 테스트케이스 개수
		
		StringBuilder sb = new StringBuilder();
		for(int test_case=1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int R = Integer.parseInt(st.nextToken()); // 행
			int C = Integer.parseInt(st.nextToken()); // 열
			visited = new boolean[R][C][4][16];
			
			char[][] program = new char[R][C]; // 프로그램 2차원 배열
			boolean endCh = false; // 프로그램 종료 문자가 존재하는지
			for(int r=0; r<R; r++) {
				String line = br.readLine();
				for(int c=0; c<C; c++) {
					char ch = line.charAt(c);
					if(ch == '@') endCh = true; // 종료 문자 있는지 확인
					program[r][c] = ch;
				}
			}
			
			boolean answer = false;

			Queue<Pos> queue = new ArrayDeque<>(); 
			queue.offer(new Pos(0, 0, 0, 3));
			visited[0][0][0][3] = true;
			
			// 프로그램 종료 문자가 없는 경우 해당 프로그램은 정지 못함
			while(endCh && !queue.isEmpty()) {
				Pos pos = queue.poll();
				
				char ch = program[pos.r][pos.c];
				int d = pos.d;
				int memory = pos.memory;
				if(ch == '@') {
					answer = true; // 프로그램 중단
					break;
				} else if(ch == '?') {
					for(int i=0; i<4; i++) {
						d = i;
						int nr = ((pos.r + dr[d]) % R + R) % R;
						int nc = ((pos.c + dc[d]) % C + C) % C;
						// 똑같은 조건으로 들어온 경우
						if(visited[nr][nc][d][memory]) continue;
						visited[nr][nc][d][memory] = true;
						queue.offer(new Pos(nr, nc, memory, d));
					}
					continue;
				} else if(ch == '<' || (ch == '_' && memory != 0)) {
					d = 1;
				} else if(ch == '>' || (ch == '_' && memory == 0)) {
					d = 3;
				} else if(ch == '^' || (ch == '|' && memory != 0)) {
					d = 0;
				} else if(ch == 'v' || (ch == '|' && memory == 0)) {
					d = 2;
				} else if(ch >= '0' && ch <= '9') {
					memory = ch - '0';
				} else if(ch == '+') {
					memory = pos.memory == 15 ? 0 : memory+1;
				} else if(ch == '-') {
					memory = pos.memory == 0 ? 15 : memory-1;
				}
				int nr = ((pos.r + dr[d]) % R + R) % R;
				int nc = ((pos.c + dc[d]) % C + C) % C;
				// 똑같은 조건으로 들어온 경우
				if(visited[nr][nc][d][memory]) continue;
				visited[nr][nc][d][memory] = true;
				queue.offer(new Pos(nr, nc, memory, d));
				
			}
			
			sb.append(String.format("#%d %s\n", test_case, answer ? "YES" : "NO"));
			
		}
		System.out.print(sb);
		br.close();
	}
}