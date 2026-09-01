import java.util.*;

class Solution {
    class Trie {
        static class Node {
            Map<Character, Node> child = new HashMap<>();
            int cnt = 0;
        }
        
        Node root;
        
        public Trie() {
            root = new Node();
        }
        
        public void insert(String word) {
            Node curr = root;
            for(char c : word.toCharArray()) {
                if(!curr.child.containsKey(c))
                    curr.child.put(c, new Node());
                
                curr = curr.child.get(c);
                curr.cnt += 1;
            }
        }
        
        public int autoComplete(String word) {
            Node curr = root;
            char[] wordArr = word.toCharArray();
            
            for(int i=0; i<wordArr.length; i++) {
                curr = curr.child.get(wordArr[i]);
                if(curr.cnt == 1)
                    return i+1;
            }
            return wordArr.length;
        }
        
    }
    public int solution(String[] words) {
        int answer = 0;
        Trie trie = new Trie();
        
        for(String word : words)
            trie.insert(word);
        for(String word : words)
            answer += trie.autoComplete(word);
        return answer;
    }
}