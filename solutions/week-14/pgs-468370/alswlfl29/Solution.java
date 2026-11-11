import java.util.*;

class Solution {
    String message;
    List<String> spoilerXWords;
    List<String> generalWords;
    boolean[] visited;
    public int solution(String message, int[][] spoiler_ranges) {
        
        this.message = message;
        spoilerXWords = new ArrayList<>();
        generalWords = new ArrayList<>();
        visited = new boolean[message.length()];
        
        for(int[] spoiler : spoiler_ranges) {
            findSpoilerXWord(spoiler[0], spoiler[1]);
        }
        
        System.out.println("스포일러 방지 단어들 >" + spoilerXWords);
        
        int start = 0;
        StringBuilder sb;
        while(start < message.length()) {
            // 스포일러 방지 단어 혹은 띄어쓰기인 경우
            if(visited[start] || message.charAt(start) == ' ') {
                start++;
                continue;
            }
            
            sb = new StringBuilder();
            int end = start;
            while(end < message.length()) {
                if(message.charAt(end) != ' ') {
                    sb.append(message.charAt(end));
                    end++;
                }
                else break;
            }
            start = end;
            String generalWord = sb.toString();
            generalWords.add(generalWord);          
        }
        
        Set<String> answer = new HashSet<>();
        for(String sw : spoilerXWords) {
            if(generalWords.contains(sw)) continue;
            answer.add(sw);
        }
        System.out.println(answer);
        return answer.size();
    }
    
    private void findSpoilerXWord(int start, int end) {
        // 단어 시작 위치 찾기(message의 처음이 아니고 공백이 아닐 때까지)
        while(start > 0 && message.charAt(start) != ' ') {
            if(message.charAt(start-1) != ' ') start--;
            else break;
        }
        // 단어 끝 위치 찾기(message의 마지막이 아니고 공백이 아닐 때까지)
        while(end < message.length()-1 && message.charAt(end) != ' ') {
            if(message.charAt(end+1) != ' ') end++;
            else break;
        }
        // System.out.println("start>" + start + ", end>" + end);
        
        StringBuilder sb = new StringBuilder();
        for(int i=start; i<=end; i++) {
            char ch = message.charAt(i);
            if(visited[i]) continue;
            if(ch != ' ') sb.append(ch);
            else {
                String str = sb.toString();
                if(!str.equals("")) spoilerXWords.add(str);
                sb = new StringBuilder();
            }
            visited[i] = true;
        }
        String str = sb.toString();
        if(!str.equals("")) spoilerXWords.add(str);
    }
}
