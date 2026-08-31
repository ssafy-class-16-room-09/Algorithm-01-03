import java.util.*;

class Solution {
    public int[] solution(String[] genres, int[] plays) {
        int L = genres.length;
        HashMap<String, Integer> genrePlays = new HashMap<>();
        
        for (int i=0; i<L; i++){
            String genre = genres[i];
            genrePlays.put(genre, genrePlays.getOrDefault(genre, 0)+plays[i]);
        }
        
        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) ->{
            if (genrePlays.get(genres[x]) != genrePlays.get(genres[y])) return Integer.compare(genrePlays.get(genres[y]), genrePlays.get(genres[x]));
            if (plays[x] != plays[y]) return Integer.compare(plays[y], plays[x]);
            return Integer.compare(x, y);
            
        });
        for (int i=0; i<L; i++){
            pq.offer(i);
        }
        
        HashMap<String, Integer> genreCnt = new HashMap<>();
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i=0; i<L; i++){
            int x = pq.poll();
            String genre = genres[x];
            if (genreCnt.getOrDefault(genre, 0) < 2){
                ans.add(x);
                genreCnt.put(genre, genreCnt.getOrDefault(genre, 0)+1);
            }
        }
        
        
        return ans.stream().mapToInt(i -> i).toArray();
    }
}