import java.util.*;
import java.io.*;

class Lamp {
    int id;
    int x;
    boolean isRemoved;

    public Lamp(int id, int x){
        this.id = id;
        this.x = x;
    }
}

public class Main {
    static PriorityQueue<Lamp[]> pq;
    static int N, M, idx;
    static Lamp[] lamps;
    static TreeSet<Lamp> lampSet;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());
        StringTokenizer st;
        for (int q=0; q<Q; q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if (cmd == 100){
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());
                lamps = new Lamp[200_001];
                lampSet = new TreeSet<>((o1, o2) -> Integer.compare(o1.x, o2.x));
                for (idx=1; idx<=M; idx++){
                    lamps[idx] = new Lamp(idx, Integer.parseInt(st.nextToken()));
                    lampSet.add(lamps[idx]);
                    
                }
                init();
                
            } else if (cmd == 200){
                add();
            } else if (cmd == 300){
                int k = Integer.parseInt(st.nextToken());
                delete(k);
            } else {
                calc();
            }
        }

    }

    private static void init() {
        pq = new PriorityQueue<>((o1, o2) -> {
            int d1 = o1[1].x - o1[0].x;
            int d2 = o2[1].x - o2[0].x;
            if (d1 != d2) return Integer.compare(d2, d1);
            return Integer.compare(o1[0].x, o2[0].x);
        });

        for (int i=1; i<M; i++){
            pq.offer(new Lamp[] {lamps[i], lamps[i+1]});
        }
    }

    private static void add(){
        Lamp[] curr;
        while (true){
            curr = pq.poll();
            if (!curr[0].isRemoved && !curr[1].isRemoved) break;
        }
        
        int mid = (curr[1].x + curr[0].x + 1 ) / 2;
        Lamp lamp = new Lamp(idx, mid);
        lamps[idx++] = lamp;
        lampSet.add(lamp);
        pq.offer(new Lamp[] {curr[0], lamp});
        pq.offer(new Lamp[] {lamp, curr[1]});

    }

    private static void delete(int id){
        Lamp lamp = lamps[id];

        Lamp prev = lampSet.lower(lamp);
        Lamp next = lampSet.higher(lamp);
        lampSet.remove(lamp);
        lamp.isRemoved = true;

        if (prev != null && next != null) {
            pq.offer(new Lamp[]{prev, next});
        }
    }
    

    private static void calc(){
        Lamp[] curr;
        while (true){
            curr = pq.poll();
            if (!curr[0].isRemoved && !curr[1].isRemoved) break;
        }
        pq.offer(curr);
        int base = curr[1].x - curr[0].x;

        base = Math.max((lampSet.first().x - 1)*2, base);
        base = Math.max((N - lampSet.last().x)*2, base);
        System.out.println(base);
    }
}

/*
pq로 두 지점을 넣고 그 길이로 최대힙, 작은 좌표
(x1+x2+1)/2

*/
