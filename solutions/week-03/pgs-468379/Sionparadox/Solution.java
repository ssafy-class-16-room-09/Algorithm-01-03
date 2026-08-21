import java.util.*;

class SegmentTree {
    int[] tree;
    
    public SegmentTree(int[] arr){
        tree = new int[arr.length*4];
        Arrays.fill(tree, Integer.MAX_VALUE);
        build(1, 0, arr.length-1, arr);
    }
    
    public void build(int node, int start, int end, int[] arr){
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        
        int mid = (start + end) / 2;
        build(node*2, start, mid, arr);
        build(node*2+1, mid+1, end, arr);
        tree[node] = Math.min(tree[node*2], tree[node*2+1]);
    }
    
    public int query(int node, int start, int end, int left, int right){
        if (start > right || end < left) return Integer.MAX_VALUE;
        if (start >= left && end <= right) return tree[node];
        
        int mid = (start+end)/2;
        int l = query(node*2, start, mid, left, right);
        int r = query(node*2+1, mid+1, end, left, right);
        return Math.min(l, r);
    }
}


class Solution {
    public int[] solution(int m, int n, int h, int w, int[][] drops) {
        // 보드 초기화
        int[][] board = new int[m][n];
        for (int[] row:board){
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        
        // 비오는 위치 갱신
        for (int i=0; i<drops.length; i++){
            int r = drops[i][0];
            int c = drops[i][1];
            if (board[r][c] == Integer.MAX_VALUE) {
                board[r][c] = i;
            }
        }
        
        // 행 범위 내 최소값 기록
        // 전치행렬로 전환
        int[][] minRow = new int[n-w+1][m];
        for (int r=0; r<m; r++){
            SegmentTree segmentTree = new SegmentTree(board[r]);
            for (int c=0; c<n-w+1; c++){
                minRow[c][r] = segmentTree.query(1, 0, n-1, c, c+w-1);
            }
        }
        
        int maxTime = 0;
        int ar = 0;
        int ac = 0;
        // 열범위에 대해 최소값을 찾아 갱신
        for (int c=0; c<n-w+1; c++){
            SegmentTree segmentTree = new SegmentTree(minRow[c]);
            for (int r=0; r<m-h+1; r++){
                int time = segmentTree.query(1, 0, m-1, r, r+h-1);
                
                // 행이 작은 값, 열이 작은 값 우선 처리
                if (time == maxTime && (r<ar || r == ar && c < ac)){
                    maxTime = time;
                    ar = r;
                    ac = c;
                }
                
                // 정답 갱신
                if (time > maxTime){
                    maxTime = time;
                    ar = r;
                    ac = c;
                }
            }
        }
        
        return new int[] {ar, ac};
    }
}
/*
m : 행
n : 열

m*n <= 500,000

최솟값을 가지는 세그먼트 트리

1~n (가로)에 대해 먼저 범위 내 최소값들로 새 배열 생성
위에서 만든 배열을 바탕으로 세로에 대해 최소값 갱신
이때 최소값 중 최대인 값의 위치를 찾기

*/