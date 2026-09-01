class Solution {
    int[] tree; // 세그먼트 트리(최대값)
    int[] stones; // 징검다리 배열
    
    public int solution(int[] stones, int k) {
        
        int N = stones.length; // 징검다리 개수
        this.stones = stones;
        tree = new int[N*4];
        
        init(0, N-1, 1); // node 번호는 1부터 시작
        
        int min = Integer.MAX_VALUE;
        for(int n=0; n<=N-k; n++) {
            int number = find(0, N-1, 1, n, n+k-1);
            min = Math.min(min, number);
        }
        return min;
    }
    
    private int init(int start, int end, int node) {
        if(start == end) {
            return tree[node] = stones[start]; // 리프 노드인 경우, 본인으로 채우기
        }
        
        int mid = (start+end) / 2;
        
        return tree[node] = Math.max(init(start, mid, node*2), init(mid+1, end, node*2+1));
    }
    
    private int find(int start, int end, int node, int left, int right) {
        if(left > end || right < start) return 0; // 범위를 벗어난 경우
        
        if(left <= start && end <= right) return tree[node]; // 범위인 경우
        
        int mid = (start+end)/2;
        return Math.max(find(start, mid, node*2, left, right),
                                     find(mid+1, end, node*2+1, left, right));
    }
}