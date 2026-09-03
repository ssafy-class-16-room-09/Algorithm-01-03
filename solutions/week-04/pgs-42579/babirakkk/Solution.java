import java.util.*;

class Song {
    int id;
    String genre;
    int playCount;
    Song(int id, String genre, int playCount) {
        this.id = id;
        this.genre = genre;
        this.playCount = playCount;
    }
}

class Genre {
    int totalPlayCount = 0;
    Song[] top2 = new Song[2];
}

class Solution {
    
    public int[] solution(String[] genres, int[] plays) {
        ArrayList<Song> songList = new ArrayList<>();
        for (int i = 0; i < genres.length; i++) {
            songList.add(new Song(i, genres[i], plays[i]));
        }
        // 재생 횟수 내림차순 정렬, 같으면 id 오름차순 정렬
        songList.sort(Comparator.comparingInt((Song s) -> s.playCount).reversed()
                      .thenComparingInt(s -> s.id));
        
        HashMap<String, Genre> bestAlbum = new HashMap<>();
        
        for (Song s : songList) {
            if (!bestAlbum.containsKey(s.genre)) {
                bestAlbum.put(s.genre, new Genre());
            }
            Genre currGenre = bestAlbum.get(s.genre);
            currGenre.totalPlayCount += s.playCount;
            
            if (currGenre.top2[1] != null) {
                continue; // 현재 장르의 상위 2곡이 이미 저장된 경우
            }
            if (currGenre.top2[0] == null) { // 현재 장르의 첫 번째 곡 저장
                currGenre.top2[0] = s;
            } else { // 가장 많이 재생된 곡이 이미 있으면
                currGenre.top2[1] = s;
            }
        }
        
        ArrayList<Genre> genreList = new ArrayList<>(bestAlbum.values());
        // 장르별 총 재생 횟수 내림차순
        genreList.sort((g1, g2) -> Integer.compare(g2.totalPlayCount, g1.totalPlayCount));
        ArrayList<Integer> playOrder = new ArrayList<>();
        for (Genre g : genreList) {
            playOrder.add(g.top2[0].id);
            if (g.top2[1] != null) {
                playOrder.add(g.top2[1].id);
            }
        }
        return playOrder.stream().mapToInt(Integer::intValue).toArray();
    }
}