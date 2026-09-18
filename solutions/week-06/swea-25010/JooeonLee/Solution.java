import java.util.*;

class Solution {
    static int N;

    // 최근 N개의 검색어 유지
    static ArrayDeque<String> queue;

    // 최근 N개 안에서 각 검색어 등장 횟수
    static HashMap<String, Integer> countMap;


    public void init(int n) {
        N = n;

        queue = new ArrayDeque<>();
        countMap = new HashMap<>();
    }

    public void addKeyword(String mKeyword) {

        // 1. 새로운 검색어 queue에 추가
        queue.offer(mKeyword);

        // 2. countMap에서 등장 횟수 증가
        countMap.put(mKeyword, countMap.getOrDefault(mKeyword, 0)+1);

        // 3. queue 크기가 N보다 크다면
        //    가장 오래된 검색어 제거
        // 4. 제거된 검색어의 count 감소
        // 5. count가 0이 되었다면 countMap에서 제거
        if(queue.size() > N) {
            String removeKeyword = queue.poll();
            
            countMap.put(removeKeyword, countMap.getOrDefault(removeKeyword, 1) -1);
            
            if(countMap.get(removeKeyword) == 0)
                countMap.remove(removeKeyword);
        }
    }

    public int top5Keyword(String[] mRet) {
        // 1. 현재 존재하는 서로 다른 검색어를 List로 변환
        List<String> keywords = new ArrayList<>(countMap.keySet());

        int size = keywords.size();

        // 2. Union-Find 초기화
        int[] parent = new int[size];
        for(int i=0; i<size; i++)
            parent[i] = i;

        // 3. 검색어의 유사 관계를 찾아 union
        // 방법:
        // 각 검색어마다 한 글자를 '*'로 바꾼 패턴 생성
        // 같은 패턴을 가진 검색어끼리 union
        HashMap<String, Integer> patternMap = new HashMap<>();
        for(int i=0; i<size; i++) {
            String keyword = keywords.get(i);
            for(int j=0; j<keyword.length(); j++) {
                StringBuilder sb = new StringBuilder(keyword);
                sb.setCharAt(j, '*');
                String pattern = sb.toString();
                
                if(patternMap.containsKey(pattern)) {
                    int other = patternMap.get(pattern);
                    union(parent, i, other);
                }
                else
                    patternMap.put(pattern, i);
            }
        }

        // 4. union 결과를 기반으로 그룹 정보 계산
        HashMap<Integer, Group> groupMap = new HashMap<>();

        // 각 검색어에 대해
        // root = find(...)
        //
        // group.totalCount += 검색어 등장 횟수
        //
        // 대표 검색어 갱신
        // - 등장 횟수가 더 크면 변경
        // - 등장 횟수가 같으면 사전순 비교
        for(int i=0; i<size; i++) {
            String keyword = keywords.get(i);
            
            int root = find(parent, i);
            int keywordCnt = countMap.get(keyword);

            if(!groupMap.containsKey(root)) {
                Group group = new Group();
                group.totalCount = keywordCnt;
                group.representative = keyword;
                groupMap.put(root, group);
            }
            else {
                Group group = groupMap.get(root);
                group.totalCount += keywordCnt;

                int representativeCnt = countMap.get(group.representative);

                if(keywordCnt > representativeCnt)
                    group.representative = keyword;
                else if(keywordCnt == representativeCnt) {
                    if(keyword.compareTo(group.representative) < 0)
                        group.representative = keyword;
                }
            }
        }

        // 5. 그룹들을 List로 변환
        List<Group> groups = new ArrayList<>(groupMap.values());

        // 6. 인기 검색어 순위 정렬
        //
        // 1순위: totalCount 내림차순
        // 2순위: representative 사전순 오름차순
        groups.sort((g1, g2) -> {
            if(g1.totalCount != g2.totalCount)
                return Integer.compare(g2.totalCount, g1.totalCount);
            else
                return g1.representative.compareTo(g2.representative);
        });

        // 7. 최대 5개를 mRet에 저장
        int returnSize = Math.min(5, groups.size());
        for(int i=0; i<returnSize; i++)
            mRet[i] = groups.get(i).representative;

        // 8. 저장한 개수 반환
        return returnSize;
    }

    static int find(int[] parent, int x) {

        // Union-Find find
        if(parent[x] == x)
            return x;

        return parent[x] = find(parent, parent[x]);
    }

    static void union(int[] parent, int a, int b) {

        // Union-Find union
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if(rootA == rootB)
            return;
        parent[rootB] = rootA;
        return;
    }
    static class Group {

        // 해당 유사 검색어 집합 전체 등장 횟수
        int totalCount;

        // 대표 검색어
        String representative;

        public Group() {

        }
    }
}