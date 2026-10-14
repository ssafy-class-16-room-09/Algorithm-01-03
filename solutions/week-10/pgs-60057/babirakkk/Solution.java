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
        if (queue1Sum > queue2Sum) {
            q1StartId = len;
            q2StartId = 0;
        }
        for (int i = 0; i < len; i++) {
            q[q1StartId + i] = queue1[i];
            q[q2StartId + i] = queue2[i];
        }
        
        if ((queue1Sum + queue2Sum) % 2 == 1) return -1;
        targetSum = (queue1Sum + queue2Sum) / 2;
        if (queue1Sum == targetSum) return 0;
        
        int count = -1;
        long currSum = q[0];
        int left = 0;
        int right = 0;
        while (left < len * 2 && right < len * 2) {
            if (currSum > targetSum) {
                currSum -= q[left];
                left++;
                continue;
            } else if (currSum < targetSum) {
                right++;
                if (right >= len * 2) break;
                currSum += q[right];
                continue;
            }
            
            if (right < len) {
                if (right == len - 1) {
                    count = left;
                } else {
                    count = left + right + 1 + len;
                }
            } else {
                count = left + (right - len + 1);
            }
            break;
        }
        return count;
    }
}