import java.util.*;
import java.io.*;

public class Main {
	
	private static Map<String, Map> roots;
	private static StringBuilder sb;
	
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      int N = Integer.parseInt(br.readLine()); // 디렉토리 전체 경로의 개수(1<=N<=500)
      
      roots = new TreeMap<>();
      for(int n=0; n<N; n++) {
        String[] directory = br.readLine().split("\\\\");
        String root = directory[0];
        if(!roots.containsKey(root)) roots.put(root, new TreeMap<>());
        Map<String, Map> parent = roots.get(root);
        for(int s=1; s<directory.length; s++) {
        	if(!parent.containsKey(directory[s])) parent.put(directory[s], new TreeMap<>());
        	parent = parent.get(directory[s]);
        }
      }
      
      sb = new StringBuilder();
      for(String root : roots.keySet()) {
    	  dfs(root, roots.get(root), 0);
      }
      
      System.out.print(sb);
      
      br.close();
    }
    
    private static void dfs(String parent, Map<String, Map> children, int tab) {
      sb.append(" ".repeat(tab)).append(parent).append("\n");
      if(children.size() == 0) return;
      for(String child : children.keySet()) {
    		dfs(child, children.get(child), tab+1);
    	}
    }
}