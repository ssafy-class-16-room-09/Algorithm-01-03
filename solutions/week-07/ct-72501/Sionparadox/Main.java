import java.util.*;
import java.io.*;

// class Group implements Comparable<Group>{
class Group {
    int idx, r1, c1, r2, c2, size;
    ArrayList<Integer> elements;
    int repR = 20, repC = 20;


    public Group(int idx, int r1, int c1, int r2, int c2){
        this.idx = idx;
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
        this.size = (r2-r1+1)*(c2-c1+1);
        this.elements = new ArrayList<>();
    }

    // @Override
    // public int compareTo(Group g){
    //     if (this.size != g.szie) return Integer.compare(g.size, this.size);
    //     return Integer.compare(this.idx, g.idx);
    // }

}

public class Main {
    static int[][] board;
    static Group[] groups;
    static boolean[][] visited;
    static int N, Q;
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    static boolean[] appeared;
    static boolean[][] meet;
    


    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        groups = new Group[Q+1];
        int ret;
        for (int i=1; i<=Q; i++){
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            groups[i] = new Group(i, r1, c1, r2-1, c2-1);

            ret = 0;
            visited = new boolean[N][N];
            appeared = new boolean[Q+1];

            //놓기
            for (int r=r1; r<r2; r++){
                for (int c=c1; c<c2; c++){
                    board[r][c] = i;
                }
            }

            //확인하기 (크기, 분리)
            // 이미 탐색했던 그룹이 또나오면 분리된 것 -> null로 만들기
            for (int r=0; r<N; r++){
                for (int c=0; c<N; c++){
                    if (visited[r][c] || board[r][c] == 0) continue;
                    int gIdx = board[r][c];
                    if (appeared[gIdx]) groups[gIdx] = null;
                    else {
                        search(r, c);
                        appeared[gIdx] = true;
                    }
                }
            }

            //그룹 정리
            for (int gIdx = 1; gIdx <= i; gIdx++) {
                if (groups[gIdx] != null && !appeared[gIdx]) {
                    groups[gIdx] = null;
                }
            }

            //보드 초기화
            for (int[] row : board){
                Arrays.fill(row, 0);
            }

            //그룹 정리
            ArrayList<Group> arr = new ArrayList<>();
            for (Group group: groups){
                if (group != null) arr.add(group);
            }
            // System.out.println("HERE: "+Arrays.toString(groups));
            // for (int[] row:board){
            //     System.out.println(Arrays.toString(row));
            // }

            Collections.sort(arr, (o1, o2) ->{
                if (o1.size != o2.size) return Integer.compare(o2.size, o1.size);
                return Integer.compare(o1.idx, o2.idx);
            });

            // 위치 업데이트
            for (Group group: arr){
                place(group.idx);
                
            }

            visited = new boolean[N][N];
            meet = new boolean[Q+1][Q+1];

            for (int r=0; r<N; r++){
                for (int c=0; c<N; c++){
                    if (visited[r][c] || board[r][c] == 0) continue;
                    ret += findNext(r, c);

                }
            }

            

            System.out.println(ret);
        }

    }

    private static void search(int r, int c){
        int gIdx = board[r][c];
        Group group = groups[gIdx];
        
        group.repR = N;
        group.repC = N;

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {r, c});

        visited[r][c] = true;

        int size = 0;
        ArrayList<Integer> list = new ArrayList<>();

        while (!queue.isEmpty()){
            int[] curr = queue.poll();
            r = curr[0];    
            c = curr[1];
            list.add(r*N+c);
            size++;

            for (int d=0; d<4; d++){
                int nr = r+dr[d], nc = c+dc[d];
                if (nr<0 || nr>=N || nc<0 || nc>=N) continue;
                if (visited[nr][nc] || board[nr][nc] != gIdx) continue;
                visited[nr][nc] = true;
                queue.offer(new int[] {nr, nc});
                
            }
        }
        group.size = size;
        group.elements = list;

        for (int pos:list){
            r = pos/N;
            c = pos%N;
            if (r<group.repR){
                group.repR = r; 
                group.repC = c;
            }
            if (r == group.repR && c<group.repC) group.repC = c;
            
        }
    }

    private static void place(int gIdx){
        Group group = groups[gIdx];
        for (int r=0; r<N; r++){
            OUT: for (int c=0; c<N; c++){
                int rDiff = r - group.repR;
                int cDiff = c - group.repC;

                for (int pos:group.elements){
                    int pr = pos/N;
                    int pc = pos%N;
                    int nr = pr+rDiff;
                    int nc = pc+cDiff;
                    if (nr<0 || nr>=N || nc<0 || nc>=N) continue OUT;
                    if (board[nr][nc] != 0) continue OUT;
                    
                }
                
                for (int pos:group.elements){
                    int pr = pos/N;
                    int pc = pos%N;
                    int nr = pr+rDiff;
                    int nc = pc+cDiff;
                    board[nr][nc] = gIdx;
                }
                return;
                
            }
        }
        groups[gIdx] = null;
    }

    private static int findNext(int r, int c){
        int ret = 0;

        int gIdx = board[r][c];

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {r, c});
        visited[r][c] = true;

        while (!queue.isEmpty()){
            int[] curr = queue.poll();
            r = curr[0];
            c = curr[1];

            for (int d=0; d<4; d++){
                int nr = r+dr[d], nc = c+dc[d];
                if (nr<0 || nr >=N || nc<0 || nc>=N) continue;
                if (visited[nr][nc]) continue;
                int nxt = board[nr][nc];
                if (nxt == 0) continue;
                if (nxt == gIdx) {
                    visited[nr][nc] = true;
                    queue.offer(new int[] {nr, nc});
                } else if (!meet[gIdx][nxt]){
                    meet[gIdx][nxt] = true;
                    meet[nxt][gIdx] = true;
                    ret += groups[gIdx].size * groups[nxt].size;
                }
            }
        }

        return ret;
    }
}

/*
idx -> group

Group에 size와 elements저장
elements : r*N+c
그룹에 r, c가 가장 작은 점 저장

N^2 = 225
놓기 : 225*50 = 만

------------------------------
직사각형 : r1, c1  ~ r2-1, c2-1
영역이 넓은 무리 -> 먼저 투입된 무리

모양을 유지

A, B가 맞닿으면 넓이곱을 누적
------------------------------
*/
