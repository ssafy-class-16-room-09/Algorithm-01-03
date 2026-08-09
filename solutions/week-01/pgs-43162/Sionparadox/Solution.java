import java.util.*;
class Solution {
    int[] parent;
    public int solution(int n, int[][] computers) {
        int answer = 0;
        parent = new int[n];
        
        for (int i=0; i<n; i++){
            parent[i] = i;
        }
        
        for (int i=0; i<n-1; i++){
            for (int j=i+1;j<n;j++){
                if (computers[i][j] == 1) union(i, j);
            }
        }
        
        HashSet<Integer> set = new HashSet<>();
        for (int i=0; i<n; i++){
            int p = find(i);
            if (!set.contains(p)){
                set.add(p);
                answer++;
            }
        }
        
        return answer;
    }
    
    int find(int node){
        if (parent[node] != node) parent[node] = find(parent[node]);
        return parent[node];
    }
    
    void union(int u,int v){
        int pu = find(u); 
        int pv = find(v);
        if (pu != pv) parent[pv] = pu;
    }
    
}