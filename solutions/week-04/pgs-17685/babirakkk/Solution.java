import java.util.*;

class TrieNode {
  HashMap<Character, TrieNode> children = new HashMap<>();
  boolean isEnd = false;
  int count = 0; // 현재 노드를 거치는 단어의 개수
}

class Solution {
  public int solution(String[] words) {
    TrieNode root = new TrieNode();
    for (String word : words) {
      insert(root, word);
    }

    int totalInputCount = 0;
    for (String word : words) {
      totalInputCount += search(root, word);
    }
    return totalInputCount;
  }

  private void insert(TrieNode root, String target) {
    TrieNode curr = root;

    for (int i = 0; i < target.length(); i++) {
      if (!curr.children.containsKey(target.charAt(i))) {
        curr.children.put(target.charAt(i), new TrieNode());
      }

      curr = curr.children.get(target.charAt(i));
      curr.count++; // 현재 prefix를 공유하는 단어 수
    }

    curr.isEnd = true;
  }

  private int search(TrieNode root, String target) {
    TrieNode curr = root;
    int searchCount = 0;

    for (int i = 0; i < target.length(); i++) {
      if (!curr.children.containsKey(target.charAt(i))) {
        return -1;
      }

      searchCount++;
      curr = curr.children.get(target.charAt(i));

      // 현재 prefix를 사용하는 단어가 하나뿐이면 더 입력할 필요 없음
      if (curr.count == 1) {
        break;
      }
    }

    return searchCount;
  }
}