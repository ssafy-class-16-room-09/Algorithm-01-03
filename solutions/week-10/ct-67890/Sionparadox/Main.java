import java.util.*;
import java.io.*;

class Jewel {
    int id, weight, price;
    boolean isRemoved;
    public Jewel(int id, int weight, int price) {
        this.id = id;
        this.weight = weight;
        this.price = price;
    }
}

public class Main {
    static Jewel[] jewels;
    static int[] dp;
    static TreeMap<Integer, Integer> weightCnt;
    static int N, jewelId;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int Q = Integer.parseInt(br.readLine());
        for (int q=0; q<Q; q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if (cmd == 1){
                N = Integer.parseInt(st.nextToken());
                init(st);
            } else if (cmd == 2){
                int w = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                addJewel(w, v);
            } else if (cmd == 3){
                int idx = Integer.parseInt(st.nextToken());
                System.out.println(removeJewel(idx));
            } else if (cmd == 4){
                int W = Integer.parseInt(st.nextToken());
                display(W);
            } else {
                int D = Integer.parseInt(st.nextToken());
                makeSet(D);
            }
        }
    }

    private static void init(StringTokenizer st){
        jewels = new Jewel[N+101];
        weightCnt = new TreeMap<>();
        dp = new int[3001];
        jewelId = 1;
        for (int i=0; i<N; i++){
            int w = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            addJewel(w, v);
        }

    }

    private static void addJewel(int weight, int price){
        jewels[jewelId] = new Jewel(jewelId, weight, price);
        jewelId++;
        weightCnt.put(weight, weightCnt.getOrDefault(weight, 0)+1);
    }

    private static int removeJewel(int idx){
        if (idx>=jewelId) return -1;
        Jewel jewel = jewels[idx];
        if (jewel.isRemoved) return -1;
        jewel.isRemoved = true;
        int cnt = weightCnt.get(jewel.weight);
        if (cnt == 1) weightCnt.remove(jewel.weight);
        else weightCnt.put(jewel.weight, cnt-1);
        return jewel.price;
    }

    private static void display(int W){
        Arrays.fill(dp, 0);

        for (int i=1; i<jewelId; i++){
            Jewel jewel = jewels[i];
            if (jewel.isRemoved) continue;
            int weight = jewel.weight;
            if (jewel.weight > W) continue;

            for (int w=W; w>=weight; w--){
                dp[w] = Math.max(dp[w], dp[w-weight]+jewel.price);
            }
        }
        System.out.println(dp[W]);
    }

    private static void makeSet(int D){
        ArrayList<Integer> weights = new ArrayList<>();
        for (int weight:weightCnt.keySet()){
            for (int i=0; i<weightCnt.get(weight); i++){
                weights.add(weight);
            }
        }
        int res = 0, left = 0;
        for (int right = 0; right<weights.size(); right++){
            while (weights.get(right) - weights.get(left) > D){
                left++;
            }
            res += right-left;
        }
        System.out.println(res);
    }
}

/*
냅색?

진열은 30번
세트 3000번

트리맵 사용하면 삽입 logN
 
세트 만들기
1 1 2 2 2 3 3 4

투포인터?
right를 이동하면서 만들 수 있는 쌍의 수를 카운트
*/
