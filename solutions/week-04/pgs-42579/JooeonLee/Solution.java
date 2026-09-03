import java.util.*;
class Solution {
    class GenrePlay {
        int totalPlay;
        ArrayList<int[]> musicList;
        
        public GenrePlay(int totalPlay, ArrayList<int[]> musicList) {
            this.totalPlay = totalPlay;
            this.musicList = musicList;
        }
    }

    public int[] solution(String[] genres, int[] plays) {
        ArrayList<Integer> answer = new ArrayList<>();
        
        Map<String, GenrePlay> best = new HashMap<>();
        for(int i=0; i<genres.length; i++) {
            String currG = genres[i];
            int play = plays[i];
            
            if(best.containsKey(currG)) {
                GenrePlay gp = best.get(currG);
                gp.totalPlay += play;
                gp.musicList.add(new int[]{i, play});
            }
            else {
                int totalPlay = play;
                ArrayList<int[]> musicPlay = new ArrayList<>();
                musicPlay.add(new int[]{i, play});
                best.put(currG, new GenrePlay(totalPlay, musicPlay));
            }
        }
        
        ArrayList<GenrePlay> bestGp = new ArrayList<>(best.values());
        
        bestGp.sort((a, b) -> {
            return Integer.compare(b.totalPlay, a.totalPlay);
        });
        
        for(GenrePlay gp : bestGp) {
            gp.musicList.sort((a, b) -> {
                if(a[1] != b[1])
                    return Integer.compare(b[1], a[1]);
                else
                    return Integer.compare(a[0], b[0]);
            });
            
            int idx = 0;
            if(gp.musicList.size() >= 2)
                idx = 2;
            else
                idx = 1;
            
            for(int i=0; i<idx; i++) {
                int[] music = gp.musicList.get(i);
                answer.add(music[0]);
            }
        }
        return answer.stream()
            .mapToInt(Integer::intValue)
            .toArray();
    }
}