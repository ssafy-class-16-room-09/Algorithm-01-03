import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int TC = Integer.parseInt(br.readLine());
        for (int tc=1; tc<=TC; tc++){
            StringTokenizer st = new StringTokenizer(br.readLine());
        	int N = Integer.parseInt(st.nextToken());
        	int M = Integer.parseInt(st.nextToken());
        	int K = Integer.parseInt(st.nextToken());
        	int A = Integer.parseInt(st.nextToken());
        	int B = Integer.parseInt(st.nextToken());


        	int[] reception = new int[N];
        	st = new StringTokenizer(br.readLine());
        	for (int i=0;i<N;i++) {
        		reception[i] = Integer.parseInt(st.nextToken());
        	}

        	int[] repair = new int[M];
        	st = new StringTokenizer(br.readLine());
        	for (int i=0;i<M;i++) {
        		repair[i] = Integer.parseInt(st.nextToken());
        	}

        	int[][] arrive = new int[K][2];
        	st = new StringTokenizer(br.readLine());
        	for (int i=0;i<K;i++) {
        		arrive[i] = new int[] {i, Integer.parseInt(st.nextToken())}; 
        	}
			Arrays.sort(arrive, (o1, o2) -> {
				if (o1[1] != o2[1]) return Integer.compare(o1[1], o2[1]);
				return Integer.compare(o1[0], o2[0]);
			});
            
			int[] receptionFree = new int[N];
			int[] repairFree = new int[M];
			int[][] customerUsed = new int[K][2];
			int[][] repairWaiting = new int[K][3];

			for (int[] customer : arrive) {
				int desk = pickDesk(receptionFree, customer[1]);
				int startTime = Math.max(customer[1], receptionFree[desk]);
				int endTime = startTime + reception[desk];

				customerUsed[customer[0]] = new int[] {desk, -1};
				repairWaiting[customer[0]] = new int[] {customer[0], desk, endTime};
				receptionFree[desk] = endTime;
			}

			Arrays.sort(repairWaiting, (o1, o2) -> {
				if (o1[2] != o2[2]) return Integer.compare(o1[2], o2[2]);
				return Integer.compare(o1[1], o2[1]);
			});

			for (int[] customer:repairWaiting) {
				int desk = pickDesk(repairFree, customer[2]);
				int startTime = Math.max(customer[2], repairFree[desk]);
				int endTime = startTime + repair[desk];
				customerUsed[customer[0]][1] = desk;
				repairFree[desk] = endTime;
			}

			int answer = 0;
			for (int i=0;i<K;i++){
				int[] used = customerUsed[i];
				if (used[0] == A-1 && used[1] == B-1){
					answer += i+1;
				}
			}

			System.out.printf("#%d %d\n",tc, answer != 0 ? answer : -1 );
        }

    }

	// 빈 창구가 있으면 번호가 작은 곳, 없으면 가장 빨리 끝나는 곳
	static int pickDesk(int[] free, int now) {
		for (int i = 0; i < free.length; i++) {
			if (free[i] <= now) return i;
		}
		int best = 0;
		for (int i = 1; i < free.length; i++) {
			if (free[i] < free[best]) best = i;
		}
		return best;
	}
}
/*

arrive : [고객 번호, 도착시간]
reception : 걸리는 시간
repair : 걸리는 시간

customerUsed : [사용한 접수대, 사용한 수리대]
repairWaiting : [고객 번호, 접수대 번호, 종료 시간]

arrive 정렬 : 도착시간, 고객번호 순
문제 조건에 따른 접수대 찾기
시작시간, 종료시간 계산 후 현재 접수대에 종료시간 설정

repairWating 정렬: 종료시간, 접수대 번호 순
조건에 따른 수리대 찾기
시작시간, 종료시간 계산 후 현재 수리대에 종료시간 설정


*/