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
    int count = 0;
    Song[] top2 = new Song[2];
    Genre(Song s) { // 빈값으로 채우기
        this.top2[0] = s;
        this.top2[1] = s;
    }
}

class Solution {
    
    final Song EMPTY = new Song(-1, "", 0);
    
    public int[] solution(String[] genres, int[] plays) {
        ArrayList<Song> songList = new ArrayList<>();
        for (int i = 0; i < genres.length; i++) {
            songList.add(new Song(i, genres[i], plays[i]));
        }
        songList.sort(Comparator.comparingInt((Song s) -> s.playCount).reversed()
                      .thenComparingInt(s -> s.id));
        
        HashMap<String, Genre> bestAlbum = new HashMap<>();
        
        for (Song s : songList) {
            if (!bestAlbum.containsKey(s.genre)) {
                bestAlbum.put(s.genre, new Genre(EMPTY));
            }
            bestAlbum.get(s.genre).count += s.playCount;
            
            if (bestAlbum.get(s.genre).top2[1] != EMPTY) {
                continue; // 두 번째로 많이 재생된 곡이 이미 수록됐다면 현재 음악은 수록될 수 없음
            }
            if (bestAlbum.get(s.genre).top2[0] == EMPTY) { // 현재 장르의 가장 많이 재생된 곡의 자리가 비어있을 경우
                bestAlbum.get(s.genre).top2[0] = s;
            } else { // 가장 많이 재생된 곡이 이미 있으면
                bestAlbum.get(s.genre).top2[1] = s;
            }
        }
        
        ArrayList<Genre> genreValues = new ArrayList<>(bestAlbum.values());
        genreValues.sort((g1, g2) -> (g2.count - g1.count));
        ArrayList<Integer> playOrder = new ArrayList<>();
        for (Genre g : genreValues) {
            playOrder.add(g.top2[0].id);
            if (g.top2[1] != EMPTY) {
                playOrder.add(g.top2[1].id);
            }
        }
        return playOrder.stream().mapToInt(Integer::intValue).toArray();
    }
}