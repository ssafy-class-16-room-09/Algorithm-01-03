import java.util.*;

class Solution {
    public int solution(int[] stones, int k) {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        
        for (int i = 0; i < k; i++) {  // 첫 번째 k 구간의 값을 단조 감소 형태로 dq에 저장
            addValue(dq, stones[i]);
        }
                                              
        int maxFriends = dq.getFirst();
        for (int i = 1; i <= stones.length - k; i++) {
            if (dq.getFirst() == stones[i - 1]) { // 범위에서 벗어난 값은 제거
                dq.pollFirst();
            }
            addValue(dq, stones[i + k - 1]); // 새로운 범위의 값 추가
            maxFriends = Math.min(maxFriends, dq.getFirst()); // 각 k 구간의 최댓값 중 최솟값 갱신
        }

        return maxFriends;
    }

    /**
     * dq에 값을 추가하는 함수
     * @param dq 단조감소 덱
     * @param n 추가할 값
     */
    private void addValue(ArrayDeque<Integer> dq, int n) {
        while (!dq.isEmpty() && dq.getLast() < n) { // 새 값보다 작은 뒤쪽 원소 제거
            dq.pollLast();
        }
        dq.addLast(n);
    }
}