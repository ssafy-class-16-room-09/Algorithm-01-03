import java.util.*;

class HikingTrail implements Comparable <HikingTrail> {
    int mt; // 연결된 산
    int cost; // 비용
    
    HikingTrail(int mt, int cost) {
        this.mt = mt;
        this.cost = cost;
    }
    
    @Override
    public int compareTo(HikingTrail ht) {
        if(this.cost != ht.cost) return Integer.compare(this.cost, ht.cost);
        return Integer.compare(this.mt, ht.mt);
    }
}

class Solution {
    static int N; // XX산 지점 수
    static int maxIntensity = 0; // 주어진 등산 코스 중 가장 긴 거리
    static int[] mountainArr; // 지점 정보(0: 그냥, 1: 출발지, 2: 산봉우리)
    static List<HikingTrail>[] connectInfo;
    
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        N = n;
        
        connectInfo = new ArrayList[n+1];
        for(int i=1; i<=n; i++) { // 각 배열 List 초기화
            connectInfo[i] = new ArrayList<>();
        }
        
        mountainArr = new int[n+1];
        for(int gate : gates) mountainArr[gate] = 1;
        for(int summit : summits) mountainArr[summit] = 2;
        
        // 산 정보 입력 및, 가장 긴 거리 찾기
        for(int[] path : paths) {
            int mountain1 = path[0];
            int mountain2 = path[1];
            int cost = path[2];
            connectInfo[mountain1].add(new HikingTrail(mountain2, cost));
            connectInfo[mountain2].add(new HikingTrail(mountain1, cost));
            maxIntensity = Math.max(maxIntensity, cost);
        }
        
        int intensity = Integer.MAX_VALUE;
        int intensitySummit = 0;
        
        int start = 1;
        int end = maxIntensity;
        while(start <= end) {
            int mid = (start+end)/2;
            int summit = dijkstra(mid);
            if(summit < Integer.MAX_VALUE) {
                intensity = mid;
                intensitySummit = summit;
                end = mid-1;
            } else {
                start = mid+1;
            }
        }

        return new int[]{intensitySummit, intensity};
    }
    
    private int dijkstra(int intensity) {
        boolean[] visited = new boolean[N+1];
        
        PriorityQueue<HikingTrail> route = new PriorityQueue<>();
        for(int m=1; m<=N; m++) {
            if(mountainArr[m] != 1) continue;
            route.offer(new HikingTrail(m, 0));
            visited[m] = true;
        }
        
        int min = Integer.MAX_VALUE;
        while(!route.isEmpty()) {
            HikingTrail mountain = route.poll(); // 현재 산 위치
            if(mountainArr[mountain.mt] == 2) {
                min = Math.min(min, mountain.mt);
                continue;
            }
            
            for(HikingTrail next: connectInfo[mountain.mt]) {
                if(visited[next.mt]) continue; // 이미 방문한 산인 경우
                if(next.cost <= intensity) {
                    route.offer(new HikingTrail(next.mt, next.cost));
                    visited[next.mt] = true;
                }
            }
        }
        return min;
    }
}

/*
return -> 휴식 없이 이동해야 하는 시간 중 가장 긴 시간

출발지 별 산봉우리 이동 시간 중 가장 긴 시간 BFS -> 시간초과


이분탐색?
- paths 배열 중 가장 큰 값을 end로 설정, 가장 작은 값인 1을 start로 설정
- mid값이 휴식 없이 이동해야 하는 시간 중 가장 긴 시간으로 설정 후, 아래 내용 수행
-- 정상 위치에서 시작해서 출발지 중 하나라도 걸리지 않음 => 즉, mid값으로는 더 이상 이동 불가 => start를 mid+1로 설정
-- 정상 위치에서 하나라도 걸리면 => end를 mid-1로 설정

이걸 정상 배열 크기 만큼 반복


*/