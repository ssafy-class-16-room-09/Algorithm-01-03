import java.util.*;
import java.io.*;

class Solution {
    static int answer=0;
    static Set[] as;
    static int[] cnt;
    static boolean[] selected;
    static Set<String> answerSet = new HashSet<String>();
    public int solution(String[] user_id, String[] banned_id) {
        as = new HashSet[banned_id.length];
        cnt = new int[user_id.length];
        selected = new boolean[user_id.length];
        
        for(int i=0; i<banned_id.length; i++)
            as[i] = new HashSet<Integer>();
        for(int i=0; i<user_id.length; i++) {
            char[] idArr = user_id[i].toCharArray();
            for(int j=0; j<banned_id.length; j++) {
                char[] bIdArr = banned_id[j].toCharArray();
                
                // 길이 다를 때
                if(idArr.length != bIdArr.length)
                    continue;
                boolean flag = true;
                for(int k=0; k<bIdArr.length; k++) {
                    if(bIdArr[k] == '*')
                        continue;
                    else if(bIdArr[k] != idArr[k]) {
                        flag = false;
                        break;
                    }
                }
                if(flag)
                    as[j].add(i);
            }
        }
        
        // 각 원소가 banned_id에 몇번 들어있는지
        for(int i=0; i<user_id.length; i++) {
            for(Set<Integer> s : as) {
                if(s.contains(i))
                    cnt[i]++;
            }
        }
        
        dfs(0, banned_id.length);
        return answerSet.size();
    }
    
    static void dfs(int d, int maxD) {
        if(d==maxD) {
            int c=0;
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<selected.length; i++)
                if(selected[i])
                    sb.append(i);
            answerSet.add(sb.toString());
            answer++;
            return;
        }
        
        Set<Integer> cand = as[d];
        
        for(int i : cand) {
            if(selected[i])
                continue;
            
            selected[i] = true;
            dfs(d+1, maxD);
            selected[i] = false;
        }
        return;
    }
}