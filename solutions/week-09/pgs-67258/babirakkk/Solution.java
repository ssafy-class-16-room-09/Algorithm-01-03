import java.util.*;

class Gem {
    int gemId;
    int gemPosition;
    Gem(int gemId, int gemPosition) {
        this.gemId = gemId;
        this.gemPosition = gemPosition;
    }
}

class Solution {
    public int[] solution(String[] gems) {
        Set<String> gemSet = new HashSet<>();
        HashMap<String, Integer> strToInt = new HashMap<>();
        int gemId = 0;
        int firstIdxAllGemsAppear = 0;
        
        for (int i = 0; i < gems.length; i++) {
            String g = gems[i];
            if (gemSet.contains(g)) continue;
            gemSet.add(g);
            strToInt.put(g, gemId++);
            firstIdxAllGemsAppear = i;
        }
        
        int minStartCounter = 0;
        int minEndCounter = firstIdxAllGemsAppear;
        int[] lastGemCounter = new int[gemId];
        Arrays.fill(lastGemCounter, -1);
        ArrayDeque<Gem> dq = new ArrayDeque<>(); // 오름차순
        
        for (int i = 0; i < gems.length; i++) { // 모든 보석들이 등장하는 가장 첫 구간
            int prevPosition = lastGemCounter[strToInt.get(gems[i])]; //변수 명이 너무 마음에 안 들어요 나중에 고쳐야지
            if (prevPosition != -1 && dq.getFirst().gemPosition == prevPosition) {
                dq.removeFirst();
            }
            /*
            지금 슬라이딩 윈도우 방식 -> 맨 앞에 작은 값만 비교함 -> 루비가 두 번째 있으니까 dq에 있는 상태로 또 루비가 들어옴
            */
            
            lastGemCounter[strToInt.get(gems[i])] = i;
            dq.add(new Gem(strToInt.get(gems[i]), i));
            
            if (i >= firstIdxAllGemsAppear) {
                while (dq.getFirst().gemPosition != lastGemCounter[dq.getFirst().gemId]) {
                    dq.removeFirst();
                }
                int currMinStartCounter = dq.getFirst().gemPosition;
                if ((i - currMinStartCounter) < (minEndCounter - minStartCounter)) {
                    minStartCounter = currMinStartCounter;
                    minEndCounter = i;
                }
            }
        }
        
        return new int[]{minStartCounter + 1, minEndCounter + 1}; // 진열대는 1번부터 시작
    }
}
