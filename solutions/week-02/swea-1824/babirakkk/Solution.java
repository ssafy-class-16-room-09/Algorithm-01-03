import java.util.*;
import java.io.*;

class Solution {
	
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	static char[][] cmdMap;
	static boolean[][][][] visited; // [r][c][dir][mem]
	static final char PADDING = ' ';
	static final int UP = 0;
	static final int DOWN = 1;
	static final int LEFT = 2;
	static final int RIGHT = 3;
	static final int[] dx = new int[] { -1, 1, 0, 0 };
	static final int[] dy = new int[] { 0, 0, -1, 1 };
	static int r;
	static int c;
	
	
	public static void main(String args[]) throws IOException {
		int testCases = Integer.parseInt(br.readLine());
		for (int t = 1; t <= testCases; t++) {
			 bw.write("#" + t + " " + program() + "\n");
		} 
		bw.flush();
		bw.close();
		br.close();
	}
	

	/**
	 * 하나의 tc에 대해 혁진이의 프로그램 확인
	 * BFS를 활용해 모든 가능한 실행 경로 탐색
	 * 
	 * @return 프로그램이 정지 문자(@)에 도달할 수 있으면 "YES", 도달할 수 없으면 "NO"
	 * @throws IOException 입력 스트림 읽기 실패 시 발생
	 */
	private static String program() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		cmdMap = new char[r + 2][c + 2];
		visited = new boolean[r + 2][c + 2][4][16]; // [r][c][dir][mem]
		
		fillMap(); // 입력을 읽고 cmpMap에 채우기
		
		boolean canStop = false;
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[]{1, 1, RIGHT, 0});
		visited[1][1][RIGHT][0] = true;
		while (!q.isEmpty()) {
			int[] curr = q.poll();
						
			char cmd = cmdMap[curr[0]][curr[1]];
			
			if (cmd == '@') {
				canStop = true;
				break;
			}
			
			if (cmd == '?') {
				for (int i = 0; i < 4; i++) {
					int[] next = move(curr[0], curr[1], i, curr[3]);
					if (next != null) {
						q.add(next);
					}
				}
			} else {
				int[] next = processAndNext(cmd, curr);
				if (next != null) {
					q.add(next);
				}
			}
		}
		
		return canStop ? "YES" : "NO";
	}
	

	/**
	 * 현재 명령어를 읽어 진행 방향과 메모리 상태를 업데이트하고 다음 위치 상태 배열을 반환
	 * 
	 * @param cmd 현재 위치의 수행 명령어
	 * @param curr 현재 상태 배열 [r, c, dir, mem]
	 * @return  다음 위치 상태 배열 [r, c, dir, mem], 만약 이미 방문한 상태라면 null 반환
	 */
	private static int[] processAndNext(char cmd, int[] curr) {
		int currX = curr[0];
		int currY = curr[1];
		int dir = curr[2];
		int mem = curr[3];
				
		switch (cmd) {
			case '<' -> dir = LEFT;
			case '>' -> dir = RIGHT;
			case '^' -> dir = UP;
			case 'v' -> dir = DOWN;
			case '_' -> dir = (mem == 0) ? RIGHT : LEFT;
			case '|' -> dir = (mem == 0) ? DOWN : UP;
			case '+' -> mem = (mem == 15) ? 0 : mem + 1;
			case '-' -> mem = (mem == 0) ? 15 : mem - 1;
		}
		
		if (cmd >= '0' && cmd <= '9') { // 숫자라면 숫자를 메모리에 저장 
			mem = cmd - '0';
		}
		
		return move(currX, currY, dir, mem);
	}


	/**
	 * 현재 위치와 상태를 기반으로 다음 위치 및 상태 배열을 계산해 반환
	 * 
	 * @param currX 현재 r 위치
	 * @param currY 현재 c 위치
	 * @param dir 진행 방향(0: UP, 1: DOWN, 2: LEFT, 3: RIGHT)
	 * @param mem 현재 메모리 값
	 * @return 다음 상태 배열 [r, c, dir, mem], 만약 이미 방문한 상태인 경우 null 반환
	 */
	private static int[] move(int currX, int currY, int dir, int mem) {
		currX += dx[dir];
		currY += dy[dir];
		if (cmdMap[currX][currY] == PADDING) {
			switch (dir) {
			case UP:
				currX += r;
				break;
			case DOWN:
				currX -= r;
				break;
			case LEFT:
				currY += c;
				break;
			case RIGHT:
				currY -= c;
				break;
			}
		}
		if (visited[currX][currY][dir][mem]) {
			return null;
		}
		visited[currX][currY][dir][mem] = true;
		return new int[] {currX, currY, dir, mem};
	}
	

	/**
	 * 명령어 데이터를 읽어 2차원 배열에 채움
	 * 상하좌우 테두리를 PADDING 문자로 채워 격자 경계 구분
	 * 
	 * @throws IOException 입력 스트림 읽기 실패 시 발생
	 */
	private static void fillMap()  throws IOException {
		Arrays.fill(cmdMap[0], PADDING);
		for (int i = 1; i < r + 1; i++) {
			cmdMap[i][0] = PADDING;
			String cmd = br.readLine();
			for (int j = 1; j < c + 1; j++) {
				cmdMap[i][j] = cmd.charAt(j - 1);
			}
			cmdMap[i][c + 1] = PADDING;
		}
		Arrays.fill(cmdMap[r + 1], PADDING);
	}
}