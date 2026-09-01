import java.util.*;

class Music {
    int total; // 장르별 총 노래 횟수
    int number; // 노래 고유 번호
    int play; // 재생 횟수
    
    Music(int total, int number, int play) {
        this.total = total;
        this.number = number;
        this.play = play;
    }
}

class Solution {
    static final int CNT = 2;
    public List<Integer> solution(String[] genres, int[] plays) {
        Map<String, Integer> genreMap = new HashMap<>(); // 장르별 재생 횟수

        for(int p=0; p<plays.length; p++) {
            String genre = genres[p]; // 장르명
            genreMap.put(genre, genreMap.getOrDefault(genre, 0) + plays[p]); // 장르별 재생 횟수에 더하기
        }
        
        PriorityQueue<Music> pq = new PriorityQueue<>((Music m1, Music m2) -> {
            // 재생 횟수가 동일한 경우, 고유 번호가 낮은 노래 먼저 수록
            if(m1.play == m2.play) return Integer.compare(m1.number, m2.number);
            // 같은 장르인 경우 재생 횟수 기준 많은거 먼저 수록
            if(m1.total == m2.total) return Integer.compare(m2.play, m1.play);
            // 장르별 총 노래 횟수 많은거 먼저 수록
            return Integer.compare(m2.total, m1.total);
        }); // 우선순위 큐
        
        for(int p=0; p<plays.length; p++) {
            pq.offer(new Music(genreMap.get(genres[p]), p, plays[p])); // 장르 총 재생 횟수, 고유 번호, 재생 횟수
        }
        
        List<Integer> answer = new ArrayList<>();
        Map<Integer, Integer> album = new HashMap<>(); // 앨범에 수록된 장르별 곡 수
        while(!pq.isEmpty()) {
            Music music = pq.poll(); // 앨범 정보 꺼내기
            
            int cnt = album.getOrDefault(music.total, 0);
            if(cnt >= CNT) continue; // 장르별 곡이 이미 2곡 이상 수록된 경우
            
            answer.add(music.number); // 앨범에 곡 수록하기
            album.put(music.total, cnt+1); // 수록된 장르별 곡 개수 카운트
        }
        return answer;
    }
}

/*
장르 별로 가장 많이 재생된 노래 2개씩 모아 베스트 앨범 출시
[수록 기준]
1. 속한 노래가 많이 재생된 장르 먼저 수록
2. 장르 내에서 많이 재생된 노래 먼저 수록
3. 장르 내에서 재생 횟수가 같은 노래 중에서는 고유 번호가 낮은 노래 먼저 수록

result: 베스트 앨범에 들어갈 노래의 고유 번호 순서대로 return

해시 맵 + PQ 이용
1) 해시 맵을 이용해서 장르별로 전체 재생 횟수 구하기
2) PQ에 (장르별 총 재생 횟수 ⬆, 각 재생 횟수 ⬆, 고유 번호 ⬇) 순으로 정렬 → '모든 장르는 재생된 횟수가 다릅니다.' 조건으로 장르 구별
3) 장르별로 2곡씩 곡 수록
*/