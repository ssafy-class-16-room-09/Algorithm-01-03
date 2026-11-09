class Solution {
    int N, M, answer;
    String[] user_id, banned_id;
    boolean[] visited;
    public int solution(String[] user_id, String[] banned_id) {
        answer = 0;
        this.user_id = user_id;
        this.banned_id = banned_id;
        N = user_id.length;
        M = banned_id.length;
        visited = new boolean[1<<N];
        
        dfs(0, 0);
        
        return answer;
    }
    
    private void dfs(int idx, int mask){
        if(idx == M) {
            if(!visited[mask]) answer++;
            visited[mask] = true;
            return;
        }
        
        for(int i=0; i<N; i++){
            if((mask & (1<<i)) >0) continue;
            if(check(user_id[i], banned_id[idx])){
                dfs(idx+1, mask|(1<<i));
            }
        }
    }
    
    
    private boolean check(String a, String b){
        if(a.length() != b.length()) return false;
        
        for (int i=0; i<a.length(); i++){
            char x = a.charAt(i);
            char y = b.charAt(i);
            if(y == '*') continue;
            if(x != y) return false;
        }
        
        return true;
    }
}
/*
N<=8
L<=8

기준으로 돌 배열 : banned
0~M
dfs

*/