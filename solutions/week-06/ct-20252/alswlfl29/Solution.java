import java.util.*;
import java.io.*;

// 가로등 객체
class StreetLamp {
    static int count = 0; // 가로등 총 개수
    int number; // 가로등 번호
    int position; // 가로등 위치
    StreetLamp prev; // 이전 가로등 위치(인덱스)
    StreetLamp next; // 다음 가로등 위치(인덱스)
    boolean isDeleted; // 삭제 여부

    StreetLamp(int position) {
        this.number = count++;
        this.position = position;
        this.isDeleted = false;
    }

    @Override
    public String toString() {
        return "[" + number + ", " + position + ", " + (prev == null ? "null" : prev.number )+ ", " + (next == null ? "null"  : next.number) + ", " + isDeleted + "]";
    }
}

// 거리 객체
class Distance implements Comparable<Distance> {
    int distance; // 가로등 간 거리
    StreetLamp lamp1;
    StreetLamp lamp2;

    Distance(StreetLamp lamp1, StreetLamp lamp2) {
        this.distance = lamp2.position - lamp1.position;
        this.lamp1 = lamp1;
        this.lamp2 = lamp2;
    }

    @Override
    public int compareTo(Distance distance) {
        if(this.distance != distance.distance) return Integer.compare(distance.distance, this.distance);
        return Integer.compare(this.lamp1.position, distance.lamp1.position);
    }
}

public class Solution {
    
    static List<StreetLamp> streetLamps; // 거리 위 가로등
    static PriorityQueue<Distance> distances; // 가로등 사이 거리
    static int firstDistance; // 시작 점과의 거리
    static int lastDistance; // 끝 점과의 거리
    static int N; // 마을 총 거리
    static int M; // 초기에 주어지는 마을 수

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine()); // 명령 횟수
        
        init(br.readLine()); // 마을 상태 확인(한 번만 실행)

        StringBuilder sb = new StringBuilder();
        for(int q=1; q<Q; q++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken());

            if(order == 200) {
                insert();
            } else if(order == 300) {
                delete(Integer.parseInt(st.nextToken()));
            } else if(order == 400) {
                int min = getMinElectricalEnergy();
                sb.append(min).append("\n");
            }
        }
        
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb);
    }

    private static void init(String info) {
        StringTokenizer st = new StringTokenizer(info);
        st.nextToken(); // 명령(100)
        N = Integer.parseInt(st.nextToken()); // N의 개수

        M = Integer.parseInt(st.nextToken()); // 초기에 존재하는 가로등 개수
        streetLamps = new ArrayList<>();
        for(int i=0; i<M; i++) {
            int L = Integer.parseInt(st.nextToken());
            streetLamps.add(new StreetLamp(L)); // 가로등 추가
        }
        
        distances = new PriorityQueue<>();
        firstDistance = streetLamps.get(0).position - 1;
        lastDistance = N - streetLamps.get(M-1).position;
        for(int i=0; i<M; i++) {
            if(i > 0) { // 이전 가로등 연결하기 & 거리 PQ에 넣기
                streetLamps.get(i).prev = streetLamps.get(i-1);
                distances.offer(new Distance(streetLamps.get(i-1), streetLamps.get(i)));
            }
            if(i < M-1) { // 다음 가로등 연결하기
                streetLamps.get(i).next = streetLamps.get(i+1);
            }
        }
    }

    private static void insert() {
        
        // 가장 거리가 먼 가로등 찾기
        Distance distance = checkRemoveStreet();

        StreetLamp lamp1 = distance.lamp1;
        StreetLamp lamp2 = distance.lamp2;

        int newPosition = (int) Math.ceil((lamp1.position + lamp2.position)/2.0); // 추가할 가로등 위치
        StreetLamp newLamp = new StreetLamp(newPosition);
        streetLamps.add(newLamp);

        // 추가된 가로등과 연결시키기
        lamp1.next = newLamp;
        lamp2.prev = newLamp;

        newLamp.prev = lamp1;
        newLamp.next = lamp2;

        distances.offer(new Distance(lamp1, newLamp));
        distances.offer(new Distance(newLamp, lamp2));
    }

    private static void delete(int number) {
        
        StreetLamp lamp = streetLamps.get(number-1); // 제거할 가로등

        if(lamp.prev == null) { // 첫 번째 가로등인 경우
            lamp.next.prev = null;
            firstDistance = lamp.next.position - 1;
        } else if(lamp.next == null) { // 마지막 가로등인 경우
            lamp.prev.next = null;
            lastDistance = N - lamp.prev.position;
        } else {
            StreetLamp prevLamp = lamp.prev; // 제거할 가로등 바로 이전 가로등
            StreetLamp nextLamp = lamp.next; // 제거할 가로등 바로 다음 가로등
            
            // 서로 연결
            prevLamp.next = nextLamp;
            nextLamp.prev = prevLamp;

            distances.offer(new Distance(prevLamp, nextLamp)); // 그 사이의 거리 PQ에 넣기
        }

        lamp.isDeleted = true; // 해당 가로등 삭제
    }

    private static int getMinElectricalEnergy() {
        
        // 가장 거리가 먼 가로등 찾기
        Distance distance = checkRemoveStreet();

        int longDistance = distance.distance;
        // 찾은 최소 전력으로 첫 번째 가로등까지 비추지 못할 경우
        if(longDistance < firstDistance * 2) longDistance = firstDistance * 2;

        // 찾은 최소 전력으로 마지막 가로등까지 비추지 못할 경우
        if(longDistance < lastDistance * 2) longDistance = lastDistance * 2;
        
        distances.offer(distance); // 다시 해당 가로등 PQ에 넣기
        return longDistance;
    }

    private static Distance checkRemoveStreet() {
        while(true) {
            Distance distance = distances.poll();
            if(!distance.lamp1.isDeleted && !distance.lamp2.isDeleted) return distance;
        }
    } 

}
