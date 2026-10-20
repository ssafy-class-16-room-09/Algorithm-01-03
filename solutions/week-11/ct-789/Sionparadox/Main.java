import java.util.*;
import java.io.*;

class Puzzle {
    int[] pos;
    HashSet<Integer> shape;
    public Puzzle(int u, int r, int d, int l){
        // 이후에 맞닿는 면 검사를 위해 u, r, d, l에 역방향을 저장
        pos = new int[] {d, l, u, r};
        shape = new HashSet<>();
        // 퍼즐 모양을 3진법으로 표시해서 가능한 모양 전부 미리 만들어둠
        shape.add(u*27+r*9+d*3+l);
        shape.add(r*27+d*9+l*3+u);
        shape.add(d*27+l*9+u*3+r);
        shape.add(l*27+u*9+r*3+d);
    }
}

public class Main {
    static int N;
    static Puzzle[][] board;
    // 만들 수 있는 스페어 모양 카운트(4회전 다)
    // 구멍에 들어가는 모양 카운트 (회전x)
    static int[] spareCnt, holeCnt;
    // u, r, d, l 순서
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};


    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int Q = Integer.parseInt(br.readLine());
        for (int q=0; q<Q; q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if (cmd == 100){
                N = Integer.parseInt(st.nextToken());
                init();
            } else if (cmd == 200){
                int y = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                int u = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                int l = Integer.parseInt(st.nextToken());
                Puzzle p = new Puzzle(u, r, d, l);
                place(y-1, x-1, p);
            } else if (cmd == 300){
                int y = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                remove(y-1, x-1);
            } else if (cmd == 400){
                int u = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                int l = Integer.parseInt(st.nextToken());
                addSpare(new Puzzle(u, r, d, l));
            } else {
                System.out.println(score());
            }
        }
    }

    private static void init(){
        board = new Puzzle[N][N];
        spareCnt = new int[81];
        holeCnt = new int[81];
    }

    private static void place(int r, int c, Puzzle p){
        // 퍼즐을 놓기 전에 r, c와 연관된 구멍 정보 깎기
        updateHole(r, c, -1);
        board[r][c] = p;
        // 퍼즐을 놓고 r, c와 연관된 구멍 정보 추가
        updateHole(r, c, 1);
    }

    private static void remove(int r, int c){
        // 퍼즐을 제거하기 전에 r, c와 연관된 구멍 정보 깎기
        updateHole(r, c, -1);
        board[r][c] = null;
        // 퍼즐을 제거하고 r, c와 연관된 구멍 정보 추가
        updateHole(r, c, 1);
    }

    // 4방향이 다 벽이거나 퍼즐이어야함. 빈칸 x
    private static boolean isHole(int r, int c){
        // 현재 칸에 퍼즐이 있으면 구멍 아님
         if (board[r][c] != null) return false;

        for (int d=0; d<4; d++){
            // 4방향 중 하나라도 빈칸이면 구멍 아님
            int nr = r+dr[d], nc = c+dc[d];
            if(nr<0 || nr>=N || nc<0 || nc>=N) continue;
            if (board[nr][nc] != null) continue;
            return false;
        }
        return true;
    }

    // 구멍의 모양을 3진법으로 표시
    static int getHoleShape(int r, int c){
        int res = 0;
        for (int d=0; d<4; d++){
            res *= 3;
            int nr=r+dr[d], nc=c+dc[d];
            // 벽이면 직선모양=1
            if (nr<0 || nr>=N || nc<0 || nc>=N) res += 1;
            else {
                Puzzle p = board[nr][nc];
                // 맞닿는 면이 퍼즐이면 2에서 해당 퍼즐의 맞닿는 면 번호를 뻄 (오목:0 -> 볼록:2, 직선:1->직선:1, 볼록:2->오목:0)
                res += 2-p.pos[d];                
            }
        }

        return res;
    }
    // k는 구멍을 어떻게 업데이트할지 정보(+1, -1)
    private static void updateHole(int r, int c, int k){
        // 현재 칸이 구멍이면 구멍 모양 +k
        if (isHole(r, c)){
            int shape = getHoleShape(r, c);
            holeCnt[shape] += k;
        }

        // 주변 4방향중 구멍이면 구멍 모양 +k
        for (int d=0; d<4; d++){
            int nr=r+dr[d], nc=c+dc[d];
            if (nr<0 || nr>=N || nc<0 || nc>=N) continue;
            if (!isHole(nr, nc)) continue;
            int shape = getHoleShape(nr, nc);
            holeCnt[shape] += k;
        }
        
    }

    private static void addSpare(Puzzle p){
        // 퍼즐의 모든 회전모양에 대해 스페어 모양 카운트 추가
        for (int shape:p.shape){
            spareCnt[shape]++;
        }
    }

    private static long score(){
        long res = 0;
        // 구멍 모양과 스페어 모양을 곱해서 만들 수 있는 개수 계산
        for (int i=0; i<81; i++){
            res += (long) holeCnt[i]*spareCnt[i];
        }

        return res;
    }
}

/*
y : r
x : c

URDL
오목 : 0, 직선 : 1, 볼록 : 2

abcd
a*27+b*9+c*3+d
*/