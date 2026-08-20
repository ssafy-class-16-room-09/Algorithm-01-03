package week-02.
import java.util.*;

class Solution
{
	public static void main(String args[]) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();

		for(int test_case = 1; test_case <= T; test_case++)
		{
      // 입력
      int N = sc.nextInt();
      int M = sc.nextInt();
      int K = sc.nextInt();
      int A = sc.nextInt();
      int B = sc.nextInt();
      int[][] visit = new int[K][2];
      for(int[] v : visit)
        Arrays.fill(v, -1);

      int[] a = new int[N];
      for (int i = 0; i < N; i++) {
          a[i] = sc.nextInt();
      }
      int[] b = new int[M];
      for (int i = 0; i < M; i++) {
          b[i] = sc.nextInt();
      }
      int[] t = new int[K];
      for (int i = 0; i < K; i++) {
          t[i] = sc.nextInt();
      }

      Queue<Integer> aQueue = new ArrayDeque<>();
      Queue<Integer> bQueue = new ArrayDeque<>();

      int[] aCustomer = new int[N];
      int[] aRemain = new int[N];

      int[] bCustomer = new int[M];
      int[] bRemain = new int[M];

      Arrays.fill(aCustomer, -1);
      Arrays.fill(bCustomer, -1);

      int time = 0;
      int nextCustomer = 0;
      int finished = 0;

      while (finished < K) {

          // 1. 정비 완료 처리
          for (int i = 0; i < M; i++) {
              if (bCustomer[i] != -1 && bRemain[i] == 0) {
                  bCustomer[i] = -1;
                  finished++;
              }
          }

          // 2. 접수 완료 처리
          // i를 작은 번호부터 확인
          // 동시에 끝났다면 접수 창구 번호가 작은 고객이 먼저 bQueue에 들어감
          for (int i = 0; i < N; i++) {
              if (aCustomer[i] != -1 && aRemain[i] == 0) {
                  bQueue.offer(aCustomer[i]);
                  aCustomer[i] = -1;
              }
          }

          // 3. 현재 시간에 도착한 고객
          while (nextCustomer < K && t[nextCustomer] <= time) {
              aQueue.offer(nextCustomer);
              nextCustomer++;
          }

          // 4. 접수 창구 배정
          for (int i = 0; i < N; i++) {
              if (aCustomer[i] == -1 && !aQueue.isEmpty()) {
                  int customer = aQueue.poll();

                  aCustomer[i] = customer;
                  aRemain[i] = a[i];

                  visit[customer][0] = i;
              }
          }

          // 5. 정비 창구 배정
          for (int i = 0; i < M; i++) {
              if (bCustomer[i] == -1 && !bQueue.isEmpty()) {
                  int customer = bQueue.poll();

                  bCustomer[i] = customer;
                  bRemain[i] = b[i];

                  visit[customer][1] = i;
              }
          }

          // 6. 현재 작업 중인 창구 시간 감소
          for (int i = 0; i < N; i++) {
              if (aCustomer[i] != -1) {
                  aRemain[i]--;
              }
          }

          for (int i = 0; i < M; i++) {
              if (bCustomer[i] != -1) {
                  bRemain[i]--;
              }
          }

          time++;
      }

      // 정답 도출
      int answer = 0;
      for (int i = 0; i < K; i++) {
        if (visit[i][0] == A - 1 && visit[i][1] == B - 1) {
            answer += (i + 1);
        }
      }
      System.out.println("#" + test_case + " " + answer);
		}
	}
}
