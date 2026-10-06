import java.util.*;

class Solution {
    public int[] solution(String[] gems) {
        
        Set<String> jewelryCategory = new HashSet<>(); // 보석 종류
        for(String gem : gems) jewelryCategory.add(gem);
        int CNT = jewelryCategory.size(); // 보석 종류 개수
        
        Map<String, Integer> jewelrys = new HashMap<>(); // 보석 종류 별 개수
        int start = 0;
        int end = start+1;
        int min = gems.length - start; // 최단 구간 초기화
        int[] answer = new int[2];
        answer[0] = start;
        answer[1] = gems.length-1;
        
        jewelrys.put(gems[start], 1); // 첫 번째 보석 먼저 넣기
        while(end <= gems.length) {
            // map에 보석 종류가 부족한 경우, end 범위 늘리기
            if(jewelrys.size() < CNT) {
                if(end >= gems.length) break; // end 범위가 넘어갈 경우 수행 중단
                jewelrys.put(gems[end], jewelrys.getOrDefault(gems[end], 0) + 1);
                end++;
            }
            // 그 외 start 범위 늘려서 구간 줄이기
            else {
                // 더 최단 구간가 존재하는 경우 갱신
                if(min > end-start) {
                    answer[0] = start;
                    answer[1] = end-1;
                    min = end-start;
                }
                jewelrys.put(gems[start], jewelrys.get(gems[start])-1);
                if(jewelrys.get(gems[start]) == 0) jewelrys.remove(gems[start]);
                start++;
            }
        }
        
        // 인덱스가 1부터 시작이기 때문에 1씩 더해주기
        answer[0]++;
        answer[1]++;
        return answer;
    }
}