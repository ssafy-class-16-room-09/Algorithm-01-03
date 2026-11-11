import java.util.*;

class Solution {
    public int solution(String message, int[][] spoiler_ranges) {
        HashSet<String> unimportant = new HashSet<>();
        HashSet<String> important = new HashSet<>();
        
        int rangeId = 0;
        int[] range = spoiler_ranges[rangeId];
        StringBuilder sb = new StringBuilder();
        boolean isNoSpoilerWord = (range[0] == 0) ? true : false;
        for (int i = 0; i <= message.length(); i++) {            
            if (i == message.length() || message.charAt(i) == ' ') { // 저장된 문자열을 저장
                String word = sb.toString();
                if (!word.equals("")) {
                    if (isNoSpoilerWord) {
                        if (!unimportant.contains(word)) {
                            // System.out.println("IMPORTANT! " + word);
                            important.add(word);
                        } else {
                            // System.out.println("ALREADY UNIMPORTANT! " + word);
                        }
                    } else {
                        if (important.contains(word)) {
                            // System.out.println("REMOVE IMPORTANT! " + word);
                            important.remove(word);
                        }
                        // System.out.println("UNIMPORTANT! " + word);
                        unimportant.add(word);
                    }
                }
                sb.setLength(0);
                isNoSpoilerWord = (((i + 1) >= range[0]) && ((i + 1) <= range[1])) ? true : false;
                continue;
            }
            
            if (i > range[1] && rangeId < spoiler_ranges.length - 1) { // 범위를 벗어나면 다음 스포 방지 구간으로 넘어감
                range = spoiler_ranges[++rangeId]; 
            }
            
            if (i >= range[0] && i <= range[1]) isNoSpoilerWord = true;
            
            sb.append(message.charAt(i));
        }
        return important.size();
    }
}