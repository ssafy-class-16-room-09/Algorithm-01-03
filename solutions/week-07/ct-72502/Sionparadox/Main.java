import java.util.*;
import java.io.*;

class Node {
    int id, pos;
    boolean removed;
    public Node(int id, int pos){
        this.id = id;
        this.pos = pos;
    }
}

public class Main {
    
    static ArrayList<Node> house;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int Q = Integer.parseInt(br.readLine());
        StringTokenizer st;
        for (int q=0; q<Q; q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            int N = Integer.parseInt(st.nextToken());
            if (cmd == 100){
                house = new ArrayList<>();
                house.add(new Node(0, 0));

                for(int i=0; i<N; i++){
                    int p = Integer.parseInt(st.nextToken());
                    house.add(new Node(house.size(), p));
                }
            } else if (cmd == 200) {
                house.add(new Node(house.size(), N));
            } else if (cmd == 300) {
                house.get(N).removed = true;
            } else if (cmd == 400) {
                System.out.println(binarySearch(N));
            }
        }
    }

    static private int binarySearch(int r){
        int left = 0, right = house.get(house.size()-1).pos;
        int ret = 0;
        int answer = -1;

        while (left <= right) {
            int mid = (left+right)/2;

            if (check(mid, r)){
                answer = mid;
                right = mid-1;
            } else {
                left = mid+1;
            }
        }

        return answer;
    }

    private static boolean check(int k, int r) {
        int cnt = 1;
        int start = -1;
        int sidx = 1;
        for (int i=1; i<house.size(); i++){
            Node curr = house.get(i);
            if (!curr.removed){
                start = curr.pos;
                sidx = i;
                break;
            }
        }

        for (int i=sidx; i<house.size(); i++){
            Node curr = house.get(i);
            if (curr.removed) continue;

            if (curr.pos - start > k) {
                cnt++;
                start = curr.pos;
            }

        }
        return cnt <= r;
    }
}

/*
check만 잘 만들면 pass

무조건 맨 앞에 배치해야함
+ 다음 노드에 설치해보다가 거리가 k초과로 멀어지면 멈춰야해. 멈춰!
*/