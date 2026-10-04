import java.util.*;
import java.io.*;

public class Main {

    static int number; // 개미집 마지막 번호
    static TreeSet<Integer> antHome; // 개미집 위치
    static int[] homeNumber; // 개미집 번호와 위치 매칭

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int Q = Integer.parseInt(br.readLine()); // 명령 수(<= 20,000)

        StringTokenizer st =  new StringTokenizer(br.readLine());
        init(st); // 마을 건설

        StringBuilder sb = new StringBuilder();
        for(int q=1; q<Q; q++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken()); // 명령
            if(order == 200) { // 개미집 건설
                int pos = Integer.parseInt(st.nextToken()); // 새로 건설할 개미집 위치
                buildHome(pos);
            } else if(order == 300) { // 개미집 철거
                int num = Integer.parseInt(st.nextToken()); // 철거할 개미집 번호
                removeHome(num);
            } else if(order == 400) { // 개미집 정찰
                int cnt = Integer.parseInt(st.nextToken()); // 정찰할 개미 개수
                int time = exploreHome(cnt);
                sb.append(time).append("\n");
            }
        }
        System.out.print(sb);
        br.close();
    }

    // 마을 건설
    private static void init(StringTokenizer st) {
        antHome = new TreeSet<>(); // 개미집 위치 트리셋 초기화
        homeNumber = new int[30001]; // 개미집 번호-위치 매칭 배열 초기화
        number = 0;

        st.nextToken(); // 명령어 100
        int N = Integer.parseInt(st.nextToken()); // 초기 개미집 개수
        for(int i=0; i<N; i++) {
            int pos = Integer.parseInt(st.nextToken()); // 개미집 위치
            antHome.add(pos);
            homeNumber[++number] = pos;
        }
    }

    // 개미집 건설
    private static void buildHome(int pos) {
        antHome.add(pos);
        homeNumber[++number] = pos;
    }

    // 개미집 철거
    private static void removeHome(int num) {
        int pos = homeNumber[num];
        antHome.remove(pos);
    }

    // 개미집 정찰
    private static int exploreHome(int cnt) {

        int min = 0;
        int max = antHome.last() - antHome.first();

        int answer = max; // 최소 정찰 시간

        // 정찰 개미가 한 마리인 경우, 전체 다 탐색해야 함
        if(cnt == 1) return answer;

        while(min <= max) {
            int mid = (min+max)/2; // 한 마리당 주어진 탐색할 시간

            int count = needAntCnt(mid);

            if(count > cnt) { // 주어진 개미 수보다 더 많은 개미가 필요한 경우
                min = mid + 1;
            } else { // 주어진 개미 수 이하의 개미가 필요한 경우
                answer = Math.min(answer, mid);
                max = mid - 1;
            }
        }

        return answer;
    }

    private static int needAntCnt(int time) {
        int count = 0; // 전체 개미집 탐색하는 데 필요한 개미 수

        int startHome = antHome.first(); // 정찰이 시작되는 개미집 위치
        while(startHome != -1) {
            count+=1; // 개미 추가

            int distance = startHome + time;

            if(antHome.higher(distance) != null) {
                startHome = antHome.higher(distance);  // 다음 정찰 시작 위치
            } else {
                startHome = -1;
            }
        }

        return count;
    }
}

/*
개미집 정찰할 때 '정찰에 걸리는 최소 시간' 구하기

- 개미집 위치 -> TreeSet 이용 (삽입 삭제 조회 O(logN))
- 개미집 정찰 -> 이분탐색 이용? 최대 개미집 거리 10^9
*/