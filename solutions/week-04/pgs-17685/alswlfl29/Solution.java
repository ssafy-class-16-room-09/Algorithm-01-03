class Alphabet {
    int cnt; // 자식 개수
    Alphabet[] children;
    
    Alphabet(int cnt, Alphabet[] children) {
        this.cnt = cnt;
        this.children = children;
    }
}

class Solution {
    static final int NUM = 97; // a 아스키코드
    static final int TOTAL = 26; // 각 배열의 크기
    public int solution(String[] words) {
        int N = words.length; // 단어 개수
        
        Alphabet[] trie = new Alphabet[TOTAL]; // 트라이 생성(루트)
        
        // 학습시키기
        for(int i=0; i<N; i++) {
            Alphabet[] parent = trie;
            for(int j=0; j<words[i].length(); j++) {
                int position = words[i].charAt(j) - NUM; // 알파벳 위치
                if(parent[position] == null) {
                    parent[position] = new Alphabet(1, new Alphabet[TOTAL]);
                } else {
                    parent[position].cnt++; // 범위에 포함되는 단어 개수
                }
                parent = parent[position].children;
            }
        }
        
        int searchCnt = 0; // 검색 횟수
        // 검색하기
        for(int i=0; i<N; i++) {
            Alphabet[] parent = trie;
            for(int j=0; j<words[i].length(); j++) {
                int position = words[i].charAt(j) - NUM; // 알파벳 위치
                searchCnt++; // 검색 횟수 카운트
                // 현재까지 탐색된 것 중에 유일한 알파벳인 경우
                if(parent[position].cnt == 1) {
                    break;
                } else {
                    parent = parent[position].children;
                }
            }
        }   
        return searchCnt;
    }
}

/*
return > 학습된 단어들을 순서대로 찾을 때 각 단어를 찾기 위해 몇 개의 문자를 입력해야 하는지

트라이(Trie) -> root -> 26개의 배열(a~z)
- 시간복잡도: 문자열 개수 N, 문자열 중 가장 긴 길이 M -> 삽입: O(NM), 조회: O(M)
- 단어들 길이 총합 L <= 1,000,000

	root
g(3)
o(2) u(1)
n(1) i(1)
e l

1) root 배열 만든 후 0으로 초기화 -> 아스키코드 이용(a->97)
[학습]
2) words에 있는 단어를 배열에 하나씩 학습시키기
2-1) 하나씩 내려가면서 그 구간에 포함되는 단어 카운트 세기
[검색]
3) 구간에 포함되는 단어 개수가 1인 경우, 더 이상 검색 안해도됨
4) 트리 내려가면서 검색 횟수 세기
*/