import java.util.*;

class Road {
    int to, fare;
    Road(int to, int fare) {
        this.to = to;
        this.fare = fare;
    }
}

class TaxiState {
    int stop, totalFare;
    TaxiState(int stop, int totalFare) {
        this.stop = stop;
        this.totalFare = totalFare;
    }
}

class Solution {
	
	ArrayList<Road>[] roadMap;
	int minTotalFare;
	int n, a, b;
	
    public int solution(int n, int s, int a, int b, int[][] fares) {
    	this.n = n;
    	this.a = a;
    	this.b = b;
    	
        roadMap = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            roadMap[i] = new ArrayList<>();
        }
        for (int[] fare : fares) {
            roadMap[fare[0]].add(new Road(fare[1], fare[2]));
            roadMap[fare[1]].add(new Road(fare[0], fare[2]));
        }
        
        minTotalFare = Integer.MAX_VALUE;
        int[] minFare = new int[n + 1];
        Arrays.fill(minFare, Integer.MAX_VALUE);
        minFare[s] = 0;
        
        PriorityQueue<TaxiState> pq = new PriorityQueue<>(Comparator.comparingInt(t -> t.totalFare));
        pq.add(new TaxiState(s, 0));
        
        while (!pq.isEmpty()) {
            TaxiState ts = pq.poll();
            if (minFare[ts.stop] >= minTotalFare) break; // 가능한 최소 비용보다 현재 위치로 오는 비용이 더 비싸다면 탐색 종료 
            if (minFare[ts.stop] < ts.totalFare) continue;
            
            findMinFareFromStop(ts.stop, minFare[ts.stop]);
            
            // 다음으로 합승 후 하차할 지점을 탐색
            for (Road r : roadMap[ts.stop]) {
                int nextFare = minFare[ts.stop] + r.fare;
                if (nextFare < minFare[r.to] && nextFare < minTotalFare) {
                    pq.add(new TaxiState(r.to, nextFare));
                    minFare[r.to] = nextFare;
                }
            }
        }
        
        return minTotalFare;
    }
    
    private void findMinFareFromStop(int currStart, int fareFromStart) {
    	// 현재 위치에서 A와 B로 가는 비용을 구함
        int[] minFare = new int[n + 1]; 
        Arrays.fill(minFare, Integer.MAX_VALUE);
        minFare[currStart] = 0;
        
        PriorityQueue<TaxiState> pq = new PriorityQueue<>(Comparator.comparingInt(t -> t.totalFare));
        pq.offer(new TaxiState(currStart, 0));
        while (!pq.isEmpty()) {
            TaxiState ts = pq.poll();
            if (fareFromStart + ts.totalFare >= minTotalFare) break; // (현재 시작점까지의 비용 + 시작점-현재 위치까지의 비용)이 이미 최소 비용을 넘었다면 더 탐색할 필요 없
            if (minFare[ts.stop] < ts.totalFare) continue;
            for (Road r : roadMap[ts.stop]) {
                int nextFare = ts.totalFare + r.fare;
                if (nextFare < minFare[r.to]) {
                	minFare[r.to] = nextFare;
                    if (r.to != a && r.to != b) {
                    	pq.offer(new TaxiState(r.to, nextFare));
                    }
                }
            }
        }
        if (minFare[a] != Integer.MAX_VALUE && minFare[b] != Integer.MAX_VALUE) { // 둘 다 도달 가능하다면 최소 비용과 비교해 작은 값을 저장 
            int currTotalFare = fareFromStart + minFare[a] + minFare[b];
            minTotalFare = Math.min(minTotalFare, currTotalFare);
        }
    }
    
}
