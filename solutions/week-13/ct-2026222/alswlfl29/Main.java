import java.util.*;
import java.io.*;

public class Main {
    static List<Long> houses; // 집 식량 비축량
    static List<Long> cumulStockpile; // 집 식량 비축량 누적합
    static long strength; // 강화량

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine()); // 이벤트 수(<= 200,000)

        houses = new ArrayList<>();
        cumulStockpile = new ArrayList<>();
        strength = 0;
        
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        for (int q=0; q<Q; q++) {
            st = new StringTokenizer(br.readLine());
            int event = Integer.parseInt(st.nextToken());
            if(event == 100) { // 게임 시작
                int N = Integer.parseInt(st.nextToken()); // N개의 집
                for(int n=0; n<N; n++) {
                    long a = Long.parseLong(st.nextToken()); // 식량 비축량
                    houses.add(a);
                }
                Collections.sort(houses); // 비축량 기준 오름차순 정렬
                cumulStockpile.add(0L); // 집이 0개일 때 누적합(초기화)
                for(int n=0; n<N; n++) {
                    cumulStockpile.add(cumulStockpile.get(n) + houses.get(n)); // 집 n 일 때 필요한 식량 비축량
                }
            } else if(event == 200) { // 이주
                long x = Long.parseLong(st.nextToken());
                addHome(x-strength);
            } else if(event == 300) { // 영지 강화
                long p = Long.parseLong(st.nextToken());
                strength += p;
            } else if(event == 400) { // 겨울나기
                long p = Long.parseLong(st.nextToken());
                sb.append(binarySearch(p)).append("\n");
            }
        }
        System.out.print(sb);
        br.close();
    }

    private static int lowerBound(long target) { // target보다 작은 집 개수 찾기
        int left = 0;
        int right = houses.size();
        while(left < right) {
            int mid = left + (right-left)/2;
            if(houses.get(mid) < target) left = mid + 1;
            else right = mid;
        }
        return left;
    }
    
    private static void addHome(long stockpile) {
        int idx = lowerBound(stockpile); // 추가된 집으로 인해 변경이 시작되는 위치 찾기
        houses.add(idx, stockpile); // 이주된 집 추가

        cumulStockpile.add(idx+1, cumulStockpile.get(idx)+stockpile);
        for(int i=idx+2; i<cumulStockpile.size(); i++) { // 이주된 집 이후 변경된 누적합 갱신
            cumulStockpile.set(i, cumulStockpile.get(i)+stockpile);
        }
    }

    private static long binarySearch(long p) {
        long left = houses.get(0);
        long right = left + p;
        
        long result = left;
        while(left <= right) {
            long mid = left + (right-left) / 2;

            if(winter(mid, p)) { // p를 사용해서 mid를 만들 수 있는 경우
                result = mid;
                left = mid + 1;
            } else { // p보다 더 많은 비축량이 필요한 경우 범위 왼쪽으로 줄이기
                right = mid - 1;
            }
        }
        return result + strength;
    }

    private static boolean winter(long target, long p) {
        int idx = lowerBound(target); // target보다 비축량이 작은 집 개수

        long sp = idx * target - cumulStockpile.get(idx); // target만큼 채우기 위해 필요한 비축량
        
        return sp <= p;
    }
}