import java.util.*;

class Solution {
    
    int[][] desert;
    ArrayDeque<Integer> dq = new ArrayDeque<>(); // dq는 단조 증가하도록 유지
    
    public int[] solution(int m, int n, int h, int w, int[][] drops) {
        desert = new int[m][n];
        for (int[] d : desert) { // 초기값은 Integer.MAX_VALUE로 설정
            Arrays.fill(d, Integer.MAX_VALUE);
        }
        
        for (int i = 0; i < drops.length; i++) { // 비가 오는 구역은 drops 배열의 인덱스 + 1로 설정
            desert[drops[i][0]][drops[i][1]] = i + 1;
        }
        
        int[][] horizontalMin = new int[m][n - w + 1]; // 가로로 슬라이딩 후 최소값을 저장할 배열
        int[][] verticalMin = new int[m - h + 1][n - w + 1]; // 세로로 슬라이딩 후 최소값을 저장할 배열
        
        slideHorizontal(desert, horizontalMin, w, m, n - w + 1);
        slideVertical(horizontalMin, verticalMin, h, m - h + 1, n - w + 1);
        
        int targetX = 0; // 비를 가장 늦게 맞는 좌표를 찾기 위한 변수
        int targetY = 0;
        int lateRain = 0; // 현재까지 가장 늦게 맞는 비의 시간을 저장하는 변수
        OUT: for (int i = 0; i < verticalMin.length; i++) {
            for (int j = 0; j < verticalMin[0].length; j++) {
                if (verticalMin[i][j] == Integer.MAX_VALUE) { // 비를 맞지 않는 구역을 찾았다면
                    targetX = i;
                    targetY = j;
                    break OUT; // 즉시 종료
                }
                if (verticalMin[i][j] > lateRain) {
                    lateRain = verticalMin[i][j];
                    targetX = i;
                    targetY = j;
                }
            }
        }
        return new int[]{ targetX, targetY };
    }
    
    private void slideHorizontal(int[][] target, int[][] result, int windowSize, int xRange, int yRange) {
        for (int i = 0; i < xRange; i++) {
            dq.clear();
            
            for (int k = 0; k < windowSize; k++) { // 초기 윈도우 설정              
                pushMaintainingOrder(target[i][k]);
            }
            result[i][0] = dq.getFirst();
            
            for (int j = 1; j < yRange; j++) {
                if (dq.getFirst() == target[i][j - 1]) { // 현재 최소값이 슬라이딩 윈도우에서 벗어나면 제거
                    dq.removeFirst();
                }
                pushMaintainingOrder(target[i][j + windowSize - 1]);
                result[i][j] = dq.getFirst(); // 현재 윈도우에서 최소값은 dq의 첫 번째 값
            }
        }
    }
    
    private void slideVertical(int[][] target, int[][] result, int windowSize, int xRange, int yRange) {
        for (int j = 0; j < yRange; j++) {
            dq.clear();
            
            for (int k = 0; k < windowSize; k++) { // 초기 윈도우 설정
                pushMaintainingOrder(target[k][j]);
            }
            result[0][j] = dq.getFirst();
            
            for (int i = 1; i < xRange; i++) {
                if (dq.getFirst() == target[i - 1][j]) { // 현재 최소값이 슬라이딩 윈도우에서 벗어나면 제거
                    dq.removeFirst();
                }
                pushMaintainingOrder(target[i + windowSize - 1][j]);
                result[i][j] = dq.getFirst(); // 현재 윈도우에서 최소값은 dq의 첫 번째 값
            }
        }
    }
    
    private void pushMaintainingOrder(int curr) {
        while (!dq.isEmpty() && dq.getLast() > curr) { // 현재 값이 dq의 마지막 값보다 작으면, dq의 마지막 값을 제거하여 단조 증가를 유지
            dq.removeLast();
        }
        dq.add(curr);
    }
    
}