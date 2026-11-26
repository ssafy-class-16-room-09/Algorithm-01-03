import java.util.*;
import java.io.*;

public class Main {
    static Node root = new Node();
    static StringBuilder sb = new StringBuilder();

    static class Node {
        TreeMap<String, Node> children = new TreeMap<>();
    }

    public static void main(String[] args) {
        FastReader fr = new FastReader();

        int N = fr.nextInt();

        for(int i=0; i<N; i++) {
            String[] dirs = fr.next().split("\\\\");
            insert(dirs);
        }

        dfs(root, 0);

        System.out.print(sb);
    }

    static void insert(String[] dirs) {
        Node curr = root;

        for(String dir : dirs) {
//            if(!curr.children.containsKey(dir))
//                curr.children.put(dir, new Node());

//            curr = curr.children.get(dir);
            curr = curr.children.computeIfAbsent(dir, k -> new Node());
        }
    }

    static void dfs(Node node, int depth) {
        for(Map.Entry<String, Node> entry : node.children.entrySet()) {
//            for(int i=0; i<depth; i++)
//                sb.append(' ');
//            sb.append(entry.getKey()).append('\n');
            sb.append(" ".repeat(depth))
              .append(entry.getKey())
              .append('\n');

            dfs(entry.getValue(), depth+1);
        }
    }

    static class FastReader {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        public String next() {
            while(st==null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}