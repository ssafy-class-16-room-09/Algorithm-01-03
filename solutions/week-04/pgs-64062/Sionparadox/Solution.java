class Solution {
    static int MAX_STONE = 200_000_000;
    int[] stones;
    int K;
    public int solution(int[] stones, int k) {
        this.stones = stones;
        this.K = k;
        
        int answer = 0;
        int left = 1;
        int right = MAX_STONE;
        while (left <= right){
            int mid = (left+right)/2;
            if (check(mid)){
                left = mid + 1;
                answer = mid;
            } else {
                right = mid - 1;
            }
        }
        
        return answer;
    }
    
    private boolean check(int n){
        int continous = 0;
        for (int value: stones){
            if (value < n){
                continous++;
            } else {
                continous = 0;
            }
            if (continous >=K) return false;
        }
        return true;
    }
}

/*
stones <= 200_000

이분탐색?
1 ~ 2억
28번 탐색

이분탐색을 한다면
check(mid) 메서드에서는 mid값 이하인 돌이 연속 K개 이하 있는지 검사
check True : mid이하인 돌이 연속 K개 없음.(가능한 mid값임)

28번*20만 = 560만이면 통과 아닌가?
*/