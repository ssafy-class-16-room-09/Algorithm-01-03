import java.util.*;
class Solution {
    boolean[] visited;
    List<Integer>[] available;
    int u, b;
    Set<Integer> combinations;
    
    public int solution(String[] user_id, String[] banned_id) {
        u = user_id.length;
        b = banned_id.length;
        
        visited = new boolean[u];
        combinations = new HashSet<>();
        
        available = new ArrayList[b];
        for(int i=0; i<b; i++) available[i] = new ArrayList<>();
        // 1. 각 user_id 이름 당 banned_id 확인
        for(int i=0; i<b; i++) {
            String banned = banned_id[i];
            
            for(int j=0; j<u; j++) {
                String name = user_id[j];
                if(name.length() != banned.length() || !checkSame(banned, name)) continue; // 두 문자열 길이가 다른 경우
                available[i].add(j);
            }
        }
        
        // 2. 조합 구성
        combinationFunc(0, new int[b]);

        return combinations.size();
    }
    
    private void combinationFunc(int i, int[] comb) {
        if(i == b) {
            int num = 0;
            for(int c : comb) num |= (1 << c);
            combinations.add(num);
            return;
        }
        
        for(int candidate : available[i]) {
            if(!visited[candidate]) {
                visited[candidate] = true;
                comb[i] = candidate;
                combinationFunc(i+1, comb);
                visited[candidate] = false;
                comb[i] = 0;
            }
        }
    }
    
    private boolean checkSame(String banned, String name) {
        int start = 0;
        int end = name.length()-1;
        while(start <= end) {
            if(banned.charAt(start) != '*' && (banned.charAt(start) != name.charAt(start))) return false;
            if(banned.charAt(end) != '*' && (banned.charAt(end) != name.charAt(end))) return false;
            start++;
            end--;
        }
        return true;
    }
}