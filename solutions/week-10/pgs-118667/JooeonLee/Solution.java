/**
결국 주어진 큐는 배열 알아서 편집해도 무관하다
queue1 = [3, 2, 7, 2] sum1 = 14
queue2 = [4, 6, 5, 1] sum2 = 16
[1, 3, 2, 7, 2] sum1 = 15
[4, 6, 5] sum2 = 15
arr = queue1 + queue2
[3, 2, 7, 2, 4, 6, 5, 1];
0, 1, 2, 3, 4, 5, 6, 7
q1           q2
0, 1, 2, 3, 4, 5, 6, 7
q1'            q2'
sum1' = 18
sum2' = 12
sum1 이 half보다 작을 때 right++

0, 1, 2, 3, 4, 5, 6, 7
   q1          q2
sum1 = 2 7 2 4 = 15
sum2 = 15 
sum1 이 half보다 클때 left++

이렇게 조작하면 역전되는 경우없나??????????? -> 큐1에 값이 완전 없게 되면 어차피 늘어나니까 상관 없는건가?
일단 해보자!
*/
class Solution {
    public int solution(int[] queue1, int[] queue2) {
        StringBuilder sb = new StringBuilder();
        
        int l = queue1.length;
        int[] arr = new int[l*2];
        long total = 0;
        long target = 0;
        long sum = 0;
        for(int i=0; i<l; i++) {
            arr[i] = queue1[i];
            sum += queue1[i];
            total += queue1[i];
        }
        for(int i=l; i<l*2; i++) {
            arr[i] = queue2[i-l];
            total += queue2[i-l];
        }
        
        if(total % 2 != 0)
            return -1;
        target = total/2;
        
        int q1 = 0;
        int q2 = l;
        int cnt = 0;
        sb.append("cnt: ").append(cnt).append(' ').append(q1).append(' ').append(q2).append('\n');
        
        // 탈출조건이 뭐지?? -> 최대 연산 횟수만큼 해도 절대 만들어지지 않는 최대 연산 횟수??(일단 원소 개수 4배 정도로 잡고 가보자) -> 탈출조건이 러프한가??? 절대 아닐거 같은데 돌아가긴함, 왜 돌아가는지 모르겠음
        while(cnt <= l*4) {
            if(sum == target) {
                System.out.print(sb);
                return cnt;
            }
            else if(sum < target) {
                sum += arr[q2];
                q2 = (q2+1) % (l*2);
                cnt++;
                sb.append("cnt: ").append(cnt).append(' ').append(q1).append(' ').append(q2).append('\n');
            }
            else {
                sum -= arr[q1];
                q1 = (q1+1) % (l*2);
                cnt++;
                sb.append("cnt: ").append(cnt).append(' ').append(q1).append(' ').append(q2).append('\n');
            }
        }
        System.out.print(sb);
        return -1;
    }
}