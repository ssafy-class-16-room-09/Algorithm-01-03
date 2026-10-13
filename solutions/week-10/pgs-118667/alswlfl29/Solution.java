class Solution {
    public int solution(int[] queue1, int[] queue2) {
        
        int size = queue1.length;
        int[] queue = new int[size*2];
        
        long total = 0;
        for(int i=0; i<queue1.length; i++) {
            queue[i] = queue1[i];
            queue[i+size] = queue2[i];
            total += queue1[i];
            total += queue2[i];
        }

        int start = 0;
        int end = size-1;

        long sum = 0;
        for(int i=0; i<=end; i++) sum += queue[i]; // 초기 합
        int count = 0; // 작업 횟수
        while(start < size*2) {
            if(sum*2 == total) return count; // 두 큐의 합이 같은 경우 작업 횟수 리턴

            if(sum*2 < total) { // 한 큐에 들어있는 합이 절반보다 작을 경우, 다른 큐에서 원소 빼서 추가하기
                end = (end+1)%(size*2);
                sum += queue[end];
            } else { // 절반보다 더 큰 경우, 현재 큐에서 원소 빼서 다른 큐에 넣기
                sum -= queue[start++];
            }
            count++; // 작업 수행 증가
        }
        
        return -1;
    }
}
