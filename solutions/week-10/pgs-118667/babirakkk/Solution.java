class Solution {
    
    int[] q;
    int len;
    long targetSum, queue1Sum, queue2Sum;
    
    public int solution(int[] queue1, int[] queue2) {
        len = queue1.length;
        q = new int[len * 2];
        queue2Sum = 0;
        queue1Sum = 0;
        for (int i = 0; i < len; i++) {
            queue1Sum += queue1[i];
            queue2Sum += queue2[i];
        }
        
        int q1StartId = 0;
        int q2StartId = len;
        if (queue1Sum > queue2Sum) { // 합이 더 작은 큐를 앞에 배치하자!
            q1StartId = len;
            q2StartId = 0;
        }
        for (int i = 0; i < len; i++) { // 두 큐를 하나의 배열에 합치기 
            q[q1StartId + i] = queue1[i];
            q[q2StartId + i] = queue2[i];
        }
        
        if ((queue1Sum + queue2Sum) % 2 == 1) return -1; // 합이 홀수이면 같게 만들 수 없음
        
        targetSum = (queue1Sum + queue2Sum) / 2;
        if (queue1Sum == targetSum) return 0; // 이미 같은 경우 더 이상 움직이지 않아도 됨 
        
        int count = -1;
        long currSum = q[0];
        int left = 0;
        int right = 0;
        while (left < len * 2) {
            if (currSum > targetSum) { // 현재 합이 목표 합보다 큰 경우 왼쪽을 한 칸 옮김 
                currSum -= q[left];
                left++;
                continue;
            } else if (currSum < targetSum) { // 현재 합이 목표 합보다 작은 경우 오른쪽을 한 칸 옮김 
                right++;
                if (right >= len * 2) break; // right가 끝에 도달했는데도 여전히 목표 합보다 작다면? 같게 만들 수 없다!
                currSum += q[right];
                continue;
            }
            
            if (right < len) {
                if (right == len - 1) { // left보다 왼쪽에 있는 값들만 오른쪽 큐로 옮기면 됨 
                    count = left;
                } else { // 아니라면 right 포함 왼쪽에 있는 값들을 모두 오른쪽 큐로 옮긴 뒤, left보다 왼쪽에 있는 값들을 왼쪽 큐로 옮겨야 함 
                    count = left + right + 1 + len;
                }
            } else { // left보다 왼쪽에 있는 값을 오른쪽 큐로 옮기고, right포함 다른 큐에 있는 부분을 왼쪽 큐로 옮기면 됨 
                count = left + (right - len + 1);
            }
            break;
        }
        return count;
    }
}
