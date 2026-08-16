import java.util.*;
import java.io.*;

class Solution {
	
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	static char[][] cmdMap;
	static boolean[][][][] visited; // [r][c][dir][mem]
	static final char OUT_OF_BOUND = ' ';
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
	
	private static int[] move(int currX, int currY, int dir, int mem) {
		currX += dx[dir];
		currY += dy[dir];
		if (cmdMap[currX][currY] == OUT_OF_BOUND) {
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
	
	private static int[] processAndNext(char cmd, int[] curr) {
		int currX = curr[0];
		int currY = curr[1];
		int dir = curr[2];
		int mem = curr[3];
				
		switch (cmd) {
		case '<':
			dir = LEFT;
			break;
		case '>':
			dir = RIGHT;
			break;
		case '^':
			dir = UP;
			break;
		case 'v':
			dir = DOWN;
			break;
		case '_':
			if (mem == 0) {
				dir = RIGHT;
			} else {
				dir = LEFT;
			}
			break;
		case '|':
			if (mem == 0) {
				dir = DOWN;
			} else {
				dir = UP;
			}
			break;
		case '.':
			break;
		case '+':
			mem = (mem == 15) ? 0 : mem + 1;
			break;
		case '-':
			mem = (mem == 0) ? 15 : mem - 1;
			break;
		}
		
		if (cmd >= '0' && cmd <= '9') { // 숫자라면 숫자를 메모리에 저장 
			mem = cmd - '0';
		}
		
		return move(currX, currY, dir, mem);
	}
	
	private static void fillMap()  throws IOException {
		Arrays.fill(cmdMap[0], OUT_OF_BOUND);
		for (int i = 1; i < r + 1; i++) {
			cmdMap[i][0] = OUT_OF_BOUND;
			String cmd = br.readLine();
			for (int j = 1; j < c + 1; j++) {
				cmdMap[i][j] = cmd.charAt(j - 1);
			}
			cmdMap[i][c + 1] = OUT_OF_BOUND;
		}
		Arrays.fill(cmdMap[r + 1], OUT_OF_BOUND);
	}
}