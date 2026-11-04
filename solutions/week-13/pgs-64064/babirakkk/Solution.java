import java.util.*;

class Solution {
    
    String[] userId;
    String[] bannedId;
    int[] masks;
    Set<Integer> cases;
    
    public int solution(String[] user_id, String[] banned_id) {
        userId = user_id;
        bannedId = banned_id;
        masks = new int[user_id.length];
        cases = new HashSet<>();
        
        for (int i = 0; i < bannedId.length; i++) {
            for (int j = 0; j < userId.length; j++) {
                if (isBlocked(i, j)) { // 불량 사용자 아이디와 유저 아이디가 같은지 확인
                    masks[j] |= (1 << i); // 같다면 표시
                }
            }
        }
        
        getCases(0, 0);
        
        return cases.size();   
    }
    
    private void getCases(int bId, int bannedMask) { // dfs
        if (bId == bannedId.length) {
            cases.add(bannedMask); // 중복 리스트 방지를 위해 set에 추가
            return;
        }
        
        for (int i = 0; i < userId.length; i++) {
            if (((masks[i] & (1 << bId)) != 0) && ((bannedMask & (1 << i)) == 0)) { // banned_id와 user_id가 일치하고 현재 유저를 제재 아이디 목록에 넣지 않은 경우
                bannedMask |= (1 << i);
                getCases(bId + 1, bannedMask);
                bannedMask &= ~(1 << i);
            }
        }
    }
    
    private boolean isBlocked(int bId, int uId) { // 유저의 아이디가 불량 사용자의 마스크된 아이디와 같은지 확인
        String banned = bannedId[bId];
        String user = userId[uId];
        
        if (user.length() != banned.length()) return false;
        
        for (int i = 0; i < banned.length(); i++) {
            if (banned.charAt(i) == '*') continue;
            if (banned.charAt(i) != user.charAt(i)) {
                return false;
            }
        }
        
        return true;
    }
}