import java.io.*;
import java.util.*;

class Bug implements Comparable<Bug>{
    int r, c, k, dir;
    public Bug(int r, int c, int k, int dir){
        this.r = r;
        this.c = c;
        this.k = k;
        this.dir = dir;
    }
    public void move(int r, int c){
        this.r = r;
        this.c = c;
    }

    public void turn(){
        k /= 2;
        if (dir == 0){
            dir = 1;
        } else if (dir == 1){
            dir = 0;
        } else if (dir == 2){
            dir = 3;
        } else {
            dir = 2;
        }
    }

    @Override
    public int compareTo(Bug o) {
        if (this.r != o.r) return Integer.compare(this.r, o.r);
        if (this.c != o.c) return Integer.compare(this.c, o.c);
        return Integer.compare(o.k, this.k);
    }
}

public class Solution {
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int TC = Integer.parseInt(br.readLine());
        for (int tc=1; tc<=TC; tc++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int K = Integer.parseInt(st.nextToken());
            
            ArrayDeque<Bug> queue = new ArrayDeque<>();

            for (int i=0; i<K; i++){
                st = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken())-1;
                queue.offer(new Bug(r, c, v, d));

            }

            for (int m=0; m<M; m++){
                if (queue.size() <= 1) break;
                int L = queue.size();
                HashMap<Integer, PriorityQueue<Bug>> moved = new HashMap<>();
                for (int qidx = 0; qidx<L; qidx++){
                    Bug curr = queue.poll();
                    
                    int nr = curr.r+dr[curr.dir];
                    int nc = curr.c+dc[curr.dir];
                    curr.move(nr, nc);
                    if (nr == 0 || nr == N-1 || nc == 0 || nc == N-1){
                        curr.turn();
                    }
                    int idx = nr*N+nc;
                    if (!moved.containsKey(idx)) moved.put(idx, new PriorityQueue<>());
                    moved.get(idx).offer(curr);
                }

                for (PriorityQueue<Bug> pq:moved.values()){
                    Bug base = pq.poll();
                    while (!pq.isEmpty()){
                        Bug removedBug = pq.poll();
                        base.k += removedBug.k;
                    }
                    queue.offer(base);
                }
            }
            int answer = 0;
            while (!queue.isEmpty()){
                Bug curr = queue.poll();
                answer += curr.k;
            }
            System.out.println("#"+tc+" "+answer);
        }

    }
}
/*
상하좌우 1234

Leveling BFS
queue : r, c, dir
*/