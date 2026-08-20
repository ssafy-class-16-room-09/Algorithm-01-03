import java.util.Scanner;
import java.io.FileInputStream;
import java.util.*;

class Solution {

	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();


		/*
		   여러 개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		*/

		StringBuilder sb = new StringBuilder();
		for(int test_case = 1; test_case <= T; test_case++) {

			int R = sc.nextInt();
			int C = sc.nextInt();
			char[][] cmd = new char[R][C];

			for(int i=0; i<R; i++) {
				String str = sc.next();
				for(int j=0; j<C; j++)
					cmd[i][j] = str.charAt(j);
			}
	      
	      if(isStoppable(cmd))
	        sb.append('#').append(test_case).append(" YES").append('\n');
	      else
	        sb.append('#').append(test_case).append(" NO").append('\n');
		}

		System.out.print(sb.toString());
	}

	public static boolean isStoppable(char[][] cmd) {

	    /*
	      0 = 아래
	      1 = 위
	      2 = 오른쪽
	      3 = 왼쪽
	    */
	    int[] dr = {1, -1, 0, 0};
	    int[] dc = {0, 0, 1, -1};

	    int R = cmd.length;
	    int C = cmd[0].length;

	    boolean[][][][] visited = new boolean[R][C][4][16];

	    Queue<int[]> queue = new ArrayDeque<>();

	    // r, c, dir, memory
	    queue.offer(new int[]{0, 0, 2, 0});
	    visited[0][0][2][0] = true;

	    while (!queue.isEmpty()) {

	        int[] curr = queue.poll();

	        int r = curr[0];
	        int c = curr[1];
	        int dir = curr[2];
	        int memory = curr[3];

	        char op = cmd[r][c];

	        // 종료
	        if (op == '@')
	            return true;

	        // ?는 네 방향으로 분기
	        if (op == '?') {

	            for (int nextDir = 0; nextDir < 4; nextDir++) {

	                int nr = (r + dr[nextDir] + R) % R;
	                int nc = (c + dc[nextDir] + C) % C;

	                if (!visited[nr][nc][nextDir][memory]) {
	                    visited[nr][nc][nextDir][memory] = true;
	                    queue.offer(new int[]{
	                        nr, nc, nextDir, memory
	                    });
	                }
	            }

	            continue;
	        }

	        // 숫자
	        if (Character.isDigit(op)) {
	            memory = op - '0';
	        }

	        // 방향 지정
	        else if (op == '<') {
	            dir = 3;
	        }
	        else if (op == '>') {
	            dir = 2;
	        }
	        else if (op == '^') {
	            dir = 1;
	        }
	        else if (op == 'v') {
	            dir = 0;
	        }

	        // 조건부 방향
	        else if (op == '_') {
	            dir = (memory == 0) ? 2 : 3;
	        }
	        else if (op == '|') {
	            dir = (memory == 0) ? 0 : 1;
	        }

	        // memory 변경
	        else if (op == '+') {
	            memory = (memory + 1) % 16;
	        }
	        else if (op == '-') {
	            memory = (memory + 15) % 16;
	        }

	        // '.'은 아무것도 하지 않음

	        // 공통 이동
	        int nr = (r + dr[dir] + R) % R;
	        int nc = (c + dc[dir] + C) % C;

	        if (!visited[nr][nc][dir][memory]) {
	            visited[nr][nc][dir][memory] = true;

	            queue.offer(new int[]{
	                nr, nc, dir, memory
	            });
	        }
	    }

	    return false;
	}
}