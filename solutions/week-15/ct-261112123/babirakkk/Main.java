 import java.util.*;
import java.io.*;

public class Main {

    static class Node {
        int id, value;
        Node parent, left, right;

        Node(int id, int value) {
            this.id = id;
            this.value = value;
        }

        void addChild(Node n) {
            if (this.left == null) {
                this.left = n;
            } else {
                this.right = n;
            }
        }
    }

    static Node[] nodes;
    static int maxRoute;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int n = Integer.parseInt(br.readLine());
        int[][] connected = new int[n + 1][4]; // c[i][3] -> i번 노드의 인접한 노드가 들어갈 인덱스

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            connected[a][connected[a][3]++] = b;
            connected[b][connected[b][3]++] = a;
        }

        nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node(i, Integer.parseInt(br.readLine()));
        }
        makeTree(nodes, connected, n);

        maxRoute = Integer.MIN_VALUE;
        dfs(nodes[1], 0);

        System.out.println(maxRoute);

    }

    private static int dfs(Node curr, int route) {
        int leftRoute = 0;
        int rightRoute = 0;
        if (curr.left != null) {
            leftRoute = dfs(curr.left, route);
        }
        if (curr.right != null) {
            rightRoute = dfs(curr.right, route);
        } 
        int currMaxRoute = curr.value;
        if (leftRoute > 0) currMaxRoute += leftRoute;
        if (rightRoute > 0) currMaxRoute += rightRoute;
        maxRoute = Math.max(currMaxRoute, maxRoute);
        
        return Math.max(0, Math.max(leftRoute, rightRoute)) + curr.value; 
    }

    private static void makeTree(Node[] nodes, int[][] connected, int n) {
        Queue<Node> q = new ArrayDeque<>();
        q.add(nodes[1]);
        while (!q.isEmpty()) {
            Node curr = q.poll();
            for (int i = 0; i < connected[curr.id][3]; i++) {
                Node child = nodes[connected[curr.id][i]];
                if (curr.parent != null && child.id == curr.parent.id) continue;
                nodes[curr.id].addChild(child);
                child.parent = curr;
                q.add(child);
            }
        }
    }
}
