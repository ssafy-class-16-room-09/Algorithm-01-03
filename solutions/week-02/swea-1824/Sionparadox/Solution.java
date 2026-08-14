import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        //RLUD
        int[] dr = {0, 0, -1, 1};
        int[] dc = {1, -1, 0, 0};
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int TC = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        for (int tc=1; tc<=TC; tc++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int R = Integer.parseInt(st.nextToken());
            int C = Integer.parseInt(st.nextToken());
            char[][] board = new char[R][C];
            for (int i=0; i<R; i++){
                String line = br.readLine();
                for (int j=0; j<C; j++){
                    board[i][j] = line.charAt(j);
                }
            }
            
            boolean isFinished = false;
            boolean[][][][] visited = new boolean[R][C][16][4];
            visited[0][0][0][0] = true;
            Queue<int[]> queue = new ArrayDeque<>();
            queue.offer(new int[] {0, 0, 0, 0});

            while (!queue.isEmpty()){
                int[] curr = queue.poll();
                int r = curr[0];
                int c = curr[1];
                int dir = curr[2];
                int memory = curr[3];
                char cmd = board[r][c];
                if (cmd == '?'){
                    for (int d=0; d<4;d++){
                        dir = d;
                        int nr = (r+dr[dir]+R)%R;
                        int nc = (c+dc[dir]+C)%C;
                        if (visited[nr][nc][memory][dir]){
                            continue;
                        }
                        visited[nr][nc][memory][dir] = true;
                        queue.offer(new int[] {nr, nc, dir, memory});

                    }
                    continue;
                }
                
                if (cmd == '<'){
                    dir = 1;
                } else if (cmd == '>'){
                    dir = 0;
                } else if (cmd == '^'){
                    dir = 2;
                } else if (cmd == 'v'){
                    dir = 3;
                } else if (cmd == '_'){
                    if (memory == 0) dir = 0;
                    else dir = 1;
                } else if (cmd == '|'){
                    if (memory == 0) dir = 3;
                    else dir = 2;
                } else if (cmd == '@'){
                    isFinished = true;
                    break;
                } else if (cmd == '+'){
                    memory = (memory + 1) % 16;
                } else if (cmd == '-'){
                    
                    memory = memory == 0 ? 15 : memory-1;
                } else if ('0'<=cmd && cmd<='9'){
                    memory = cmd-'0';
                } else {
                    
                }
                int nr = (r+dr[dir]+R)%R;
                int nc = (c+dc[dir]+C)%C;
                if (visited[nr][nc][memory][dir]){
                    continue;
                }
                visited[nr][nc][memory][dir] = true;
                queue.offer(new int[] {nr, nc, dir, memory});
            }

            sb.append("#"+ tc + (isFinished ? " YES\n":" NO\n"));
        }
        System.out.println(sb.toString());
    }
}
/**
 * <	이동 방향을 왼쪽으로 바꾼다.
>	이동 방향을 오른쪽으로 바꾼다.
^	이동 방향을 위쪽으로 바꾼다.
v	이동 방향을 아래쪽으로 바꾼다.
_	메모리에 0이 저장되어 있으면 이동 방향을 오른쪽으로 바꾸고, 아니면 왼쪽으로 바꾼다.
|	메모리에 0이 저장되어 있으면 이동 방향을 아래쪽으로 바꾸고, 아니면 위쪽으로 바꾼다.
?	이동 방향을 상하좌우 중 하나로 무작위로 바꾼다. 방향이 바뀔 확률은 네 방향 동일하다.
.	아무 것도 하지 않는다.
@	프로그램의 실행을 정지한다.
0~9	메모리에 문자가 나타내는 값을 저장한다.
+	메모리에 저장된 값에 1을 더한다. 만약 더하기 전 값이 15이라면 0으로 바꾼다.
-	메모리에 저장된 값에 1을 뺀다. 만약 빼기 전 값이 0이라면 15로 바꾼다.
 * 
 */