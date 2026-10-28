import java.util.*;
import java.io.*;

class Baggage{
    int n, turn, r, c, w;
    int stay;
    boolean broken, onRail;
    public Baggage(int n, int turn, int r, int c, int w){
        this.n = n;
        this.turn = turn;
        this.r = r;
        this.c = c;
        this.w = w;
    }
}

public class Main {
    static int N, B;
    static int[][] grid, rails;
    static ArrayList<Integer> exits;
    static Baggage[] baggages;
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        N = Integer.parseInt(br.readLine());
        grid = new int[N][N];
        exits = new ArrayList<>();

        for (int r=0; r<N; r++){
            st = new StringTokenizer(br.readLine());
            for (int c=0; c<N; c++){
                grid[r][c] = Integer.parseInt(st.nextToken());
                if (grid[r][c] == 9) exits.add(r*N+c);
            }
        }
        
        B = Integer.parseInt(br.readLine());
        baggages = new Baggage[B+1];
        for (int i=1; i<=B; i++){
            st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            baggages[i] = new Baggage(i, t, r-1, c-1, w);
        }
        
        int[] answer = new int[B];
        rails = new int[N][N];

        for (int t=1; t<=200; t++){
            move();

            // 도착지점에 있는 수하물 적재 처리
            for (int exit:exits){
                int r = exit/N;
                int c = exit%N;
                int num =  rails[r][c];
                if (num != 0){
                    int k = baggages[num].broken ? -t : t;
                    answer[num-1] = k;
                    rails[r][c] = 0;
                }
            }

            put(t);
        }
        StringBuilder sb = new StringBuilder();
        for (int ans:answer){
            sb.append(ans).append(" ");
        }
        System.out.println(sb);
    }

    // 도착지점에서부터 BFS를 돌며 컨베이어 역주행
    // 현재 칸에 올 수 있는 후보 중에 조건에 맞는 수하물을 선택
    private static void move(){
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        for (int exit: exits){
            queue.offer(exit);
        }

        while (!queue.isEmpty()){
            int curr = queue.poll();
            int r = curr/N;
            int c = curr%N;
            
            // 현재 칸에 올 수 있는 후보 중에 조건에 맞는 수하물을 선택
            ArrayList<Baggage> candidates = new ArrayList<>();
            for (int d=0; d<4; d++){
                int nr = r+dr[d], nc = c+dc[d];
                if (nr<0 || nr>=N || nc<0 || nc>=N) continue;
                // 정의된 dr, dc와 문제 조건의 방향에 따라 현재 칸으로 들어오는 칸인지 확인하기
                // 나의 오른쪽이면 왼쪽방향으로 움직이는 칸이어여함 -> 내가 찾는 방향과 역방향이라는 뜻
                if (grid[nr][nc] != d+1) continue;
                // 수하물이 없어도 컨베이어 벨트는 연결되어 있으므로 벨트를 따라 가는 BFS는 계속 탐색
                queue.offer(nr*N+nc);

                // 다음 레일에 수하물이 없으면 패스
                if(rails[nr][nc] == 0) continue;
                Baggage bag = baggages[rails[nr][nc]];
                // 다음 레일에 수하물이 있는데 지금 레일에 수하물이 있으면 못옴
                if (rails[r][c] != 0) {
                    bag.stay++;
                    if (bag.stay >= 3) bag.broken = true;
                    continue;
                }
                // 다음 레일에 수하물이 있는데 지금 레일에 수하물이 없으면 올 수 있음
                candidates.add(bag);
            }

            // 후보 중에 무게가 무거운 수하물 순으로 정렬
            Collections.sort(candidates, (o1, o2) -> {
                if (o1.w != o2.w) return Integer.compare(o2.w, o1.w);
                return Integer.compare(o1.n, o2.n);
            });

            // 정렬된 첫 번째 수하물을 선택해서 현재 레일에 올림
            if (!candidates.isEmpty()){
                Baggage bag = candidates.get(0);
                rails[bag.r][bag.c] = 0;
                bag.r = r;
                bag.c = c;
                rails[r][c] = bag.n;
                bag.stay = 0;
            }

            // 나머지 후보 수하물들은 못움직이고 가만히
            for(int i=1; i<candidates.size(); i++){
                Baggage bag = candidates.get(i);
                bag.stay++;
                if(bag.stay >= 3) bag.broken = true;
            }
            
        }
    }

    private static void put(int t){
        // 한 칸에 하나의 수하물만 오도록 맵을 사용
        HashMap<Integer, Baggage> map = new HashMap<>();
        for (int i=1; i<=B; i++){
            Baggage bag = baggages[i];
            if (bag.onRail || bag.turn>t) continue;
            if(rails[bag.r][bag.c] !=0) continue;
            int key = bag.r*N+bag.c;
            Baggage prev = map.get(key);
            // 이미 수하물이 있으면 더 턴이 빠른 수하물을 선택
            if(prev == null || bag.turn < prev.turn){
                map.put(key, bag);
            }
        }
        
        // 맵에 있는 수하물들을 레일에 올림
        for (int key:map.keySet()){
            int r = key/N;
            int c = key%N;
            Baggage bag = map.get(key);
            rails[r][c] = bag.n;
            bag.onRail = true;
            
        }
    }

}

/*
1234
dlur

0123
urdl


*/