import java.util.*;
import java.io.*;

public class Solution {
    public static void main(String[] args) {
    FastReader fr = new FastReader();
    StringBuilder sb = new StringBuilder();

    int V, E;
    ArrayList<ArrayList<Integer>> graph;

    for(int t=1; t<=10; t++) {
        V = fr.nextInt();
        E = fr.nextInt();
        int[] indegree = new int[V+1];

        graph = new ArrayList<>();
        for(int i=0; i<=V; i++) {
            graph.add(new ArrayList<>());
        }
        for(int i=0; i<E; i++) {
            int from = fr.nextInt();
            int to = fr.nextInt();
            
            graph.get(from).add(to);
            indegree[to]++;
        }

        sb.append('#').append(t).append(' ');
        Queue<Integer> queue = new ArrayDeque<>();
        for(int i=1; i<=V; i++)
            if(indegree[i] == 0)
            queue.offer(i);
        while(!queue.isEmpty()) {
            int curr = queue.poll();
            sb.append(curr).append(' ');
            
            for(int to : graph.get(curr)) {
            indegree[to]--;
                
            if(indegree[to] == 0)
                queue.offer(to);
            }
        }
            sb.append('\n');
        }
        System.out.print(sb);
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public String next() {
            while(st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}