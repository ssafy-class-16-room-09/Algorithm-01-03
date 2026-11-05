import java.util.*;
import java.io.*;

class Node {
    // 왼쪽 자식, 오른쪽 자식
    Node left, right;
    // 이 구간의 집의 개수
    int count;
    // 구간의 집의 식량 합, 구간의 식량 중 최소값
    long sum, key;
}

class SegmentTree {
    Node root = new Node();
    static final long MAX = (1L<<50);
    static final long MIN = -(1L<<50);

    void add(long k){
        update(root, MIN, MAX, k);
    }

    // l~r 구간 업데이트
    // value : 추가하려는 집의 식량
    void update(Node node, long l, long r, long value){
        node.count++;
        node.sum += value;

        // 리프 노드면 식량 키에 저장하고 리턴
        if(l == r){
            node.key = value;
            return;
        }

        // 구간의 중앙 구하기
        long mid = l + (r-l)/2;

        if (value <= mid){
            // 값이 왼쪽 구간에 속하면 왼쪽으로 이동
            if(node.left == null) node.left = new Node();
            update(node.left, l, mid, value);

        } else {
            // 값이 오른쪽 구간에 속하면 오른쪽으로 이동
            if(node.right == null) node.right = new Node();
            update(node.right, mid+1, r, value);
        }

        // 작은 자식의 키로 키 재설정
        if (node.left != null){
            node.key = node.left.key;
        } else {
            node.key = node.right.key;
        }
    }

    long event(long p){
        return query(root, MIN, MAX, p, 0, 0);
    }

    // l~r 구간을 p보급량으로 보급 채우기
    // p: 남은 보급량, cnt: 낮은 구간부터 누적된 집 개수, sum : 낮은 구간부터 누적된 식량합
    long query(Node node, long l, long r, long p, long cnt, long sum){
        // 끝까지 탐색했으면 남은 보급량(p)을 집에 균등하게 나눠준 최소값 리턴
        if (l == r) return l + p/(cnt + node.count);

        // 구간의 중앙 구하기
        long mid = l + (r-l)/2;

        // 왼쪽에 집이 없으면 오른쪽 자식으로 탐색
        if(node.left == null) return query(node.right, mid+1, r, p, cnt, sum);
        // 오른쪽에 집이 없으면 왼쪽 자식으로 탐색
        if(node.right == null) return query(node.left, l, mid, p, cnt, sum);

        // 오른쪽 구간의 최소식량 << 여기까지 왼쪽 구간을 끌어올려야함
        long nxt = node.right.key;
        // 왼쪽 구간 + cnt를 오른쪽 구간의 최소값까지 끌어올리기
        long cost = nxt * (cnt+node.left.count) - (sum+node.left.sum);

        // 보급량이 충분하면 오른쪽 구간으로 넘어가기
        if (p >= cost){
            long ncnt = cnt+node.left.count;
            return query(node.right, mid+1, r, p-cost, ncnt, nxt*ncnt);
        }

        // 보급량이 부족하면 왼쪽 구간으로 넘어가기
        return query(node.left, l, mid, p, cnt, sum);
    }

}

public class Main {
    static SegmentTree segmentTree;
    static long supply = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int Q = Integer.parseInt(br.readLine());
        segmentTree = new SegmentTree();

        for(int q=0; q<Q; q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if (cmd == 100){
                int N = Integer.parseInt(st.nextToken());
                for (int i=0; i<N; i++){
                    long k = Long.parseLong(st.nextToken());
                    segmentTree.add(k);
                }
            } else if (cmd == 200) {
                long k = Long.parseLong(st.nextToken());
                segmentTree.add(k-supply);
            } else if (cmd == 300) {
                long p = Long.parseLong(st.nextToken());
                supply += p;
            } else {
                long p = Long.parseLong(st.nextToken());
                System.out.println(segmentTree.event(p)+supply);
            }
        }

    }
}

/*
집 추가
밥 공금

명령 20민

집 20000 + 1000

겨울 공급은 임시 : 이후 회수됨
강화는 lazy propagation

트리맵 쓰니까 시간초과

*/
