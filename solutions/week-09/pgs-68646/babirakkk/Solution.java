class Solution {
    public int solution(int[] a) {
        int[] minFromLeft = new int[a.length]; // 가장 왼쪽 풍선부터 i번째 풍선까지 큰 번호의 풍선을 터트릴 때 남아있는 풍선의 번호
        int[] minFromRight = new int[a.length]; // 가장 오른쪽 풍선부터 i번째 풍선까지 큰 번호의 풍선을 터트릴 때 남아있는 풍선의 번호
        
        minFromLeft[0] = a[0];
        for (int i = 1; i < a.length; i++) {
            minFromLeft[i] = Math.min(minFromLeft[i - 1], a[i]);
        }
        
        minFromRight[a.length - 1] = a[a.length - 1];
        for (int i = a.length - 2; i > 0; i--) {
            minFromRight[i] = Math.min(minFromRight[i + 1], a[i]);
        }
        
        int countFinalBalloons = Math.min(a.length, 2); // 양 끝 풍선은 항상 남길 수 있음
        for (int i = 1; i < a.length - 1; i++) {
            int left = minFromLeft[i - 1]; // i번째 풍선 기준 왼쪽 풍선을 모두 터뜨리고 남은 풍선의 번호
            int right = minFromRight[i + 1]; // i번째 풍선 기준 오른쪽 풍선을 모두 터뜨리고 남은 풍선의 번호
            
            // 현재 풍선 기준 양쪽의 남은 두 풍선이 모두 현재 풍선보다 작으면 현재 풍선을 남길 수 없음
            if (a[i] > left && a[i] > right) continue; 
            
            countFinalBalloons++;
        }
        
        return countFinalBalloons;
    }
}