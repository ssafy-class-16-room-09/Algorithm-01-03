import java.util.*;
/**
단어 알파벳 소문자 + 숫자
한 단어가 여러개의 스포방지 or 스포 방지가 여러개의 단어 포함

중요한 단어 조건
1. 스포 방지 단어
2. 단어가 모두 스포 방지 구간 안에 있어야함 X -> 잘못해석함
단어가 일부라도 스포 방지 구간에 있으면 괜춘!
3. 이전 스포 방지 단어와 겹치지 않아야 함
4. 왼쪽부터 판단

*/
class Solution {
    
    public class Word {
        String content;
        int sIdx;
        int eIdx;
        
        Word(String content, int sIdx, int eIdx) {
            this.content = content;
            this.sIdx = sIdx;
            this.eIdx = eIdx;
        }
    }
    
    public int solution(String message, int[][] spoiler_ranges) {
        List<Word> words = new ArrayList<>();
        int idx = 0;

        while (idx < message.length()) {
            if (message.charAt(idx) == ' ') {
                idx++;
                continue;
            }

            int start = idx;
            while (idx < message.length() && message.charAt(idx) != ' ') {
                idx++;
            }

            words.add(new Word(message.substring(start, idx), start, idx - 1));
        }
        
        Set<String> impWords = new HashSet<>();
        Set<String> notImpWords = new HashSet<>();
        boolean[] spoiler = new boolean[message.length()];
        for(int[] range : spoiler_ranges) {
            int start = range[0];
            int end = range[1];
            for(int i=start; i<=end; i++)
                spoiler[i] = true;
        }
        
        for (Word word : words) {
            boolean isSpoilerWord = false;

            for (int i = word.sIdx; i <= word.eIdx; i++) {
                if (spoiler[i]) {
                    isSpoilerWord = true;
                    break;
                }
            }

            if (isSpoilerWord) {
                impWords.add(word.content);
            } else {
                notImpWords.add(word.content);
            }
        }

        impWords.removeAll(notImpWords);
        return impWords.size();
    }
}