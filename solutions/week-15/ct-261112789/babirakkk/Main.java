import java.util.*;
import java.io.*;

public class Main {

    static class Node {
        int id, value;
        ArrayList<Node> children = new ArrayList<>();

        Node(int id, int value) {
            this.id = id;
            this.value = value;
        }
    }

    static Node[] nodes;
    static int[][][] dp;
    static int numNodes, numColors;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        numNodes = Integer.parseInt(br.readLine());
        ArrayList<Integer>[] connected = new ArrayList[numNodes + 1];
        for (int i = 1; i < numNodes + 1; i++) {
            connected[i] = new ArrayList<>();
        }

        for (int i = 0; i < numNodes - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            connected[a].add(b);
            connected[b].add(a);
        }

        nodes = new Node[numNodes + 1];
        for (int i = 1; i <= numNodes; i++) {
            nodes[i] = new Node(i, Integer.parseInt(br.readLine()));
        }
        makeTree(connected);

        numColors = Integer.parseInt(br.readLine());
        dp = new int[numNodes + 1][2][numColors + 1];

        dfs(nodes[1]);

        int maxValue = 0;
        for (int i = 1; i <= numNodes; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k <= numColors; k++) {
                    maxValue = Math.max(dp[i][j][k], maxValue);
                }
            }
        }
        System.out.println(maxValue);
    }


    private static void dfs(Node curr) {
        for (Node child : curr.children) {
            dfs(child);
        }


        int[] temp = new int[numColors + 1];
        for (int k = numColors; k > 0; k--) {
            for (Node child : curr.children) {
                for (int ck = 0; ck <= k; ck++) {
                    if (dp[child.id][0][ck] != 0 || dp[child.id][1][ck] != 0) {
                        if (k > ck && dp[curr.id][0][k-ck] == 0) continue;
                        temp[k] = Math.max(
                            temp[k],
                            temp[k-ck] + Math.max(dp[child.id][0][ck], dp[child.id][1][ck])
                        );
                    }
                    if (k - 1 < ck || k > 1 && dp[child.id][0][ck] == 0) continue;
                    dp[curr.id][1][k] = Math.max(
                        dp[curr.id][1][k],
                        dp[curr.id][1][k-ck-1] + dp[child.id][0][ck] + curr.value
                    );
                }
                dp[curr.id][0] = Arrays.copyOf(temp, temp.length);
            }

        }

        dp[curr.id][1][1] = curr.value;
        System.out.println("id: " + curr.id + ", val: " + curr.value);
        System.out.println(Arrays.toString(dp[curr.id][0]));
        System.out.println(Arrays.toString(dp[curr.id][1]));
    }


    private static void makeTree(ArrayList<Integer>[] connected) {
        Queue<Node> q = new ArrayDeque<>();
        q.add(nodes[1]);
        while (!q.isEmpty()) {
            Node curr = q.poll();
            for (int childId : connected[curr.id]) {
                Node child = nodes[childId];
                if (child.children.size() > 0) continue; // 이미 탐색을 한 노드
                
                curr.children.add(child);
                q.add(child);
            }
        }
    }
}

/*
3차원 배열 dp로 해?
dp[i][0][k]: i번째 노드를 칠했고, 칠한 노드의 개수가 k개일 때의 값
dp[i][1][k]: i번째 노드를 칠하지 않았고, 칠한 노드의 개수가 k개일 때의 값
*/