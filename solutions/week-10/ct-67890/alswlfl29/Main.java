import java.util.*;
import java.io.*;

class Jewelry {
    int w, v; // 보석 무게와 가치
    boolean isSelled; // 판매 여부

    Jewelry(int w, int v) {
        this.w = w;
        this.v = v;
        isSelled = false;
    }
}

public class Main {

    static int number; // 다음 추가할 보석 번호
    static int remainCnt; // 남아있는 보석 개수
    static Jewelry[] jewelries; // 보석 정보들
    static int[] weights; // 무게에 해당하는 보석 정보

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine()); // 작업 수(<=3231)
        init(); // 변수 및 배열 초기화

        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        for(int q=0; q<Q; q++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken()); // 작업 번호
            if(order == 1) { // 보석 준비
                ready(st);
            } else if(order == 2) { // 보석 입고
                int w = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                add(w, v);
            } else if(order == 3) { // 보석 판매
                int idx = Integer.parseInt(st.nextToken());
                if(idx >= number) {// 존재하지 않는 보석인 경우
                    sb.append(-1).append("\n");
                    continue;
                }
                Jewelry jewelry = jewelries[idx];
                if(jewelry.isSelled) { // 이미 판매된 보석임
                    sb.append(-1).append("\n");
                    continue;
                }
                // 아직 판매되지 않고 존재하는 보석인 경우
                remainCnt--;
                weights[jewelry.w]--; // 해당 무게의 보석 수 1 감소
                jewelry.isSelled = true; // 판매 표시
                sb.append(jewelry.v).append("\n"); // 보석의 가치 출력
            } else if(order == 4) {
                int w = Integer.parseInt(st.nextToken()); // 최대 무게 w
                long maxValue = display(w);
                sb.append(maxValue).append("\n");
            } else if(order == 5) {
                int d = Integer.parseInt(st.nextToken()); // 무게 차이 d

                if(remainCnt < 2) { // 남아있는 보석 개수가 2개 미만이면 0 출력
                    sb.append(0).append("\n");
                    continue;
                }

                int set = makeSet(d);
                sb.append(set).append("\n");
            }
        }
        System.out.print(sb);
    }

    private static void init() {
        number = 1; // 보석 번호
        remainCnt = 0; // 남아있는 보석 수
        weights = new int[3001]; // 무게 별 보석 개수 배열 초기화
    }

    private static void ready(StringTokenizer st) {
        jewelries = new Jewelry[1101];
        int N = Integer.parseInt(st.nextToken());
        for(int n=1; n<=N; n++) {
            int w = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            jewelries[n] = new Jewelry(w, v);
            number++;
            remainCnt++;
            weights[w]++;
        }
    }

    // 보석 추가
    private static void add(int w, int v) {
        jewelries[number] = new Jewelry(w, v);
        number++;
        weights[w]++;
        remainCnt++;
    }

    // 냅색 이용해서 최대 W 무게를 가진 최대 가치 구하기
    private static long display(int W) {
        int[][] dp = new int[number][3001];

        for(int j=1; j<number; j++) {
            for(int w=1; w<=3000; w++) {
                // 이미 판매된 보석인 경우 혹은 현재 보석으로 무게를 만들 수 없는 경우 이전 값 그대로 넣기
                if(jewelries[j].isSelled || jewelries[j].w > w) { 
                    dp[j][w] = dp[j-1][w];
                    continue;
                }

                int currentW = jewelries[j].w;
                int currentV = jewelries[j].v;
                dp[j][w] = Math.max(dp[j-1][w], dp[j-1][w-currentW] + currentV);
            }
        }
        long maxValue = 0;
        for(int w=0; w<=W; w++) {
            maxValue = Math.max(maxValue, dp[number-1][w]);
        }
        return maxValue;
    }

    // 세트 구성
    static private int makeSet(int D) {

        int start = 0;
        int end = start+D; // 차이가 D이하인 범위를 나타내는 인덱스
    
        int total = 0; // 현재 D 차이를 만족하는 합의 개수
        for(int i=start; i<end; i++) {
            total += weights[i];
        }

        int set = ((total * (total-1))/2); // 조합 개수
        while(end < 3001) {
            // end 무게 기준 D 차이를 만족하는 조합 계산
            set += (total * weights[end]);

            // 윈도우 이동
            total -= weights[start++];
            total += weights[end++];
        }

        // 같은 무게로 만들 수 있는지 확인(즉 D가 0인 상황)
        // D부터 탐색: D 이전의 값들은 위에서 이미 조합에 포함되었기 때문에
        for(int w=D; w<3001; w++) {
            set += ((weights[w] * (weights[w]-1))/2);
        }
        return set;
    }
}
