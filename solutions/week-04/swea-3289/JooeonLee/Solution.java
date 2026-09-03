import java.util.*;
import java.io.*;
 
public class Solution {
    static int[] parent;
    static int N;
    static int M;
 
    static void init() {
      parent = new int[N+1];
      for(int i=1; i<N+1; i++)
        parent[i] = i;
    }
 
    static int find(int a) {
      if(parent[a] == a)
        return parent[a];
 
      return parent[a] = find(parent[a]);
    }
 
    static boolean union(int a, int b) {
      int rootA = find(a);
      int rootB = find(b);
 
      if(rootA == rootB)
        return false;
      if(rootA <= rootB) 
        parent[rootB] = rootA;
      else
        parent[rootA] = rootB;
 
      return true;
    }
 
    static int isSameSet(int a, int b) {
      if(find(a) == find(b))
        return 1;
      else
        return 0;
    }
 
    public static void main(String[] args) {
      FastReader fr = new FastReader();
      StringBuilder sb = new StringBuilder();
 
      int T = fr.nextInt();
      for(int t=1; t<=T; t++) {
        N = fr.nextInt();
        M = fr.nextInt();
 
        sb.append('#').append(t).append(' ');
 
        init();
 
        for(int i=0; i<M; i++) {
          int opt = fr.nextInt();
          int a = fr.nextInt();
          int b = fr.nextInt();
 
          if(opt == 0)
            union(a, b);
          else
            sb.append(isSameSet(a, b));
        }
 
        sb.append('\n');
      }
 
      System.out.println(sb.toString());
    }
 
    static class FastReader {
      BufferedReader br;
      StringTokenizer st;
 
      public FastReader() {
        br = new BufferedReader(new InputStreamReader(System.in));
      }
 
      String next() {
        while(st==null || !st.hasMoreTokens()) {
          try {
            st = new StringTokenizer(br.readLine());
          } catch(IOException e) {
            e.printStackTrace();
          }
        }
 
        return st.nextToken();
      }
 
      int nextInt() {
        return Integer.parseInt(next());
      } 
    }
}