import java.util.*;
import java.io.*;

class Node {
  TreeMap<String, Node> children = new TreeMap<>();
  
}

class Trie {
  Node root = new Node();

  void insert(String[] path){
    Node curr = root;
    for (String word:path){
      curr.children.putIfAbsent(word, new Node());
      curr = curr.children.get(word);
    }
  }

  void print(){
    StringBuilder sb = new StringBuilder();
    dfs(root, 0, sb);
    System.out.println(sb);
  }

  void dfs(Node node, int depth, StringBuilder sb){
    for (String key:node.children.keySet()){
      sb.append(" ".repeat(depth)).append(key).append("\n");
      dfs(node.children.get(key), depth+1, sb);
    }
  }

}

public class Main {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st;
      StringBuilder sb = new StringBuilder();

      int N = Integer.parseInt(br.readLine());
      Trie trie = new Trie();

      for (int i=0; i<N; i++){
        String[] arr = br.readLine().split("\\\\");
        trie.insert(arr);
      }
      trie.print();

    }


}

/*
누가봐도 트라이

정렬해야하니까 트리맵
*/
