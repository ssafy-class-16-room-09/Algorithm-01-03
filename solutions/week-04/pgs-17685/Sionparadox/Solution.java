import java.util.*;

class Node {
    Node[] children = new Node[26];
    int cnt;
    boolean isFinished;
}

class Trie {
    Node root = new Node();
    
    void insert(String word){
        Node curr = root;
        for (char c : word.toCharArray()){
            curr.cnt ++;
            int idx = c-'a';
            if (curr.children[idx] == null) curr.children[idx] = new Node();
            curr = curr.children[idx];
        }
        curr.isFinished = true;
        curr.cnt++;
    }
    
    int search(String word){
        Node curr = root;
        
        for (int i=0; i<word.length(); i++){
            char c = word.charAt(i);
            if (curr.cnt == 1) return i;
            curr = curr.children[c-'a'];
        }
        
        return word.length();
    }
}

class Solution {
    public int solution(String[] words) {
        int answer = 0;
        Trie trie = new Trie();
        for (String word:words){
            trie.insert(word);
        }
        for (String word:words){
            answer += trie.search(word);
        }
        return answer;
    }
}