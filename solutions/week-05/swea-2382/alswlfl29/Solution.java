import java.util.*;
import java.io.*;
 
class Item {
    int id; // 미생물 번호
    int r; // 세로 위치
    int c; // 가로 위치
    int cnt; // 미생물 개수
    int direction; // 이동방향
     
    Item(int id, int r, int c, int cnt, int direction) {
        this.id = id;
        this.r = r;
        this.c = c;
        this.cnt = cnt;
        this.direction = direction;
    }
     
    @Override
    public String toString() {
        return "id> " + id + ", cnt> " + cnt;
    }
}
 
class Pos {
    int count;
    Queue<Item> items;
     
    Pos() {
        this.count = 0;
        this.items = new ArrayDeque<>();
    }
     
    void add(Item item) {
        count++;
        items.offer(item);
    }
     
    Item poll() {
        count--;
        return items.poll();
    }
     
    void clear() {
        this.count = 0;
        items.clear();
    }
}
 
public class Solution {
    // 상: 1, 하: 2, 좌: 3, 우: 4
    static final int[] dr = new int[] {0, -1, 1, 0, 0};
    static final int[] dc = new int[] {0, 0, 0, -1, 1};
     
    static final int[] different = new int[] {0, 2, 1, 4, 3};
     
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine()); // 테스트 케이스 개수
         
        StringBuilder sb = new StringBuilder();
        for(int test_case=1; test_case<=T; test_case++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken()); // N
            int M = Integer.parseInt(st.nextToken()); // M(시간)
            int K = Integer.parseInt(st.nextToken()); // K(미생물 군집 개수)
             
            Pos[][] board = new Pos[N][N];
            Item[] items = new Item[K];
     
            // 미생물 정보 입력 및 구역 표시
            for(int i=0; i<K; i++) {
                st = new StringTokenizer(br.readLine());
                 
                int r = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                int cnt = Integer.parseInt(st.nextToken());
                int direction = Integer.parseInt(st.nextToken());
                 
                items[i] = new Item(i, r, c, cnt, direction); // 미생물 정보
                board[r][c] = new Pos(); // 구역에 미생물 표시
                board[r][c].add(items[i]);
            }
             
            for(int m=0; m<M; m++) {
                for(int i=0; i<K; i++) {
                    Item item = items[i];
                    if(item.cnt == 0) continue; // 미생물이 존재하지 않는 경우
                 
                    // 이동 방향으로 이동
                    int nr = item.r + dr[item.direction];
                    int nc = item.c + dc[item.direction];
                     
                    // 다음으로 이동한 위치가 약품이 칠해져 있는 경우
                    if(isMedicine(nr, nc, N)) {
                        items[i].cnt = (items[i].cnt / 2);
                        items[i].direction = different[item.direction]; // 반대 방향(1<->2, 3<->4)
                    }
                     
                    // 이동
                    board[item.r][item.c].poll();
                    if(items[i].cnt > 0) {
                        items[i].r = nr;
                        items[i].c = nc;
                        if(board[nr][nc] == null) board[nr][nc] = new Pos();
                        board[nr][nc].add(items[i]);
                    }
                }
 
                // 다음으로 이동한 위치가 미생물이 2개인 경우
                for(int r=0; r<N; r++) {
                    for(int c=0; c<N; c++) {
                        if(board[r][c] == null) continue;
                        Pos pos = board[r][c];
                        if(pos.count <= 1) continue;
                         
                        int maxId = -1;
                        int maxCnt = 0;
                        int sum = 0;
                        while(pos.count > 0) {
                            Item item = pos.poll();
                             
                            if(maxCnt < item.cnt) {
                                maxId = item.id;
                                maxCnt = item.cnt;
                            }
                            sum += item.cnt;
                            items[item.id].cnt = 0;
                        }
                        items[maxId].cnt = sum;
                        board[r][c].clear();
                        board[r][c].add(items[maxId]);
                    }
                }
            }
            int total = 0; // 미생물 개수
            for(Item item : items) {
                total += item.cnt;
            }
             
            sb.append(String.format("#%d %d\n", test_case, total));
        }
        System.out.print(sb);
    }
     
    private static boolean isMedicine(int r, int c, int n) {
        return (r == 0 || r == n-1 || c == 0 || c == n-1);
    }
 
}