import java.util.*;
import java.io.*;

class Person {
	int num; // 고객 번호
	int time; // 들어온 시간
	
	public Person(int num, int time) {
		this.num = num;
		this.time = time;
	}
}

class Solution {
	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine()); // 총 테스트 케이스 개수
		
		StringBuilder sb = new StringBuilder();
		for(int test_case = 1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int[] info = new int[5]; // N: 접수 창구 개수, M: 정비 창구 개수, K: 고객 수, A: 지갑 고객 접수 창구 번호, B: 지갑 고객 정비 창구 번호
			int i = 0;
			while(st.hasMoreTokens()) {
				info[i++] = Integer.parseInt(st.nextToken());
			}
			
			st = new StringTokenizer(br.readLine());
			int[] aTimes = new int[info[0]]; // 접수 창구가 고장을 접수하는 데 걸리는 시간
			i = 0;
			while(st.hasMoreTokens()) {
				aTimes[i++] = Integer.parseInt(st.nextToken());
			}
			
			st = new StringTokenizer(br.readLine());
			int[] bTimes = new int[info[1]]; // 정비 창구가 고장을 접수하는 데 걸리는 시간
			i = 0;
			while(st.hasMoreTokens()) {
				bTimes[i++] = Integer.parseInt(st.nextToken());
			}
			
			st = new StringTokenizer(br.readLine());
			Person[] people = new Person[info[2]];
			i = 0;
			while(st.hasMoreTokens()) {
				people[i] = new Person(i+1, Integer.parseInt(st.nextToken())); // 사람 번호와 들어온 시간
				i++;
			}
			Arrays.sort(people, (a, b) -> a.time - b.time); // 들어온 시간 기준 오름차순 정렬
			
			int index = 0; // 들어온 사람 카운트
			Queue<Person> waitingA = new LinkedList<>(); // 접수 창구 대기하고 있는 손님
			Person[] reception = new Person[info[0]]; // 현재 접수 창구 상황
			Queue<Person> waitingB = new LinkedList<>(); // 정비 창구 대기하고 있는 손님
			Person[] repairDesk = new Person[info[1]]; // 현재 정비 창구 상황
			
			int[][] carcenterNum = new int[info[2]+1][2]; // 각 손님별 카센터 창구 정보
			int time = 0; // 현재 시각
			int completeCnt = 0; // 완료된 인원 수
			while(completeCnt < info[2]) {
				// 현재 시각 기준 들어온 손님 접수 창구 대기에 넣기
				while(index < info[2]) {
					if(people[index].time > time) break;
					waitingA.add(new Person(people[index].num, time));
					index++;
				}
				
				// 접수 창구를 이용한 고객 중 처리가 다 완료된 고객은 정비 창구로 옮기기
				for(int a=0; a<info[0]; a++) {
					if(reception[a] != null && time - reception[a].time == aTimes[a]) {
						waitingB.add(new Person(reception[a].num, time));
						reception[a] = null;
					}
				}
				
				// 접수 창구 대기에 있는 손님 중 현재 접수 처리 가능한 손님 할당
				for(int a=0; a<info[0]; a++) {
					if(reception[a] == null && !waitingA.isEmpty()) {
						Person person = waitingA.poll();
						carcenterNum[person.num][0] = a+1;
						reception[a] = new Person(person.num, time);
					}
				}
				
				// 정비 창구 대기에 있는 손님 중 처리가 다 완료된 고객 빼기
				for(int b=0; b<info[1]; b++) {
					if(repairDesk[b] != null && time - repairDesk[b].time == bTimes[b]) {
						repairDesk[b] = null;
						completeCnt++;
					}
				}
				
				// 정비 창구 대기에 있는 손님 중 현재 접수 처리 가능한 손님 할당
				for(int b=0; b<info[1]; b++) {
					if(repairDesk[b] == null && !waitingB.isEmpty()) {
						Person person = waitingB.poll();
						carcenterNum[person.num][1] = b+1;
						repairDesk[b] = new Person(person.num, time);
					}
				}
				time++;
			}
			
			int sum = 0;
			for(int p=1; p<=info[2]; p++) {
				if(carcenterNum[p][0] == info[3] && carcenterNum[p][1] == info[4]) sum += p;
			}
			
			sb.append(String.format("#%d %d\n", test_case, sum == 0 ? -1 : sum));	
		}
		System.out.println(sb);
		br.close();
	}
}