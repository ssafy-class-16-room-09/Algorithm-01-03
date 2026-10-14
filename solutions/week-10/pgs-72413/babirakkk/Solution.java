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
    public int solution(int n, int s, int a, int b, int[][] fares) {
        ArrayList<Road>[] roadMap = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            roadMap[i] = new ArrayList<>();
        }
        for (int[] fare : fares) {
            roadMap[fare[0]].add(new Road(fare[1], fare[2]));
            roadMap[fare[1]].add(new Road(fare[0], fare[2]));
        }
        
        int minTotalFare = Integer.MAX_VALUE;
        int[] minFareFromStart = new int[n + 1];
        Arrays.fill(minFareFromStart, Integer.MAX_VALUE);
        minFareFromStart[s] = 0;
        
        PriorityQueue<TaxiState> pq = new PriorityQueue<>(Comparator.comparingInt(t -> t.totalFare));
        pq.add(new TaxiState(s, 0));
        
        while (!pq.isEmpty()) {
            TaxiState ts = pq.poll();
            if (minFareFromStart[ts.stop] >= minTotalFare) break;
            if (minFareFromStart[ts.stop] < ts.totalFare) continue;
            
            // 현재 위치에서 A와 B로 가는 비용을 구함
            int[] minFareFromNewStart = new int[n + 1]; 
            Arrays.fill(minFareFromNewStart, Integer.MAX_VALUE);
            minFareFromNewStart[ts.stop] = 0;
            
            PriorityQueue<TaxiState> npq = new PriorityQueue<>(Comparator.comparingInt(t -> t.totalFare));
            npq.offer(new TaxiState(ts.stop, 0));
            while (!npq.isEmpty()) {
                TaxiState nts = npq.poll();
                if (minFareFromStart[ts.stop] + nts.totalFare >= minTotalFare) break;
                if (minFareFromNewStart[nts.stop] < nts.totalFare) continue;
                for (Road r : roadMap[nts.stop]) {
                    int nextFare = nts.totalFare + r.fare;
                    if (nextFare < minFareFromNewStart[r.to]) {
                        minFareFromNewStart[r.to] = nextFare;
                        if (r.to != a && r.to != b) {
                            npq.offer(new TaxiState(r.to, nextFare));
                        }
                    }
                }
            }
            if (minFareFromNewStart[a] != Integer.MAX_VALUE && minFareFromNewStart[b] != Integer.MAX_VALUE) {
                int currTotalFare = minFareFromStart[ts.stop] + minFareFromNewStart[a] + minFareFromNewStart[b];
                minTotalFare = Math.min(minTotalFare, currTotalFare);
            }
            
            // 다음으로 합승 후 하차할 지점을 탐색
            for (Road r : roadMap[ts.stop]) {
                int nextFare = minFareFromStart[ts.stop] + r.fare;
                if (nextFare < minFareFromStart[r.to] && nextFare < minTotalFare) {
                    pq.add(new TaxiState(r.to, nextFare));
                    minFareFromStart[r.to] = nextFare;
                }
            }
        }
        
        return minTotalFare;
    }
}