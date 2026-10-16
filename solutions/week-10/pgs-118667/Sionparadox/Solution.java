class Solution {
    public int solution(int[] queue1, int[] queue2) {
        int answer = Integer.MAX_VALUE;
        long total = 0L;
        int L = queue1.length;
        int[] arr = new int[L*2];
        
        for (int i=0; i<L; i++){
            total += queue1[i];
            total += queue2[i];
            arr[i] = queue1[i];
            arr[i+L] = queue2[i];
        }
        
        long half = total/2;
        long cnt = 0L;
        
        int s = 0;
        
        for (int e=0; e<2*L; e++){
            cnt += arr[e];
            while (cnt>half && s<e){
                cnt -= arr[s++];
            }
            
            if (cnt == half){
                System.out.println("HERE: "+s + " "+e);
                // if (e-s>=L) continue;
                if (s<L && e>=L){
                    int k = s + (e-L+1);
                    answer = Math.min(answer, k);
                    System.out.println("CASE1: "+s + " "+e+" "+answer);
                    continue;
                }
                int ts = s, te = e;
                if (s >= L && e >= L){
                    ts -= L;
                    te -= L;
                }
                if (te == L-1){
                    answer = Math.min(answer, ts);
                    System.out.println("CASE2: "+ts + " "+te+" "+answer);
                } else {
                    int k = (L+ts) + (te+1);
                    answer = Math.min(answer, k);
                    System.out.println("CASE3: "+ts + " "+te+" "+answer);
                }
            }
        }
        if (answer == Integer.MAX_VALUE) return -1;
        return answer;
    }
}

/*
원소 합이 같게 만들기

또 슬라이딩 윈도우? 카카오는 슬라이딩 윈도우밖에 모름?

연결된 원형 큐
배열 붙이고 첫 배열을 뒤에 한번 더 붙여야할까? NO. 어차피 절반 찾기라 뒤에서 나올 집합은 이미 앞에서 나옴


15 만들기
3 2 7 2 4 6 5 1

2 7 2 4 (s = 1, e = 4)
4 6 5   (s = 4, e = 6)

4 6 5 1 3 2 7 2
4 6 5   (s=0, e=2) -> 2+1+4+0 = 7
6 5 1 3 (s=1, e=4) -> 4-4+1+1 = 2
1 3 2 7 2 (s=3, e=7) 4-7+1+3 = 1


10 만들기

1 10 1 2 1 2 1 2

10 (s = 1, e = 1)

무조건 나온 값을 왼쪽 큐에 배치해야 최적


1 5 5 2  2 2 2 1
5 5 (s=1, e=2) -> 3번 이동 + 5번 이동

2 2 2 1 1 5 5 2
5 5 (s=5, e=6)


if (s<L && e>=L) << 양쪽에 위치한 경우
    L-e+1만큼 뒤에서 밀고 s만큼 앞에서 밀고
if (s, e <L) << 다 앞쪽 큐에 위치한 경우
    if (e == L-1) << 끝나는 점이 맨 뒤인 경우
        s만큼 앞에서 밀기
    else << 끝나는 점이 맨 뒤가 아닌 경우
        e+1번 이동 + L+s번 이동
    
if (s, e >= L) << 다 뒤쪽 큐에 위치한 경우
    s-L, e-L 해서 바로 다 앞이라 치고 풀기

*/