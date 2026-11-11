class Solution {
    
    public int solution(int n, int[] stations, int w) {
        int numUsedStations = 0;
        int nextCoverApt = 1;
        int nextInstalledId = 0;
        
        while (true) { // nextCoverApt > n 모든 집에 전파가 도달함
            while (nextInstalledId < stations.length 
                   && stations[nextInstalledId] - w <= nextCoverApt) {
                nextCoverApt = stations[nextInstalledId++] + w + 1;
            }
            
            if (nextCoverApt > n) break;
            
            numUsedStations++;
            nextCoverApt += 2 * w + 1; // 기지국이 설치된 위치 + 양쪽으로 w씩  
        } 
        
        return numUsedStations;
    }
}