import java.util.*;
import java.io.*;

class Baggage {
    int number; // 수하물 번호
    int cr, cc; // 현재위치
    int inTurn; // 투입 예정 턴
    int weight; // 무게
    int count; // 횟수
    boolean isBreak; // 파손 여부

    Baggage(int number, int cr, int cc, int inTurn, int weight) {
        this.number = number;
        this.cr = cr;
        this.cc = cc;
        this.inTurn = inTurn;
        this.weight = weight;
        this.count = 0;
        this.isBreak = false;
    }
}

public class Main {
    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, -1, 0, 1};

    static int N; // 격자의 크기
    static int B; // 수하물 개수
    static int[][] convey; // 컨베이어 벨트 정보

    static PriorityQueue<Baggage> waiting; // 대기 중인 수하물 정보(투입 턴 기준 오름차순 정렬)
    static List<Integer> carryingDestination; // 적재 장소 목록
    static Map<Integer, Baggage> conveyPos; // 컨베이어 위 수하물 위치

    static int[] carringTurn; // 각 수하물의 적재 턴
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        carryingDestination = new ArrayList<>();
        StringTokenizer st = null;
        convey = new int[N][N];
        for(int r=0; r<N; r++) {
            st = new StringTokenizer(br.readLine());
            for(int c=0; c<N; c++) {
                int num = Integer.parseInt(st.nextToken());
                if(num == 9) carryingDestination.add(r*N+c); // 적재 위치 저장
                if(num != 0) convey[r][c] = num-1;
                else convey[r][c] = -1;
            }
        }

        B = Integer.parseInt(br.readLine());
        carringTurn = new int[B];
        waiting = new PriorityQueue<>((a, b) -> Integer.compare(a.inTurn, b.inTurn));
        for(int b=0; b<B; b++) {
            st = new StringTokenizer(br.readLine(), " ");
            int t = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            Baggage baggage = new Baggage(b, r-1, c-1, t, w);
            waiting.offer(baggage);
        }

        conveyPos = new HashMap<>(); // 수하물 이동 후 상황
        int turn = 1;
        while(!waiting.isEmpty() || conveyPos.size() > 0) {
            move(); // 수하물 이동
            checkDestinationAndBreak(turn); // 적재 및 파손
            inBaggage(turn); // 수하물 투입
            turn++; // 턴 증가
        }

        StringBuilder sb = new StringBuilder();
        for(int ct : carringTurn) {
            sb.append(ct).append(" ");
        }
        System.out.print(sb);
    }

    private static void move() { // 수하물 이동
        ArrayDeque<Baggage> conveyStatus = new ArrayDeque<>(); // 이동시켜야 하는 수하물 정보

        // 벨트 위에 있는 수하물들 conveyStatus에 넣기
        for(int pos : conveyPos.keySet()) {
            conveyStatus.offer(conveyPos.get(pos));
        }
        boolean[] isVisited = new boolean[B]; // 이동 여부
        boolean[] newBaggage = new boolean[B]; // 이동한 곳이 새로운 곳인가
        while(!conveyStatus.isEmpty()) {
            Baggage baggage = conveyStatus.poll(); // 수하물 꺼내기

            int pos = baggage.cr*N+baggage.cc; // 수하물 현재 위치
            if(!isVisited[baggage.number]) { // 처음으로 이동시키는 수하물
                int d = convey[baggage.cr][baggage.cc];
                int nr = baggage.cr + dr[d];
                int nc = baggage.cc + dc[d]; // 이동시킬 위치
                if(convey[nr][nc] == -1) { // 다음 칸에 이동시킬 수 없는 경우
                    newBaggage[baggage.number] = false; // 이동 못함
                    isVisited[baggage.number] = true;
                    continue;
                }

                if(conveyPos.containsKey(nr*N+nc)) { // 다음에 놓을 위치에 이미 수하물이 놓여 있는 경우
                    Baggage origin = conveyPos.get(nr*N+nc);
                    if(!isVisited[origin.number]) { // 해당 수하물이 아직 이동되지 않은 수하물인 경우는 현재 수하물 다시 넣기
                        conveyStatus.offer(baggage);
                    } else {
                        if(newBaggage[origin.number]) { // 해당 수하물이 이동된 수하물인 경우
                            if(origin.weight < baggage.weight 
                                || ((origin.weight == baggage.weight) && (origin.number > baggage.number))) { // 해당 수하물보다 무게가 크거나 혹은 무게가 같지만 번호가 더 작은 경우 현재 수하물로 선택
                                isVisited[baggage.number] = true;
                                newBaggage[baggage.number] = true;
                                conveyPos.remove(pos); // 원래 있던 위치에서 제거
                                conveyPos.put(nr*N+nc, baggage); // 이동한 위치에 현재 수하물 놓기
                                conveyStatus.offer(origin);
                            } else {
                                newBaggage[baggage.number] = false; // 이동 못함
                                isVisited[baggage.number] = true;
                            }
                        } else { // 해당 수하물이 이동할 수 없는 수하물인 경우, 현재 수하물 원래자리로 돌아가기
                            newBaggage[baggage.number] = false; // 이동 못함
                            isVisited[baggage.number] = true;
                        }
                    }
                } else { // 다음에 놓을 위치에 수하물이 없는 경우
                    newBaggage[baggage.number] = true;
                    isVisited[baggage.number] = true;
                    conveyPos.remove(pos); // 원래 있던 위치에서 제거
                    conveyPos.put(nr*N+nc, baggage); // 이동한 위치에 현재 수하물 놓기
                }
            } else { // 다시 원복해야 하는 수하물인 경우
                if(conveyPos.containsKey(pos)) { // 다른 수하물이 놓여 있는 경우, 해당 수하물 다시 넣기
                    Baggage origin = conveyPos.get(pos);
                    conveyStatus.offer(origin);
                }
                newBaggage[baggage.number] = false; // 이동 못함
                conveyPos.put(pos, baggage);
            }
        }

        for(int pos : conveyPos.keySet()) { // 최종 위치 갱신
            int r = pos / N;
            int c = pos % N;
            Baggage baggage = conveyPos.get(pos);
            baggage.cr = r;
            baggage.cc = c;
            if(newBaggage[baggage.number]) baggage.count = 0; // 수하물이 이동된 경우 카운트 0으로 초기화
            else baggage.count++; // 수하물이 이동 못하므로 카운트 증가
        }
    }

    private static void checkDestinationAndBreak(int turn) { // 적재 및 파손
        ArrayDeque<Integer> removePos = new ArrayDeque<>(); // 적재된 위치
        OUTER: for(int pos : conveyPos.keySet()) {
            Baggage baggage = conveyPos.get(pos);
            for(int carringPos : carryingDestination) {
                if(carringPos == pos) { // 내려 놓을 수 있는 곳
                    carringTurn[baggage.number] = turn;
                    if(baggage.isBreak) carringTurn[baggage.number] *= -1; // 수하물이 파손된 경우 -1 곱하기
                    removePos.offer(pos);
                    continue OUTER;
                }
            }
            
            if(baggage.count >= 3) { // 움직이지 못한 힛수가 3회 이상이면 파손
                baggage.isBreak = true;
            }
        }
        while(!removePos.isEmpty()) { // 적재된 수하물 제거하기
          conveyPos.remove(removePos.poll());
        }
    }

    private static void inBaggage(int turn) { // 수하물 투입
        ArrayDeque<Baggage> notIn = new ArrayDeque<>(); // 투입 못한 수하물
        while(!waiting.isEmpty() && waiting.peek().inTurn <= turn) {
            Baggage baggage = waiting.poll(); // 현재 턴에 투입될 수 있는 수하물 꺼내기
            int pos = baggage.cr * N + baggage.cc;
            if(conveyPos.containsKey(pos)) { // 이미 수하물이 있는 경우 다시 대기 큐로
                notIn.offer(baggage);
            }else {
                conveyPos.put(pos, baggage);
            }
        }
        while(!notIn.isEmpty()) { // 투입 못한 수하물 다시 넣기
            Baggage baggage = notIn.poll();
            waiting.offer(baggage);
        }
    }
}