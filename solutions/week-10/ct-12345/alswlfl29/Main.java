import java.util.*;
public class Main {

    static int N;
    static int K;
    static int[] positions;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt(); // 구멍 개수
        K = scanner.nextInt(); // 보수 패치 최대 개수
        positions = new int[N]; // 구멍 위치
        for (int i = 0; i < N; i++) {
            positions[i] = scanner.nextInt();
        }
        
        int min = 1;
        int max = positions[N-1] - positions[0] + 1;
        int answer = max;
        while(min <= max) {
            int mid = min + (max - min) / 2;

            if(count(mid) > K) { // mid 길이로 구멍 메우는데 필요한 패치 수가 K보다 더 많이 필요한 경우, 길이 늘리기
                min = mid + 1;
            } else {
                answer = Math.min(answer, mid);
                max = mid - 1;
            }
        }
        System.out.println(answer);
    }

    private static int count(int len) {
        int cnt = 0; // 메울 수 있는 구멍 개수

        int current = 0;
        for(int n=0; n<N; n++) {
            // 이전 패치로 현재 구멍을 메울 수 없는 경우, 패치 추가
            if(current < positions[n]) {
                cnt++;
                current = positions[n] + len - 1;
            }
        }
        return cnt;
    }
}
