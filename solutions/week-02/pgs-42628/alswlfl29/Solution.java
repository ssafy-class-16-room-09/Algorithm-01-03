import java.util.*;

class Solution {
    Map<Integer, Integer> hashMap = new HashMap<>(); // 현재 큐에 들어가있는 숫자
    
    public int[] solution(String[] operations) {
        
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // 최대 힙
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // 최소 힙

        for(String operation : operations) {
            char order = operation.charAt(0); // 명령어
            int data = Integer.parseInt(operation.substring(2)); // 데이터
            
            // 큐에 주어진 숫자 삽입
            if(order == 'I') {
                maxHeap.offer(data); // 최대 힙에 숫자 삽입
                minHeap.offer(data); // 최소 힙에 숫자 삽입
                hashMap.put(data, hashMap.getOrDefault(data, 0) + 1);
            } else if(data == 1 && hashMap.size() > 0) { // 큐에서 최댓값 삭제
                int num = removeHeap(maxHeap);
                hashMap.put(num, hashMap.get(num)-1);
                count(num);
            } else if(data == -1 && hashMap.size() > 0) { // 큐에서 최솟값 삭제
                int num = removeHeap(minHeap);
                hashMap.put(num, hashMap.get(num)-1);
                count(num);
            }
        }
        
        int maxNum = 0;
        int minNum = 0;
        if(!hashMap.isEmpty()) {
            // 최대힙의 진정한 최대 구하기
            if(!maxHeap.isEmpty() ) {
                maxNum = removeHeap(maxHeap);
            }
            // 최소힙의 진정한 최소 구하기
            if(!minHeap.isEmpty()) {
                minNum = removeHeap(minHeap);
            }
        }
        
        int[] answer = {maxNum, minNum};
        return answer;
    }
    
    // PQ의 우선순위가 높은 숫자 큐에서 제거
    // 이때, 이미 삭제된 숫자들 제거해줘야 함
    private int removeHeap(PriorityQueue<Integer> pq) {
        int num = pq.poll();
        while(!pq.isEmpty() && hashMap.get(num) == null) {
            num = pq.poll();
        }
        return num;
    }
    
    // hashMap에 있는 숫자(=key)의 나온 횟수가 0이면 hashMap에서 삭제,
    // 아니면 큐에서 꺼낸 숫자의 나온 횟수 감소
    private void count(int num) {
        int cnt = hashMap.get(num);
        if(cnt <= 0) hashMap.remove(num);
    }
}

/*
모든 연산 수행 후, 큐가 비었음 -> [0,0] 출력, 비어있지 않음 -> [최대, 최소]

[연산]
- I 숫자 -> 큐에 주어진 숫자 삽입
- D 1 -> 큐에서 최댓값 삭제
- D -1 -> 큐에서 최솟값 삭제

<최대힙>
(16) 123 -5643
<최소힙>
(-5643) 16 123
<Set>
(16) (-5643) 123


["I 100", "D 1", "I 101"]
정답 -> 101
*/