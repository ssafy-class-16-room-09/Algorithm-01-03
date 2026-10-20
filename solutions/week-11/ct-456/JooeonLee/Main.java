import java.util.*;

public class Main {
    static int N, B;
    static int[][] board;
    static int[] result;

    // 현재 벨트 위의 수하물: 위치 -> 수하물
    static Map<Integer, Container> mapContainer = new HashMap<>();

    // 목적지 -> 이동 경쟁에서 이긴 수하물
    static Map<Integer, Container> winner;

    // null: 미판정, true: 이동 가능, false: 이동 불가
    static Boolean[] movable;

    static PriorityQueue<Container> simulPq = new PriorityQueue<>((a, b) -> {
        if(a.t != b.t) {
            return Integer.compare(a.t, b.t);
        }
        return Integer.compare(a.id, b.id);
    });

    // 아래, 왼쪽, 위, 오른쪽
    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, -1, 0, 1};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();
        board = new int[N + 1][N + 1];

        for(int r = 1; r <= N; r++) {
            for (int c = 1; c <= N; c++) {
                board[r][c] = sc.nextInt();
            }
        }

        B = sc.nextInt();
        result = new int[B + 1];

        for(int id = 1; id <= B; id++) {
            int t = sc.nextInt();
            int r = sc.nextInt();
            int c = sc.nextInt();
            int w = sc.nextInt();

            simulPq.offer(new Container(id, t, r, c, w));
        }

        int turnCnt = 1;
        int loadedCnt = 0;

        while(loadedCnt < B) {
            // 1단계: 이동
            move();


            // 2단계: 적재 및 파손
            // 적재 완료해서 삭제할 키 리스트
            List<Integer> keysToRemove = new ArrayList<>();

            for(Container curr : mapContainer.values()) {
                if(board[curr.r][curr.c] == 9) {
                    result[curr.id] = curr.isBreaked ? -turnCnt : turnCnt;

                    loadedCnt++;
                    keysToRemove.add(curr.currPos);
                }
                else if(curr.unMoveCnt >= 3)
                    curr.isBreaked = true;
            }

            // 한번에 삭제 (keySet으로 순회 중에는 삭제 안됨, 왜 안되는지는 나중에 알아보기)
            for(int key : keysToRemove)
                mapContainer.remove(key);


            // 3단계: 투입
            // 실패한 수하물은 이번 턴의 시도가 끝난 후 다시 넣는다.
            List<Container> waiting = new ArrayList<>();

            while (!simulPq.isEmpty() && simulPq.peek().t <= turnCnt) {
                Container curr = simulPq.poll();

                if(mapContainer.containsKey(curr.currPos)) {
                    waiting.add(curr);
                }
                else {
                    mapContainer.put(curr.currPos, curr);
                }
            }

            simulPq.addAll(waiting);

            turnCnt++;
        }

        StringBuilder sb = new StringBuilder();

        for (int id = 1; id <= B; id++) {
            sb.append(result[id]).append(' ');
        }

        System.out.print(sb);
    }

    static void move() {
        winner = new HashMap<>();
        movable = new Boolean[B + 1];

        // 1. 목적지별 승자 선정
        for(Container curr : mapContainer.values()) {
            int nextPos = getNextPos(curr);

            Container prev = winner.get(nextPos);

            if(prev == null || curr.w > prev.w || (curr.w == prev.w && curr.id < prev.id)) {
                winner.put(nextPos, curr);
            }
        }

        // 2. 현재 배치를 유지한 채 모든 이동 가능 여부 판정
        for(Container curr : mapContainer.values()) {
            canMove(curr);
        }

        // 3. 판정 결과를 한꺼번에 반영
        Map<Integer, Container> nextMap = new HashMap<>();

        for(Container curr : mapContainer.values()) {
            if (movable[curr.id]) {
                int d = board[curr.r][curr.c] - 1;

                curr.r += dr[d];
                curr.c += dc[d];
                curr.unMoveCnt = 0;
            } 
            else {
                curr.unMoveCnt++;
            }

            curr.currPos = curr.r * (N + 1) + curr.c;
            nextMap.put(curr.currPos, curr);
        }

        mapContainer = nextMap;
    }

    static boolean canMove(Container curr) {
        if(movable[curr.id] != null) {
            return movable[curr.id];
        }

        int nextPos = getNextPos(curr);

        // 목적지 경쟁에서 졌다면 이동 불가
        if(winner.get(nextPos).id != curr.id) {
            return movable[curr.id] = false;
        }

        Container front = mapContainer.get(nextPos);

        // 앞 칸이 비어 있다면 이동 가능
        if(front == null) {
            return movable[curr.id] = true;
        }

        // 앞 수하물이 이동할 수 있어야 나도 이동 가능
        return movable[curr.id] = canMove(front);
    }

    static int getNextPos(Container curr) {
        int d = board[curr.r][curr.c] - 1;
        int nr = curr.r + dr[d];
        int nc = curr.c + dc[d];

        return nr * (N + 1) + nc;
    }

    static class Container {
        int id, t, r, c, w;
        int currPos;
        boolean isBreaked;
        int unMoveCnt;

        Container(int id, int t, int r, int c, int w) {
            this.id = id;
            this.t = t;
            this.r = r;
            this.c = c;
            this.w = w;
            this.currPos = r * (N + 1) + c;
            this.isBreaked = false;
            this.unMoveCnt = 0;
        }
    }
}